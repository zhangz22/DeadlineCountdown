package localParser;

import com.sun.istack.internal.NotNull;
import main.controller.AbstractController;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import main.viewer.Log;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import model.Deadline;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class loads data from local files
 */
public class Load extends Parser {
    private AbstractController parent;
    private Reader reader;

    /**
     * Constructor
     *
     * @param file the IO reader that will be saved to / load from.
     * @requires allCourses != null
     * @modifies this.allCourses
     * @effects create a LocalParser.Load object
     */
    public Load(@NotNull AbstractController parent, Reader file) {
        this.parent = parent;
        this.reader = file;
    }

    /**
     * This method would save data to a local JSON file.
     *
     * @requires file != null
     * @modifies None
     * @effects load from local file
     * @return result: true -> successful; false -> failed
     */
    @Override
    public synchronized boolean Json() {
        // Read the JSON file
        JsonElement root;
        try {
            root = new JsonParser().parse(this.reader);
        } catch (Exception e) {
            e.printStackTrace();
            errMsg = e.getMessage();
            return false;
        }
        boolean success = false;
        errMsg = "File is empty.";
        int i = 1;
        for (Map.Entry<String, JsonElement> course : root.getAsJsonObject().entrySet()) {
            // iterate through each course
            String courseName = course.getKey();
            for (Map.Entry<String, JsonElement> deadline : course.getValue().getAsJsonObject().entrySet()) {
                String deadlineName = deadline.getKey();
                Integer year = null, month = null, date = null, hour = null, minute = null;
                String status = null, link = "";
                for (Map.Entry<String, JsonElement> entry : deadline.getValue().getAsJsonObject().entrySet()) {
                    switch (entry.getKey()) {
                        case "year":
                            year = entry.getValue().getAsInt();
                            break;
                        case "month":
                            month = entry.getValue().getAsInt();
                            break;
                        case "day":
                        case "date":
                            date = entry.getValue().getAsInt();
                            break;
                        case "hour":
                            hour = entry.getValue().getAsInt();
                            break;
                        case "minute":
                            minute = entry.getValue().getAsInt();
                            break;
                        case "status":
                            status = entry.getValue().getAsString();
                            break;
                        case "link":
                            link = entry.getValue().getAsString();
                            break;
                    }
                }
                if (year == null || month == null || date == null || hour == null || minute == null) {
                    errMsg = "Unrecognizable file format on line " + i;
                } else {
                    parent.addDeadline(courseName, deadlineName, year, month, date, hour, minute, status, link); // TODO status and link
                    success = true;
                }
                i++;
            }
        }
        try {
            this.reader.close();
        } catch (IOException e) {
            System.err.println("[Load] Error when closing file" + e);
        }
        return success;
    }
    @Override
    @SuppressWarnings("deprecation")
    public synchronized boolean Ics() {
        List<ICalendar> icals;
        try {
            icals = Biweekly.parse(this.reader).all();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            errMsg = e.getMessage();
            return false;
        }

        for (ICalendar ical: icals) {
            List<VEvent> events = ical.getEvents();
            for (VEvent event: events) {
                String names = event.getSummary().getValue();
                String[] nameList = names.split(": ");
                String courseName;
                String deadlineName;
                if (nameList.length >= 2) {
                    courseName = nameList[0];
                    deadlineName = names.replace(courseName + ": ", "");
                } else {
                    courseName = "Unknown Course";
                    deadlineName = nameList[0];
                }
                Date d = event.getDateStart().getValue();
                int year = d.getYear() + 1900;
                int month = d.getMonth() + 1;
                int day = d.getDate();
                int hour = d.getHours();
                int minute = d.getMinutes();
                String status, link = "";
                if (event.getDescription().getValue().startsWith("Status = ")) {
                    status = event.getDescription().getValue().split("\n")[0].replace("Status = ", "");
                    if (event.getDescription().getValue().split("\n").length == 2) {
                        link = event.getDescription().getValue().split("\n")[1].replace("Link = ", "");
                    }
                } else {
                    status = Deadline.STATUS.DEFAULT;
                    link = "";
                }
                parent.addDeadline(courseName, deadlineName, year, month, day, hour, minute, status, link);
            }
        }

        try {
            this.reader.close();
        } catch (IOException e) {
            Log.error("[Load] Error when closing file", e);
        }
        return true;
    }

    /**
     * This method would save data to a local CSV file.
     *
     * @requires file != null
     * @modifies None
     * @effects save to / load from local file
     * @return result: true -> successful; false -> failed
     */
    @Override
    public synchronized boolean Csv() {
        if (this.reader == null) {
            return false;
        }
        BufferedReader reader = new BufferedReader(this.reader);
        String str;
        int i = 0;
        while (true) {
            try {
                str = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                errMsg = e.getMessage();
                return false;
            }
            if (i == 0) {
                // skip the first line
                i++;
                continue;
            }
            if (str != null) {
                String[] parts = str.split(",");
                if (parts.length < 7) {
                    continue;
                }
                try {
                    String courseName = parts[0];
                    String deadlineName = parts[1];
                    int year = Integer.parseInt(parts[4]);
                    int month = Integer.parseInt(parts[2]);
                    int date = Integer.parseInt(parts[3]);
                    int hour = Integer.parseInt(parts[5]);
                    int minute = Integer.parseInt(parts[6]);
                    String status = parts[7];
                    String link = "";
                    if (parts.length == 9) {
                        link = parts[8];
                    }
                    parent.addDeadline(courseName, deadlineName, year, month, date, hour, minute, status, link);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    Log.error("Error when reading csv file", e);
                }
            } else {
                break;
            }
        }

        try {
            this.reader.close();
        } catch (IOException e) {
            Log.error("[Load] Error when closing file", e);
        }
        return true;
    }
}

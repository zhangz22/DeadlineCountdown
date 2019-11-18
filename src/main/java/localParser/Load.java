package localParser;

import com.sun.istack.internal.NotNull;
import main.controller.AbstractController;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
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
                    }
                }
                if (year == null || month == null || date == null || hour == null || minute == null) {
                    errMsg = "Unrecognizable file format on line " + i;
                } else {
                    parent.addDeadline(courseName, deadlineName, year, month, date, hour, minute);
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

    /**
     * This method would save data to a local ICS file.
     *
     * @requires None
     * @modifies None
     * @effects save to / load from local file
     * @return result: true -> successful; false -> failed
     */
    @Override
    @SuppressWarnings("deprecation")
    public synchronized boolean Ics() {
        // TODO Feature in the future
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
        // TODO Feature in the future
        return true;
    }
}

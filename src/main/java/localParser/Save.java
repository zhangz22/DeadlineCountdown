package localParser;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Summary;
import biweekly.util.Duration;
import com.sun.istack.internal.NotNull;
import model.CalendarWrapper;
import model.Course;
import model.Deadline;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class saves data to local files
 */
public class Save extends Parser {
    private PrintWriter writer;

    /**
     * Constructor
     *
     * @param allCourses a map to store every course object with its name
     * @param file the IO writer that will be saved to / load from.
     * @requires allCourses != null
     * @modifies this.allCourses
     * @effects create a LocalParser.Save object
     */
    public Save(ConcurrentHashMap<String, Course> allCourses, @NotNull Writer file) {
        this.allCourses = allCourses;
        this.writer = new PrintWriter(file);
    }

    /**
     * This method would save data to a local JSON file.
     *
     * @requires file != null
     * @modifies None
     * @effects save to local file
     * @return result: true -> successful; false -> failed
     */
    @Override
    public synchronized boolean Json() {
        writer.println("{");
        int courseCount = 0;
        for (String courseName: this.allCourses.keySet()) {
            writer.println("\t\"" + courseName + "\": {");
            TreeMap<String, Deadline> deadlines = this.allCourses.get(courseName).getDeadlines();
            int deadlineCount = 0;
            for (String deadlineName : deadlines.keySet()) {
                writer.println("\t\t\"" + deadlineName + "\": {");
                writer.println("\t\t\t\"year\": " + deadlines.get(deadlineName).getYear() + ",");
                writer.println("\t\t\t\"month\": " + deadlines.get(deadlineName).getMonth() + ",");
                writer.println("\t\t\t\"day\": " + deadlines.get(deadlineName).getDay() + ",");
                writer.println("\t\t\t\"hour\": " + deadlines.get(deadlineName).getHour() + ",");
                writer.print("\t\t\t\"minute\": " + deadlines.get(deadlineName).getMinute());
                if (deadlines.get(deadlineName).getStatus() != null) {
                    writer.println(",");
                    writer.print("\t\t\t\"status\": \"" + deadlines.get(deadlineName).getStatus() + "\"");
                }
                if (!deadlines.get(deadlineName).getLink().equals("")) {
                    writer.println(",");
                    writer.print("\t\t\t\"link\": \"" + deadlines.get(deadlineName).getLink() + "\"");
                }
                writer.println();
                if (deadlineCount != deadlines.size() - 1) {
                    writer.println("\t\t},");
                } else {
                    writer.println("\t\t}");
                }
                deadlineCount++;
            }
            if (courseCount != this.allCourses.size() - 1) {
                writer.println("\t},");
            } else {
                writer.println("\t}");
            }
            courseCount++;
        }
        writer.println("}");
        writer.close();
        return true;
    }

    /**
     * This method would save data to a local ICS file.
     *
     * @requires file != null
     * @modifies None
     * @effects save to / load from local file
     */
    @Override
    public synchronized boolean Ics() {
        // create the ICalendar
        ICalendar ical = new ICalendar();
        for (String courseName: this.allCourses.keySet()) {
            TreeMap<String, Deadline> deadlines = this.allCourses.get(courseName).getDeadlines();
            for (String deadlineName : deadlines.keySet()) {
                Deadline deadline = deadlines.get(deadlineName);
                VEvent event = new VEvent();
                Summary summary = event.setSummary(courseName + ": " + deadlineName);
                summary.setLanguage("en-us");

                Date start = CalendarWrapper.createJavaDate(deadline.getYear(),
                        deadline.getMonth(), deadline.getDay(), deadline.getHour(), deadline.getMinute());
                event.setDateStart(start);

                Duration duration = new Duration.Builder().hours(1).build();
                event.setDuration(duration);

                event.setDescription("Status = " + deadline.getStatus() +
                        System.lineSeparator() + "Link = " + deadline.getLink());

                ical.addEvent(event);

            }
        }
        writer.print(Biweekly.write(ical).go());
        writer.close();
        return true;
    }

    /**
     * This method would save data to a local CSV file.
     *
     * @requires file != null
     * @modifies None
     * @effects save to / load from local file
     */
    @Override
    public synchronized boolean Csv() {
        writer.println("Course Name, Deadline Name, Month, Day, Year, Hour, Minute, Status, Link");
        for (String courseName: this.allCourses.keySet()) {
            TreeMap<String, Deadline> deadlines = this.allCourses.get(courseName).getDeadlines();
            for (String deadlineName : deadlines.keySet()) {
                Deadline deadline = deadlines.get(deadlineName);
                writer.print(deadline.getCourseName());
                writer.print(",");
                writer.print(deadline.getName());
                writer.print(",");
                writer.print(deadline.getMonth());
                writer.print(",");
                writer.print(deadline.getDay());
                writer.print(",");
                writer.print(deadline.getYear());
                writer.print(",");
                writer.print(deadline.getHour());
                writer.print(",");
                writer.print(deadline.getMinute());
                writer.print(",");
                writer.print(deadline.getStatus());
                writer.print(",");
                writer.print(deadline.getLink());
                writer.print("\r\n");
            }
        }
        writer.close();
        return true;
    }
}

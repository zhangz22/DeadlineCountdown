package localParser;

import model.Course;
import model.Deadline;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the Save class.
 */
public class LocalSaveTest {
    private StringWriter stringWriter;
    private Save save;
    private String newline = System.lineSeparator();

    /**
     * Because all tests run this method before executing, ALL TESTS WILL FAIL until
     * the constructor does not throw exceptions. Also, any incorrectness in this
     * method may have unforeseen consequences elsewhere in the tests, so it is a good
     * idea to make sure this method is correct before moving on to others.
     */
    // Tests that are intended to verify the constructor should not use variables
    // declared in this setUp method
    @Before
    public void setUp() {
        ConcurrentHashMap<String, Course> allCourses = new ConcurrentHashMap<>();
        Course course = new Course("Operating System");
        course.addDeadline("HW4", 2019, 4,26,23,
                59, Deadline.STATUS.DEFAULT, "");
        allCourses.put("Operating System", course);
        this.stringWriter = new StringWriter();
        this.save = new Save(allCourses, stringWriter);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  JSON test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void jsonTest() {
        Thread thread = Parser.getParserThread(this.save, "JSON", null, false);
        thread.run();
        assertEquals("{" + newline +
                "\t\"Operating System\": {" + newline +
                "\t\t\"HW4\": {" + newline +
                "\t\t\t\"year\": 2019," + newline +
                "\t\t\t\"month\": 4," + newline +
                "\t\t\t\"day\": 26," + newline +
                "\t\t\t\"hour\": 23," + newline +
                "\t\t\t\"minute\": 59," + newline +
                "\t\t\t\"status\": \"WAIT FOR SUBMISSION\"" + newline +
                "\t\t}" + newline +
                "\t}" + newline +
                "}" + newline, this.stringWriter.toString());
    }

    @Test
    public void jsonTestBig() {
        ConcurrentHashMap<String, Course> allCourses = new ConcurrentHashMap<>();
        Course course = new Course("Operating System");
        course.addDeadline("HW4", 2019, 4,26,23,
                59, Deadline.STATUS.DEFAULT, "");
        course.addDeadline("Project2", 2019, 4,26,23,
                59, Deadline.STATUS.DEFAULT, "");
        allCourses.put("Operating System", course);
        Course course1 = new Course("Database System");
        course1.addDeadline("HW", 2019,4,18,13,59,Deadline.STATUS.DEFAULT, "");
        allCourses.put(course1.getCourseName(), course1);
        StringWriter stringWriter = new StringWriter();
        Save save = new Save(allCourses, stringWriter);
        Thread thread = Parser.getParserThread(save, "JSON", null, false);
        thread.run();
        assertEquals("{" + newline +
                "\t\"Database System\": {" + newline +
                "\t\t\"HW\": {" + newline +
                "\t\t\t\"year\": 2019," + newline +
                "\t\t\t\"month\": 4," + newline +
                "\t\t\t\"day\": 18," + newline +
                "\t\t\t\"hour\": 13," + newline +
                "\t\t\t\"minute\": 59," + newline +
                "\t\t\t\"status\": \"WAIT FOR SUBMISSION\"" + newline +
                "\t\t}" + newline +
                "\t}," + newline +
                "\t\"Operating System\": {" + newline +
                "\t\t\"HW4\": {" + newline +
                "\t\t\t\"year\": 2019," + newline +
                "\t\t\t\"month\": 4," + newline +
                "\t\t\t\"day\": 26," + newline +
                "\t\t\t\"hour\": 23," + newline +
                "\t\t\t\"minute\": 59," + newline +
                "\t\t\t\"status\": \"WAIT FOR SUBMISSION\"" + newline +
                "\t\t}," + newline +
                "\t\t\"Project2\": {" + newline +
                "\t\t\t\"year\": 2019," + newline +
                "\t\t\t\"month\": 4," + newline +
                "\t\t\t\"day\": 26," + newline +
                "\t\t\t\"hour\": 23," + newline +
                "\t\t\t\"minute\": 59," + newline +
                "\t\t\t\"status\": \"WAIT FOR SUBMISSION\"" + newline +
                "\t\t}" + newline +
                "\t}" + newline +
                "}" + newline, stringWriter.toString());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  ICS test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void icsTest() {
        Thread thread = Parser.getParserThread(this.save, "ICS", null, false);
        thread.run();
        String[] expected = ("BEGIN:VCALENDAR" + newline +
                "VERSION:2.0" + newline +
                "PRODID:-//Michael Angstadt//biweekly 0.6.3//EN" + newline +
                "BEGIN:VEVENT" + newline +
                "UID:ec21af1b-4592-422f-bebd-d824133ea8fd" + newline +
                "DTSTAMP:20190415T173206Z" + newline +
                "SUMMARY;LANGUAGE=en-us:Operating System: HW4" + newline +
                "DTSTART:20190427T035900Z" + newline +
                "DURATION:PT1H" + newline +
                "DESCRIPTION:Status = WAIT FOR SUBMISSION" + newline + "Link = " + newline +
                "END:VEVENT" + newline +
                "END:VCALENDAR" +  newline).split(newline);
        String[] actual = this.stringWriter.toString().replace("\\n", newline).split(newline);
        assertEquals(expected.length, actual.length);
        for (int i=0; i<expected.length; i++) {
            if (expected[i].startsWith("UID") || expected[i].startsWith("DTSTAMP")) {
                continue;
            }
            assertEquals(expected[i], actual[i]);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    ////  csv Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void csvTest() {
        Thread thread = Parser.getParserThread(this.save, "CSV", null, false);
        thread.run();
        assertEquals("Course Name, Deadline Name, Month, Day, Year, Hour, Minute, Status, Link"
                        +  newline
                + "Operating System,HW4,4,26,2019,23,59,WAIT FOR SUBMISSION," +  newline,
                this.stringWriter.toString());
    }
}
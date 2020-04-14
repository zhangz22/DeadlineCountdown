package localParser;

import model.Course;
import main.controller.AbstractController;
import main.controller.CommandLineController;
import main.controller.GUIController;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the Load class.
 */
public class LocalLoadTest {
    private StringReader stringReader;
    private String newline;
    private AbstractController controller;

    /**
     * Because all tests run this method before executing, ALL TESTS WILL FAIL until
     * the constructor does not throw exceptions. Also, any incorrectness in this
     * method may have unforeseen consequences elsewhere in the tests, so it is a good
     * idea to make sure this method is correct before moving on to others.
     */
    @Before
    public void setUp(){
        this.newline = System.lineSeparator();
        ConcurrentHashMap<String, Course> allCourses = new ConcurrentHashMap<>();
        this.controller = new CommandLineController();
    }

    /////////////////////////////////////////////////////////////////////////
    ////  a helper function to test the loading result
    /////////////////////////////////////////////////////////////////////////

    private void loadTest(String extension) {
        Load load = new Load(this.controller, stringReader);
        Thread thread = Parser.getParserThread(load, extension.toUpperCase(), null, false);
        thread.run();
        assertFalse(this.controller.getAllCourses().isEmpty());
        assertTrue(this.controller.getAllCourses().containsKey("Operating System"));
        assertFalse(this.controller.getAllCourses().get("Operating System").getDeadlines().isEmpty());
        assertEquals("HW4",
                this.controller.getAllCourses().get("Operating System").getDeadlines().get("HW4").getName());
    }

    private void loadTestFailed(String extension) {
        Load load = new Load(this.controller, stringReader);
        Thread thread = Parser.getParserThread(load, extension.toUpperCase(), null, false);
        thread.run();
        assertTrue(this.controller.getAllCourses().isEmpty());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  JSON test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void jsonTest() {
        this.stringReader = new StringReader("{" + newline +
                "\t\"Operating System\": {" + newline +
                "\t\t\"HW4\": {" + newline +
                "\t\t\t\"year\": 2019," + newline +
                "\t\t\t\"month\": 4," + newline +
                "\t\t\t\"date\": 26," + newline +
                "\t\t\t\"hour\": 23," + newline +
                "\t\t\t\"minute\": 59," + newline +
                "\t\t\t\"status\": \"WAIT FOR SUBMISSION\"," + newline +
                "\t\t\t\"link\": \"link\"" + newline +
                "\t\t}" + newline +
                "\t}" + newline +
                "}" + newline);
        this.loadTest("JSON");
    }

    @Test
    public void jsonNoLinkTest() {
        this.stringReader = new StringReader("{" + newline +
                "\t\"Operating System\": {" + newline +
                "\t\t\"HW4\": {" + newline +
                "\t\t\t\"year\": 2019," + newline +
                "\t\t\t\"month\": 4," + newline +
                "\t\t\t\"date\": 26," + newline +
                "\t\t\t\"hour\": 23," + newline +
                "\t\t\t\"minute\": 59," + newline +
                "\t\t\t\"status\": \"WAIT FOR SUBMISSION\"" + newline +
                "\t\t}" + newline +
                "\t}" + newline +
                "}" + newline);
        this.loadTest("JSON");
    }

    @Test
    public void jsonBadFormatTest() {
        this.stringReader = new StringReader("{" + newline +
                "\t\"Operating System\": {" + newline +
                "\t\t\"HW4\": {" + newline +
                "\t\t\t\"month\": 4," + newline +
                "\t\t\t\"date\": 26," + newline +
                "\t\t\t\"hour\": 23," + newline +
                "\t\t\t\"minute\": 59," + newline +
                "\t\t\t\"status\": \"WAIT FOR SUBMISSION\"" + newline +
                "\t\t}" + newline +
                "\t}" + newline +
                "}" + newline);
        this.loadTestFailed("JSON");
    }

    /////////////////////////////////////////////////////////////////////////
    ////  ICS test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void icsTest() {
        this.stringReader = new StringReader("BEGIN:VCALENDAR" + newline +
                "VERSION:2.0" + newline +
                "PRODID:-//Michael Angstadt//biweekly 0.6.3//EN" + newline +
                "BEGIN:VEVENT" + newline +
                "UID:ec21af1b-4592-422f-bebd-d824133ea8fd" + newline +
                "DTSTAMP:20190415T173206Z" + newline +
                "SUMMARY;LANGUAGE=en-us:Operating System: HW4" + newline +
                "DTSTART:20190427T035900Z" + newline +
                "DURATION:PT1H" + newline +
                "DESCRIPTION:Status = WAIT FOR SUBMISSION\\nLink = link" + newline +
                "END:VEVENT" + newline +
                "END:VCALENDAR" +  newline);
        this.loadTest("ICS");
    }

    @Test
    public void icsNoLinkTest() {
        this.stringReader = new StringReader("BEGIN:VCALENDAR" + newline +
                "VERSION:2.0" + newline +
                "PRODID:-//Michael Angstadt//biweekly 0.6.3//EN" + newline +
                "BEGIN:VEVENT" + newline +
                "UID:ec21af1b-4592-422f-bebd-d824133ea8fd" + newline +
                "DTSTAMP:20190415T173206Z" + newline +
                "SUMMARY;LANGUAGE=en-us:Operating System: HW4" + newline +
                "DTSTART:20190427T035900Z" + newline +
                "DURATION:PT1H" + newline +
                "DESCRIPTION:Status = WAIT FOR SUBMISSION" + newline +
                "END:VEVENT" + newline +
                "END:VCALENDAR" +  newline);
        this.loadTest("ICS");
    }

    @Test
    public void icsFormatTest() {
        this.stringReader = new StringReader("BEGIN:VCALENDAR" + newline +
                "VERSION:2.0" + newline +
                "PRODID:-//Michael Angstadt//biweekly 0.6.3//EN" + newline +
                "BEGIN:VEVENT" + newline +
                "UID:ec21af1b-4592-422f-bebd-d824133ea8fd" + newline +
                "DTSTAMP:20190415T173206Z" + newline +
                "SUMMARY;LANGUAGE=en-us:Operating System HW4" + newline +
                "DTSTART:20190427T035900Z" + newline +
                "DURATION:PT1H" + newline +
                "DESCRIPTION:RESUBMIT" + newline +
                "END:VEVENT" + newline +
                "END:VCALENDAR" +  newline);

        Load load = new Load(this.controller, stringReader);
        Thread thread = Parser.getParserThread(load, "ICS", null, false);
        thread.run();
        assertFalse(this.controller.getAllCourses().isEmpty());
        assertFalse(this.controller.getAllCourses().containsKey("Operating System"));
        assertTrue(this.controller.getAllCourses().containsKey("Unknown Course"));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  csv Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void csvTest() {
        this.stringReader = new StringReader("Course Name, Deadline Name, Month, Date, Year, Hour, Minute, Status, Link"
                + newline
                + "Operating System,HW4,4,26,2019,23,59,WAIT FOR SUBMISSION,link" + newline);
        this.loadTest("CSV");
    }

    @Test
    public void csvNoLinkTest() {
        this.stringReader = new StringReader("Course Name, Deadline Name, Month, Date, Year, Hour, Minute, Status"
                + newline
                + "Operating System,HW4,4,26,2019,23,59,WAIT FOR SUBMISSION," + newline);
        this.loadTest("CSV");
    }

    @Test
    public void csvLengthTest() {
        this.stringReader = new StringReader("Course Name, Deadline Name, Month, Date, Year, Hour, Minute, Status"
                + newline
                + "Operating System,HW4,4,26,2019,WAIT FOR SUBMISSION," + newline);
        this.loadTestFailed("CSV");
    }

    @Test
    public void csvStatusTest() {
        this.stringReader = new StringReader("Course Name, Deadline Name, Month, Date, Year, Hour, Minute, Status"
                + newline
                + "Operating System,HW4,4,26,2019,23,59" + newline);
        this.loadTestFailed("CSV");
    }

    @Test
    public void csvBadFormatTest() {
        this.stringReader = new StringReader("Course Name, Deadline Name, Month, Date, Year, Hour, Minute, Status"
                + newline
                + "Operating System,HW5,4,2019,23,59,WAIT FOR SUBMISSION" + newline
                + "Operating System,HW5,APR,4,2019,23,59,WAIT FOR SUBMISSION" + newline
                + "Operating System,HW6,14,2019,23,59,WAIT FOR SUBMISSION" + newline);
        this.loadTestFailed("CSV");
    }

    @Test
    public void csvBadIOTest() {
        this.stringReader = new StringReader("Course Name, Deadline Name, Month, Date, Year, Hour, Minute, Status"
                + newline
                + "Operating System,HW4,4,26, " + "\0x80" + ",WAIT FOR SUBMISSION" + newline);
        this.loadTestFailed("CSV");
    }

    /////////////////////////////////////////////////////////////////////////
    ////  Null Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void nullTest() {
        Load load = new Load(this.controller, stringReader);
        Thread thread1 = Parser.getParserThread(load, "JSON", null, false);
        thread1.run();
        assertTrue(this.controller.getAllCourses().isEmpty());
        Thread thread2 = Parser.getParserThread(load, "ICS", null, false);
        thread2.run();
        assertTrue(this.controller.getAllCourses().isEmpty());
        Thread thread3 = Parser.getParserThread(load, "CSV", null, false);
        thread3.run();
        assertTrue(this.controller.getAllCourses().isEmpty());
    }
}
package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the Course class.
 */
public class CourseTest {
    private Course csci4210;
    private Course csci4220;

    /**
     * Because all tests run this method before executing, ALL TESTS WILL FAIL until
     * addDeadline() does not throw exceptions. Also, any incorrectness in this
     * method may have unforeseen consequences elsewhere in the tests, so it is a good
     * idea to make sure this method is correct before moving on to others.
     */
    // Tests that are intended to verify addDeadline() should not use variables
    // declared in this setUp method
    @Before
    public void setUp() {
        csci4210 = new Course("Spring 2019     CSCI4210     Operating Systems");
        csci4220 = new Course("Network Programming");

        Deadline OShw2 = new Deadline(2019,2,15,23,59,
                "Homework 2","Spring 2019     CSCI4210     Operating Systems", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline OSpj1 = new Deadline(2019,3,14,23,59,
                "Project 1","Spring 2019     CSCI4210     Operating Systems", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);

        csci4210.addDeadline(OShw2.getName(), OShw2);
        csci4210.addDeadline(OSpj1.getName(), OSpj1);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  Constructor
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void testCtor() {
        Course course1 = new Course("test");
        assertNotNull(course1);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getCourseName Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getCourseNameTest() {
        Course ds = new Course("Data Structures");
        assertEquals("Data Structures", ds.getCourseName());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  size Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void sizeTest() {
        assertEquals(2, csci4210.size());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  addDeadline Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void addDeadlineTest() {
        Course ds = new Course("Data Structures");
        ds.addDeadline("Homework 6: Crossword Blackout",
                new Deadline(2019,3,14,23,59,
                        "Homework 6: Crossword Blackout","Data Structures ", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE));
        assertFalse(ds.getDeadlines().isEmpty());
    }

    @Test
    public void addDeadlineObjTest() {
        Course ds = new Course("Data Structures");
        ds.addDeadline(new Deadline(2019,3,14,23,59,
                        "Homework 6: Crossword Blackout","Data Structures ", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE));
        assertFalse(ds.getDeadlines().isEmpty());
    }

    @Test
    public void addDeadlineDirectTest() {
        Course ds = new Course("Data Structures");
        ds.addDeadline("Homework 6: Crossword Blackout",
                2019,3,14,23,59,Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertFalse(ds.getDeadlines().isEmpty());
    }

    @Test
    public void addDeadlineDirectInvalidTest() {
        Course ds = new Course("Data Structures");
        try {
            ds.addDeadline("Homework 6: Crossword Blackout",
                    2019, 3, 32, 23, 59, Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"day\"(32)  should be in range [1,31]", e.getMessage());
        }
        assertTrue(ds.getDeadlines().isEmpty());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  append Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void appendTest() {
        assertEquals(2, csci4210.size());
        Course os = new Course("Spring 2019     CSCI4210     Operating Systems");
        os.addDeadline("Project 2", 2019, 3, 14, 23, 59, Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        csci4210.append(os);
        assertEquals(3, csci4210.size());
    }

    @Test
    public void appendDiffTest() {
        assertEquals(2, csci4210.size());
        Course os = new Course("CSCI4210     Operating Systems");
        os.addDeadline("Project 2", 2019, 3, 14, 23, 59, Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        csci4210.append(os);
        assertEquals(2, csci4210.size());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  removeDeadline Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void removeDeadlineTest() {
        assertEquals(2, csci4210.size());
        csci4210.removeDeadline("Homework 2");
        assertEquals(1, csci4210.size());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getDeadlines Test
    ///////////////////////////////////////////////////////////////////////

    @Test
    public void getDeadlinesTest() {
        Course ds = new Course("Data Structures");
        Deadline ds_hw6 = new Deadline(2019,3,14,23,59,
                        "Homework 6: Crossword Blackout","Data Structures ", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        ds.addDeadline("Homework 6: Crossword Blackout", ds_hw6);
        assertTrue(ds.getDeadlines().containsKey(ds_hw6.getName()));
        assertTrue(ds.getDeadlines().containsValue(ds_hw6));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  equals Test
    ///////////////////////////////////////////////////////////////////////

    @Test
    public void equalsTest() {
        assertFalse(csci4210.equals(csci4220));
        Course opsys = new Course("Spring 2019     CSCI4210     Operating Systems");
        assertFalse(csci4210.equals(opsys));
        Deadline OShw2 = new Deadline(2019,2,15,23,59,
                "Homework 2","Spring 2019     CSCI4210     Operating Systems", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline OSpj1 = new Deadline(2019,3,14,23,59,
                "Project 1","Spring 2019     CSCI4210     Operating Systems", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        opsys.addDeadline(OShw2);
        opsys.addDeadline(OSpj1);
        assertTrue(csci4210.equals(opsys));
        assertEquals(csci4210, csci4210);
        assertNotEquals(csci4210, null);
        assertNotEquals(csci4210, new Object());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  hashCode Test
    ///////////////////////////////////////////////////////////////////////

    @Test
    public void hashCodeTest() {
        Course opsys = new Course("Spring 2019     CSCI4210     Operating Systems");
        Deadline OShw2 = new Deadline(2019,2,15,23,59,
                "Homework 2","Spring 2019     CSCI4210     Operating Systems", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline OSpj1 = new Deadline(2019,3,14,23,59,
                "Project 1","Spring 2019     CSCI4210     Operating Systems", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        opsys.addDeadline(OShw2);
        opsys.addDeadline(OSpj1);
        HashSet<Course> courses = new HashSet<>();
        courses.add(csci4210);
        assertFalse(courses.isEmpty());
        courses.add(opsys);
        assertEquals(1,courses.size());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  toString Test
    ///////////////////////////////////////////////////////////////////////

    @Test
    public void toStringTest() {
        assertEquals("Course {courseName='Spring 2019     CSCI4210     Operating Systems'}",
                csci4210.toString());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  clone Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void cloneTest() {
        Course c = null;
        try {
            c = csci4210.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertNotNull(c);
        assertEquals(csci4210.getCourseName(), c.getCourseName());
        assertEquals(csci4210.getDeadlines(), c.getDeadlines());
        assertEquals(csci4210, c);
        assertNotSame(csci4210, c);
    }
}
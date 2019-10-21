package model;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the Deadline class.
 */
public class DeadlineTest {
    private Deadline hw1;

    /**
     * Because all tests run this method before executing, ALL TESTS WILL FAIL until
     * the constructor does not throw exceptions. Also, any incorrectness in this
     * method may have unforeseen consequences elsewhere in the tests, so it is a good
     * idea to make sure this method is correct before moving on to others.
     */
    // Tests that are intended to verify the constructor should not use variables
    // declared in this setUp method
    @Before
    public void setUp(){
        // int year, int month, int date, int hrs, int min
        CalendarWrapper date1 = new CalendarWrapper(2019, CalendarWrapper.DECEMBER, 1, 23, 59);
        hw1 = new Deadline(date1,"Deadline Name","Course Name");
    }

    /////////////////////////////////////////////////////////////////////////
    ////  Constructor
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void testCtor() {
        Deadline ds_hw6 = new Deadline(new CalendarWrapper(2019, CalendarWrapper.OCTOBER, 24,23,59),
                "Data Structures Homework 6", "Fall 2019     CSCI1200     Data Structures");
        assertNotNull(ds_hw6);
    }

    @Test
    public void testDirectTimeCtor() {
        Deadline ds_hw6 = new Deadline(2019, 10, 24, 23, 59,
                "Data Structures Homework 6", "Fall 2019     CSCI1200     Data Structures");
        assertNotNull(ds_hw6);
    }

    @Test
    public void testInvalidTimeCtor() {
        Deadline ds_hw6_1 = null;
        try {
            ds_hw6_1 = new Deadline(2019, 13, 24, 23, 59,
                    "Data Structures Homework 6", "Fall 2019     CSCI1200     Data Structures");
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"month\"(13) should be in range [1,12]", e.getMessage());
        }
        assertNull(ds_hw6_1);
        Deadline ds_hw6_2 = null;
        try {
            ds_hw6_2 = new Deadline(2019, 10, 34, 23, 59,
                    "Data Structures Homework 6", "Fall 2019     CSCI1200     Data Structures");
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"day\"(34)  should be in range [1,31]", e.getMessage());
        }
        assertNull(ds_hw6_2);
        Deadline ds_hw6_3 = null;
        try {
            ds_hw6_3 = new Deadline(2019, 10, 24, 25, 59,
                    "Data Structures Homework 6", "Fall 2019     CSCI1200     Data Structures");
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"hour\"(25)  should be in range [0,23]", e.getMessage());
        }
        assertNull(ds_hw6_3);
        Deadline ds_hw6_4 = null;
        try {
            ds_hw6_4 = new Deadline(2019, 10, 24, 23, 62,
                    "Data Structures Homework 6", "Fall 2019     CSCI1200     Data Structures");
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"minute\"(62)  should be in range [0,59]", e.getMessage());
        }
        assertNull(ds_hw6_4);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMinute Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMinuteTest() {
        assertEquals(59, hw1.getMinute());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getHour Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getHourTest() {
        assertEquals(23, hw1.getHour());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getDateTest() {
        assertEquals(1, hw1.getDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMonth Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMonthTest() {
        assertEquals(12, hw1.getMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getYear Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getYearTest() {
        assertEquals(2019, hw1.getYear());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getCourseName Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getCourseTest() {
        assertEquals("Course Name", hw1.getCourse());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getName Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getNameTest() {
        assertEquals("Deadline Name", hw1.getName());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  isAfter Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void isAfterTest() {
        Deadline past = new Deadline(1990, 1, 1, 1, 1,
                "A Very Old Date", "Old Course");
        Deadline future = new Deadline(2050, 1, 1, 1, 1,
                "Future", "Future");
        CalendarWrapper standard =
                new CalendarWrapper(2019, CalendarWrapper.JANUARY, 1, 1, 1);
        assertTrue(future.isAfter(standard));
        assertFalse(past.isAfter(standard));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  isBefore Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void isBeforeTest() {
        Deadline past = new Deadline(1990, 1, 1, 1, 1,
                "A Very Old Date", "Old Course");
        Deadline future = new Deadline(2050, 1, 1, 1, 1,
                "Future", "Future");
        CalendarWrapper standard =
                new CalendarWrapper(2019, CalendarWrapper.JANUARY, 1, 1, 1);
        assertFalse(future.isBefore(standard));
        assertTrue(past.isBefore(standard));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getRemainPeriod Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getRemainPeriodTest() {
        Deadline NewYearEve2019 =
                new Deadline(2018, 12,31,23,59,"New Year", "2019");
        CalendarWrapper Christmas2018 = new CalendarWrapper(2018, CalendarWrapper.DECEMBER,25,0,0);
        assertEquals(6,
                NewYearEve2019.getRemainPeriod(Christmas2018).getKey().getDays());
        assertEquals(23,
                NewYearEve2019.getRemainPeriod(Christmas2018).getKey().getHours());
        assertEquals(59,
                NewYearEve2019.getRemainPeriod(Christmas2018).getKey().getMinutes());
        assertEquals(true,
                NewYearEve2019.getRemainPeriod(Christmas2018).getValue());
    }

    @Test
    public void getRemainPeriodAfterTest() {
        CalendarWrapper NewYearEve2019 = new CalendarWrapper(2018, CalendarWrapper.DECEMBER,31,23,59);
        Deadline Christmas2018 =
                new Deadline(2018, 12,25,0,0,"Christmas", "2018");
        assertEquals(6,
                Christmas2018.getRemainPeriod(NewYearEve2019).getKey().getDays());
        assertEquals(23,
                Christmas2018.getRemainPeriod(NewYearEve2019).getKey().getHours());
        assertEquals(59,
                Christmas2018.getRemainPeriod(NewYearEve2019).getKey().getMinutes());
        assertEquals(false,
                Christmas2018.getRemainPeriod(NewYearEve2019).getValue());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getRemainingText Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getRemainingTextTest() {
        Deadline NewYearEve2019 =
                new Deadline(2018, 12,31,23,59,"New Year", "2019");
        CalendarWrapper Christmas2018 = new CalendarWrapper(2018, CalendarWrapper.DECEMBER,25,0,0);
        CalendarWrapper Nov2018 = new CalendarWrapper(2018, CalendarWrapper.NOVEMBER,1,0,0);
        assertEquals("06 Days 23 Hours 59 Minutes Left.", NewYearEve2019.getRemainingText(Christmas2018));
        assertEquals("01 Months 30 Days 23 Hours 59 Minutes Left.", NewYearEve2019.getRemainingText(Nov2018));
    }

    @Test
    public void getRemainingTextAfterTest() {
        CalendarWrapper NewYearEve2019 =
                new CalendarWrapper(2018, CalendarWrapper.DECEMBER,31,23,59);
        Deadline Christmas2018 =
                new Deadline(2018, 12,25,0,0,"Christmas", "2018");
        assertEquals("06 Days 23 Hours 59 Minutes Ago.",
                Christmas2018.getRemainingText(NewYearEve2019));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  compareTo Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void compareToTest() {
        Deadline NewYearEve2019 =
                new Deadline(2018, 12,31,23,59,"New Year", "2019");
        Deadline NewYearEve2019_0 =
                new Deadline(2018, 12,31,23,59,"New Year", "2019");
        Deadline Christmas2018 = new Deadline(
                new CalendarWrapper(2018, CalendarWrapper.DECEMBER,25,0,0),
                "Christmas 2018", "2018");
        assertTrue(Christmas2018.compareTo(NewYearEve2019) < 0);
        assertEquals(0, NewYearEve2019_0.compareTo(NewYearEve2019));
        assertTrue(NewYearEve2019.compareTo(Christmas2018) > 0);
    }

    @Test
    public void compareToNowTest() {
        Deadline future =
                new Deadline(2020, 12,31,23,59,"Future", "Future");
        Deadline past =
                new Deadline(2003, 12,31,23,59,"Past", "Past");
        assertTrue(past.compareTo(future)<0);
        assertTrue(future.compareTo(past)>0);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  parseDate Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void parseDateTest() {
        String valid = "(due 04/26/2019 @ 23:59)";
        CalendarWrapper d = Deadline.parseDate(valid);
        assertNotNull(d);
        assertEquals(d,
                (new CalendarWrapper(2019,CalendarWrapper.APRIL,26,23,59)));
    }

    @Test
    public void parseTeamDateTest() {
        String valid = "(teams lock 04/26/2019 @ 23:59)";
        CalendarWrapper d = Deadline.parseDate(valid);
        assertNotNull(d);
        assertEquals(d,
                (new CalendarWrapper(2019,CalendarWrapper.APRIL,26,23,59)));
    }

    @Test
    public void parseDateTestStartsWNum() {
        CalendarWrapper d = null;
        String invalid1 = "04/26/2019 @ 23:59";
        try {
            d = Deadline.parseDate(invalid1);
        } catch (RuntimeException e) {
            assertEquals(invalid1 + " is not a valid due text", e.getMessage());
        }
        assertNull(d);
    }

    @Test
    public void parseDateTestInvalid() {
        CalendarWrapper d = null;
        String invalid2 = "(due 4/26/2019 @ 23:59)";
        try {
            d = Deadline.parseDate(invalid2);
        } catch (RuntimeException e) {
            assertEquals(invalid2 + " is not a valid due text", e.getMessage());
        }
        assertNull(d);
    }

    @Test
    public void parseDateTestInvalidMonth() {
        CalendarWrapper d = null;
        String invalid3 = "(due 13/26/2019 @ 23:59)";
        try {
            d = Deadline.parseDate(invalid3);
        } catch (RuntimeException e) {
            assertEquals("13" + " is not a valid month text", e.getMessage());
        }
        assertNull(d);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  equals Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void equalsTest() {
        Deadline Christmas2018 = new Deadline(
                new CalendarWrapper(2018, CalendarWrapper.DECEMBER,25,0,0),
                "Christmas 2018", "2018");
        Deadline Christmas2018_2 = new Deadline(2018,12,25,0,0,
                "Christmas 2018", "2018");
        Deadline Christmas2018_3 = new Deadline(2018,12,25,0,0,
                "Christmas 2018", "2018");
        assertEquals(Christmas2018_2, Christmas2018_3);
        assertEquals(Christmas2018, Christmas2018_2);
        assertEquals(Christmas2018, Christmas2018);
        assertNotEquals(Christmas2018, null);
        assertNotEquals(Christmas2018, new Object());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  hashCode Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void hashCodeTest() {
        HashSet<Deadline> set = new HashSet<>();
        Deadline hw2 = new Deadline(new CalendarWrapper(2019, CalendarWrapper.DECEMBER, 1, 23, 59),
                "Deadline Name 2","Course Name 2");
        Deadline hw3 = new Deadline(new CalendarWrapper(2019, CalendarWrapper.DECEMBER, 1, 12, 59),
                "Deadline Name","Course Name");
        Deadline hw4 = new Deadline(new CalendarWrapper(2019, CalendarWrapper.DECEMBER, 1, 23, 59),
                "Deadline Name","Course Name");
        set.add(hw1);
        set.add(hw1);
        set.add(hw2);
        set.add(hw2);
        set.add(hw4);
        assertEquals(2, set.size());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  toString Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void toStringTest() {
        assertEquals("Deadline Name <Course Name> (12/01/2019 @ 23:59)",
                hw1.toString());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  clone Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void cloneTest() {
        Deadline hw1_0 = null;
        try {
            hw1_0 = (Deadline) hw1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertNotNull(hw1_0);
        assertEquals(hw1.getName(), hw1_0.getName());
        assertEquals(hw1.getCourse(), hw1_0.getCourse());
        assertEquals(hw1, hw1_0);
        assertNotSame(hw1, hw1_0);
    }
}
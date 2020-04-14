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
    private Deadline DBhw1;
    private Deadline AIhw1;
    private Deadline OShw2;

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
        CalendarWrapper db = new CalendarWrapper(2019, CalendarWrapper.JANUARY, 24, 23, 59);
        CalendarWrapper ai = new CalendarWrapper(2019, CalendarWrapper.JANUARY, 28, 23, 59);
        DBhw1 = new Deadline(db,"Database hw1","Database", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        AIhw1 = new Deadline(ai,"AI project1","AI", Deadline.STATUS.LATE_RESUBMIT, Deadline.LINK.NONE);
        OShw2 = new Deadline(2019,2,15,23,59,
                "Homework 2","Spring 2019     CSCI4210     Operating Systems", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  Constructor
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void testCtor() {
        Deadline ds_hw6 = new Deadline(new CalendarWrapper(2019, CalendarWrapper.MARCH, 12,23,23),
                "Data Structures Homework 6", "Spring 2019     CSCI1200     Data Structures", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertNotNull(ds_hw6);
    }

    @Test
    public void testDirectTimeCtor() {
        Deadline ds_hw6 = new Deadline(2019, 3, 12, 23, 23,
                "Data Structures Homework 6", "Spring 2019     CSCI1200     Data Structures", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertNotNull(ds_hw6);
    }

    @Test
    public void testInvalidTimeCtor() {
        Deadline ds_hw6_1 = null;
        try {
            ds_hw6_1 = new Deadline(2019, 13, 12, 23, 23,
                    "Data Structures Homework 6", "Spring 2019     CSCI1200     Data Structures", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"month\"(13) should be in range [1,12]", e.getMessage());
        }
        assertNull(ds_hw6_1);
        Deadline ds_hw6_2 = null;
        try {
            ds_hw6_2 = new Deadline(2019, 6, 34, 23, 23,
                    "Data Structures Homework 6", "Spring 2019     CSCI1200     Data Structures", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"day\"(34)  should be in range [1,31]", e.getMessage());
        }
        assertNull(ds_hw6_2);
        Deadline ds_hw6_3 = null;
        try {
            ds_hw6_3 = new Deadline(2019, 6, 12, 25, 23,
                    "Data Structures Homework 6", "Spring 2019     CSCI1200     Data Structures", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        } catch (CalendarWrapper.CalendarFormatException e) {
            assertEquals("Error: \"hour\"(25)  should be in range [0,23]", e.getMessage());
        }
        assertNull(ds_hw6_3);
    }

    @Test
    public void testStatusCtor() {
        CalendarWrapper ai = new CalendarWrapper(2019, CalendarWrapper.JANUARY, 28, 23, 59);
        Deadline AIHW1 = new Deadline(ai,"AI project1","AI", Deadline.STATUS.LATE_RESUBMIT, Deadline.LINK.NONE);
        assertNotNull(AIHW1);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  STATUS Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void statusTest() {
        Deadline.STATUS s = new Deadline.STATUS();
        assertNotNull(s);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getAllStatus Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getAllStatusTest() {
        Deadline.STATUS s = new Deadline.STATUS();
        assertNotNull(s);
        String[] allStatus = Deadline.STATUS.getAllStatus();
        assertEquals(8, allStatus.length);
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.DEFAULT));
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.RESUBMIT));
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.FINISHED));
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.LATE_SUBMIT));
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.LATE_RESUBMIT));
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.OVERDUE_SUBMISSION));
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.MUST_ON_TEAM));
        assertTrue(Arrays.asList(allStatus).contains(Deadline.STATUS.NO_SUBMISSION));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMinute Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMinuteTest() {
        Deadline OSpj1 = new Deadline(2019, 3, 14, 23, 0, "Project 1", "OS", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertEquals(0, OSpj1.getMinute());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getHour Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getHourTest() {
        Deadline OSpj1 = new Deadline(2019, 3, 14, 23, 59, "Project 1", "OS", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertEquals(23, OSpj1.getHour());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getDateTest() {
        Deadline OSpj1 = new Deadline(2019, 3, 14, 23, 59, "Project 1", "OS", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertEquals(14, OSpj1.getDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMonth Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMonthTest() {
        Deadline OSpj1 = new Deadline(2019, 3, 14, 23, 59, "Project 1", "OS", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertEquals(3, OSpj1.getMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getYear Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getYearTest() {
        Deadline OSpj1 = new Deadline(2019, 3, 14, 23, 59, "Project 1", "OS", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertEquals(2019, OSpj1.getYear());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getCourseName Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getCourseTest() {
        assertEquals("Database", DBhw1.getCourseName());
        assertEquals("AI", AIhw1.getCourseName());
        assertEquals("Spring 2019     CSCI4210     Operating Systems", OShw2.getCourseName());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getName Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getNameTest() {
        assertEquals("Database hw1",DBhw1.getName());
        assertEquals("AI project1",AIhw1.getName());
        assertEquals("Homework 2", OShw2.getName());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getStatus Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getStatusTest() {
        assertEquals("LATE RESUBMIT", AIhw1.getStatus());
        assertEquals(Deadline.STATUS.DEFAULT, OShw2.getStatus());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getLink Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getLinkTest() {
        assertEquals("", AIhw1.getLink());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  isAfter Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void isAfterTest() {
        Deadline past = new Deadline(1990, 1, 1, 1, 1,
                "A Very Old Date", "Old Course", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline future = new Deadline(2050, 1, 1, 1, 1,
                "Future", "Future", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        CalendarWrapper standard = new CalendarWrapper(2019, CalendarWrapper.JANUARY, 1, 1, 1);
        assertTrue(future.isAfter(standard));
        assertFalse(past.isAfter(standard));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  isBefore Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void isBeforeTest() {
        Deadline past = new Deadline(1990, 1, 1, 1, 1,
                "A Very Old Date", "Old Course", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline future = new Deadline(2050, 1, 1, 1, 1,
                "Future", "Future", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        CalendarWrapper standard = new CalendarWrapper(2019, CalendarWrapper.JANUARY, 1, 1, 1);
        assertFalse(future.isBefore(standard));
        assertTrue(past.isBefore(standard));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getRemainPeriod Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getRemainPeriodTest() {
        Deadline NewYearEve2019 =
                new Deadline(2018, 12,31,23,59,"New Year", "2019", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        CalendarWrapper Christmas2018 = new CalendarWrapper(2018, Calendar.DECEMBER,25,0,0);
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
        CalendarWrapper NewYearEve2019 =
                new CalendarWrapper(2018, CalendarWrapper.DECEMBER,31,23,59);
        Deadline Christmas2018 =
                new Deadline(2018, 12,25,0,0,"Christmas", "2018", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
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
                new Deadline(2018, 12,31,23,59,"New Year", "2019", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        CalendarWrapper Christmas2018 =
                new CalendarWrapper(2018, CalendarWrapper.DECEMBER,25,0,0);
        CalendarWrapper Nov2018 =
                new CalendarWrapper(2018, CalendarWrapper.NOVEMBER,1,0,0);
        assertEquals("06 Days 23 Hours 59 Minutes Left.", NewYearEve2019.getRemainingText(Christmas2018));
        assertEquals("01 Months 30 Days 23 Hours 59 Minutes Left.", NewYearEve2019.getRemainingText(Nov2018));
    }

    @Test
    public void getRemainingTextAfterTest() {
        CalendarWrapper NewYearEve2019 = new CalendarWrapper(2018, CalendarWrapper.DECEMBER,31,23,59);
        Deadline Christmas2018 =
                new Deadline(2018, 12,25,0,0,"Christmas", "2018", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertEquals("06 Days 23 Hours 59 Minutes Ago.",
                Christmas2018.getRemainingText(NewYearEve2019));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  compareTo Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void compareToTest() {
        Deadline NewYearEve2019 =
                new Deadline(2018, 12,31,23,59,"New Year", "2019", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline NewYearEve2019_0 =
                new Deadline(2018, 12,31,23,59,"New Year", "2019", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline Christmas2018 = new Deadline(
                new CalendarWrapper(2018, CalendarWrapper.DECEMBER,25,0,0),
                "Christmas 2018", "2018", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertTrue(Christmas2018.compareTo(NewYearEve2019) < 0);
        assertEquals(0, NewYearEve2019_0.compareTo(NewYearEve2019));
        assertTrue(NewYearEve2019.compareTo(Christmas2018) > 0);
    }

    @Test
    public void compareToNowTest() {
        Deadline future =
                new Deadline(2020, 12,31,23,59,"Future", "Future", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline past =
                new Deadline(2003, 12,31,23,59,"Past", "Past", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        assertTrue(past.compareTo(future)>0);
        assertTrue(future.compareTo(past)<0);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  parseDate Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void parseDateTest() {
        String valid = "(due 04/26/2019 @ 23:59)";
        CalendarWrapper d = Deadline.parseDate(valid);
        assertNotNull(d);
        assertEquals(d.toString(),
                (new CalendarWrapper(2019,CalendarWrapper.APRIL,26,23,59).toString()));
    }

    @Test
    public void parseTeamDateTest() {
        String valid = "(teams lock 04/26/2019 @ 23:59)";
        CalendarWrapper d = Deadline.parseDate(valid);
        assertNotNull(d);
        assertEquals(d.toString(),
                (new CalendarWrapper(2019,CalendarWrapper.APRIL,26,23,59).toString()));
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
        } catch (CalendarWrapper.CalendarFormatException e) {
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
                "Christmas 2018", "2018", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline Christmas2018_2 = new Deadline(2018,12,25,0,0,
                "Christmas 2018", "2018", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
        Deadline Christmas2018_3 = new Deadline(2018,12,25,0,0,
                "Christmas 2018", "2018", Deadline.STATUS.DEFAULT, Deadline.LINK.NONE);
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
        set.add(DBhw1);
        set.add(AIhw1);
        set.add(OShw2);
        assertEquals(3, set.size());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  toString Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void toStringTest() {
        assertEquals("Homework 2 <Spring 2019     CSCI4210     Operating Systems> (02/15/2019 @ 23:59)",
                OShw2.toString());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  clone Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void cloneTest() {
        Deadline OShw2_0 = null;
        try {
            OShw2_0 = OShw2.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertNotNull(OShw2_0);
        assertEquals(OShw2.getName(), OShw2_0.getName());
        assertEquals(OShw2.getCourseName(), OShw2_0.getCourseName());
        assertEquals(OShw2, OShw2_0);
        assertNotSame(OShw2, OShw2_0);
    }
}
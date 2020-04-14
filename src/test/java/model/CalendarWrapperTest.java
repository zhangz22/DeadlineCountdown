package model;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the CalendarWrapper class.
 */
public class CalendarWrapperTest {
    private CalendarWrapper newYear2018;
    private CalendarWrapper Mar03302333;

    /**
     * Because all tests run this method before executing, ALL TESTS WILL FAIL until
     * setDate(), setMonthYear() and setYear() does not throw exceptions. Also,
     * any incorrectness in this method may have unforeseen consequences elsewhere
     * in the tests, so it is a good idea to make sure this method is correct before
     * moving on to others.
     */
    // Tests that are intended to verify setDate(), setMonthYear() and setYear() should
    // not use variables declared in this setUp method
    @Before
    public void setUp() {
        CalendarWrapper c = new CalendarWrapper();
        newYear2018 = new CalendarWrapper();
        newYear2018.setYear(2018);
        newYear2018.setMonth(1);
        newYear2018.setDay(1);
        newYear2018.setHour(0);
        newYear2018.setMinute(0);
        Mar03302333 = new CalendarWrapper(2019,3,30,23,33);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  Constructor
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void testCtor() {
        CalendarWrapper c2 = new CalendarWrapper();
        assertNotNull(c2);
    }

    @Test
    public void testCtorArgs() {
        CalendarWrapper c2 = new CalendarWrapper(2019,3,30,23,33);
        assertNotNull(c2);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMinuteOfHour Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMinuteOfHourTest() {
        assertEquals(0,newYear2018.getMinuteOfHour());
        assertEquals(33, Mar03302333.getMinuteOfHour());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getHourOfDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getHourOfDayTest() {
        assertEquals(0, newYear2018.getHourOfDay());
        assertEquals(23, Mar03302333.getHourOfDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getDateTest() {
        assertEquals(1, newYear2018.getDay());
        assertEquals(30, Mar03302333.getDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getWeekDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getWeekDayTest() {
        assertEquals(1, newYear2018.getWeekDay());
        assertEquals(6, Mar03302333.getWeekDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMonth Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMonthTest() {
        assertEquals(1, newYear2018.getMonth());
        assertEquals(3, Mar03302333.getMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getWeekDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getYearTest() {
        assertEquals(2018, newYear2018.getYear());
        assertEquals(2019, Mar03302333.getYear());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMaxDayNumOfMonth Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMaxDayNumOfMonthTest() {
        assertEquals(31, newYear2018.getMaxDayNumOfMonth());
        assertEquals(31, Mar03302333.getMaxDayNumOfMonth());
    }

    @Test
    public void getMaxDayNumOfMonthArgTest() {
        int[] days = {0,31,28,31,30,31,30,31,31,30,31,30,31};
        for (int i = 1; i <= 12; i++) {
            assertEquals(days[i], CalendarWrapper.getMaxDayNumOfMonth(i,2019));
        }
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getWeekDayOfFirstDayInMonth Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getWeekDayOfFirstDayInMonthTest() {
        assertEquals(1, newYear2018.getWeekDayOfFirstDayInMonth());
        assertEquals(5, Mar03302333.getWeekDayOfFirstDayInMonth());
        assertEquals(1, new CalendarWrapper(2019,4,11,12,0).getWeekDayOfFirstDayInMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  setYear Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void setYearTest() {
        newYear2018.setYear(2019);
        assertEquals(2019, newYear2018.getYear());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  setMonthYear Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void setMonthTest() {
        newYear2018.setMonth(5);
        assertEquals(5, newYear2018.getMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  setDate Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void setDateTest() {
        newYear2018.setDay(5);
        assertEquals(5, newYear2018.getDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getTimeStr Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getTimeStrTest() {
        assertEquals("03/30/2019 @ 23:33", CalendarWrapper.getTimeStr(2019, 3, 30, 23, 33));
        assertEquals("06/06/2016 @ 06:06", CalendarWrapper.getTimeStr(2016, 6, 6, 6, 6));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getJavaCalendar Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getJavaCalendarTest() {
        assertEquals(13, CalendarWrapper.createJavaCalendar(null, 2019, 4,13,23,59).get(Calendar.DAY_OF_MONTH));
        assertEquals(14, CalendarWrapper.createJavaCalendar(null, 2019, 4,13,24,59).get(Calendar.DAY_OF_MONTH));
        TimeZone zone = TimeZone.getTimeZone("America/New_York");
        assertEquals(14, CalendarWrapper.createJavaCalendar(zone, 2019, 4,13,24,59).get(Calendar.DAY_OF_MONTH));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  createJavaDate Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    @SuppressWarnings("deprecation")
    public void createJavaDateTest() {
        assertEquals(2019-1900, CalendarWrapper.createJavaDate(2019, 4,13,23,59).getYear());
        assertEquals(Calendar.APRIL, CalendarWrapper.createJavaDate(2019, 4,13,23,59).getMonth());
        assertEquals(13, CalendarWrapper.createJavaDate(2019, 4,13,23,59).getDate());
        assertEquals(23, CalendarWrapper.createJavaDate(2019, 4,13,23,59).getHours());
        assertEquals(59, CalendarWrapper.createJavaDate(2019, 4,13,23,59).getMinutes());

    }

    /////////////////////////////////////////////////////////////////////////
    ////  now Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void nowTest() {
        assertEquals(CalendarWrapper.now().getYear(), DateTime.now().getYear());
        assertEquals(CalendarWrapper.now().getMonth(), DateTime.now().getMonthOfYear());
        assertEquals(CalendarWrapper.now().getDay(), DateTime.now().getDayOfMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  clone Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void cloneTest() {
        CalendarWrapper c2 = null;
        try {
            c2 = newYear2018.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertNotNull(c2);
        assertNotSame(c2, newYear2018);
        assertEquals(c2, newYear2018);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  equals Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void equalsTest() {
        CalendarWrapper c2 = new CalendarWrapper(2018,1,1,0,0);
        assertEquals(c2, newYear2018);
        assertEquals(c2, c2);
        assertNotEquals(c2, null);
        assertNotEquals(c2, new Object());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  hashCode Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void hashCodeTest() {
        CalendarWrapper c2 = new CalendarWrapper(2018,1,1,0,0);
        assertEquals(c2.hashCode(), newYear2018.hashCode());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  toString Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void toStringTest() {
        assertEquals("CalendarWrapper (03/30/2019 @ 23:33)", Mar03302333.toString());
    }
}
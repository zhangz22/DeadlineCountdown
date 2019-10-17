package model;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the CalendarWrapper class.
 */
public class CalendarWrapperTest {
    private CalendarWrapper time20190901;
    private CalendarWrapper time20190330;

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
        time20190901 = new CalendarWrapper();
        time20190901.setYear(2019);
        time20190901.setMonth(9);
        time20190901.setDay(1);
        time20190901.setHour(0);
        time20190901.setMinute(0);
        time20190330 = new CalendarWrapper(2019,CalendarWrapper.MARCH,30,23,59);
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
        CalendarWrapper c2 = new CalendarWrapper(2019,CalendarWrapper.MARCH,30,23,33);
        assertNotNull(c2);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  setYear Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void setYearTest() {
        time20190901.setYear(2018);
        assertEquals(2018, time20190901.getYear());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  setMonthYear Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void setMonthTest() {
        time20190901.setMonth(5);
        assertEquals(5, time20190901.getMonth());
    }

    @Test
    public void setMonthExceptTest() {
        try {
            time20190901.setMonth(13);
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "Error: \"month\" should be in range [1,12]");
        }
        assertEquals(9, time20190901.getMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  setDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void setDayTest() {
        time20190901.setDay(5);
        assertEquals(5, time20190901.getDay());
    }

    @Test
    public void setDayExceptTest() {
        try {
            time20190901.setDay(32);
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "Error: \"day\" should be in range [1,31]");
        }
        assertEquals(1, time20190901.getDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMinuteOfHour Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMinuteOfHourTest() {
        assertEquals(0, time20190901.getMinuteOfHour());
        assertEquals(59, time20190330.getMinuteOfHour());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getHourOfDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getHourOfDayTest() {
        assertEquals(0, time20190901.getHourOfDay());
        assertEquals(23, time20190330.getHourOfDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getDayTest() {
        assertEquals(1, time20190901.getDay());
        assertEquals(30, time20190330.getDay());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getWeekDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getWeekDayTest() {
        assertEquals(0, time20190901.getWeekDay());  // Sunday
        assertEquals(6, time20190330.getWeekDay());  // Saturday
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getMonth Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getMonthTest() {
        assertEquals(9, time20190901.getMonth());
        assertEquals(3, time20190330.getMonth());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  getWeekDay Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void getYearTest() {
        assertEquals(2019, time20190901.getYear());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  isAfter Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void isAfterTest() {
        assertTrue(time20190901.isAfter(time20190330));
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
    ////  now Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void nowTest() {
        assertEquals(CalendarWrapper.now().getYear(), Calendar.getInstance().get(Calendar.YEAR));
        assertEquals(CalendarWrapper.now().getMonth(), Calendar.getInstance().get(Calendar.MONTH) + 1);
        assertEquals(CalendarWrapper.now().getDay(), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    /////////////////////////////////////////////////////////////////////////
    ////  clone Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void cloneTest() {
        CalendarWrapper c2 = null;
        try {
            c2 = time20190330.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertNotNull(c2);
        assertNotSame(c2, time20190330);
        assertEquals(c2, time20190330);
    }

    /////////////////////////////////////////////////////////////////////////
    ////  equals Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void equalsTest() {
        CalendarWrapper c2 = new CalendarWrapper(2019,CalendarWrapper.SEPTEMBER,1,0,0);
        assertEquals(c2, time20190901);
        assertEquals(c2, c2);
        assertNotEquals(c2, null);
        assertNotEquals(c2, new Object());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  hashCode Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void hashCodeTest() {
        CalendarWrapper c2 = new CalendarWrapper(2019,CalendarWrapper.SEPTEMBER,1,0,0);
        assertEquals(c2.hashCode(), time20190901.hashCode());
    }

    /////////////////////////////////////////////////////////////////////////
    ////  toString Test
    /////////////////////////////////////////////////////////////////////////

    @Test
    public void toStringTest() {
        assertEquals("CalendarWrapper (03/30/2019 @ 23:59)", time20190330.toString());
    }
}

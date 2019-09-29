package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        time20190330 = new CalendarWrapper(2019,3,30,23,59);
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
        assertEquals(0, time20190901.getWeekDay());
        assertEquals(6, time20190330.getWeekDay());
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
        assertEquals(2019, time20190330.getYear());
    }
}

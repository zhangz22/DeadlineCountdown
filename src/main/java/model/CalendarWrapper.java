package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @overview
 * This function is applying the adapter design pattern to Java's Calendar library to
 * make some API get the expected behaviour that can coordinate with other APIs in
 * this program
 */
public class CalendarWrapper implements Cloneable {
    /** cal: A Java Calendar instance containing current time.  */
    private Calendar cal;

    /**
     * The basic constructor.
     *
     * @requires None
     * @modifies this.cal
     * @effects create a new CalendarWrapper instance
     */
    public CalendarWrapper() {
        this.cal = new GregorianCalendar();
    }
}

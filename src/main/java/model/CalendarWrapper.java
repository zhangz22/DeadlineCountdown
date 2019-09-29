package model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

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

    /**
     * Constructor.
     *
     * @param year         year number
     * @param month        a number represents the month number. The first month of
     *                     the year starts from 1 (Jan)
     * @param day          the day number; starts from 1 to 31
     * @param hour         the hour number
     * @param minute       the minute number
     * @modifies this.cal
     * @effects create a new CalendarWrapper instance
     */
    public CalendarWrapper(final int year, final int month, final int day,
                           final int hour, final int minute) {
        this.cal = new GregorianCalendar();
        this.setYear(year);
        this.setMonth(month);
        this.setDate(day);
        this.setHour(hour);
        this.setMinute(minute);
    }

    /**
     *
     * This function updates the minute of the time cal is representing
     *
     * @param minute the minute number, starts from 0 to 59
     * @requires None
     * @modifies cal
     * @effects update the minute
     */
    public void setMinute(int minute) {
        this.cal.set(Calendar.MINUTE, minute);
    }

    /**
     * This function updates the hour of the day of the time cal is representing
     *
     * @param hour a number indicating the hour of the day, which is user for the
     *             24-hour clock. Noon and midnight are represented by 0, not by 12/24.
     * @requires None
     * @modifies cal
     * @effects update the hour
     */
    public void setHour(int hour) {
        this.cal.set(Calendar.HOUR_OF_DAY, hour);
    }

    /**
     * This function updates the date of the date cal is representing
     *
     * @param date the date number of a month. The first day of the month has value 1.
     * @requires None
     * @modifies cal
     * @effects update the date
     */
    public void setDate(int date) {
        this.cal.set(Calendar.DATE, date);
    }

    /**
     * This function updates the month of the date cal is representing
     *
     * @param month the month number of a year, starts from 1 (Jan)
     * @requires None
     * @modifies cal
     * @effects update the month
     */
    public void setMonth(int month) {
        this.cal.set(Calendar.MONTH, month-1);
    }

    /**
     * This function updates the year of the date cal is representing
     *
     * @param year the year number
     * @requires None
     * @modifies cal
     * @effects update the year
     */
    public void setYear(int year) {
        this.cal.set(Calendar.YEAR, year);
    }

    /**
     * This function returns the minute of the time cal is representing.
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return a number represents current minute. Starts from 0 to 59
     */
    public int getMinuteOfHour() {
        return this.cal.get(Calendar.MINUTE);
    }

    /**
     * This function returns the hour of the time cal is representing.
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return a number represents current hour. It is used for the 24-hour clock and
     *         starts from 0 to 23
     */
    public int getHourOfDay() {
        return this.cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * This function returns the day of the date cal is representing.
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return a number represents current date. The first day of the month has value 1.
     */
    public int getDay() {
        return this.cal.get(Calendar.DATE);
    }

    /**
     * This function returns the week day of the date cal is representing.
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return a number represents current week number. Starts from 0 as Sunday, 1 as Monday...
     */
    public int getWeekDay() {
        return this.cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * This function returns the month of the date cal is representing
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return the month number of today. The first month of the year starts from 1 (Jan)
     */
    public int getMonth() {
        return this.cal.get(Calendar.MONTH) + 1;
    }

    /**
     * This function returns the year of the date cal is representing
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return a number represents current year
     */
    public int getYear() {
        return this.cal.get(Calendar.YEAR);
    }

    /**
     * This function returns whether this CalendarWrapper's time is after the time
     * represented by {@code another}.
     * @param another the other time object to be compared.
     * @requires anthoer != null
     * @modifies None
     * @effects None
     * @return  {@code true} if the time reprsented by this CalendarWrapper is after
     *          the time represented by {@code another}; {@code false} otherwise.
     */
    public boolean isAfter(CalendarWrapper another) {
        return this.cal.after(another.cal);
    }


    /**
     * This function returns a string representing the given time in the format of
     * { MM/DD/YYYY @ HH:MM }
     *
     * @param year         year number
     * @param month        a number represents the month number. The first month of
     *                     the year starts from 1 (Jan)
     * @param day          the day number; starts from 1 to 31
     * @param hour         the hour number
     * @param minute       the minute number
     * @requires None
     * @modifies None
     * @effects None
     * @return a String representing the given time in the format of { MM/DD/YYYY @ HH:MM }
     */
    public static String getTimeStr(int year, int month, int day, int hour, int minute) {
        return  (((String.valueOf(month).length()) == 1) ? "0" : "") + month + "/" +
                (((String.valueOf(day).length()) == 1) ? "0" : "") + day + "/" +
                year + " @ " +
                (((String.valueOf(hour).length()) == 1) ? "0" : "") + hour + ":" +
                (((String.valueOf(minute).length()) == 1) ? "0" : "") + minute;
    }

    /**
     * This function returns a CalendarWrapper instance representing the current time
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return a new CalendarWrapper instance
     */
    public static CalendarWrapper now() {
        return new CalendarWrapper();
    }

    /**
     * Creates and returns a copy of this object. The precise meaning
     * of "copy" may depend on the class of the object.
     * @return     a clone of this instance.
     * @throws  CloneNotSupportedException  if the object's class does not
     *               support the {@code Cloneable} interface. Subclasses
     *               that override the {@code clone} method can also
     *               throw this exception to indicate that an instance cannot
     *               be cloned.
     * @see java.lang.Cloneable
     */
    @Override
    public CalendarWrapper clone() throws CloneNotSupportedException {
        CalendarWrapper c = (CalendarWrapper) super.clone();
        c.cal = this.cal;
        return c;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * The {@code equals} method implements an equivalence relation
     * on non-null object references
     * @requires None
     * @modifies None
     * @effects None
     * @param   o   the reference object with which to compare.
     * @return  {@code true} if this object is the same as the obj
     *          argument; {@code false} otherwise.
     * @see     #hashCode()
     * @see     java.util.HashMap
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CalendarWrapper that = (CalendarWrapper) o;
        return this.getYear() == that.getYear()
                && this.getMonth() == that.getMonth()
                && this.getDay() == that.getDay()
                && this.getHourOfDay() == that.getHourOfDay()
                && this.getMinuteOfHour() == that.getMinuteOfHour();
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables
     * @requires None
     * @modifies None
     * @effects None
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getYear(), this.getMonth(), this.getDay(),
                this.getHourOfDay(), this.getMinuteOfHour()) * 13;
    }

    /**
     * Returns a string representation of the object.
     * @requires None
     * @modifies None
     * @effects None
     * @return  a string representation of the object.
     */
    @Override
    public String toString() {
        return "CalendarWrapper ("
                + CalendarWrapper.getTimeStr(getYear(), getMonth(),
                        getDay(), getHourOfDay(), getMinuteOfHour())
                + ')';
    }
}

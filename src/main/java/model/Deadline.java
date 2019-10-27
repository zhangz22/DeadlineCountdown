package model;

import javafx.util.Pair;

import java.util.Calendar;
import java.util.Objects;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

/**
 * @overview
 * This class represents a Deadline ADT expression.
 *
 * @abstract_function
 * a deadline contains four properties, a date represented by a joda.time.DateTime,
 * a deadlineName represented by a String, a courseName represented by a String and a status
 * (ready to submit/ resubmit/ late submit...) represented by a String
 *
 * @abstract_fields
 * date -> a DateTime object which represents a deadline time
 * deadlineName -> the deadlineName of a deadline
 * courseName -> the deadlineName of the course
 *
 * @creators
 * @creators
 * Deadline(Calendar date_, String name_, String course_)
 *
 * @mutator
 * This class is immutable
 *
 * @observers
 * TODO
 *
 * @representation_invariant
 * TODO
 */
public final class Deadline implements DeadlineInterface, Comparable<Deadline> {
    private final CalendarWrapper date;
    private final String deadlineName;
    private final String courseName;

    /**
     * Basic constructor for Deadline Object
     * @param date_ a Calendar object which represents a deadline time
     * @param name_ the name of a deadline
     * @param course_ the name of the course
     * @requires date_ != null, name_ != null, course_ != null
     * @modifies date, deadlineName, course
     * @effects create a new Deadline instance
     */
    public Deadline(CalendarWrapper date_, String name_, String course_) {
        this.date = date_;
        this.deadlineName = name_;
        this.courseName = course_;
    }

    /**
     * Basic constructor for Deadline Object
     * @param year the year number
     * @param month the month number of a year, starts from 1 (Jan)
     * @param day the day number of a month
     * @param hour the hour number of a day
     * @param minute the minute number of an hour
     * @param name_ the name of a deadline
     * @param course_ the name of the course
     * @requires name_ != null, course_ != null
     * @modifies date, name, course
     * @effects create a new Deadline instance
     * @throws CalendarWrapper.CalendarFormatException if the number of date is invalid
     */
    public Deadline(int year, int month, int day, int hour,
                             int minute, String name_, String course_)
            throws CalendarWrapper.CalendarFormatException {
        this(new CalendarWrapper(year, month, day ,hour, minute), name_, course_);
    }

    /**
     * This function returns the minute of the deadline due time
     *
     * @return a number represents current minute. Starts from 0 to 59
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getMinute() {
        return this.date.getMinuteOfHour();
    }

    /**
     * This function returns the hour of the deadline due time
     *
     * @return a number represents the deadline due time hour.
     * It is used for the 24-hour clock and starts from 0 to 23
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getHour() {
        return this.date.getHourOfDay();
    }

    /**
     * This function returns the date of the deadline due date
     *
     * @return a number represents date of the deadline due date.
     * The first day of the month has value 1.
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getDay() {
        return this.date.getDay();
    }

    /**
     * This function returns the month of the deadline due date
     *
     * @return the month number of the month number of the deadline due date.
     * The first month of the year starts from 1 (Jan)
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getMonth() {
        return this.date.getMonth();
    }

    /**
     * This function returns the year of the deadline due date
     *
     * @return a number represents the year number of the deadline due date
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getYear() {
        return this.date.getYear();
    }

    /**
     * This function returns the name of the course this deadline belongs to
     * @requires None
     * @modifies None
     * @effects None
     * @return the course name
     */
    @Override
    public String getCourse() {
        return courseName;
    }

    /**
     * This function returns the date of this deadline
     *
     * @return the name of this deadline
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public String getName() {
        return deadlineName;
    }

    /**
     * Check if a deadline has already passed away
     *
     * @param d a date to be compared
     * @return {@code true} if the current deadline is after date time d;
     * {@code false} otherwise
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public boolean isAfter(CalendarWrapper d) {
        return this.date.isAfter(d);
    }

    /**
     * Check if a deadline has is due before a specified date time
     *
     * @param d a date to be compared
     * @return {@code true} if the current deadline is before date time d;
     * {@code false} otherwise
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public boolean isBefore(CalendarWrapper d) {
        return !this.date.isAfter(d);
    }

    /**
     * This function will return the remaining period of this deadline
     *
     * @param otherTime a date to be compared
     * @return a pair of which the key is a Period object representing the remaining
     * time and the value is a boolean representing if the deadline has passed away
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public Pair<Period, Boolean> getRemainPeriod(CalendarWrapper otherTime) {
        if (otherTime == null) otherTime = CalendarWrapper.now();
        // interval from start to end
        if (otherTime.isAfter(this.date)) {
            // deadline passed
            Interval interval = new Interval(new DateTime(this.date.getCalendarInstance()),
                    new DateTime(otherTime.getCalendarInstance()));
            return new Pair<>(new Period(interval), false);
        } else {
            Interval interval = new Interval(new DateTime(otherTime.getCalendarInstance()),
                    new DateTime(this.date.getCalendarInstance()));
            return new Pair<>(new Period(interval), true);
        }
    }

    /**
     * This function will return a text of the remaining time
     * @param otherTime a date to be compared
     * @requires None
     * @modifies None
     * @effects None
     * @return a String representing the remaining time
     */
    @Override
    public String getRemainingText(CalendarWrapper otherTime) {
        if (otherTime == null) otherTime = CalendarWrapper.now();
        Pair<Period, Boolean> remain = this.getRemainPeriod(otherTime);
        Period pd = remain.getKey();
        String month;
        if (pd.getMonths() > 0) {
            month = String.format("%02d Months ", pd.getMonths());
        } else {
            month = "";
        }
        String remainingText = month + String.format("%02d Days %02d Hours %02d Minutes ",
                pd.getWeeks()*7+pd.getDays(),pd.getHours(),pd.getMinutes());
        if (remain.getValue().equals(true)) remainingText+="Left.";
        else remainingText += "Ago.";
        return remainingText;
    }

    /**
     * This function will take a String which has a format as "(due MM/DD/YYYY @ HH:MM)"
     * and return a Calendar object based on the time represented by the string
     * @param dueText the time string
     * @requires dueText != null
     * @modifies None
     * @effects parse the string and create a Calendar object
     * @return a Calendar object based on the time represented by the string
     * @throws RuntimeException TODO docs needed for throws
     * @throws CalendarWrapper.CalendarFormatException TODO
     */
    public static CalendarWrapper parseDate(String dueText) throws RuntimeException, CalendarWrapper.CalendarFormatException {
        int startLength = 0;
        if (!(dueText.startsWith("(due ") || dueText.startsWith("(teams lock ")))
            throw new RuntimeException(dueText + " is not a valid due text");
        else {
            if (dueText.startsWith("(due ")) {
                startLength = 5;
            } else if (dueText.startsWith("(teams lock ")) {
                startLength = 12;
            }
        }
        assert(startLength == 5 || startLength == 12);
        String monthStr = dueText.substring(startLength,startLength+2);  // 5-7
        String dayStr = dueText.substring(startLength+3,startLength+5); // 8-10
        String yearStr = dueText.substring(startLength+6,startLength+10); // 11-15
        String hourStr = dueText.substring(startLength+13,startLength+15); // 18-20
        String minuteStr = dueText.substring(startLength+16,startLength+18); // 21,23
        int year, month, day, hour, minute;
        try {
            year = Integer.parseInt(yearStr);
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException(dueText + " is not a valid due text");
        }
        if (!(month >= 0 && month <= 11)) {
            throw new CalendarWrapper.CalendarFormatException(monthStr + " is not a valid month text");
        }
        if (dueText.endsWith("PM)")) {
            hour += 12;
        }
        return new CalendarWrapper(year, month, day, hour, minute);
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Deadline o) {
        // TODO compareTo testing needed
        if (this.date.equals(o.date)) {
            return 0;
        }
        if (!this.date.isAfter(o.date)) {
            return -1;
        }
        return 1;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Deadline deadline = (Deadline) o;
        return Objects.equals(date, deadline.date) &&
                Objects.equals(deadlineName, deadline.deadlineName) &&
                Objects.equals(courseName, deadline.courseName);
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
        return Objects.hash(date, deadlineName, courseName);
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
        return deadlineName + " <" + courseName + "> (" +
                CalendarWrapper.getTimeStr(getYear(),getMonth(), getDay(),
                        this.getHour(), this.getMinute()) + ")";
    }


    /**
     * Creates and returns a copy of this object. The precise meaning
     * of "copy" may depend on the class of the object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @requires None
     * @modifies None
     * @effects make a clone
     * @see Cloneable
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Deadline) super.clone();
    }
}

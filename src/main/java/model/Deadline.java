package model;

import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;

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
public final class Deadline implements DeadlineInterface {
    private final CalendarWrapper date;
    private final String deadlineName;
    private final String courseName;

    /**
     * Basic constructor for Deadline Object
     * @param date_ a Calendar object which represents a deadline time
     * @param deadline_ the name of a deadline
     * @param course_ the name of the course
     * @requires date_ != null, name_ != null, course_ != null
     * @modifies date, deadlineName, course
     * @effects create a new Deadline instance
     */
    public Deadline(CalendarWrapper date_, String deadline_, String course_) {
        this.date = date_;
        this.deadlineName = deadline_;
        this.courseName = course_;
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
        LocalDate due = LocalDateTime.ofInstant(this.date.getCalendarInstance().toInstant(),
                this.date.getCalendarInstance().getTimeZone().toZoneId()).toLocalDate();
        LocalDate other = LocalDateTime.ofInstant(otherTime.getCalendarInstance().toInstant(),
                otherTime.getCalendarInstance().getTimeZone().toZoneId()).toLocalDate();
        if (otherTime.isAfter(this.date)) {
            // deadline passed
            return new Pair<>(Period.between(due, other), false);
        } else {
            return new Pair<>(Period.between(other, due), true);
        }
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

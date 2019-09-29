package model;

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
    private final Calendar date;
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
    public Deadline(Calendar date_, String deadline_, String course_) {
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
        return 0; //TODO
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
        return 0; // TODO
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
        return 0; // TODO
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
        return 0; // TODO
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
        return 0; // TODO
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

package model;

import java.util.Calendar;

/**
 * @overview
 * This class represents a Deadline ADT expression.
 *
 * @abstract_function
 * a deadline contains four properties, a date represented by a joda.time.DateTime,
 * a name represented by a String, a courseName represented by a String and a status
 * (ready to submit/ resubmit/ late submit...) represented by a String
 *
 * @abstract_fields
 * TODO
 *
 * @creators
 * TODO
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
public final class Deadline {
    private final Calendar date;
    private final String name;
    private final String courseName;

    /**
     * Basic constructor for Deadline Object
     * @param date_ a Calendar object which represents a deadline time
     * @param name_ the name of a deadline
     * @param course_ the name of the course
     * @requires date_ != null, name_ != null, course_ != null
     * @modifies date, name, course
     * @effects create a new Deadline instance
     */
    public Deadline(Calendar date_, String name_, String course_) {
        this.date = date_;
        this.name = name_;
        this.courseName = course_;
    }

    /**
     * This function returns the name of the course this deadline belongs to
     * @requires None
     * @modifies None
     * @effects None
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * This function returns the date of this deadline
     * @requires None
     * @modifies None
     * @effects None
     * @return the name of this deadline
     */
    public String getName() {
        return name;
    }
}

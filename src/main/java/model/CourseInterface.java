package model;

import java.util.List;
import java.util.Map;

/**
 * @overview
 * This class represents the interface of a Course object
 *
 * @mutator
 * addDeadline(Deadline date)
 * addDeadline(String deadlineName, Deadline date)
 * append(Course c)
 * removeDeadline(String deadlineName)
 *
 * @observers
 * String getCourseName()
 * int size()
 * TreeMap<String, Deadline> getDeadlines()
 */
public interface CourseInterface extends Cloneable {
    /**
     * This function returns the name of this course
     * @requires None
     * @modifies None
     * @effects None
     * @return the course name
     */
    String getCourseName();

    /**
     * This function returns the number of deadlines in this course
     * @requires None
     * @modifies None
     * @effects None
     * @return the number of deadlines
     */
    int size();

    /**
     * This function will add a deadline to current course
     * @param deadline the deadline object instance
     * @requires deadlineName != null, date != null
     * @modifies Deadlines
     * @effects add a new deadline object to the Deadlines map
     */
    void addDeadline(Deadline deadline);

    /**
     * This function will add a deadline to current course
     * @param deadlineName the name of the deadline
     * @param deadline the deadline object instance
     * @requires deadlineName != null, date != null
     * @modifies Deadlines
     * @effects add a new deadline object to the Deadlines map
     */
    void addDeadline(String deadlineName, Deadline deadline);

    /**
     * This function will add a deadline to current course
     * @param deadlineName the name of the deadline
     * @param year the year number
     * @param month the month number of a year, starts from 1 (Jan)
     * @param day the day number of a month
     * @param hour the hour number of a day
     * @param minute the minute number of an hour
     * @param status       the status of this deadline
     * @param link         the link to the project
     * @requires deadlineName != null
     * @modifies Deadlines
     * @effects add a new deadline object to the Deadlines map
     * @throws CalendarWrapper.CalendarFormatException  of the number of date is invalid
     */
    void addDeadline(String deadlineName, int year, int month,
                            int day, int hour, int minute, String status, String link);

    /**
     * This function will add all deadlines from another course object to current
     * course if they got the same course name
     * @param c another course name;
     * @requires deadlineName != null
     * @modifies Deadlines
     * @effects add new deadlines to the deadlines map
     */
    void append(Course c);

    /**
     * This function will remove a deadline from current course
     * @param deadlineName the name of the deadline
     * @requires deadlineName != null
     * @modifies Deadlines
     * @effects This function will remove a deadline from current course
     */
    void removeDeadline(String deadlineName);

    /**
     * This function will return a copy of the Deadlines map which would
     * contain <deadline_name, deadline_object> pairs
     * @requires None
     * @modifies None
     * @effects None
     * @return a copy of the Deadlines map
     */
    Map<String, Deadline> getDeadlines();

    /**
     * This function will return a list of deadline names based on their dates,
     * ascending order.
     * @requires None
     * @modifies None
     * @effects None
     * @return a list of deadline names based on their dates
     */
    List<String> getSortedDeadlines();

    /**
     * This function will return a list of deadline names based on their dates,
     * descending order.
     * @requires None
     * @modifies None
     * @effects None
     * @return a list of deadline names based on their dates
     */
    List<String> getReversedSortedDeadlines();

    /**
     * Creates and returns a copy of this object. The precise meaning
     * of "copy" may depend on the class of the object.      *
     * @return     a clone of this instance.
     * @throws  CloneNotSupportedException  if the object's class does not
     *               support the {@code Cloneable} interface. Subclasses
     *               that override the {@code clone} method can also
     *               throw this exception to indicate that an instance cannot
     *               be cloned.
     * @see java.lang.Cloneable
     */
    Object clone() throws CloneNotSupportedException;
}

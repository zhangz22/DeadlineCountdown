package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @overview
 * This class represents a Course ADT expression.
 *
 * @abstract_function
 * a deadline contains two properties, a courseName represented by a String and all
 * deadlines of this course stored in a Map
 *
 * @abstract_fields
 * courseName -> the name of the course
 * Deadlines -> a map storing <deadline_name, deadline_object>
 *
 * @creators
 * Course(String courseName_)
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
 *
 * @representation_invariant
 * courseName != null && deadlines != null
 */
public class Course implements CourseInterface {
    private final String courseName;
    private Map<String, Deadline> deadlines;

    /**
     * The basic constructor of this object
     * @param courseName_ the name of the course
     * @requires courseName != null
     * @modifies courseName, Deadlines
     * @effects create a new instance
     */
    public Course(String courseName_) {
        courseName = courseName_;
        deadlines = new TreeMap<>();
    }

    /**
     * This function returns the name of this course
     * @requires None
     * @modifies None
     * @effects None
     * @return the course name
     */
    @Override
    public String getCourseName() {
        return courseName;
    }

    /**
     * This function returns the number of deadlines in this course
     *
     * @return the number of deadlines
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int size() {
        return this.deadlines.size();
    }


    /**
     * This function will add a deadline to current course
     * @param deadline the deadline object instance
     * @requires deadlineName != null, date != null
     * @modifies Deadlines
     * @effects add a new deadline object to the Deadlines map
     */
    @Override
    public void addDeadline(Deadline deadline) {
        deadlines.put(deadline.getName(), deadline);
    }

    /**
     * This function will add a deadline to current course
     * @param deadlineName the name of the deadline
     * @param deadline the deadline object instance
     * @requires deadlineName != null, date != null
     * @modifies Deadlines
     * @effects add a new deadline object to the Deadlines map
     */
    @Override
    public void addDeadline(String deadlineName, Deadline deadline) {
        deadlines.put(deadlineName, deadline);
    }

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
    @Override
    public void addDeadline(String deadlineName, int year, int month,
                            int day, int hour, int minute, String status, String link)
            throws CalendarWrapper.CalendarFormatException  {
        Deadline d = new Deadline(year, month, day, hour, minute, deadlineName,
                this.courseName, status, link);
        deadlines.put(deadlineName,d);
    }

    /**
     * This function will add all deadlines from another course object to current
     * course if they got the same course name
     *
     * @param c another course name;
     * @requires deadlineName != null
     * @modifies Deadlines
     * @effects add new deadlines to the deadlines map
     */
    @Override
    public void append(Course c) {
        if (!c.courseName.equals(this.courseName)) {
            return;
        }
        for (String deadlineName: c.deadlines.keySet()) {
            this.deadlines.put(deadlineName.trim(), c.deadlines.get(deadlineName));
        }
    }

    /**
     * This function will remove a deadline from current course
     *
     * @param deadlineName the name of the deadline
     * @requires deadlineName != null
     * @modifies Deadlines
     * @effects This function will remove a deadline from current course
     */
    @Override
    public void removeDeadline(String deadlineName) {
        deadlines.remove(deadlineName);
    }

    /**
     * This function will return a copy of the Deadlines map which would
     * contain <deadline_name, deadline_object> pairs
     * @requires None
     * @modifies None
     * @effects None
     * @return a copy of the Deadlines map
     */
    @Override
    public TreeMap<String, Deadline> getDeadlines() {
        return new TreeMap<>(deadlines);
    }

    /**
     * This function will return a list of deadline names based on their dates,
     * ascending order.
     * @requires None
     * @modifies None
     * @effects None
     * @return a list of deadline names based on their dates
     */
    @Override
    public List<String> getSortedDeadlines() {
        List<String> SortedDeadlineList = new ArrayList<>(deadlines.keySet());
        SortedDeadlineList.sort(Comparator.comparing(o -> deadlines.get(o)));
        return SortedDeadlineList;
    }

    /**
     * This function will return a list of deadline names based on their dates,
     * descending order.
     * @requires None
     * @modifies None
     * @effects None
     * @return a list of deadline names based on their dates
     */
    @Override
    public List<String> getReversedSortedDeadlines() {
        List<String> SortedDeadlineList = new ArrayList<>(deadlines.keySet());
        SortedDeadlineList.sort((o1, o2) -> deadlines.get(o2).compareTo(deadlines.get(o1)));
        return SortedDeadlineList;
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
        Course course = (Course) o;
        return courseName.equals(course.courseName) &&
                deadlines.equals(course.deadlines);
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
        return Objects.hash(courseName, deadlines);
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
        return "Course {" +
                "courseName='" + courseName + '\'' + '}';
    }

    /**
     * Creates and returns a copy of this object. The precise meaning
     * of "copy" may depend on the class of the object.
     * @requires None
     * @modifies None
     * @effects make a clone
     * @return     a clone of this instance.
     * @throws  CloneNotSupportedException  if the object's class does not
     *               support the {@code Cloneable} interface. Subclasses
     *               that override the {@code clone} method can also
     *               throw this exception to indicate that an instance cannot
     *               be cloned.
     * @see java.lang.Cloneable
     */
    @Override
    public Course clone() throws CloneNotSupportedException {
        Course c = (Course) super.clone();
        c.deadlines = new HashMap<>(this.deadlines);
        return c;
    }
}

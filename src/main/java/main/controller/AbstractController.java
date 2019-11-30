package main.controller;

import model.CalendarWrapper;
import model.Course;
import model.Deadline;

import java.util.List;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class defines a general main controller.
 */
public abstract class AbstractController implements Operations {
    /** allCourses: a map to store every course object with its name. */
    protected ConcurrentHashMap<String, Course> allCourses;
    /** ignoredCoursesSet: a list to store every course that should be ignored. */
    protected HashSet<String> ignoredCoursesSet;
    /** allDeadlines: a sorted set to store every deadline based on their dates. */
    protected TreeSet<Deadline> allDeadlines;
    /** settings: program settings */
    protected Settings settings;

    /**
     * This method will return a specified course object given the course name.
     * If no such object is found, then create a new instance.
     * @param courseName the course name
     * @requires None
     * @modifies None
     * @effects None
     * @return a course object
     */
    protected Course getCourseByName(String courseName) {
        Course c;
        if (!allCourses.containsKey(courseName)) {
            c = new Course(courseName);
        } else {
            c = allCourses.get(courseName);
        }
        return c;
    }

    /**
     * This method returns a copy of the allCourses map
     * @requires None
     * @modifies None
     * @effects return a copy of the allCourse map
     * @return this.allCourses
     */
    public ConcurrentHashMap<String, Course> getAllCourses() {
        return new ConcurrentHashMap<>(allCourses);
    }

    /**
     * This method returns the closest deadline
     * @requires None
     * @modifies None
     * @effects return the closest deadline
     * @return the closest deadline
     */
    public Deadline getClosestDeadline() {
        for (Deadline d: allDeadlines) {
            if (d.isAfter(CalendarWrapper.now()) && !this.isIgnoring(d.getCourseName()) && !d.getStatus().equals(Deadline.STATUS.FINISHED)) {
                return d;
            }
        }
        return null;
    }

    /**
     * This method would go through the AllCourse map and add all deadlines to the
     * allDeadlines set
     * @requires None
     * @modifies allDeadlines
     * @effects update the AllDeadline set
     */
    protected void updateDeadlineSet() {
        for (String i : this.allCourses.keySet()) {
            TreeMap<String, Deadline> dueMap = this.allCourses.get(i).getDeadlines();
            List<String> dueList = this.allCourses.get(i).getReversedSortedDeadlines();
            for (String j : dueList) {
                this.allDeadlines.add(dueMap.get(j));
            }
        }
    }

    /**
     * This method will check if a course is in ignoredCoursesSet
     * @param courseName the course name
     * @requires None
     * @modifies None
     * @effects None
     * @return true if ignoredCoursesSet contains the course
     */
    public boolean isIgnoring(final String courseName) {
        return ignoredCoursesSet.contains(courseName);
    }
}

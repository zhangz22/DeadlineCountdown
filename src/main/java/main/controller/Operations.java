package main.controller;

import model.Deadline;

import java.io.File;

/**
 * This class defines the gerneral behaviour for the controller
 */
public interface Operations {
    /**
     * This method would read data from a previous saved local file.
     * @param file the file that will be load from. enter null if you'd like to create
     *             a new file by default
     * @param extension the file extension
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects read from local file
     * @return None
     */
    void loadFromLocal(File file, String extension);

    /**
     * This method would save data to a local file.
     * @param file the file that will be saved to. enter null if you'd like to create
     *             a new file by default
     * @param extension the file extension
     * @requires None
     * @modifies None
     * @effects save to local file
     * @return None
     */
    void saveToLocal(File file, String extension);

    /**
     * This method would try to access web pages via the id and password that the
     * user provided
     * @param id the user id
     * @param password the user password
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    void access(String id, String password);

    /**
     * This method would add a new deadline to an existing course. If such course
     * doesn't exist, then such course instance will be created
     * @param course the course name
     * @param deadlineName the deadline name
     * @param year year number
     * @param month month number; starts from 1 to 12
     * @param day the day number; starts from 1 to 31
     * @param hour the hour number
     * @param minute the minute number
     * @param status the status of this deadline
     * @param link the link to the project
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    void addDeadline(String course, String deadlineName, int year, int month,
                            int day, int hour, int minute, String status, String link);

    /**
     * This method would remove a deadline from an existing course.
     * @param course the course name
     * @param deadlineName the deadline name
     * @param year year number
     * @param month month number; starts from 1 to 12
     * @param day the day number; starts from 1 to 31
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    void removeDeadline(String course, String deadlineName, int year, int month, int day);

    /**
     * This method would edit a deadline
     * @param d the deadline object that is about to be edited
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    void editDeadline(Deadline d);

    /**
     * This function would add a course name that would be ignored when displaying all
     * deadlines
     * @param course the course name
     * @requires None
     * @modifies a list that stores every course that would be ignored
     * @effects None
     */
    void addIgnoredCourse(String course);

    /**
     * This function would remove a course name that would be ignored when displaying all
     * deadlines
     * @param course the course name
     * @requires None
     * @modifies a list that stores every course that would be ignored
     * @effects None
     */
    void removeIgnoredCourse(String course);

    /**
     * This function would run the program and display the result to the user
     * @param args arguments when running the program
     * @requires None
     * @modifies None
     * @effects display the result
     */
    void run(String[] args);
}

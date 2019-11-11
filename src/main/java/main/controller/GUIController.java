package main.controller;

import model.CalendarWrapper;
import model.Course;
import model.Deadline;
import main.viewer.GUIViewer;
import webService.SubmittyAccess;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This class is the main controller of GUI version of oneSecond
 */
public class GUIController extends AbstractController implements Operations {

    public static final String VERSION = "0.0.1-alpha-preview";
    private GUIViewer frame;

    /**
     * Default Constructor
     *
     * @requires None
     * @modifies this, ignoredCoursesSet, allCourses, allDeadlines
     * @effects create a CommandLineController instance
     */
    public GUIController() {
        this.frame = new GUIViewer(this);
        // initialize variables
        this.allCourses = new ConcurrentHashMap<>();
        this.allDeadlines = new TreeSet<>();
        this.ignoredCoursesSet = new HashSet<>();
    }

    /**
     * This function would run the program and display the frame to the user
     *
     * @param args arguments when running the program
     * @requires None
     * @modifies None
     * @effects display the result
     */
    @Override
    public void run(String[] args) {
        this.frame.setUp(false);
        this.frame.run(args);
    }

    /**
     * This function would restart the program and display the result to the user
     *
     * @requires None
     * @modifies None
     * @effects restart the program and display the result
     */
    public void restart() {
        this.frame.restart();
        System.gc();
    }

    /**
     * This function will show a popup windows to display a message
     *
     * @param message the message
     * @requires None
     * @modifies None
     * @effects send a message
     */
    public void alert(String message) {
        JLabel label = new JLabel(message);
        JOptionPane.showConfirmDialog(frame, label, "", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * This function will send a notification via the system
     *
     * @param title the title of the notification
     * @param message the message
     * @param subtitle the subtitle of the notification
     * @requires None
     * @modifies None
     * @effects send a message
     */
    public void notification(String title, String message, String subtitle) {
        this.frame.notification(title, message, subtitle);
    }

    /**
     * This method would read data from a previous saved local file.
     *
     * @param file      the file that will be load from. enter null if you'd like to create
     *                  a new file by default
     * @param extension the file extension
     * @return None
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects read from local file
     */
    @Override
    public void loadFromLocal(File file, String extension) {

    }

    /**
     * This method would save data to a local file.
     *
     * @param file      the file that will be saved to. enter null if you'd like to create
     *                  a new file by default
     * @param extension the file extension
     * @return None
     * @requires None
     * @modifies None
     * @effects save to local file
     */
    @Override
    public void saveToLocal(File file, String extension) {

    }

    /**
     * This method would try to access submitty via the username and password that the
     * user provided
     *
     * @param id       the user username
     * @param password the user password
     * @throws SubmittyAccess.LoginFailException if the user username or password is not valid
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Override
    public void access(String id, String password) throws SubmittyAccess.LoginFailException {

        // create a thread to access submitty
        Runnable access = new Runnable() {
            @Override
            public void run() {
                SubmittyAccess sa = new SubmittyAccess(id, password, null);
                sa.login();
                System.out.println("DEBUG: [access] login succeeded.");
                sa.parser(ignoredCoursesSet);
                System.out.println("DEBUG: [access] parser succeeded.");
                ConcurrentHashMap<String, Course> newCourses = sa.getCourseMap();
                sa.close();
                System.out.println("DEBUG: [access] getCourseMap succeeded.");
                boolean shouldAdd = true;
                for (String fullCourseName : newCourses.keySet()) {
                    System.out.println("DEBUG: [access] handling course " + fullCourseName);
                    String courseName = fullCourseName.replace("Spring 2019     ", "").trim();
                    synchronized (this) {
                        if (allCourses.containsKey(courseName)) {
                            assert (allCourses.get(courseName) != null);
                            assert (newCourses.get(fullCourseName) != null);
                            allCourses.get(courseName).append(newCourses.get(fullCourseName));
                        } else {
                            assert (newCourses.get(fullCourseName) != null);
                            allCourses.put(courseName, newCourses.get(fullCourseName));
                        }
                    }
                }
                System.out.println("DEBUG: [access] handling courses succeeded.");
                if (newCourses.isEmpty()) {
                    alert("no_deadline");
                }
            }
        };
        final RuntimeException[] exceptionFromAccess = {null};
        Thread.UncaughtExceptionHandler loginFailedHandler = (th, e) -> {
            if (e instanceof SubmittyAccess.LoginFailException) {
                System.out.println("DEBUG [UncaughtExceptionHandler] caught exception " + e.getMessage());
                exceptionFromAccess[0] = (SubmittyAccess.LoginFailException) e;
            }
        };

        Thread accessThread = new Thread(access);
        accessThread.setUncaughtExceptionHandler(loginFailedHandler);

        // access submitty
        accessThread.start();

        // block main thread until submitty access finished
        if (exceptionFromAccess[0] == null) {
            System.out.println("DEBUG: [access] login succeed. Dialog closed.");
        } else {
            System.out.println("DEBUG [MainThreadAccess] success = false");
            throw exceptionFromAccess[0];
        }
    }

    /**
     * This method would add a new deadline to an existing course. If such course
     * doesn't exist, then such course instance will be created
     *
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day          the day number; starts from 1 to 31
     * @param hour         the hour number
     * @param minute       the minute number
     * @param status       the status of this deadline
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Deprecated
    public synchronized void addDeadline(String course, String deadlineName, int year, int month,
                            int day, int hour, int minute, String status) {
        this.addDeadline(course, deadlineName, year, month, day, hour, minute, status, "");
    }

    /**
     * This method would add a new deadline to an existing course. If such course
     * doesn't exist, then such course instance will be created
     *
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day          the day number; starts from 1 to 31
     * @param hour         the hour number
     * @param minute       the minute number
     * @param status       the status of this deadline
     * @param link         the link to the project
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Override
    public synchronized void addDeadline(String course, String deadlineName, int year, int month,
                            int day, int hour, int minute, String status, String link) {
        Deadline deadline;
        try {
            deadline = new Deadline(year, month, day, hour, minute, deadlineName, course);
        } catch (CalendarWrapper.CalendarFormatException e) {
            System.err.println("DEBUG: [controller] Due date format not correct: " + e.getMessage());
            return;
        }
        this.addDeadline(deadline);
    }

    /**
     * This method would add a new deadline to an existing course. If such course
     * doesn't exist, then such course instance will be created
     *
     * @param deadline    the deadline that is about to be added
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    public synchronized void addDeadline(Deadline deadline) {
        Course c = this.getCourseByName(deadline.getCourse());
        c.addDeadline(deadline);
        this.allCourses.put(deadline.getCourse(), c);
        this.allDeadlines.add(deadline);
        this.frame.addDeadlineBlock(deadline);
        this.frame.updateTrayIcon();
    }

    /**
     * This method would remove a deadline from an existing course.
     *
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day the day number; starts from 1 to 31
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Override
    public synchronized void removeDeadline(String course, String deadlineName, int year, int month, int day) {
        Course c = this.getCourseByName(course);
        c.removeDeadline(deadlineName);
        if (!c.getDeadlines().isEmpty()) {
            allCourses.put(course, c);
        } else {
            allCourses.remove(course);
        }
        this.frame.removeDeadlineBlock(course, deadlineName, year, month, day);
    }

    /**
     * This method would edit a deadline
     *
     * @param deadline the deadline object that is about to be edited
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Override
    public void editDeadline(Deadline deadline) {
        this.frame.showEditDeadlinePanel(deadline);
    }

    /**
     * This function would add a course name that would be ignored when displaying all
     * deadlines
     *
     * @param course the course name
     * @requires None
     * @modifies a list that stores every course that would be ignored
     * @effects None
     */
    @Override
    public void addIgnoredCourse(String course) {

    }

    /**
     * This function would remove a course name that would be ignored when displaying all
     * deadlines
     *
     * @param course the course name
     * @requires None
     * @modifies a list that stores every course that would be ignored
     * @effects None
     */
    @Override
    public void removeIgnoredCourse(String course) {

    }

    public JFrame getFrame() {
        return this.frame;
    }
}


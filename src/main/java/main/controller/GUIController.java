package main.controller;

import main.viewer.GUIViewer;
import main.viewer.util.DeadlineTimer;
import main.viewer.util.LoadingDialog;
import model.Course;
import model.Deadline;
import webService.SubmittyAccess;

import java.awt.Color;
import java.io.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;


/**
 * This class is the main controller of GUI version of main
 */
public class GUIController extends AbstractController implements Operations {
    public static final String VERSION = "1.0";
    private GUIViewer frame;
    private String settingPath;

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
        // create folders to store local data
        boolean folderResult = true;
        if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("win")) {
            // Windows
            this.settingPath = System.getenv("APPDATA") + "/main/";
            File file = new File(this.settingPath);
            if (!file.exists()) {
                folderResult = file.mkdirs();
                System.out.println("DEBUG: [creating folder] folder [" + this.settingPath + "] create result " + folderResult);
            }
        } else if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("mac")) {
            // macOS
            this.settingPath = System.getProperty("user.home") + "/Library/Application Support/main/";
            File file = new File(this.settingPath);
            if (!file.exists()) {
                folderResult = file.mkdirs();
                System.out.println("DEBUG: [creating folder] folder [" + this.settingPath + "] create result " + folderResult);
            }
        }
        else {
            this.settingPath = "./";
        }
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
        // load deadline information from saved files
        this.loadFromLocal(null, "JSON", false);
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
     * This function refreshes the calendar panel and the summary panel
     *
     * @requires None
     * @modifies sideBar, calendarPanel
     * @effects refresh
     */
    public void refresh() {
        this.frame.refresh();
    }

    /**
     * This function would shutdown the program
     *
     * @requires None
     * @modifies None
     * @effects shutdown the program
     */
    public void shutdown() {
        this.getFrame().shutdown();
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
     * This function returns the frame of main
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return the current frame
     */
    synchronized public final GUIViewer getFrame() {
        return this.frame;
    }

    /**
     * This function starts or stops all timers
     * @param start if the timers will be started
     * @modifies this.getFrame().getAllTimersMap()
     * @effects start or stop the timers
     */
    public void setTimerStatus(Boolean start) {
        for (DeadlineTimer timer: this.getFrame().getAllTimersMap().values()) {
            if (start) {
                timer.start();
            } else {
                timer.stop();
            }
        }
    }

    /**
     * This method would read data from a previous saved local file.
     *
     * @param file the file that will be load from. enter null if you'd like to create
     *             a new file by default
     * @param extension the file extension
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects read from local file
     */
    @Override
    public void loadFromLocal(File file, String extension) {
        this.loadFromLocal(file, extension, true);
    }

    /**
     * This method would read data from a previous saved local file.
     *
     * @param file the file that will be load from. enter null if you'd like to create
     *             a new file by default
     * @param extension the file extension
     * @param showDialog if the program should show a dialog indicator
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects read from local file
     */
    public void loadFromLocal(File file, String extension, boolean showDialog) {
        if (file == null)
            file = new File(this.settingPath + "deadlines.json");
        if (file.exists() && !file.isDirectory()) {
            Reader fileReader = null;
            try {
                 fileReader = new FileReader(file);
            } catch (IOException e) {
                System.err.println("[MainController] error when loading deadlines: " + e);
                this.alert("<html>" + this.getFrame().getText("loading_from") + "<br>"
                        + this.getFrame().getText("error_code") + e.getMessage());
                return;
            }
            localParser.Parser load = new localParser.Load(this, fileReader);
            Thread thread = localParser.Parser.getParserThread(load, extension, this, showDialog);
            thread.run();
        } else {
            System.out.println("DEBUG: [Load] Error when loading settings from local: " +
                    "file.exists() = " + file.exists() + ", file.isDirectory() = " + file.isDirectory());
        }
    }

    /**
     * This method would save data to a local file.
     *
     * @param file the file that will be saved to. enter null if you'd like to create
     *             a new file by default
     * @param extension the file extension
     * @requires None
     * @modifies None
     * @effects save to local file
     */
    @Override
    public void saveToLocal(File file, String extension) {
        this.saveToLocal(file, extension, true);
    }


    /**
     * This method would save data to a local file.
     *
     * @param file the file that will be saved to. enter null if you'd like to create
     *             a new file by default
     * @param extension the file extension
     * @param showDialog if the program should show a dialog indicator
     * @requires None
     * @modifies None
     * @effects save to local file
     */
    public void saveToLocal(File file, String extension, boolean showDialog) {
        if (file == null)
            file = new File(this.settingPath + "deadlines.json");

        if (!file.isDirectory()) {
            PrintWriter fileWriter = null;
            try {
                 fileWriter = new PrintWriter(file, "UTF-8");
            } catch (IOException e) {
                System.err.println("[MainController] error when saving settings: " + e);
                this.alert("<html>" + this.getFrame().getText("saving_to") + "<br>"
                        + this.getFrame().getText("error_code") + e.getMessage());
                return;
            }
            localParser.Parser save = new localParser.Save(this.allCourses, fileWriter);
            Thread thread = localParser.Parser.getParserThread(save, extension, this, showDialog);
            thread.start();
        } else {
            System.out.println("DEBUG: [Save] IOException");
        }
    }

    /**
     * This method should ask the user to agree with following statement:
     * I acknowledge that main will not save my username and
     * password in any form after the program terminates and will not
     * use my username and password for any use other than grabbing deadline
     * dates. I understand that I should take my own risk using this
     * program.
     *
     * @return the user's choice
     * @requires None
     * @modifies None
     * @effects None
     */
    public boolean agreement() {
        JLabel label = new JLabel(this.frame.getTextFormat().userAgreement());
        int n = JOptionPane.showConfirmDialog(this.frame, label,
                this.frame.getTextFormat().userAgreementTitle(), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        return n == 0;
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
        LoadingDialog dialog = new LoadingDialog(this);

        // create a thread to access submitty
        Runnable access = new Runnable() {
            @Override
            public void run() {
                SubmittyAccess sa = new SubmittyAccess(id, password, dialog.getCurrProgressLabel());
                sa.login();
                System.out.println("DEBUG: [access] login succeeded.");
                sa.parser(ignoredCoursesSet);
                System.out.println("DEBUG: [access] parser succeeded.");
                ConcurrentHashMap<String, Course> newCourses = sa.getCourseMap();
                sa.close();
                System.out.println("DEBUG: [access] getCourseMap succeeded.");
                boolean shouldAdd = true;
                synchronized (this) {
                    if (!dialog.isVisible()) {
                        System.out.println("DEBUG: [access] dialog no longer visible");
                        shouldAdd = false;
                    }
                }
                if (!shouldAdd) {
                    dialog.dispose();
                    System.out.println("DEBUG: [access] closing thread");
                    return;
                }
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
                    alert(getFrame().getText("no_deadline"));
                }

                // close the dialog
                synchronized (this) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        };
        final RuntimeException[] exceptionFromAccess = {null};
        Thread.UncaughtExceptionHandler loginFailedHandler = (th, e) -> {
            if (e instanceof SubmittyAccess.LoginFailException) {
                System.out.println("DEBUG [UncaughtExceptionHandler] caught exception " + e.getMessage());
                exceptionFromAccess[0] = (SubmittyAccess.LoginFailException) e;
                dialog.setVisible(false);
                dialog.dispose();
            }
        };

        Thread accessThread = new Thread(access);
        accessThread.setUncaughtExceptionHandler(loginFailedHandler);

        // access submitty
        accessThread.start();

        // block main thread until submitty access finished
        dialog.run();
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
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Override
    public synchronized void addDeadline(String course, String deadlineName, int year, int month,
                            int day, int hour, int minute) {
        Deadline deadline;
        try {
            deadline = new Deadline(year, month, day, hour, minute, deadlineName, course);
        } catch (RuntimeException e) { // TODO make specific
            System.err.println("DEBUG: [controller] Due date format not correct: " + e);
            this.notification("Add deadline failed",
                    this.getFrame().getTextFormat().dateFormat(year, month, day) +
                    " is not a valid date.", "");
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
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Override
    public void addIgnoredCourse(String course) {
        if (course == null || course.equals("")) {
            return;
        }
        System.out.println("DEBUG: [GUIController] <" + course + "> is now ignored");
        this.ignoredCoursesSet.add(course);
        for (Map.Entry<String, DeadlineTimer> item: this.frame.getAllTimersMap().entrySet()) {
            if (item.getKey().startsWith(course)) {
                item.getValue().stop();
            }
        }
        this.frame.refresh();
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
        System.out.println("DEBUG: [GUIController] <" + course + "> is no longer ignored");
        this.ignoredCoursesSet.remove(course);
        for (Map.Entry<String, DeadlineTimer> item: this.frame.getAllTimersMap().entrySet()) {
            if (item.getKey().startsWith(course)) {
                item.getValue().start();
            }
        }
        this.frame.refresh();
    }
}


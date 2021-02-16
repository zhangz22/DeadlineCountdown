package main.controller;

import model.CalendarWrapper;
import model.Course;
import model.Deadline;
import main.viewer.Log;
import main.viewer.textFormat.ViewerFont;
import main.viewer.util.DeadlineTimer;
import main.viewer.GUIViewer;
import main.viewer.util.LoadingDialog;
import webService.SubmittyAccess;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

import static main.viewer.Log.*;

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
        // load basic settings
        this.settings = new Settings();
        // create folders to store local data
        boolean folderResult = true;
        if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("win")) {
            // Windows
            this.settingPath = System.getenv("APPDATA") + "/DeadlineCountdown/";
            File file = new File(this.settingPath);
            if (!file.exists()) {
                folderResult = file.mkdirs();
                Log.debug("DEBUG: [creating folder] folder [" + this.settingPath + "] create result " + folderResult);
            }
        } else if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("mac")) {
            // macOS
            this.settingPath = System.getProperty("user.home") + "/Library/Application Support/DeadlineCountdown/";
            File file = new File(this.settingPath);
            if (!file.exists()) {
                folderResult = file.mkdirs();
                Log.debug("DEBUG: [creating folder] folder [" + this.settingPath + "] create result " + folderResult);
            }
        }
        else {
            this.settingPath = "./";
        }
        // check permission
        if (!folderResult) {
            this.settings.put(Settings.SUPPORTED.SAVE_LOCAL_FILE, false);
            Log.error("Error when creating directory \"" + "\": access denied.");
            this.notification("Access denied","Error when creating directory \"" + "\"","");
        } else {
            this.settings.put(Settings.SUPPORTED.SAVE_LOCAL_FILE, true);
        }
        this.loadSettings(false);
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
        label.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 16));
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
     * This function returns the settings of main
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return a copy of the current settings
     */
    public Settings getSettings() {
        return new Settings(this.settings);
    }

    /**
     * This function changes the settings of main
     *
     * @param key key for the setting item
     * @param value value for the setting item
     * @requires None
     * @modifies settings
     * @effects change the settings
     */
    public void setSettings(String key, boolean value) {
        this.settings.put(key, value);
    }

    /**
     * This function resets the settings of main
     *
     * @requires None
     * @modifies settings
     * @effects reset the settings
     */
    public void resetSettings() {
        this.settings.setDefaultSettings();
    }

    /**
     * This function changes the language of main
     *
     * @param language the locale for new language
     * @param alert whether the program shows alert to the user
     * @requires None
     * @modifies settings
     * @effects change the language
     */
    public void setLanguage(String language, boolean alert) {
        this.settings.setLanguage(language, this, alert);
    }

    /**
     * This function changes the theme for the program
     *
     * @param theme the new theme set for the program
     * @param mainColor the main theme color
     * @requires None
     * @modifies this.theme
     * @effects change the theme
     */
    public void setTheme(String theme, Color mainColor) {
        this.settings.setTheme(theme, this, mainColor);
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
     * This method would read settings from a previous saved local file.
     *
     * @param showDialog if the program should show a dialog indicator
     * @requires settings != null
     * @modifies settings
     * @effects read from local file
     */
    public void loadSettings(boolean showDialog) {
        if (this.settings.isSaveLocalUnavailable()) {
            return;
        }
        File f = new File(this.settingPath + "settings.ini");
        if (f.exists() && !f.isDirectory()) {
            try {
                BufferedReader b = new BufferedReader(new FileReader(f));
                String line;
                String theme = null;
                Integer red = null;
                Integer green = null;
                Integer blue = null;
                while ((line = b.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length < 2) {
                        continue;
                    }
                    Log.debug(ANSI_CYAN + "DEBUG: [GUIController] Loading setting item \""
                            + parts[0] + "\"= " + ANSI_RESET + parts[1]);
                    switch (parts[0]) {
                        case "language":
                            this.settings.setLanguage(parts[1], this, false);
                            break;
                        case "theme":
                            theme = parts[1];
                            break;
                        case "red":
                            red = Integer.parseInt(parts[1]);
                            break;
                        case "green":
                            green = Integer.parseInt(parts[1]);
                            break;
                        case "blue":
                            blue = Integer.parseInt(parts[1]);
                            break;
                        case "ignore":
                            for (String courseName: parts[1].split(",")) {
                                this.addIgnoredCourse(courseName);
                            }
                    }
                    this.settings.put(parts[0], Boolean.parseBoolean(parts[1]));
                }
                if (theme != null && red != null && green != null && blue != null) {
                    this.settings.setTheme(theme, this, new Color(red, green, blue));
                }
                b.close();
            } catch (IOException e) {
                Log.debug("[GUIController] Error when loading settings: ", e.getMessage());
                Log.error("[GUIController] Error when loading settings: ", e);
                return;
            }
            if (showDialog) {
                JLabel label = new JLabel("Settings load successfully.");
                label.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 16));
                JOptionPane.showConfirmDialog(frame, label,
                        "", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * This method would save settings to a local file.
     *
     * @param showDialog if the program should show a dialog indicator
     * @requires settings != null
     * @modifies None
     * @effects save to local file
     */
    public void saveSettings(boolean showDialog) {
        if (this.settings.isSaveLocalUnavailable()) {
            return;
        }
        PrintWriter writer;
        try {
            writer = new PrintWriter(this.settingPath + "settings.ini", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
                Log.error("[MainController] error when saving settings: ", e);
            this.alert("<html>" + this.getFrame().getText("settings_save_failed")
                    + "<br>" + this.getFrame().getText("error_code") + " " + e.getMessage() + "</html>");
            return;
        }
        writer.println("language=" + settings.getLanguage());
        writer.println("theme=" + settings.getTheme());
        writer.println("red=" + settings.getThemeColor().getRed());
        writer.println("green=" + settings.getThemeColor().getGreen());
        writer.println("blue=" + settings.getThemeColor().getBlue());
        writer.print("ignore=");
        for (String courseName: this.ignoredCoursesSet) {
            writer.print(courseName + ",");
        }
        writer.println();
        for (Map.Entry<String, Boolean> item: this.settings.getAllSettings().entrySet()) {
            writer.println(item.getKey() + "=" + item.getValue());
        }
        writer.close();

        if (showDialog) {
            this.notification("main", this.getFrame().getText("settings_save_success"), "");
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
        if (this.settings.isSaveLocalUnavailable()) {
            return;
        }
        if (file == null)
            file = new File(this.settingPath + "deadlines.json");
        if (file.exists() && !file.isDirectory()) {
            Reader fileReader = null;
            try {
                 fileReader = new FileReader(file);
            } catch (IOException e) {
                Log.error("[MainController] error when loading deadlines: ", e);
                this.alert("<html>" + this.getFrame().getText("loading_from") + "<br>"
                        + this.getFrame().getText("error_code") + e.getMessage());
                return;
            }
            localParser.Parser load = new localParser.Load(this, fileReader);
            Thread thread = localParser.Parser.getParserThread(load, extension, this, showDialog);
            thread.run();
        } else {
            Log.debug("DEBUG: [Load] Error when loading settings from local: " +
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
        if (this.getSettings().isSaveLocalUnavailable()) {
            return;
        }
        if (file == null)
            file = new File(this.settingPath + "deadlines.json");

        if (!file.isDirectory()) {
            PrintWriter fileWriter = null;
            try {
                 fileWriter = new PrintWriter(file, "UTF-8");
            } catch (IOException e) {
                Log.error("[MainController] error when saving settings: ", e);
                this.alert("<html>" + this.getFrame().getText("saving_to") + "<br>"
                        + this.getFrame().getText("error_code") + e.getMessage());
                return;
            }
            localParser.Parser save = new localParser.Save(this.allCourses, fileWriter);
            Thread thread = localParser.Parser.getParserThread(save, extension, this, showDialog);
            thread.start();
        } else {
            Log.debug("DEBUG: [Save] IOException");
        }
    }

    /**
     * This method should ask the user to agree with following statement:
     * I acknowledge that this program will not save my username and
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
    @Override
    public boolean agreement() {
        JLabel label = new JLabel(this.frame.getTextFormat().userAgreement());
        label.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 16));
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
                Log.debug("DEBUG: [access] login succeeded.");
                sa.parser(ignoredCoursesSet);
                Log.debug("DEBUG: [access] parser succeeded.");
                ConcurrentHashMap<String, Course> newCourses = sa.getCourseMap();
                sa.close();
                Log.debug("DEBUG: [access] getCourseMap succeeded.");
                boolean shouldAdd = true;
                synchronized (this) {
                    if (!dialog.isVisible()) {
                        Log.debug("DEBUG: [access] dialog no longer visible");
                        shouldAdd = false;
                    }
                }
                if (!shouldAdd) {
                    dialog.dispose();
                    Log.debug("DEBUG: [access] closing thread");
                    return;
                }
                for (String fullCourseName : newCourses.keySet()) {
                    Log.debug("DEBUG: [access] handling course " + fullCourseName);
                    String courseName = fullCourseName.replace("Fall 2019     ", "").trim();
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
                Log.debug("DEBUG: [access] handling courses succeeded.");
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
            Log.debug("DEBUG [UncaughtExceptionHandler] caught exception " + e.getMessage());
            if (e instanceof SubmittyAccess.LoginFailException) {
                exceptionFromAccess[0] = (SubmittyAccess.LoginFailException) e;
                if (e.getMessage().equals("Login failed: Could not login using " +
                    "that user id or password")) {
                    dialog.getCurrProgressLabel().setText(e.getMessage());
                } else {
                    dialog.getCurrProgressLabel().setText("<html>Login failed. " +
                            "Please check your  <br> Internet connection and try again. </html>");
                }
            } else {
                exceptionFromAccess[0] = (RuntimeException) e;
                dialog.getCurrProgressLabel().setText("<html>Login failed. " +
                        "Please check your  <br> Internet connection and try again. </html>");
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            dialog.setVisible(false);
            dialog.dispose();
        };

        Thread accessThread = new Thread(access);
        accessThread.setUncaughtExceptionHandler(loginFailedHandler);

        // access submitty
        accessThread.start();

        // block main thread until submitty access finished
        dialog.run();
        if (exceptionFromAccess[0] == null) {
            Log.debug("DEBUG: [access] login succeed. Dialog closed.");
        } else {
            Log.debug("DEBUG [MainThreadAccess] success = false");
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
            deadline = new Deadline(year, month, day, hour, minute, deadlineName, course, status, link);
        } catch (CalendarWrapper.CalendarFormatException e) {
            Log.error("DEBUG: [controller] Due date format not correct: ", e);
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
        Course c = this.getCourseByName(deadline.getCourseName());
        c.addDeadline(deadline);
        this.allCourses.put(deadline.getCourseName(), c);
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
        Log.debug("DEBUG: [GUIController] <" + course + "> is now ignored", ANSI_PURPLE);
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
        Log.debug("DEBUG: [GUIController] <" + course + "> is no longer ignored", ANSI_CYAN);
        this.ignoredCoursesSet.remove(course);
        for (Map.Entry<String, DeadlineTimer> item: this.frame.getAllTimersMap().entrySet()) {
            if (item.getKey().startsWith(course)) {
                item.getValue().start();
            }
        }
        this.frame.refresh();
    }
}


package main.controller;

import javafx.util.Pair;
import model.CalendarWrapper;
import model.Course;
import model.Deadline;
import webService.SubmittyAccess;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

/**
 * This class is the controller of the command-line version of Deadline Countdown.
 * This class uses System.out as the View
 */
public class CommandLineController extends AbstractController implements Operations {
    /**
     * Default Constructor
     * @requires None
     * @modifies ignoredCoursesSet, allCourses, allDeadlines
     * @effects create a CommandLineController instance
     */
    public CommandLineController() {
        ignoredCoursesSet = new HashSet<>();
        allCourses = new ConcurrentHashMap<>();
        allDeadlines = new TreeSet<>();
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
     * @return None
     */
    @Override
    public void loadFromLocal(File file, String extension) {
        /* This feature is not supported in a command line version */
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
     * @return None
     */
    @Override
    public void saveToLocal(File file, String extension) {
         /* This feature is not supported in a command line version */
    }

    /**
     * This method should ask the user to agree with following statement:
     * I acknowledge that DeadlineCountdown will not save my id and
     * password in any form after the program terminates and will not
     * use my id and password for any use other than grabbing deadline
     * dates. I understand that I should take my own risk using this
     * program.
     * @requires None
     * @modifies None
     * @effects None
     * @return the user's choice
     */
    public boolean agreement() {
        return Agreement(false);
    }

    /**
     * This method should ask the user to agree with following statement:
     * I acknowledge that DeadlineCountdown will not save my id and
     * password in any form after the program terminates and will not
     * use my id and password for any use other than grabbing deadline
     * dates. I understand that I should take my own risk using this
     * program.
     *
     * This method should only be used in a command-line version that user may input
     * an empty string
     * @param defaultChoice the program's default choice, which should be false
     * @requires None
     * @modifies None
     * @effects None
     * @return the user's choice
     */
    private boolean Agreement(boolean defaultChoice) {
        System.out.println("Term of agreement");
        System.out.print("I acknowledge that DeadlineCountdown will not save my id and " +
                "password \nin any form after the program terminates and will not " +
                "use my \nid and password for any use other than grabbing deadline " +
                "dates. \nI understand that I should take my own risk using this " +
                "program. \n[Y]/N:   ");
        if (defaultChoice) {
            System.out.println(" Y ");
            return true;
        }
        String choice = null;
        try {
            choice = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert(choice != null);
        return choice.equals("Y") || /*Debug Use*/choice.equals("");
    }

    /**
     * This method should ask the user for his/her id and his/her password
     * @requires None
     * @modifies None
     * @effects None
     * @return user's id
     */
    private String AskID() {
        System.out.print("\nEnter the username: ");
        // read from System.in
        String id = null;
        try {
            id = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * This method should ask the user for his/her id and his/her password
     * @requires None
     * @modifies None
     * @effects None
     * @return user's password
     */
    private String AskPassword() {
        Console console = System.console();
        String password = null;
        if (console == null) {
            final JPasswordField jpf = new JPasswordField();
            JOptionPane jop = new JOptionPane(jpf, JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION) {
                @Override
                public void selectInitialValue() {
                    jpf.requestFocusInWindow();
                }
            };
            JDialog dialog = jop.createDialog("Enter the password: ");
            dialog.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            jpf.requestFocusInWindow();
                        }
                    });
                }
            });
            int result = 0;
            try {
                dialog.setVisible(true);
                result = (Integer) jop.getValue();
                dialog.dispose();
            } catch (NullPointerException e) {
                exit(1);
            }

            char[] passwordArray = null;
            if (result == JOptionPane.OK_OPTION) {
                passwordArray = jpf.getPassword();
            }
            if (passwordArray != null) {
                password = new String(passwordArray);
            } else {
                throw new RuntimeException("Password cannot be empty.");
            }
            return password;
        } else {
            char[] passwordArray = console.readPassword("Enter your password: ");
            password = new String(passwordArray);
        }
        return password;
    }

    /**
     * This method should ask the user for his/her id and his/her password
     * @param message the additional message
     * @param lastID the last user_id from the user's input
     * @requires None
     * @modifies None
     * @effects None
     * @return user's username and password
     */
    public Pair<String, String> askIDandPassword(String message, String lastID) {
        return new Pair<>(this.AskID(), this.AskPassword());
    }

    /**
     * This method would try to access submitty via the id and password that the
     * user provided
     * @param id the user id
     * @param password the user password
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     * @throws SubmittyAccess.LoginFailException if the user id or password is not valid
     */
    @Override
    public void access(String id, String password) {
        SubmittyAccess sa = null;
        try {
            sa = new SubmittyAccess(id, password, null);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
        sa.login();
        sa.parser(ignoredCoursesSet);
        allCourses = sa.getCourseMap();
        sa.close();
    }

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
     * @throws CalendarWrapper.CalendarFormatException 
     */
    @Override
    public void addDeadline(String course, String deadlineName, int year, int month,
                            int day, int hour, int minute, String status, String link) {
        Course c = getCourseByName(course);
        try {
            c.addDeadline(deadlineName, year, month, day, hour, minute);
        } catch (CalendarWrapper.CalendarFormatException  e) {
            System.err.println("Due date format not correct:" + e.getMessage());
            e.printStackTrace();
            return;
        }
        allCourses.put(course, c);
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
    public void removeDeadline(String course, String deadlineName, int year, int month, int day) {
        /* This feature is not supported in the command-line version of DeadlineCountdown
         * since the output of System.out is immutable
         */
        Course c = getCourseByName(course);
        c.removeDeadline(deadlineName);
        allCourses.put(course, c);
    }

    /**
     * This method would edit a deadline
     *
     * @param d the deadline object that is about to be edited
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    @Override
    public void editDeadline(Deadline d) {
        /* This feature is not supported in a command line version */
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
        ignoredCoursesSet.add(course);
    }

    /**
     * This function would remove a course name that would be ignored when displaying all
     * deadlines
     * @param course the course name
     * @requires None
     * @modifies a list that stores every course that would be ignored
     * @effects None
     */
    @Override
    public void removeIgnoredCourse(String course) {
        ignoredCoursesSet.remove(course);
    }

    /**
     * This function would run the program and display the result to the user
     * @param args arguments when running the program
     * @requires None
     * @modifies None
     * @effects display the result
     */
    @Override
    public void run(String[] args) {
        int maxlen = 1;
        for (String i : this.allCourses.keySet()) {
            // iterate through courses
            System.out.println("\n"+i);
            System.out.println("========================================================");
            TreeMap<String, Deadline> dueMap = this.allCourses.get(i).getDeadlines();
            List<String> dueList = this.allCourses.get(i).getReversedSortedDeadlines();
            for (String j : dueList) {
                // iterate through deadlines
                System.out.println(j.trim() + ": \t\t" + dueMap.get(j).getRemainingText(CalendarWrapper.now()));
                int currlen = (dueMap.get(j).getCourse().trim() + " " + dueMap.get(j).getName().trim() + ":   ").length();
                if (currlen > maxlen) maxlen = currlen;
            }
        }
        System.out.println("\n\nSUMMARY");
        System.out.println("========================================================");
        for (Deadline d: this.allDeadlines) {
            System.out.printf("%-"+maxlen+"s", d.getCourse().trim() + " " + d.getName().trim() + ":   ");
            System.out.println(d.getRemainingText(CalendarWrapper.now()));
        }
    }

    /**
     * The main function to run the command line version of DeadlineCountdown
     * @param args command line arguments
     */
    public static void main(String[] args) {
        CommandLineController sv = new CommandLineController();
        sv.agreement();
        Pair<String, String> IDandPassword = sv.askIDandPassword("","");
        String id = IDandPassword.getKey();
        String password = IDandPassword.getValue();
        sv.access(id, password);
        sv.updateDeadlineSet();
        sv.run(args);
    }
}

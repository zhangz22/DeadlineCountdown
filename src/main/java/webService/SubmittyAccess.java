package webService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.sun.istack.internal.NotNull;
import model.CalendarWrapper;
import model.Course;
import model.Deadline;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @overview
 * This class will manipulate access action to submitty, and then grab the due dates
 * from every courses
 *
 * @abstract_value
 * driver -> a WebDriver object which will simulate a browser
 * user_id, password -> String object which stores the user_id and password for login
 * courseList -> a <course_name, course_object> map
 * SilentHtmlUnitDriver -> a extended HtmlUnitDriver which suppresses CSS error
 * LoginFailException -> an Exception which happens when login failed
 *
 * @constructor
 * SubmittyAccess(String user_id, String password)
 *
 * @mutator
 * login() throws LoginFailException
 * close()
 *
 * @accessor
 * String getPageSource()
 * HashMap<String, Course> getCourseMap()
 */
public class SubmittyAccess {
    private WebDriver driver;
    private final String user_id;
    private final String password;
    private final Object currProgressLabel;
    private ConcurrentHashMap<String, Course> courseList;
    private class SilentHtmlUnitDriver extends HtmlUnitDriver {
        SilentHtmlUnitDriver(BrowserVersion b) {
            super(b);
            this.getWebClient().setCssErrorHandler(new SilentCssErrorHandler());
        }
    }
    public static class LoginFailException extends RuntimeException {
        LoginFailException(String e) {
            super(e);
        }
    }

    /**
     * This is the basic constructor of this class. Creating a new WebDriver and
     * set up user_id and password
     * @param user_id Submitty user id
     * @param password Submitty password
     * @param currProgressLabel a label to show current progress
     * @requires user_id != NULL; password != NULL
     * @modifies user_id, password
     * @effects create a new SubmittyAccess object
     * @throws LoginFailException if user id or password is empty
     */
    public SubmittyAccess(@NotNull String user_id, @NotNull String password, Object currProgressLabel) throws LoginFailException {
        if (user_id.equals("") || password.equals(""))
            throw new LoginFailException("User id or password cannot be empty.");
        // Create a new instance of the html unit driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20160101 Firefox/66.0";
        driver = new SilentHtmlUnitDriver(new BrowserVersion(
                "Firefox", "5.0 (Windows)",
                USER_AGENT, 66
        ));
        this.user_id = user_id;
        this.password = password;
        this.courseList = new ConcurrentHashMap<>();
        this.currProgressLabel = currProgressLabel;
    }

    /**
     * This function will perform a login action using the user id and password which
     * the user provides
     * @requires None
     * @modifies driver
     * @effects login into submitty
     * @throws LoginFailException when the program cannot login into submitty using
     *                            the provided user id and password
     */
    public void login() throws LoginFailException {
        synchronized (this) {
            if (currProgressLabel != null){
                if (currProgressLabel instanceof JLabel) {
                    ((JLabel) currProgressLabel).setText("Establishing connection...");
                    ((JLabel) currProgressLabel).repaint();
                }
            }
        }

        // And now use this to visit Submitty
        driver.get("https://submitty.cs.rpi.edu/");

        // Find the text input element by its name
        WebElement IdElement = driver.findElement(By.name("user_id"));
        WebElement PasswordElement = driver.findElement(By.name("password"));

        // Enter the id and password
        IdElement.sendKeys(user_id);
        PasswordElement.sendKeys(password);

        // Now submit the form. WebDriver will find the form for us from the element
        PasswordElement.submit();

        // throw an exception if login failed
        if (driver.getPageSource().contains("Could not login using that user id or password")) {
            throw new LoginFailException("Login failed: Could not login using " +
                    "that user id or password");
        }
    }

    /**
     * This function will return a copy of the courseList Map
     * @requires None
     * @modifies None
     * @effects None
     * @return courseList, a <course_name, course_object> map
     */
    public ConcurrentHashMap<String, Course> getCourseMap() {
        return new ConcurrentHashMap<>(this.courseList);
    }

    /**
     * This function will loop over the course list and get information from each
     * course's gradeables
     * @requires None
     * @modifies courseList, driver
     * @effects loop over every courses and get their gradeables
     */
    public void parser(HashSet<String> ExceptionCourses) {
        List<WebElement> CourseElementList = driver.findElements(By.className("btn-block"));
        for (int i=0; i<CourseElementList.size(); ++i) {
            WebElement currCourseBtn = CourseElementList.get(i);
            String courseName = currCourseBtn.getText();
            System.err.println("DEBUG: [accessDriver] Parsing current course " + courseName);

            // Skip useless courses
            if (!courseName.startsWith("Spring 2019")) continue;

            // generate a course object
            Course currCourse = new Course(courseName.replace("Spring 2019", "").trim());
            synchronized (this) {
                if (currProgressLabel != null) {
                    if (currProgressLabel instanceof JLabel) {
                        String courseNameText = currCourse.getCourseName();
                        if (courseNameText.startsWith("CSCI")) {
                            courseNameText = courseNameText.replace(courseNameText.split(" ")[0], "").trim();
                        }
                        ((JLabel) currProgressLabel).setText("Analyzing " + courseNameText);
                        ((JLabel) currProgressLabel).repaint();
                    }
                }
            }

            // Get information about current course
            currCourseBtn.click();
            List<WebElement> DeadlineList = driver.findElements(By.className("btn-nav-submit"));
            System.err.println("DEBUG: [accessDriver] Course <" + courseName + "> Found " + DeadlineList.size()+ " gradables");
            for (WebElement gradeable: DeadlineList) {
                String text = gradeable.findElement(By.xpath("..")).findElement(By.xpath("..")).getText();
                System.err.println("DEBUG: [accessDriver] Working on a new gradable " + text);
                if (text.contains("VIEW GRADE") ||             // Finished Homework
                        text.contains("NO SUBMISSION") ||      // Past Homework
                        text.contains("OVERDUE SUBMISSION") || // Past Homework
                        text.contains("GRADE") ||              // TA Homework
                        text.contains("REGRADE"))              // TA Homework
                    continue;
                String id = gradeable.getAttribute("id");


                String name = text.replace("REGRADE","")
                        .replace("VIEW GRADE","")
                        .replace("GRADE","")
                        .replace("VIEW TEAM","")
                        .replace("LATE SUBMIT","")
                        .replace("LATE RESUBMIT","")
                        .replace("RESUBMIT","")
                        .replace("SUBMIT","")
                        .replace("OVERDUE SUBMISSION","")
                        .replace("NO SUBMISSION","");
                String[] textArr = name.split("\n");
                name = textArr[0].trim();
                try {
                    // team works
                    if (text.contains("CREATE TEAM") || text.contains("MANAGE TEAM")
                            || text.contains("CREATE/JOIN TEAM")) {
                        String teamDueText = textArr[1].trim();
                        CalendarWrapper teamDueDate;
                        try {
                            teamDueDate = Deadline.parseDate(teamDueText);
                            name = name.
                                    replace("CREATE TEAM", "").
                                    replace("MANAGE TEAM", "").
                                    replace("CREATE/JOIN TEAM", "");
                            String createTeamName = name + " (MANAGE TEAM)";
                            Deadline teamDue = new Deadline(teamDueDate, createTeamName, currCourse.getCourseName());
                            currCourse.addDeadline(teamDue);
                        } catch (CalendarWrapper.CalendarFormatException  ept) {
                            System.err.println("Due date format not correct: " + textArr[1].trim());
                        } catch (ArrayIndexOutOfBoundsException ept) {
                            System.err.println("No team deadlines");
                        }
                    }
                    WebElement due = gradeable.findElement(By.className("subtitle"));
                    String dueText = due.getText();
                    CalendarWrapper currDueDate = Deadline.parseDate(dueText);
                    Deadline currDue = new Deadline(currDueDate, name, currCourse.getCourseName());
                    System.err.println("DEBUG: [accessDriver] Adding " + currDue.toString());
                    currCourse.addDeadline(currDue);
                } catch (NoSuchElementException ept) {
                    System.err.println("No due for this element " + name);
                } catch (CalendarWrapper.CalendarFormatException  ept) {
                    System.err.println("Due date format not correct.");
                }
            }
            this.courseList.put(courseName, currCourse);
            driver.navigate().back();
            CourseElementList = driver.findElements(By.className("btn-block"));
        }
        System.err.println("DEBUG: [accessDriver] End of parsing");
    }

    /**
     * This function will terminates the web driver
     * @requires None
     * @modifies driver
     * @effects terminates the web driver
     */
    public void close() {
        driver.quit();
    }
}

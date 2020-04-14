package main.viewer.calendarPanel;

import javafx.util.Pair;
import model.CalendarWrapper;
import model.Deadline;
import main.viewer.Log;
import main.controller.GUIController;
import main.viewer.textFormat.BaseText;
import main.viewer.textFormat.ViewerFont;
import main.viewer.theme.Theme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.*;


/**
 * This component is created represent the center part (a visual calendar) of main
 */
public class CalendarPanel extends JPanel {
    private final GUIController parent;
    private JPanel weekdayTitlePanel;
    private JPanel datesPanel;
    private ArrayList<DateBlock> allDateBlocks;
    private BaseText textStr;
    private int monthOfToday;
    private int yearOfToday;
    private int dateOfToday;
    private int maxDayNumDisplayMonth;
    private int displayMonth;
    private int displayYear;
    private HashMap<String, Deadline> allDeadlines;

    /**
     * Constructor
     * @param parent the parent component
     * @param t stores all text needed
     * @requires None
     * @modifies this, textStr, allDateBlocks, allDeadlines, weekdayTitlePanel
     * @effects create a calendarPanel
     */
    public CalendarPanel(GUIController parent, BaseText t) {
        super();
        this.textStr = t;
        this.allDateBlocks = new ArrayList<>();
        this.allDeadlines = new HashMap<>();
        this.parent = parent;

        // add components
        this.setLayout(new BorderLayout());
        this.setBackground(getTheme().CAL_BACKGROUND());
        this.setBorder(BorderFactory.createEmptyBorder());

        // create the weekday title panel
        this.weekdayTitlePanel = new JPanel();
        this.weekdayTitlePanel.setLayout(new GridLayout(0, 7, 0, 2));
        this.weekdayTitlePanel.setBackground(getTheme().CAL_TITLE_SEP());
        this.createWeekdayTitlePanel();

        JPanel underline = new JPanel();
        underline.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        underline.setPreferredSize(new Dimension(0, 1));
        underline.setBackground(this.getTheme().CAL_TITLE_SEP());

        JPanel weekdayWithUnderline = new JPanel();
        weekdayWithUnderline.setLayout(new BorderLayout());
        weekdayWithUnderline.add(underline, BorderLayout.SOUTH);
        weekdayWithUnderline.add(this.weekdayTitlePanel, BorderLayout.NORTH);
        weekdayWithUnderline.setBackground(getTheme().CAL_INVALID_BACKGROUND());
        this.add(weekdayWithUnderline, BorderLayout.NORTH);

        // create the center panel of the calendar
        this.datesPanel = new JPanel();
        this.datesPanel.setLayout(new GridLayout(0, 7, 0, 1));
        this.datesPanel.setBackground(getTheme().CAL_INVALID_BACKGROUND());
        this.add(datesPanel, BorderLayout.CENTER);

        this.dateOfToday = CalendarWrapper.now().getDay();
        this.monthOfToday = CalendarWrapper.now().getMonth();
        this.yearOfToday = CalendarWrapper.now().getYear();

        // set the calendar's date to today
        this.createDatePanels(this.monthOfToday, this.yearOfToday);

        this.addPropertyChangeListener(
                e -> {
                    if ("date".equals(e.getPropertyName())) {
                        monthOfToday = ((Calendar) e.getNewValue()).get(Calendar.DAY_OF_MONTH);
                        refresh();
                    }
                });
    }

    /**
     * This function would set up the weekday title panel (Monday, Tuesday ...)
     * @requires None
     * @modifies weekdayTitlePanel
     * @effects set up the weekdayTitlePanel
     */
    private void createWeekdayTitlePanel() {
        this.weekdayTitlePanel.removeAll();
        this.weekdayTitlePanel.revalidate();
        this.weekdayTitlePanel.repaint();
        int i, max;
        if (parent.getSettings().isStartFromSunday()) {
            i = 0;
            max = 7;
        } else {
            i = 1;
            max = 8;
        }
        for (; i < max; i++) {
            DateBlock currWeekday = new DateBlock(0, 0, 0, textStr.weekFormat(i), this);
            currWeekday.getDateTextPanel().setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 19));
            this.weekdayTitlePanel.add(currWeekday);
        }
    }

    /**
     * This function creates date blocks in the dates panel for a specified month
     * @param month the month number of a year, starts from 1 (Jan)
     * @param year the year number
     * @requires None
     * @modifies datesPanel, allDateBlocks, displayYear, displayMonth,
     *           maxDayNumDisplayMonth
     * @effects create all date blocks based on the dates of the required month
     */
    private void createDatePanels(int month, int year) {
        // change the year and month that the calendar is display
        this.displayYear = year;
        this.displayMonth = month;
        CalendarWrapper c = new CalendarWrapper();
        c.setYear(this.displayYear);
        c.setMonth(this.displayMonth);
        // how many days there are before the first day
        int numOfDaysBeforeFirstDay = c.getWeekDayOfFirstDayInMonth() - 1;
        if (parent.getSettings().isStartFromSunday()) {
            numOfDaysBeforeFirstDay += 1;
        }
        if (numOfDaysBeforeFirstDay == -1) {
            numOfDaysBeforeFirstDay = 6;
        }
        this.maxDayNumDisplayMonth = c.getMaxDayNumOfMonth();

        // create the dates panel
        for (int i = 1; i <= maxDayNumDisplayMonth + numOfDaysBeforeFirstDay; i++) {
            DateBlock currDayBlock;
            if (i <= numOfDaysBeforeFirstDay) {
                currDayBlock = new DateBlock(0, 0,0,"", this);
                currDayBlock.setColor(this.getTheme().CAL_INVALID_BACKGROUND());
            } else {
                int currDate = i - numOfDaysBeforeFirstDay;
                currDayBlock = new DateBlock(this.displayYear, this.displayMonth,
                        currDate, String.valueOf(currDate), this);
                if (currDate == dateOfToday &&
                        displayMonth == monthOfToday &&
                        displayYear == yearOfToday) {
                    currDayBlock.getDateTextPanel().setDisabledTextColor(this.getTheme().TODAY_TEXT());
                    currDayBlock.setColor(this.getTheme().TODAY());
                }
                this.allDateBlocks.add(currDayBlock);
            }
            this.datesPanel.add(currDayBlock);
        }
        this.displayAllDeadlines();
    }

    /**
     * This function clears the dates panel
     * @requires None
     * @modifies datesPanel, allDatesBlocks
     * @effects dispose all DateBlocks
     */
    private void clearDatesPanel() {
        this.datesPanel.removeAll();
        this.datesPanel.revalidate();
        this.datesPanel.repaint();
        this.allDateBlocks.clear();
    }

    /**
     * This method would add a new DeadlineBlock to a specified date block
     * @param deadline the deadline that would be added
     * @requires None
     * @modifies a list that stores every course and their information
     * @effects None
     */
    private void addSingleDeadlineBlock(Deadline deadline) {
        if (deadline.isBefore(CalendarWrapper.now()) &&
            !parent.getSettings().isShowPastDeadlines()) {
            return;
        }
        if (parent.isIgnoring(deadline.getCourseName())) {
            return;
        }
        if (deadline.getMonth() == this.displayMonth && deadline.getYear() == this.displayYear) {
            Log.debug("DEBUG: [CalendarPanel] {" +
                    deadline.getName() + " <" + deadline.getCourseName() + "> (" +
                    CalendarWrapper.getTimeStr(deadline.getYear(), deadline.getMonth(), deadline.getDay(), deadline.getHour(), deadline.getMinute()) + ")" +
                    "} will be added to calendar.", Log.ANSI_BLUE);
            this.getDatePanel(deadline.getDay()).addDeadline(new DeadlineBlock(this, deadline));
            this.getDatePanel(deadline.getDay()).getLowerPart().add(Box.createVerticalStrut(1));
        }
    }

    /**
     * This method would remove a deadline block from a specified date block
     * @param deadline the deadline that would be removed
     * @requires None
     * @modifies parent
     * @effects remove a deadline from a specified date block
     */
    private void removeSingleDeadlineBlock(Deadline deadline) {
        if (deadline.getMonth() == this.displayMonth && deadline.getYear() == this.displayYear) {
            this.getDatePanel(deadline.getDay()).removeDeadline(new DeadlineBlock(this, deadline));
        }
        this.getDatePanel(deadline.getDay()).getLowerPart().revalidate();
        this.getDatePanel(deadline.getDay()).getLowerPart().repaint();
    }

    /**
     * This function will display deadlines from this.allDeadlines to the date block
     * which represents the deadline due date
     * @requires None
     * @modifies this.getDatePanel(i).getLowerPart()
     * @effects display all deadlines
     */
    public void displayAllDeadlines() {
        for (int i = 1; i <= this.maxDayNumDisplayMonth; i++) {
            this.getDatePanel(i).getLowerPart().removeAll();
            this.getDatePanel(i).getLowerPart().revalidate();
            this.getDatePanel(i).getLowerPart().repaint();
        }
        for (String deadlineName: this.allDeadlines.keySet()) {
            Deadline currDeadline = this.allDeadlines.get(deadlineName);
            String course = this.allDeadlines.get(deadlineName).getCourseName();
            if (parent.isIgnoring(course)) continue;
            if (!parent.getSettings().isShowPastDeadlines() &&
                    currDeadline.isBefore(CalendarWrapper.now())) {
                continue;
            }
            this.addSingleDeadlineBlock(currDeadline);
        }
    }

    /**
     * This function returns a specified dateBlock based on a given date
     * @param date the date number of a month. The first day of the month has value 1.
     * @requires None
     * @modifies None
     * @effects None
     * @return a date block representing the required date
     */
    private DateBlock getDatePanel(int date) {
        return this.allDateBlocks.get(date-1);
    }

    /**
     * This function returns the displayed month and year
     * @requires None
     * @modifies None
     * @effects None
     * @return display month and display year
     */
    public Pair<Integer, Integer> getDisplayDate() {
        return new Pair<>(this.displayMonth, this.displayYear);
    }

    /**
     * This function returns the main component of main
     * @requires None
     * @modifies None
     * @effects None
     * @return the main component
     */
    GUIController getMainmainGUI() { return this.parent; }

    /**
     * This function returns the theme of main
     * @return the current theme
     * @requires None
     * @modifies None
     * @effects None
     */
    public Theme getTheme() {
        return this.parent.getFrame().getTheme();
    }

    /**
     * This function updates the month and year which the panel is displaying
     * @param month the month number of a year, starts from 1 (Jan)
     * @param year the year number
     * @requires None
     * @modifies datesPanel, allDateBlocks, displayYear, displayMonth,
     *           maxDayNumDisplayMonth
     * @effects update the month and year
     */
    public void setMonthYear(int month, int year) {
        if (month == this.displayMonth && year == this.displayYear)
            // when the user clicks on a deadline within current month, no need to reset all DateBlocks
            return;
        this.clearDatesPanel();
        this.createDatePanels(month, year);
    }

    /**
     * This function will make the calendar panel highlight on a specified date
     * @param date the date number of a month. The first day of the month has value 1.
     * @requires None
     * @modifies a date block representing the required date
     * @effects None
     */
    public void highlightDateBlock(int date) {
        this.getDatePanel(date).highlight();
    }

    /**
     * This method would add a new deadline to the date block of the deadline due
     * date
     * @param newDeadline the new deadline that will be added
     * @requires None
     * @modifies allDateBlocks, allDeadlines
     * @effects None
     */
    public void addDeadline(Deadline newDeadline) {
        if (this.allDeadlines.containsKey(newDeadline.getName())) {
            if (this.allDeadlines.get(newDeadline.getName()).equals(newDeadline)) {
                return;
            } else {
                // update the deadline information
                Deadline oldTimeInfo = allDeadlines.get(newDeadline.getName());
                this.removeSingleDeadlineBlock(oldTimeInfo);
            }
        }
        this.allDeadlines.put(newDeadline.getName(), newDeadline);
        this.addSingleDeadlineBlock(newDeadline);
    }

    /**
     * This method would remove a deadline
     *
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day          the day number; starts from 1 to 31
     * @requires None
     * @modifies parent
     * @effects remove a deadline
     */
    public void removeDeadline(String course, String deadlineName, int year, int month, int day) {
        this.removeSingleDeadlineBlock(new Deadline(year, month, day, 0, 0, deadlineName, course, Deadline.STATUS.DEFAULT, Deadline.LINK.NONE));
        this.allDeadlines.remove(deadlineName);
    }

    /**
     * This method will call the removeDeadline method of the parent component
     *
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day          the day number; starts from 1 to 31
     * @requires None
     * @modifies parent
     * @effects remove a deadline
     */
    void handleRemoveSignal(String course, String deadlineName, int year, int month, int day) {
        parent.removeDeadline(course, deadlineName, year, month, day);
    }

    /**
     * This method will call the editDeadline method of the parent component
     *
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day          the day number; starts from 1 to 31
     * @requires None
     * @modifies parent
     * @effects edit a deadline
     */
    void handleEditSignal(String course, String deadlineName, int year, int month,
                            int day, int hour, int minute, String status) {
        if (year == 0) {
            year = displayYear;
        }
        if (month == 0) {
            month = displayMonth;
        }
        if (hour == 0) {
            hour = CalendarWrapper.now().getHourOfDay();
        }
        if (minute == 0) {
            minute = CalendarWrapper.now().getMinuteOfHour();
        }
        if (status == null) {
            status = Deadline.STATUS.DEFAULT;
        }
        parent.editDeadline(new Deadline(year, month, day, hour, minute, deadlineName, course, status, Deadline.LINK.NONE));
    }

    /**
     * This method will call the editDeadline method of the parent component
     *
     * @param deadline the deadline being edited
     * @requires None
     * @modifies parent
     * @effects edit a deadline
     */
    void handleEditSignal(Deadline deadline) {
        parent.editDeadline(deadline);
    }

    /**
     * This function refreshes the whole calendar panel, including the weekday title
     * and the dates panel
     *
     * @requires None
     * @modifies None
     * @effects None
     */
    public void refresh() {
        this.createWeekdayTitlePanel();
        this.clearDatesPanel();
        this.createDatePanels(this.displayMonth, this.displayYear);
    }
}
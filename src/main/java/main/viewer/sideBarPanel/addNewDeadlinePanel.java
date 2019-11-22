package main.viewer.sideBarPanel;

import javafx.util.Pair;
import model.CalendarWrapper;
import model.Deadline;
import main.viewer.Log;
import main.viewer.DeadlineCountdownFactory;
import main.viewer.textFormat.ViewerFont;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.TreeSet;

/**
 * This panel would appear only after user clicks the "add a new deadline" button
 */
class addNewDeadlinePanel extends JPanel {
    private final SideBarPanel parent;
    private TreeSet<String> allCourseName;
    private GridBagConstraints constraint;
    private JComboBox<String> courseNameBox;
    private JTextField deadlineNameField;
    private JTextField linkField;
    private JComboBox<String> yearField;
    private JComboBox<String> monthField;
    private JComboBox<String> dayField;
    private JComboBox<String> minuteField;
    private JComboBox<String> hourField;
    private JComboBox<String> statusField;
    private int oldYear, oldMonth, oldDay;
    private String oldName, oldCourse;

    /**
     * Constructor
     * @param parent the sideBarPanel main component
     * @requires parent != null
     * @modifies this, parent, allCourseName, constraint, courseNameBox
     *           deadlineNameField, yearField, monthField, dayField, minuteField,
     *           hourField
     * @effects create an instance of addNewDeadlinePanel
     */
    addNewDeadlinePanel(SideBarPanel parent, boolean isAdd) {
        // create a new Panel
        super();
        super.setBackground(parent.getTheme().SIDEBAR_BACKGROUND());
        super.setBounds(0,0,0,0);
        super.setMaximumSize(new Dimension(330, Integer.MAX_VALUE));
        super.setPreferredSize(new Dimension(330, 0));
        super.setMinimumSize(new Dimension(330, 0));
        super.setBorder(BorderFactory.createEmptyBorder());

        // set the GridBagLayout configurations
        super.setLayout(new GridBagLayout());
        this.constraint = new GridBagConstraints();
        this.constraint.fill = GridBagConstraints.HORIZONTAL;
        this.constraint.anchor = GridBagConstraints.WEST;
        this.constraint.ipadx = 10;
        this.constraint.ipady = 10;
        this.constraint.insets = new Insets(3, 3, 3, 3);

        // set the parent panel
        this.parent = parent;
        this.allCourseName = new TreeSet<>();

        // area to let user enters the course name
        JLabel courseNameLabel = SideBarFactory.createSimpleLabel(getText("enter_course_name") + " ",
                18, parent.getTheme().SIDEBAR_TEXT());
        this.addToPanel(courseNameLabel, 0,0,1);

        this.courseNameBox = SideBarFactory.createEditableComboBox(parent.getTheme().SIDEBAR_HOVER(),
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        for (String c: parent.getAllCourseNames()) {
            this.courseNameBox.addItem(c);
        }
        this.courseNameBox.setSelectedItem("");
        this.courseNameBox.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.courseNameBox.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.courseNameBox.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.courseNameBox.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.courseNameBox.setPrototypeDisplayValue("XXXXXXX");
        ((JLabel)this.courseNameBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        this.addToPanel(this.courseNameBox, 0, 1, 1);

        // area to let user enters the deadline name
        JLabel deadlineNameLabel = SideBarFactory.createSimpleLabel(getText("enter_deadline_name") + " ",
                18, parent.getTheme().SIDEBAR_TEXT());
        this.addToPanel(deadlineNameLabel, 0,2,1);

        this.deadlineNameField = new JTextField();
        this.deadlineNameField.setBackground(parent.getTheme().SIDEBAR_HOVER());
        this.deadlineNameField.setForeground(parent.getTheme().SIDEBAR_TEXT());
        this.deadlineNameField.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 18));
        this.deadlineNameField.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.deadlineNameField.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.deadlineNameField.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.deadlineNameField.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.deadlineNameField.setBorder(BorderFactory.createEmptyBorder());
        this.deadlineNameField.setCaretColor(parent.getTheme().SIDEBAR_TEXT());
        this.addToPanel(this.deadlineNameField, 0, 3, 1);

        // get current time
        CalendarWrapper now = CalendarWrapper.now();
        int yearOfToday = now.getYear();
        int maxDayNumOfMonth = now.getMaxDayNumOfMonth();

        // area to let user enters the deadline date
        constraint.fill = GridBagConstraints.NONE;
        JPanel dateSelectionPanel = DeadlineCountdownFactory.createPanel(330, 45, true);
        dateSelectionPanel.setLayout(new GridLayout(2, 3,5,5));
        dateSelectionPanel.add(SideBarFactory.createSimpleLabel(getText("year_field"), 15,
                parent.getTheme().SIDEBAR_TEXT()));
        dateSelectionPanel.add(SideBarFactory.createSimpleLabel(getText("month_field"), 15,
                parent.getTheme().SIDEBAR_TEXT()));
        dateSelectionPanel.add(SideBarFactory.createSimpleLabel(getText("date_field"), 15,
                parent.getTheme().SIDEBAR_TEXT()));
        dateSelectionPanel.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        dateSelectionPanel.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        dateSelectionPanel.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        dateSelectionPanel.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        this.addToPanel(dateSelectionPanel, 0, 5, 1);

        this.yearField = SideBarFactory.createEditableComboBox(parent.getTheme().SIDEBAR_HOVER(),
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        for (int y = yearOfToday; y < yearOfToday + 5; y++) {
            this.yearField.addItem(String.valueOf(y));
        }
        this.yearField.setSelectedItem(String.valueOf(yearOfToday));

        this.monthField = SideBarFactory.createEditableComboBox(parent.getTheme().SIDEBAR_HOVER(),
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        for (int m = 1; m < 13; m ++) {
            this.monthField.addItem(String.valueOf(m));
        }
        this.monthField.setSelectedItem(String.valueOf(now.getMonth()));

        this.dayField = SideBarFactory.createEditableComboBox(parent.getTheme().SIDEBAR_HOVER(),
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        for (int d = 1; d <= maxDayNumOfMonth; d++) {
            this.dayField.addItem(String.valueOf(d));
        }
        this.dayField.setSelectedItem(String.valueOf(now.getDay()));

        dateSelectionPanel.add(this.yearField);
        dateSelectionPanel.add(this.monthField);
        dateSelectionPanel.add(this.dayField);

        // area to let user enters the deadline hour
        JPanel timeSelectionPanel = DeadlineCountdownFactory.createPanel(330, 45, true);
        timeSelectionPanel.setLayout(new GridLayout(2, 2,5,5));
        timeSelectionPanel.add(SideBarFactory.createSimpleLabel(getText("hour_field"),
                15, parent.getTheme().SIDEBAR_TEXT()));
        timeSelectionPanel.add(SideBarFactory.createSimpleLabel(getText("minute_field"),
                15, parent.getTheme().SIDEBAR_TEXT()));
        timeSelectionPanel.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        timeSelectionPanel.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        timeSelectionPanel.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        timeSelectionPanel.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 45));
        this.hourField = SideBarFactory.createEditableComboBox(parent.getTheme().SIDEBAR_HOVER(),
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        for (int h = 0; h < 24; h++) {
            this.hourField.addItem(String.valueOf(h));
        }
        this.hourField.setSelectedItem(String.valueOf(now.getHourOfDay()));

        // area to let user enters the deadline minute
        this.minuteField = SideBarFactory.createEditableComboBox(parent.getTheme().SIDEBAR_HOVER(),
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        for (int m = 0; m < 60; m++) {
            this.minuteField.addItem(String.valueOf(m));
        }
        this.minuteField.setSelectedItem(String.valueOf(now.getMinuteOfHour()));
        timeSelectionPanel.add(this.hourField);
        timeSelectionPanel.add(this.minuteField);
        this.addToPanel(timeSelectionPanel, 0, 6, 1);

        // area to let user enters the deadline link
        this.linkField = new JTextField();
        this.linkField.setBackground(parent.getTheme().SIDEBAR_HOVER());
        this.linkField.setForeground(parent.getTheme().SIDEBAR_TEXT());
        this.linkField.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 18));
        this.linkField.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.linkField.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.linkField.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.linkField.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.linkField.setBorder(BorderFactory.createEmptyBorder());
        this.linkField.setCaretColor(parent.getTheme().SIDEBAR_TEXT());

        JPanel linkSelectionPanel = DeadlineCountdownFactory.createPanel(SideBarPanel.SIDEBAR_WIDTH, 45, true);
        linkSelectionPanel.setLayout(new GridLayout(2, 1,5,5));
        linkSelectionPanel.add(SideBarFactory.createSimpleLabel(getText("link_field"), 15,
                parent.getTheme().SIDEBAR_TEXT()));
        linkSelectionPanel.add(this.linkField);
        this.addToPanel(linkSelectionPanel, 0, 7, 1);

        // area to let user enters the deadline status
        this.statusField = SideBarFactory.createEditableComboBox(parent.getTheme().SIDEBAR_HOVER(),
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        for (String s: Deadline.STATUS.getAllStatus()) {
            this.statusField.addItem(s);
        }
        this.statusField.setEditable(false);
        this.statusField.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.statusField.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.statusField.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.statusField.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 20));
        this.statusField.setSelectedItem(Deadline.STATUS.DEFAULT);

        JPanel statusSelectionPanel = DeadlineCountdownFactory.createPanel(SideBarPanel.SIDEBAR_WIDTH, 45, true);
        statusSelectionPanel.setLayout(new GridLayout(2, 2,5,5));
        statusSelectionPanel.add(SideBarFactory.createSimpleLabel(getText("status_field"), 15,
                parent.getTheme().SIDEBAR_TEXT()));
        statusSelectionPanel.add(this.statusField);
        this.addToPanel(statusSelectionPanel, 0, 8, 1);
        this.addToPanel(DeadlineCountdownFactory.createEmptyArea(1, true), 0, 9, 1);


        // the confirm and cancel button

        JPanel buttons = DeadlineCountdownFactory.createPanel(SideBarPanel.SIDEBAR_WIDTH, 22,true);
        buttons.setLayout(new BorderLayout());

        JButton confirmBtn = DeadlineCountdownFactory.createSimpleButton("  " + getText("ok") + "  ",
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        confirmBtn.setBackground(parent.getTheme().SIDEBAR_BACKGROUND());
        confirmBtn.setFont(new Font(ViewerFont.XHEI, Font.BOLD, 18));
        confirmBtn.setMargin(new Insets(0,0,0,0));
        confirmBtn.setBorderPainted(true);
        confirmBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        confirmBtn.addMouseListener(DeadlineCountdownFactory.getHoverEffect(confirmBtn,
                parent.getTheme().SIDEBAR_HOVER()));
        confirmBtn.addActionListener(e -> {
            if (isAdd)
                addDeadline(true);
            else
                addDeadline(false);
            parent.showSummary();
        });

        JButton cancelBtn = DeadlineCountdownFactory.createSimpleButton("  " + getText("cancel") + "  ",
                parent.getTheme().SIDEBAR_BACKGROUND(), parent.getTheme().SIDEBAR_TEXT());
        cancelBtn.setBackground(parent.getTheme().SIDEBAR_BACKGROUND());
        cancelBtn.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 18));
        cancelBtn.setMargin(new Insets(0,0,0,0));
        cancelBtn.addActionListener(e -> parent.showSummary());
        cancelBtn.addMouseListener(DeadlineCountdownFactory.getHoverEffect(cancelBtn,
                parent.getTheme().SIDEBAR_HOVER()));

        buttons.add(confirmBtn, BorderLayout.WEST);
        buttons.add(cancelBtn, BorderLayout.EAST);
        this.addToPanel(buttons, 0, 10, 1);
    }

    /**
     * This function returns a text string contained within the ResourceBundle
     * @param context the string need to be found
     * @requires None
     * @modifies None
     * @effects None
     * @return a string in required locale
     */
    private String getText(String context) {
        return parent.getMainmainGUI().getFrame().getText(context);
    }


    /**
     * This function should set the input fields to be the same as the old deadline
     * @param deadline the deadline object that is about to be edited
     * @requires deadline != null
     * @modifies courseNameBox, deadlineNameField, yearField, monthField, dayField,
     *           hourField, minuteField, statusField
     * @effects change the input fields
     */
    void handleEditSignal(Deadline deadline) {
        // update course combo box
        this.refreshCourseNameBox();
        // change the input fields
        this.courseNameBox.setSelectedItem(deadline.getCourseName());
        SwingUtilities.invokeLater(() ->
                ((JTextField)courseNameBox.getEditor().getEditorComponent()).setCaretPosition(0));
        this.deadlineNameField.setText(deadline.getName());
        SwingUtilities.invokeLater(() -> deadlineNameField.setCaretPosition(0));
        this.yearField.setSelectedItem(String.valueOf(deadline.getYear()));
        this.monthField.setSelectedItem(String.valueOf(deadline.getMonth()));
        this.dayField.setSelectedItem(String.valueOf(deadline.getDay()));
        this.hourField.setSelectedItem(String.valueOf(deadline.getHour()));
        this.minuteField.setSelectedItem(String.valueOf(deadline.getMinute()));
        this.linkField.setText(deadline.getLink());
        SwingUtilities.invokeLater(() -> linkField.setCaretPosition(0));
        switch (deadline.getStatus()) {
            case Deadline.STATUS.RESUBMIT:
                this.statusField.setSelectedItem(Deadline.STATUS.RESUBMIT);
                break;
            case Deadline.STATUS.FINISHED:
                this.statusField.setSelectedItem(Deadline.STATUS.FINISHED);
                break;
            case Deadline.STATUS.LATE_SUBMIT:
                this.statusField.setSelectedItem(Deadline.STATUS.LATE_SUBMIT);
                break;
            case Deadline.STATUS.LATE_RESUBMIT:
                this.statusField.setSelectedItem(Deadline.STATUS.LATE_RESUBMIT);
                break;
            case Deadline.STATUS.MUST_ON_TEAM:
                this.statusField.setSelectedItem(Deadline.STATUS.MUST_ON_TEAM);
                break;
            case Deadline.STATUS.OVERDUE_SUBMISSION:
                this.statusField.setSelectedItem(Deadline.STATUS.OVERDUE_SUBMISSION);
                break;
            case Deadline.STATUS.NO_SUBMISSION:
                this.statusField.setSelectedItem(Deadline.STATUS.NO_SUBMISSION);
                break;
            default:
                this.statusField.setSelectedItem(Deadline.STATUS.DEFAULT);
                break;
        }
        this.oldYear = deadline.getYear();
        this.oldMonth = deadline.getMonth();
        this.oldDay = deadline.getDay();
        this.oldName = deadline.getName();
        this.oldCourse = deadline.getCourseName();
    }

    /**
     * This function would get called once the user clicked the "OK" button. It would
     * receive inputs from comboBoxes and textFields and add a new deadline to main
     * based on the input
     * @param add a boolean to indicate if this deadline is a newly-added or an edited one
     * @requires None
     * @modifies parent
     * @effects add a new deadline
     */
    private void addDeadline(boolean add) {
        if (this.yearField.getSelectedItem() == null || this.monthField.getSelectedItem() == null ||
                this.dayField.getSelectedItem() == null || this.hourField.getSelectedItem() == null ||
                this.minuteField.getSelectedItem() == null ||
                this.deadlineNameField.getText() == null || this.deadlineNameField.getText().equals("") ||
                this.statusField.getSelectedItem() == null) {
            return;
        }
        int year = Integer.parseInt((String) this.yearField.getSelectedItem());
        int month = Integer.parseInt((String) this.monthField.getSelectedItem());
        int day = Integer.parseInt((String) this.dayField.getSelectedItem());
        int hour = Integer.parseInt((String) this.hourField.getSelectedItem());
        int minute = Integer.parseInt((String) this.minuteField.getSelectedItem());
        String status = (String) this.statusField.getSelectedItem();
        String deadlineName = this.deadlineNameField.getText();
        String courseName = (String) this.courseNameBox.getSelectedItem();
        String link = this.linkField.getText();
        Log.debug("DEBUG: [newDeadlinePanel] adding " + courseName +
                ": " + deadlineName + " (" + year + "/" + month + "/" + day +"/"+ hour +"/" +
                minute + ").");
        if (!add) {
            parent.handleRemoveSignal(this.oldCourse, this.oldName, this.oldYear, this.oldMonth, this.oldDay);
        }
        parent.handleAddDeadlineSignal(courseName, deadlineName, year, month, day, hour, minute, status, link);
        if (!new Pair<>(month,year).equals(parent.getMainmainGUI().getFrame().getCalendarPanel().getDisplayDate())
            && !((month <= 0 || month > 12) || (day <= 0 || day > 31))) {
            parent.getMainmainGUI().getFrame().setMonthYear(month, year);
        }
    }

    /**
     * This function would add a component to the specified coordinate
     * @param component the JComponent to be added
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the gridwidth
     * @requires None
     * @modifies this, constraint
     * @effects add a new component
     */
    private void addToPanel(JComponent component, int x, int y, int width) {
        constraint.gridx = x;
        constraint.gridy = y;
        constraint.gridwidth = width;
        super.add(component, constraint);
    }

    /**
     * This function would add a new course name to the selection box
     * @param courseName the name of the course
     * @requires None
     * @modifies allCourseName, courseNameBox
     */
    void addCourse(String courseName) {
        this.allCourseName.add(courseName);
        this.courseNameBox.removeAllItems();
        for (String c: this.allCourseName) {
            this.courseNameBox.addItem(c);
        }
        this.courseNameBox.setSelectedItem("");
    }

    /**
     * This function refreshes the selections in course combobox
     * @requires None
     * @modifies courseNameBox
     * @effects refresh selectable courses
     */
    void refreshCourseNameBox() {
        this.courseNameBox.removeAllItems();
        for (String c: parent.getAllCourseNames()) {
            this.courseNameBox.addItem(c);
        }
        this.courseNameBox.setSelectedItem("");
    }
}

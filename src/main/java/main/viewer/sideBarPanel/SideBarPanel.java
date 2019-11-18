package main.viewer.sideBarPanel;

import model.CalendarWrapper;
import model.Deadline;
import main.controller.GUIController;
import main.viewer.mainFactory;
import main.viewer.theme.Theme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * This component is created to represent the side bar of main.
 */
public class SideBarPanel extends JPanel {
    public static final int SIDEBAR_WIDTH = 350;
    private final GUIController parent;
    private HashMap<String, Deadline> allDeadlines;
    private TreeSet<String> allCourseNames;
    private String username;
    private JTextArea welcomeTextArea;
    private JComboBox<String> sortOrderBox;
    private Comparator<Deadline> sortOrder;
    private JPanel lowerPart;
    private JScrollPane summaryScrollPanel;
    private JPanel summaryPanel;
    private JScrollPane courseScrollPanel;
    private JPanel coursePanel;
    private addNewDeadlinePanel addNewDeadlinePanel;
    private addNewDeadlinePanel editDeadlinePanel;
    private JCheckBox pastDeadlinesBox;
    private CardLayout cardLayout;

    public static final class PANEL {
        public static final String DEADLINE_PANEL = "Deadline list";
        public static final String ADD_NEW_DEADLINE_PANEL = "Add new deadline";
        public static final String EDIT_DEADLINE_PANEL = "Edit deadline";
        public static final String COURSE_PANEL = "Course";
    }

    /**
     * Constructor
     * @param deadlines a hashmap stores all deadline objects
     * @param username a string stores the username
     * @param parent the GUIController main component
     * @requires deadlines != null, username != null, parent != null
     * @modifies this, username, parent, sortOrder, sortOrderBox, summaryPanel,
     *           summaryScrollPanel, addNewDeadlinePanel, settingsPanel, cardLayout
     * @effects create an instance of the side bar of main
     */
    public SideBarPanel(HashMap<String, Deadline> deadlines, String username, GUIController parent) {
        super();
        this.setPreferredSize(new Dimension(350, 0));
        this.setMinimumSize(new Dimension(350, 0));
        this.setBackground(parent.getFrame().getTheme().SIDEBAR_BACKGROUND());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.allDeadlines = (deadlines != null) ? deadlines: (new HashMap<>());
        this.allCourseNames = new TreeSet<>();
        for (Deadline d: allDeadlines.values()) {
            this.allCourseNames.add(d.getCourse());
        }
        this.username = username;
        this.parent = parent;

        // configure the order of sorting the deadlines
        this.sortOrder = Comparator.naturalOrder();
        this.sortOrderBox = SideBarFactory.createComboBox(parent.getFrame().getTheme().SIDEBAR_HOVER(),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        this.sortOrderBox.setPreferredSize(new Dimension(170, 30));
        this.sortOrderBox.addItem(parent.getFrame().getText("date2"));
        this.sortOrderBox.addItem(parent.getFrame().getText("date"));
        this.sortOrderBox.addItem(parent.getFrame().getText("name"));
        this.sortOrderBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object source = e.getSource();
                if (source instanceof JComboBox) {
                    JComboBox cb = (JComboBox) source;
                    Object selectedItem = cb.getSelectedItem();
                    if (parent.getFrame().getText("date2").equals(selectedItem)) {
                        sortOrder = Comparator.naturalOrder();
                    } else if (parent.getFrame().getText("date").equals(selectedItem)) {
                        sortOrder = Comparator.naturalOrder();  // TODO need a function to compare the time
                    } else if (parent.getFrame().getText("name").equals(selectedItem)) {
                        sortOrder = Comparator.comparing(o -> (o.getCourse() + " " + o.getName()));
                    }
                }
                this.updateSummaryPanel();  // Refresh after changing order
            }
        });

        // create a panel to list the deadlines
        this.summaryPanel = new JPanel();
        this.summaryPanel.setBackground(this.getTheme().SIDEBAR_BACKGROUND());
        this.summaryPanel.setBounds(0,0,0,0);
        this.summaryPanel.setBorder(BorderFactory.createEmptyBorder());
        this.summaryPanel.setLayout(new BoxLayout(this.summaryPanel, BoxLayout.Y_AXIS));

        this.summaryScrollPanel = SideBarFactory.createSimpleScrollPanel(this.summaryPanel,
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());

        // create a panel to list the courses
        this.coursePanel = new JPanel();
        this.coursePanel.setBackground(this.getTheme().SIDEBAR_BACKGROUND());
        this.coursePanel.setBounds(0,0,0,0);
        this.coursePanel.setBorder(BorderFactory.createEmptyBorder());
        this.coursePanel.setLayout(new BoxLayout(this.coursePanel, BoxLayout.Y_AXIS));
        this.coursePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, SIDEBAR_WIDTH-5));

        this.courseScrollPanel = SideBarFactory.createSimpleScrollPanel(this.coursePanel,
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        this.courseScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.courseScrollPanel.getHorizontalScrollBar().setEnabled(false);
        this.courseScrollPanel.getHorizontalScrollBar().setVisible(false);

        // show past deadlines checkbox
        this.pastDeadlinesBox = SideBarFactory.createCheckBox(parent.getFrame().getText("show_past_deadlines"),
                parent.getFrame().getTheme().SIDEBAR_TEXT());

        // create a panel that can let user manually create a deadline
        this.addNewDeadlinePanel = new addNewDeadlinePanel(this, true);
        this.editDeadlinePanel = new addNewDeadlinePanel(this, false);

        // create upper part
        this.addUpperPartComponents();

        // create the lower part of the side bar
        this.cardLayout = new CardLayout();
        this.lowerPart = new JPanel(this.cardLayout);
        this.lowerPart.add(summaryScrollPanel, PANEL.DEADLINE_PANEL);
        this.lowerPart.add(addNewDeadlinePanel, PANEL.ADD_NEW_DEADLINE_PANEL);
        this.lowerPart.add(editDeadlinePanel, PANEL.EDIT_DEADLINE_PANEL);
        this.lowerPart.add(courseScrollPanel, PANEL.COURSE_PANEL);
        // the lower part: including the summary panel and the addNewDeadline panel
        this.add(this.lowerPart);

        // automatically update the summary panel every minute
        new Timer(60000, e -> refresh()).start();
    }

    /**
     * This function would return a copy of the allCourseNames set
     * @requires None
     * @modifies None
     * @effects None
     * @return allCourseName
     */
    TreeSet<String> getAllCourseNames() {
        return new TreeSet<>(allCourseNames);
    }

    /**
     * This function will add all components of the upper part of the sidebar
     * @requires None
     * @modifies this
     * @effects add components
     */
    private void addUpperPartComponents() {
        // Top blank area
        this.add(mainFactory.createEmptyArea(10, true));

        // main Icon
        JPanel northWest = mainFactory.createPanel(SIDEBAR_WIDTH, 55,true);
        northWest.setLayout(new BorderLayout());
        northWest.add(SideBarFactory.createIcon(parent.getFrame().getTheme().SIDEBAR_TEXT()), BorderLayout.CENTER);
        this.add(northWest);

        // Separator
        this.add(SideBarFactory.createLineSeparator(parent.getFrame().getTheme().SIDEBAR_TEXT()));
        this.add(mainFactory.createEmptyArea(3, true));

        // Create new line button
        this.welcomeTextArea = SideBarFactory.createSimpleTextArea(parent.getFrame().getText("welcome") +
                (!username.equals("") ? (", " + username) : ""), 30, parent.getFrame().getTheme().SIDEBAR_TEXT());
        this.add(this.welcomeTextArea);
        JComponent newDeadlineButton = SideBarFactory.createSimpleTextArea("⊕  "+parent.getFrame().getText("add_a_new_deadline"),
                33, parent.getFrame().getTheme().SIDEBAR_TEXT());
        newDeadlineButton.addMouseListener(mainFactory.getHoverEffect(newDeadlineButton, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        newDeadlineButton.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                showAddNewDeadline();
            }
        });
        this.add(newDeadlineButton);
        this.add(mainFactory.createEmptyArea(2, true));

        // a panel to configure the sorting order
        this.add(SideBarFactory.createSortPanel(this.sortOrderBox, parent.getFrame().getText("sort_by"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(),
                parent.getFrame().getTheme().SIDEBAR_TEXT()));
        this.add(mainFactory.createEmptyArea(10, true));

        // separator line
        this.add(SideBarFactory.createLineSeparator(parent.getFrame().getTheme().SIDEBAR_TEXT()));
        this.add(mainFactory.createEmptyArea(10, true));
        this.add(Box.createVerticalGlue());
    }

    /**
     * This function would add a new deadline to the summary panel and the set containing
     * all course names
     * @param deadline the deadline object
     * @requires deadline != null
     * @modifies allDeadlines, allCourseNames, addNewDeadlinePanel, summaryPanel
     * @effects add a new deadline to the summary panel
     */
    public void addDeadline(Deadline deadline) {
        if (deadline == null) return;
        this.allDeadlines.put(deadline.getCourse().replace("Spring 2019     ", "") + ": " + deadline.getName(),
                deadline);
        this.allCourseNames.add(deadline.getCourse());
        this.addNewDeadlinePanel.addCourse(deadline.getCourse());
        System.out.println("DEBUG: [SideBarPanel_] {" + deadline.toString() + "} will be added to sidebar.");
        this.updateSummaryPanel();   // update the summary after adding a deadline
        this.updateCoursePanel();
    }

    /**
     * This function would add an empty course to the course name list
     * @param courseName the course name
     * @requires courseName != null
     * @modifies allCourseNames, addNewDeadlinePanel
     * @effects add an empty course to the course name list
     */
    public void addCourse(String courseName) {
        this.allCourseNames.add(courseName);
        this.addNewDeadlinePanel.addCourse(courseName);
        System.out.println("DEBUG: [SideBarPanel_] adding an empty course {"
                + courseName + "} to list.");
    }

    /**
     * This method would remove a deadline from an existing course.
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day the day number; starts from 1 to 31
     * @requires None
     * @modifies parent
     * @effects remove a deadline
     */
    public void removeDeadline(String course, String deadlineName, int year, int month, int day) {
        this.allDeadlines.remove(course.replace("Spring 2019     ", "")
                + ": " + deadlineName);
        this.allCourseNames.remove(course);
        this.updateSummaryPanel();   // update the summary panel after removing a course
    }

    /**
     * This method would edit a deadline
     * @param d the deadline object that is about to be edited
     * @requires None
     * @modifies editDeadlinePanel, lowerPart
     * @effects None
     */
    public void editDeadline(Deadline d) {
        this.editDeadlinePanel.handleEditSignal(d);
        this.showPanel(PANEL.EDIT_DEADLINE_PANEL);
    }

    /**
     * This function would add a new deadline to the whole program
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day          the day number; starts from 1 to 31
     * @param hour         hour number; starts from 0 to 23
     * @param minute       minute number; starts from 0 to 59
     * @requires None
     * @modifies parent
     * @effects add a deadline
     */
    void handleAddDeadlineSignal(String course, String deadlineName, int year,
                                 int month, int day, int hour, int minute) {
        this.parent.addDeadline(course, deadlineName, year, month,day, hour, minute);
    }

    /**
     * This method will call the removeDeadline method of the parent component
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
     * @param d the deadline object that is about to be edited
     * @requires None
     * @modifies parent
     * @effects remove a deadline
     */
    void handleEditSignal(Deadline d) {
        parent.editDeadline(d);
    }

    /**
     * This function will update the username
     * @param username the new username
     * @requires None
     * @modifies username
     * @effects update the username
     */
    public void updateUsername(String username) {
        this.username = username;
        this.welcomeTextArea.setText(parent.getFrame().getText("welcome") +
                (!username.equals("") ? (", " + username) : ""));
        this.welcomeTextArea.repaint();
    }

    /**
     * This function update the state of pastDeadlinesBox
     * @param showPastDeadlines the correct setting
     * @requires None
     * @modifies pastDeadlinesBox
     * @effects set selected/unselected based on the value of showPastDeadlines
     */
    public void updatePastDeadlineBox(boolean showPastDeadlines) {
        this.pastDeadlinesBox.setSelected(showPastDeadlines);
    }

    /**
     * This function would update the remaining time information of all DeadlineInfoBlocks
     * @requires None
     * @modifies this.summaryPanel
     * @effects update the remaining time
     */
    private void refresh() {
        int pos = this.summaryScrollPanel.getVerticalScrollBar().getValue();
        for (Component c: this.summaryPanel.getComponents()) {
            if (c instanceof DeadlineInfoBlock) {
                DeadlineInfoBlock block = (DeadlineInfoBlock) c;
                block.updateRemainingText(
                        parent.getFrame().getTextFormat().getRemainingText(
                                this.allDeadlines.get(block.getBlockName()), null, true));
            }
        }
        // move the scrollbar back to the original position
        SwingUtilities.invokeLater(() -> summaryScrollPanel.getVerticalScrollBar().setValue(pos));
    }

    /**
     * This function would remove all components from the course panel and re-add
     * all deadline blocks
     * @requires None
     * @modifies summaryPanel
     * @effects re-add all deadline information to the summary panel
     */
    private void updateCoursePanel() {
        // delete old components
        this.coursePanel.removeAll();
        this.coursePanel.revalidate();
        this.coursePanel.repaint();

        // show past deadline box
        this.coursePanel.add(this.pastDeadlinesBox);
        this.coursePanel.add(Box.createVerticalStrut(2));
        this.coursePanel.add(SideBarFactory.createLineSeparator(parent.getFrame().getTheme().SIDEBAR_TEXT()));
        this.coursePanel.add(Box.createVerticalStrut(3));
        // save scroll bar position
        int pos = this.courseScrollPanel.getVerticalScrollBar().getValue();

        // add deadlines one by one
        for (String courseName: this.allCourseNames) {
            JCheckBox checkBox = SideBarFactory.createCheckBox(courseName,
                    parent.getFrame().getTheme().SIDEBAR_TEXT());
            checkBox.setMaximumSize(new Dimension(SIDEBAR_WIDTH - 5, 20));
            if (!parent.isIgnoring(courseName)) {
                checkBox.setSelected(true);
            } else {
                checkBox.setSelected(false);
            }
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    parent.removeIgnoredCourse(courseName);
                } else {
                    parent.addIgnoredCourse(courseName);
                }
            });
            this.coursePanel.add(checkBox);
        }
        this.coursePanel.add(Box.createVerticalGlue());
        // restore scroll bar's position
        SwingUtilities.invokeLater(() -> courseScrollPanel.getVerticalScrollBar().setValue(pos));
    }

    /**
     * This function would remove all components from the summary panel and re-add
     * all deadline blocks
     * @requires None
     * @modifies summaryPanel
     * @effects re-add all deadline information to the summary panel
     */
    public void updateSummaryPanel() {
        // delete old components
        this.summaryPanel.removeAll();
        this.summaryPanel.revalidate();
        this.summaryPanel.repaint();

        // save scroll bar position
        int pos = this.summaryScrollPanel.getVerticalScrollBar().getValue();

        // add deadlines one by one
        int i = 0;
        ArrayList<Deadline> list = new ArrayList<>(this.allDeadlines.values());
        list.sort(sortOrder);
        for (Deadline d: list) {
            if (d.isBefore(CalendarWrapper.now())) {
                continue;
            }
            if (parent.isIgnoring(d.getCourse())) {
                continue;
            }
            this.summaryPanel.add(new DeadlineInfoBlock(this,
                    d.getCourse().replace("Spring 2019     ", "") + ": " + d.getName(), d));
            if (i != list.size() - 1) {
                this.summaryPanel.add(mainFactory.createEmptyArea(13, true));
            }
            i++;
        }

        // restore scroll bar's position
        SwingUtilities.invokeLater(() -> summaryScrollPanel.getVerticalScrollBar().setValue(pos));
    }

    /**
     * This function would let the lowerPart panel shows the deadline summary list
     * @requires None
     * @modifies this.cardLayout
     * @effects show the deadline summary list
     */
    void showSummary() {
        this.showPanel(PANEL.DEADLINE_PANEL);
    }

    /**
     * This function would let the lowerPart panel shows the "add new deadline" panel
     * @requires None
     * @modifies this.cardLayout
     * @effects show the "add new deadline" panel
     */
    private void showAddNewDeadline() {
        this.addNewDeadlinePanel.refreshCourseNameBox();
        this.showPanel(PANEL.ADD_NEW_DEADLINE_PANEL);
    }

    /**
     * This function controls which panel should be displayed
     * @param panelName the name of the panel
     * @requires None
     * @modifies this.cardLayout
     * @effects show the specified panel
     */
    public void showPanel(String panelName) {
        this.cardLayout.show(this.lowerPart, panelName);
    }

    /**
     * This function returns the theme of main
     * @requires None
     * @modifies None
     * @effects None
     * @return the current theme
     */
    Theme getTheme() {
        return parent.getFrame().getTheme();
    }

    /**
     * This function returns the main component of main
     * @requires None
     * @modifies None
     * @effects None
     * @return the current frame
     */
    GUIController getMainmainGUI() { return parent; }
}
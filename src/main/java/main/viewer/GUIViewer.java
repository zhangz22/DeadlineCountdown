package main.viewer;

import model.CalendarWrapper;
import model.Deadline;
import main.controller.GUIController;
import main.controller.Settings;
import main.viewer.calendarPanel.CalendarPanel;
import main.viewer.calendarPanel.TitlePanel;
import main.viewer.sideBarPanel.SideBarPanel;
import main.viewer.textFormat.BaseText;
import main.viewer.textFormat.ViewerFont;
import main.viewer.textFormat.TextFactory;
import main.viewer.theme.Theme;
import main.viewer.theme.ThemeFactory;
import main.viewer.util.DeadlineTimer;
import main.viewer.util.LoginDialog;
import webService.SubmittyAccess;
import javafx.util.Pair;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is the View part of the GUI Version of main
 */
public class GUIViewer extends JFrame {
    private final GUIController controller;

    // GUI components
    private Theme theme;
    private CalendarPanel calendarPanel;
    private TitlePanel calendarTitle;
    private SideBarPanel sideBar;
    private BaseText textFormat;
    private ResourceBundle textResource;
    private Notification notification;
    private ConcurrentHashMap<String, DeadlineTimer> allTimersMap;

    // Display information
    private String username;
    private int month;
    private int year;

    /**
     * Constructor
     *
     * @param parent the controller for main
     * @requires parent != null
     * @modifies controller, month, year, username, allTimersMap
     */
    public GUIViewer(GUIController parent) {
        this.controller = parent;

        // set up basic window close action
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                shutdown();
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // load the font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            URL is = getClass().getResource("/fonts/XHei.ttf");
            URL is2 = getClass().getResource("/fonts/BRLNSDB.TTF");
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is.openStream()));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is2.openStream()));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // set icon
        URL iconPath = getClass().getResource("/images/icon.png");
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            this.setIconImage(icon.getImage());
        }

        // get current year and month
        this.month = CalendarWrapper.now().getMonth();
        this.year = CalendarWrapper.now().getYear();
        this.username = "";

        // create timer
        this.allTimersMap = new ConcurrentHashMap<>();
    }

    /**
     * Setup components
     *
     * @param restart a boolean to indicate if the program is restarting
     * @requires None
     * @modifies theme, textFormat, notification, calendarPanel, calendarTitle, sideBar
     * @effects setup components
     */
    public void setUp(boolean restart) {
        // theme
        this.setUpTheme();

        // get language information
        this.setUpLocale(restart);

        // notification
        if (!restart) {
            this.notification = new Notification(this.controller);
        }

        // add components
        this.getContentPane().setLayout(new BorderLayout());
        this.addCalendar();
        this.addSideBar();

        // set theme
        this.setSwingLookAndFeel("");
    }

    /**
     * This function generates and sets the theme based on the settings
     *
     * @requires None
     * @modifies theme
     * @effects generate the theme based on the setting
     */
    private void setUpTheme() {
        this.theme = ThemeFactory.createTheme(this.controller.getSettings().getTheme());
        Log.debug("DEBUG: [UIViewerSetup] Using " +
                this.controller.getSettings().getTheme() + " as current theme");
        if (this.controller.getSettings().getThemeColor() != null ) {
            Color newThemeColor = this.controller.getSettings().getThemeColor();
            if (!newThemeColor.equals(this.theme.SIDEBAR_BACKGROUND())) {
                this.theme.set(Theme.SIDE_BAR_THEME, Theme.SIDEBAR_BACKGROUND, newThemeColor);
                this.theme.set(Theme.SIDE_BAR_THEME, Theme.HOVER_OVER_SIDEBAR, new Color(
                        (int) (newThemeColor.getRed() * 0.75),
                        (int) (newThemeColor.getGreen() * 0.75),
                        (int) (newThemeColor.getBlue() * 0.75)));
            }
            Log.debug("DEBUG: [UIViewerSetup] Changing main "
                    + "theme color to " + newThemeColor.toString() + "(" + "#"
                    + Integer.toHexString(newThemeColor.getRGB()).substring(2) +")");
        }
        UIManager.put("RadioButton.highlight", this.theme.SIDEBAR_TEXT());
        UIManager.put("RadioButton.darkShadow", this.theme.SIDEBAR_TEXT());
        UIManager.put("RadioButton.disabledText", this.theme.SIDEBAR_TEXT());
        UIManager.put("RadioButton.foreground", this.theme.SIDEBAR_TEXT());
        UIManager.put("RadioButton.highlight", this.theme.SIDEBAR_TEXT());
        UIManager.put("RadioButton.light", this.theme.SIDEBAR_TEXT());
        UIManager.put("RadioButton.select", this.theme.SIDEBAR_TEXT());
        UIManager.put("RadioButton.shadow", this.theme.SIDEBAR_TEXT());
    }

    /**
     * This function changes the language of the application
     *
     * @param restart a boolean to indicate if the program is restarting
     * @modifies textResource, controller.settings[Settings.SUPPORTED.START_FROM_SUNDAY]
     * @effects changes the lanugage based on setting
     */
    private void setUpLocale(boolean restart) {
        Locale locale;
        switch (this.controller.getSettings().getLanguage()) {
            case "en_US":
                locale = Locale.US;
                break;
            case "zh_CN":
                locale = Locale.CHINA;
                break;
            case "zh_Hant":
                locale = new Locale("zh", "Hant");
                break;
            default:
                locale = Locale.US;
                break;
        }
        try {
            this.textResource = ResourceBundle.getBundle("text", locale);
        } catch (MissingResourceException e) {
            this.textResource = ResourceBundle.getBundle("text", Locale.US);
        }
        this.textFormat = TextFactory.createTextFormat(this.controller.getSettings().getLanguage(), this.textResource);
        if (!restart) {
            this.controller.setSettings(Settings.SUPPORTED.START_FROM_SUNDAY, textFormat.startsFromSunday() == 1);
        }
    }

    /**
     * This function configures the main calendar panel
     *
     * @requires None
     * @modifies calendarPanel, calendarTitle
     * @effects configures the main calendar panel
     */
    private void addCalendar() {
        // add components
        this.calendarPanel = new CalendarPanel(this.controller, this.textFormat);
        this.calendarTitle = new TitlePanel(this.textFormat, this);

        // Add prev-month, next-month and setUpForAccess button to the calendarTitle
        JButton prevMonthButton = DeadlineCountdownFactory.createTitleButton("↑",
                this.getTheme().CAL_BACKGROUND(), this.getTheme().CAL_DATE_TEXT(), this.getTheme().HOVER_OVER_CAL());
        prevMonthButton.addActionListener(e -> this.increaseMonth(-1));

        JButton nextMonthButton = DeadlineCountdownFactory.createTitleButton("↓",
                this.getTheme().CAL_BACKGROUND(), this.getTheme().CAL_DATE_TEXT(), this.getTheme().HOVER_OVER_CAL());
        nextMonthButton.addActionListener(e -> this.increaseMonth(1));

        JButton refreshButton = DeadlineCountdownFactory.createTitleButton("↻ " + this.getText("submitty_login"),
                this.getTheme().CAL_BACKGROUND(), this.getTheme().REFRESH_BUTTON(), this.getTheme().HOVER_OVER_REFRESH());
        refreshButton.addActionListener(e -> this.setUpForAccess());

        // Implement the title bar
        JToolBar titleBar = DeadlineCountdownFactory.createToolbar();
        titleBar.setBackground(this.getTheme().CAL_BACKGROUND());
        titleBar.setPreferredSize(new Dimension(0, (int) prevMonthButton.getPreferredSize().getHeight()));
        titleBar.add(prevMonthButton);
        titleBar.add(nextMonthButton);
        titleBar.add(calendarTitle);
        titleBar.add(refreshButton);

        // add the calendar panel to the center
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setMinimumSize(new Dimension(600, 650));
        centerPanel.setPreferredSize(new Dimension(600, 650));
        centerPanel.add(titleBar, BorderLayout.NORTH);
        centerPanel.add(this.calendarPanel, BorderLayout.CENTER);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * This function configures the side bar and the setting bar at the bottom of the
     * side bar
     *
     * @requires None
     * @modifies sideBar
     * @effects configures the sideBar
     */
    private void addSideBar() {
        // add the side bar
        this.sideBar = new SideBarPanel(new HashMap<>(), this.username, this.controller);
        this.sideBar.setMinimumSize(new Dimension(400, 0));

        // settings toolbar
        JToolBar settingsToolbar = DeadlineCountdownFactory.createToolbar();
        settingsToolbar.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH, 55));
        settingsToolbar.setBackground(this.getTheme().SIDEBAR_BACKGROUND());

        JButton saveButton = DeadlineCountdownFactory.createSettingsToolbarButton("/images/icon_save.png",
                this.getTheme().SIDEBAR_BACKGROUND(), this.getTheme().SIDEBAR_TEXT(), this.getTheme().SIDEBAR_HOVER());

        UIManager.put("OptionPane.buttonFont", new Font(ViewerFont.XHEI, Font.PLAIN, 14));
        saveButton.addMouseListener(DeadlineCountdownFactory.createButtonActionLoadSave("SAVE", this, this.controller));
        saveButton.setToolTipText(this.getText("save_tooltip"));

        JButton loadButton = DeadlineCountdownFactory.createSettingsToolbarButton("/images/icon_load.png",
                this.getTheme().SIDEBAR_BACKGROUND(), this.getTheme().SIDEBAR_TEXT(), this.getTheme().SIDEBAR_HOVER());
        loadButton.addMouseListener(DeadlineCountdownFactory.createButtonActionLoadSave("LOAD", this, this.controller));
        loadButton.setToolTipText(this.getText("load_tooltip"));

        JButton filterButton = DeadlineCountdownFactory.createSettingsToolbarButton("/images/icon_filter.png",
                this.getTheme().SIDEBAR_BACKGROUND(), this.getTheme().SIDEBAR_TEXT(), this.getTheme().SIDEBAR_HOVER());
        filterButton.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let the side bar show the course fitler panel
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                sideBar.updatePastDeadlineBox(controller.getSettings().isShowPastDeadlines());
                sideBar.showPanel(SideBarPanel.PANEL.COURSE_PANEL);
            }
        });
        filterButton.setToolTipText(this.getText("course_tooltip"));

        JButton listButton = DeadlineCountdownFactory.createSettingsToolbarButton("/images/icon_list.png",
                this.getTheme().SIDEBAR_BACKGROUND(), this.getTheme().SIDEBAR_TEXT(), this.getTheme().SIDEBAR_HOVER());
        listButton.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let the side bar show the deadline summary panel
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                sideBar.showPanel(SideBarPanel.PANEL.DEADLINE_PANEL);
            }
        });
        listButton.setToolTipText(this.getText("deadline_tooltip"));

        JButton settingsButton = DeadlineCountdownFactory.createSettingsToolbarButton("/images/icon_settings.png",
                this.getTheme().SIDEBAR_BACKGROUND(), this.getTheme().SIDEBAR_TEXT(), this.getTheme().SIDEBAR_HOVER());
        settingsButton.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let the side bar show the settings panel
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                sideBar.showPanel(SideBarPanel.PANEL.SETTINGS_PANEL);
            }
        });
        settingsButton.setToolTipText(this.getText("settings_tooltip"));

        settingsToolbar.add(listButton);
        settingsToolbar.add(filterButton);
        settingsToolbar.add(saveButton);
        settingsToolbar.add(loadButton);
        settingsToolbar.add(Box.createHorizontalGlue());
        settingsToolbar.add(settingsButton);

        // configure the sidebar border
        JPanel sideBarWIthInsets = new JPanel(new BorderLayout());
        sideBarWIthInsets.add(this.sideBar, BorderLayout.CENTER);
        sideBarWIthInsets.add(DeadlineCountdownFactory.createPanel(10, 10, true), BorderLayout.NORTH);
        sideBarWIthInsets.add(DeadlineCountdownFactory.createPanel(10, 10, true), BorderLayout.EAST);
        sideBarWIthInsets.add(DeadlineCountdownFactory.createPanel(10, 10, true), BorderLayout.WEST);
        sideBarWIthInsets.add(settingsToolbar, BorderLayout.SOUTH);
        sideBarWIthInsets.setBackground(this.getTheme().SIDEBAR_BACKGROUND());
        this.getContentPane().add(sideBarWIthInsets, BorderLayout.WEST);
    }

    /**
     * This function would ask the user for the username and password, then it will
     * setUpForAccess the deadline list after connecting to submitty.
     *
     * @requires None
     * @modifies username, allCourses
     * @effects setUpForAccess the deadline list
     */
    private void setUpForAccess() {
        // agreement
        if (!this.controller.agreement())
            return;
        boolean passwordNeeded = true;
        String message = "";
        // ask for username and password
        String password = null;
        while (this.username == null || password == null || passwordNeeded) {
            Pair<String, String> IDandPassword = this.askIDandPassword(message, this.username);
            this.username = IDandPassword.getKey();
            password = IDandPassword.getValue();
            if (username == null || password == null) {
                return;
            }
            if (username.equals("")) {
                message = this.getText("username_empty");
                passwordNeeded = true;
                continue;
            }
            if (password.equals("")) {
                message = this.getText("password_empty");
                passwordNeeded = true;
                continue;
            }
            passwordNeeded = false;
            try {
                this.controller.access(this.username, password);
            } catch (SubmittyAccess.LoginFailException e) {
                Log.debug("DEBUG: [SetUpAccess] " + e.getMessage());
                message = this.getText("password_incorrect");
                passwordNeeded = true;
            } catch (RuntimeException e) {
                Log.debug("DEBUG: [SetUpAccess] " + e.getMessage());
                message = this.getText("login_failed");
                passwordNeeded = true;
            }
        }
        // update sideBar and calendar to display all deadlines
        this.refreshDeadlines();

        if (this.controller.getSettings().isAutoSaveAfterRefresh())
            this.controller.saveToLocal(null, "JSON", false);
    }

    /**
     * This method should ask the user for his/her username and his/her password
     *
     * @param message the additional message
     * @param lastID  the last user_id from the user's input
     * @return user's username and password
     * @requires None
     * @modifies None
     * @effects None
     */
    private Pair<String, String> askIDandPassword(String message, String lastID) {
        LoginDialog login = new LoginDialog(this.controller, message, lastID);
        login.setVisible(true);
        login.dispose();
        return new Pair<>(login.getUsername(), login.getPassword());
    }

    /**
     * This function set the LookAndFeel of main
     *
     * @param themeName the name of the theme
     * @requires None
     * @modifies UIManager
     * @effects None
     */
    private void setSwingLookAndFeel(String themeName) {
        switch (themeName) {
            case "JAVA":
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException |
                        IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            default:
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
        }
        UIManager.put("Menu.selectionBackground", getTheme().CAL_DATE_TEXT());
        UIManager.put("Menu.selectionForeground", Color.BLACK);
        UIManager.put("Menu.background", getTheme().HOVER_OVER_CAL());
        UIManager.put("Menu.foreground", Color.BLACK);
        UIManager.put("Menu.opaque", false);
    }

    /**
     * This method will change the current month being displayed on the Calendar
     *
     * @param month the new displayed month
     * @param year the new displayed year
     * @requires None
     * @modifies month
     * @effects change the date being displayed
     */
    public void setMonthYear(int month, int year) {
        this.month = month;
        this.year = year;
        this.calendarPanel.setMonthYear(month, year);
        this.calendarTitle.setMonthYear(month, year);
    }

    /**
     * This method will increase (or decrease based on the value of i) the current
     * month being displayed
     *
     * @param i the number of increment/decrement
     * @requires None
     * @modifies month
     * @effects increase/decrease month
     */
    private void increaseMonth(int i) {
        this.month += i;
        while (this.month >= 13) {
            this.month -= 12;
            this.year++;
        }
        while (this.month <= 0) {
            this.month += 12;
            this.year--;
        }
        this.setMonthYear(month, year);
    }

    /**
     * This function updates the month and year which the panel is displaying,
     * and highlight a specified date
     *
     * @param month the month number of a year, starts from 1 (Jan)
     * @param year  the year number
     * @param date  the date number
     * @requires None
     * @modifies calendarPanel
     * @effects update the month and year
     */
    public void highlightDate(int month, int year, int date) {
        this.setMonthYear(month, year);
        this.calendarPanel.highlightDateBlock(date);
    }

    /**
     * This function updates sideBar and calendar to display all deadlines
     *
     * @requires None
     * @modifies sideBar.allCourseNames, sideBar.addNewDeadlinePanel, this.sideBar, this.calendarPanel
     * @effects display all existing deadlines
     */
    private void refreshDeadlines() {
        // update the username
        this.sideBar.updateUsername(this.username);

        // update the allCourse map
        for (String courseName : this.controller.getAllCourses().keySet()) {
            TreeMap<String, Deadline> dueMap = this.controller.getAllCourses().get(courseName).getDeadlines();
            if (dueMap == null || dueMap.isEmpty() || this.controller.getAllCourses().get(courseName).size() == 0) {
                this.sideBar.addCourse(courseName);
                continue;
            }
            List<String> dueList = this.controller.getAllCourses().get(courseName).getReversedSortedDeadlines();
            for (String deadlineName : dueList) {
                this.controller.addDeadline(dueMap.get(deadlineName));
            }
        }
    }

    /**
     * This function returns the theme of main
     *
     * @return the current theme
     * @requires None
     * @modifies None
     * @effects None
     */
    public Theme getTheme() {
        return this.theme;
    }

    /**
     * This function returns the ResourceBundle that contains the text strings
     *
     * @param str the string need to be found
     * @return a string in required locale
     * @requires None
     * @modifies None
     * @effects None
     */
    public String getText(String str) {
        try {
            return textResource.getString(str);
        } catch (MissingResourceException | ClassCastException | NullPointerException e) {
            return str;
        }
    }

    /**
     * This function returns the text format
     *
     * @return textFormat
     * @requires None
     * @modifies None
     * @effects None
     */
    public BaseText getTextFormat() {
        return this.textFormat;
    }

    /**
     * This function returns the ResourceBundle containing all text strings
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return textFormat
     */
    public final ResourceBundle getTextResource() {
        return this.textResource;
    }

    /**
     * This method updates the tooltip text
     *
     * @requires None
     * @modifies trayIcon
     * @effects set new tool tip text
     */
    public void updateTrayIcon() {
        this.notification.updateToolTip();
    }

    /**
     * This function returns the calendarPanel
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return calendarPanel
     */
    public final CalendarPanel getCalendarPanel() {
        return this.calendarPanel;
    }

    /**
     * This function returns the side bar panel
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return sideBar
     */
    public final SideBarPanel getSideBar() {
        return this.sideBar;
    }

    /**
     * This function returns the calendarPanel
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return allTimersMap
     */
    public ConcurrentHashMap<String, DeadlineTimer> getAllTimersMap() {
        return this.allTimersMap;
    }

    /**
     * This method would add a new deadline to the calendar panel and the side bar panel,
     * creating a timer for notification
     *
     * @param deadline the deadline that will be added
     * @requires None
     * @modifies calendarPanel, sideBar, allTimersMap
     * @effects None
     */
    synchronized public void addDeadlineBlock(Deadline deadline) {
        this.calendarPanel.addDeadline(deadline);
        this.sideBar.addDeadline(deadline);
        DeadlineTimer timer = new DeadlineTimer(deadline, this.controller);
        if (!this.controller.isIgnoring(deadline.getCourseName())) {
            timer.start();
        }
        this.allTimersMap.put(deadline.getCourseName() + deadline.getName(), timer);
    }

    /**
     * This method would remove a deadline from the calendar panel and the side bar panel,
     * disable all timers for this deadline
     *
     * @param course       the course name
     * @param deadlineName the deadline name
     * @param year         year number
     * @param month        month number; starts from 1 to 12
     * @param day the day number; starts from 1 to 31
     * @requires None
     * @modifies allTimersMap, calendarPanel, sideBar
     * @effects None
     */
    synchronized public void removeDeadlineBlock(String course, String deadlineName, int year, int month, int day) {
        DeadlineTimer timer = this.allTimersMap.get(course + deadlineName);
        if (timer != null) {
            timer.stop();
            this.allTimersMap.remove(course + deadlineName);
        }
        this.calendarPanel.removeDeadline(course, deadlineName, year, month, day);
        this.sideBar.removeDeadline(course, deadlineName, year, month, day);
    }


    /**
     * This method would let the side bar show edit deadline panel
     *
     * @param deadline the deadline object that is about to be edited
     * @requires None
     * @modifies sideBar
     * @effects None
     */
    public void showEditDeadlinePanel(Deadline deadline) {
        this.sideBar.editDeadline(deadline);
    }

    /**
     * This function will send a notification
     *
     * @param title    the title of the notification
     * @param message  the message
     * @param subtitle the subtitle of the notification
     * @requires None
     * @modifies None
     * @effects send a message
     */
    public void notification(String title, String message, String subtitle) {
        if (this.controller.getSettings().isNotificationEnabled()) {
            this.notification.send(title, message, subtitle);
        } else {
            Log.debug("DEBUG: [GUIController] Notification disabled: " + message);
        }
    }

    /**
     * This method would start the frame and make it visible
     *
     * @param args arguments when running the program
     * @requires None
     * @modifies this
     * @effects make the frame visible
     */
    public void run(String[] args) {
        // set the frame
        this.setTitle("DeadlineCountdown " + GUIController.VERSION);
        this.setMinimumSize(new Dimension(800, 738));
        this.setPreferredSize(new Dimension(1062, 738));

        // make the frame visible
        this.setLocationRelativeTo(null);

        this.setResizable(true);
        this.pack();

        // arguments
        if (Arrays.asList(args).contains("-s")) {
            // run in backgrounds
            this.setExtendedState(JFrame.ICONIFIED);
            String message;
            Deadline closest = this.controller.getClosestDeadline();
            if (closest == null) {
                message = "No incoming due dates";
            } else {
                message = closest.getName() + " (" + closest.getCourseName() + ") " + this.getText("due_in")
                        + this.getTextFormat().getRemainingText(closest, CalendarWrapper.now(), false);
            }
            this.notification("Deadline countdown is running in background.", message, "");
        } else {
            this.setVisible(true);
        }
    }

    /**
     * This function refreshes the calendar panel and the summary panel
     *
     * @requires None
     * @modifies sideBar, calendarPanel
     * @effects refresh
     */
    public void refresh() {
        if (this.sideBar != null) {
            this.sideBar.updateSummaryPanel();
        }
        if (this.calendarPanel != null) {
            this.calendarPanel.displayAllDeadlines();
        }
    }

    /**
     * This function refreshes the calendar panel
     *
     * @requires None
     * @modifies calendarPanel
     * @effects refresh
     */
    public void refreshCalendar() {
        this.calendarPanel.refresh();
    }

    /**
     * This function would run the program and display the result to the user
     *
     * @requires None
     * @modifies None
     * @effects display the result
     */
    public void restart() {
        this.getContentPane().removeAll();
        this.setSwingLookAndFeel("JAVA");
        this.setUp(true);
        this.refreshDeadlines();
    }

    /**
     * This method brings the window to the front
     *
     * @requires None
     * @modifies None
     * @effects bring the window to the front
     */
    void showFront() {
        SwingUtilities.invokeLater(() -> {
            Log.debug("DEBUG: [toFront]");
            setVisible(true);
            setExtendedState(JFrame.NORMAL);
            toFront();
            repaint();
        });
    }

    /**
     * This function would shutdown the program
     *
     * @requires None
     * @modifies None
     * @effects shutdown the program
     */
    public void shutdown() {
        this.controller.saveSettings(false);
        this.notification.close();
    }
}

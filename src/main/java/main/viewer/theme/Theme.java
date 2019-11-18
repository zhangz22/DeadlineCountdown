package main.viewer.theme;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a theme that will be used in main
 */
public abstract class Theme implements Cloneable, ThemeInterface {
    // Color names
    public static final String CAL_BACKGROUND = "CAL_BACKGROUND";
    public static final String CAL_DATE_TEXT = "CAL_DATE_TEXT";
    public static final String CAL_TITLE_TEXT = "CAL_TITLE_TEXT";
    public static final String HIGHLIGHT = "HIGHLIGHT";
    public static final String REFRESH_BUTTON = "REFRESH_BUTTON";
    public static final String CAL_TITLE_SEP = "CAL_TITLE_SEP";
    public static final String HOVER_OVER_CAL = "HOVER_OVER_CAL";
    public static final String HOVER_OVER_REFRESH = "HOVER_OVER_REFRESH";
    public static final String TODAY = "TODAY";
    public static final String TODAY_TEXT = "TODAY_TEXT";
    public static final String CAL_BACKGROUND_INVALID = "CAL_BACKGROUND_INVALID";
    public static final String SIDEBAR_BACKGROUND = "SIDEBAR_BACKGROUND";
    public static final String SIDEBAR_TEXT = "SIDEBAR_TEXT";
    public static final String HOVER_OVER_SIDEBAR = "SIDEBAR_HOVER";
    public static final String SUBMIT_COLOR = "SUBMIT_COLOR";
    public static final String FINISH_COLOR = "FINISH_COLOR";
    public static final String LATE_SUBMIT_COLOR = "LATE_SUBMIT_COLOR";
    public static final String RESUBMIT_COLOR = "RESUBMIT_COLOR";
    public static final String RESUBMIT_COLOR_TODAY = "RESUBMIT_COLOR_TODAY";
    public static final String NO_SUBMISSION_COLOR = "NO_SUBMISSION_COLOR";
    public static final String RIGHT_MENU_TEXT = "RIGHT_MENU_TEXT";
    public static final String RIGHT_MENU_BACKGROUND = "RIGHT_MENU_BACKGROUND";

    // different component sub-themes
    public static final int SIDE_BAR_THEME = 0;
    public static final int CALENDAR_THEME = 1;
    public static final int DEADLINE_THEME = 2;

    private static abstract class ColorMap {
        // A map storing all theme colors
        ConcurrentHashMap<String, Color> theme;

        /**
         * Constructor
         * @requires None
         * @modifies None
         * @effects initialize theme map
         */
        ColorMap() {
            this.theme = new ConcurrentHashMap<>();
        }

        // Setters

        /**
         * This method changes a specified color
         * @param colorKey the key of the item being changed
         * @param color the expected color
         * @requires colorKey != null, color != null
         * @modifies theme
         * @effects change a key
         */
        public void set(String colorKey, Color color) {
            this.theme.put(colorKey, color);
        }

        /**
         * This method returns a specified color
         * @param colorKey the key of the color
         * @requires colorKey != null
         * @modifies theme
         * @effects return a key
         * @return a color with the specified key
         */
        public Color get(String colorKey) {
            return this.theme.get(colorKey);
        }

        /**
         * This function changes the main color of the current component
         * @param color the expected color
         * @requires color != null
         * @modifies theme
         * @effects change a key
         */
        public abstract void setMainColor(Color color);
    }

    public static abstract class SideBarColor extends ColorMap {
        SideBarColor() {
            super();
        }

        /**
         * This function changes the main color of the current component
         * @param color the expected color
         * @requires color != null
         * @modifies theme
         * @effects change a key
         */
        @Override
        public void setMainColor(Color color) {
            this.set(SIDEBAR_BACKGROUND, color);
        }
    }

    public static abstract class CalendarColor extends ColorMap {
        CalendarColor() {
            super();
        }

        /**
         * This function changes the main color of the current component
         * @param color the expected color
         * @requires color != null
         * @modifies theme
         * @effects change a key
         */
        @Override
        public void setMainColor(Color color) {
            this.set(CAL_TITLE_TEXT, color);
            this.set(TODAY, color);
        }
    }

    public static abstract class DeadlineColor extends ColorMap {
        public DeadlineColor() {
            super();
        }

        /**
         * This function changes the main color of the current component
         * @param color the expected color
         * @requires color != null
         * @modifies theme
         * @effects change a key
         */
        @Override
        public void setMainColor(Color color) {
            this.set(SUBMIT_COLOR, color);
        }
    }

    // Member variables
    protected SideBarColor sidebarColor;
    protected CalendarColor calendarColor;
    protected DeadlineColor deadlineColor;

    // Getters
    // Calendar general
    public Color CAL_BACKGROUND() {
        return this.calendarColor.get(CAL_BACKGROUND);
    }
    public Color CAL_DATE_TEXT() {
        return this.calendarColor.get(CAL_DATE_TEXT);
    }
    public Color CAL_TITLE_TEXT() {
        return this.calendarColor.get(CAL_TITLE_TEXT);
    }
    public Color HIGHLIGHT() {
        return this.calendarColor.get(HIGHLIGHT);
    }
    public Color REFRESH_BUTTON() {
        return this.calendarColor.get(REFRESH_BUTTON);
    }
    public Color CAL_TITLE_SEP() {
        return this.calendarColor.get(CAL_TITLE_SEP);
    }
    public Color CAL_INVALID_BACKGROUND() {
        return this.calendarColor.get(CAL_BACKGROUND_INVALID);
    }
    public Color HOVER_OVER_CAL() {
        return this.calendarColor.get(HOVER_OVER_CAL);
    }
    public Color HOVER_OVER_REFRESH() {
        return this.calendarColor.get(HOVER_OVER_REFRESH);
    }
    public Color TODAY() {
        return this.calendarColor.get(TODAY);
    }
    public Color TODAY_TEXT() {
        return this.calendarColor.get(TODAY_TEXT);
    }
    // Sidebar
    public Color SIDEBAR_BACKGROUND() {
        return this.sidebarColor.get(SIDEBAR_BACKGROUND);
    }
    public Color SIDEBAR_TEXT() {
        return this.sidebarColor.get(SIDEBAR_TEXT);
    }
    public Color SIDEBAR_HOVER() {
        return this.sidebarColor.get(HOVER_OVER_SIDEBAR);
    }
    // Calendar Deadline
    public Color SUBMIT_COLOR() {
        return this.deadlineColor.get(SUBMIT_COLOR);
    }
    public Color FINISH_COLOR() {
        return this.deadlineColor.get(FINISH_COLOR);
    }
    public Color LATE_SUBMIT_COLOR() {
        return this.deadlineColor.get(LATE_SUBMIT_COLOR);
    }
    public Color RESUBMIT_COLOR() {
        return this.deadlineColor.get(RESUBMIT_COLOR);
    }
    public Color RESUBMIT_COLOR_TODAY() {
        return this.deadlineColor.get(RESUBMIT_COLOR_TODAY);
    }
    public Color NO_SUBMISSION_TODAY() {
        return this.deadlineColor.get(NO_SUBMISSION_COLOR);
    }
    public Color RIGHT_MENU_TEXT() {
        return this.deadlineColor.get(RIGHT_MENU_TEXT);
    }
    public Color RIGHT_MENU_BACKGROUND() {
        return this.deadlineColor.get(RIGHT_MENU_BACKGROUND);
    }

    /**
     * This method set a new CalendarColor item
     * @param calendarColor the color theme for calendar panel
     * @requires calendarColor != null
     * @modifies calendarColor
     * @effects change the calendarColor
     */
    public void setCalendarColor(CalendarColor calendarColor) {
        this.calendarColor = calendarColor;
    }

    /**
     * This method set a new deadlineColor item
     * @param deadlineColor the color theme for deadline blocks
     * @requires deadlineColor != null
     * @modifies deadlineColor
     * @effects change the deadlineColor
     */
    public void setDeadlineColor(DeadlineColor deadlineColor) {
        this.deadlineColor = deadlineColor;
    }

    /**
     * This method set a new CalendarColor item
     * @param sidebarColor the color theme for side bar panel
     * @requires sidebarColor != null
     * @modifies sidebarColor
     * @effects change the sidebarColor
     */
    public void setSidebarColor(SideBarColor sidebarColor) {
        this.sidebarColor = sidebarColor;
    }

    /**
     * This method changes a specified color of one component
     * @param component the component being changed
     * @param colorKey the key of the item being changed
     * @param color the expected color
     * @requires component != null, colorKey != null, color != null
     * @modifies theme
     * @effects change a key
     */
    @Override
    public void set(int component, String colorKey, Color color) {
        switch (component) {
            case SIDE_BAR_THEME:
                this.sidebarColor.set(colorKey, color);
                break;
            case CALENDAR_THEME:
                this.calendarColor.set(colorKey, color);
                break;
            case DEADLINE_THEME:
                this.deadlineColor.set(colorKey, color);
                break;
        }
    }

    /**
     * This method gets a specified color of one component     *
     * @param component the component being changed
     * @param colorKey  the key of the item being changed
     * @requires component != null, colorKey != null, color != null
     * @modifies theme
     * @effects return a key
     * @return the specified color
     */
    @Override
    public Color get(int component, String colorKey) {
        switch (component) {
            case SIDE_BAR_THEME:
                return this.sidebarColor.get(colorKey);
            case CALENDAR_THEME:
                return this.calendarColor.get(colorKey);
            case DEADLINE_THEME:
                return this.deadlineColor.get(colorKey);
            default:
                return null;
        }
    }

    /**
     * Creates and returns a copy of this object. The precise meaning
     * of "copy" may depend on the class of the object.
     * @requires None
     * @modifies None
     * @effects make a clone
     * @return     a clone of this instance.
     * @throws  CloneNotSupportedException  if the object's class does not
     *               support the {@code Cloneable} interface. Subclasses
     *               that override the {@code clone} method can also
     *               throw this exception to indicate that an instance cannot
     *               be cloned.
     * @see java.lang.Cloneable
     */
    @Override
    public Theme clone() throws CloneNotSupportedException {
        Theme t = (Theme) super.clone();
        t.calendarColor = this.calendarColor;
        t.calendarColor.theme = new ConcurrentHashMap<>(this.calendarColor.theme);
        t.deadlineColor = this.deadlineColor;
        t.deadlineColor.theme = new ConcurrentHashMap<>(this.deadlineColor.theme);
        t.sidebarColor = this.sidebarColor;
        t.sidebarColor.theme = new ConcurrentHashMap<>(this.sidebarColor.theme);
        return t;
    }
}


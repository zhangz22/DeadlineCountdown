package main.viewer.theme;

import java.awt.Color;

class OrangeTheme extends Theme {
    static final class OrangeSideBar extends SideBarColor {
        OrangeSideBar() {
            super();
            this.set(SIDEBAR_BACKGROUND, new Color(229,83,0));
            this.set(SIDEBAR_TEXT, Color.WHITE);
            this.set(HOVER_OVER_SIDEBAR, new Color(56, 56, 56));
        }
    }

    public static final class OrangeCalendar extends CalendarColor {
        OrangeCalendar() {
            super();
            this.set(CAL_BACKGROUND, new Color(255, 43, 0));
            this.set(CAL_DATE_TEXT, Color.WHITE);
            this.set(CAL_TITLE_TEXT, new Color(105, 175, 229));
            this.set(REFRESH_BUTTON, Color.WHITE);
            this.set(CAL_TITLE_SEP, new Color(38, 38, 38));
            this.set(CAL_BACKGROUND_INVALID, new Color(26, 26, 26));
            this.set(HOVER_OVER_CAL, new Color(22, 100, 167));
            this.set(HOVER_OVER_REFRESH, new Color(54, 54, 54));
            this.set(TODAY, new Color(38,38,38));
            this.set(TODAY_TEXT, new Color(105, 175, 229));
            this.set(HIGHLIGHT, new Color(59, 98, 130));
        }
    }

    public static final class OrangeDeadline extends DeadlineColor {
        OrangeDeadline() {
            super();
            this.set(SUBMIT_COLOR, new Color(110, 168, 215));
            this.set(LATE_SUBMIT_COLOR, new Color(255, 43, 0));
            this.set(FINISH_COLOR, new Color(21, 171, 0));
            this.set(RESUBMIT_COLOR, new Color(26, 188, 156));
            this.set(RESUBMIT_COLOR_TODAY, new Color(110, 168, 215));
            this.set(NO_SUBMISSION_COLOR, new Color(231, 139, 146));
            this.set(RIGHT_MENU_TEXT, new Color(243, 243, 243));
            this.set(RIGHT_MENU_BACKGROUND, new Color(60, 73, 81));
        }
    }

    OrangeTheme() {
        // Calendar general
        this.calendarColor = new OrangeCalendar();
        // Sidebar
        this.sidebarColor = new OrangeSideBar();
        // Deadline
        this.deadlineColor = new OrangeDeadline();
    }
}

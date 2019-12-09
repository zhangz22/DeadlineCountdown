package main.viewer.theme;

import java.awt.Color;

class LightTheme extends Theme {
    public static final class LightSideBar extends SideBarColor {
        LightSideBar() {
            super();
            this.set(SIDEBAR_BACKGROUND, new Color(225, 225, 225));
            this.set(SIDEBAR_TEXT, new Color(34, 34, 34));
            this.set(HOVER_OVER_SIDEBAR, new Color(200, 200, 200));
        }
    }

    static final class LightCalendar extends CalendarColor {
        LightCalendar() {
            super();
            this.set(CAL_BACKGROUND, Color.WHITE);
            this.set(CAL_DATE_TEXT, new Color(102, 102, 102));
            this.set(CAL_TITLE_TEXT, new Color(30, 114, 186));
            this.set(REFRESH_BUTTON, new Color(30, 114, 186));
            this.set(CAL_TITLE_SEP, new Color(230,230,230));
            this.set(CAL_BACKGROUND_INVALID, new Color(238, 238, 238));
            this.set(HOVER_OVER_CAL, new Color(206, 228, 247));
            this.set(HOVER_OVER_REFRESH, new Color(243, 243, 243));
            this.set(TODAY, new Color(30, 114, 186));
            this.set(TODAY_TEXT, Color.WHITE);
            this.set(HIGHLIGHT, new Color(0x47aef1));
        }
    }

    static final class LightDeadline extends DeadlineColor {
        LightDeadline() {
            super();
            this.set(SUBMIT_COLOR, new Color(0, 99, 152));
            this.set(LATE_SUBMIT_COLOR, new Color(255, 102, 85));
            this.set(FINISH_COLOR, new Color(11, 128, 67));
            this.set(RESUBMIT_COLOR, new Color(95, 186, 125));
            this.set(RESUBMIT_COLOR_TODAY, new Color(57, 132, 255));
            this.set(NO_SUBMISSION_COLOR, new Color(232, 72, 72));
            this.set(RIGHT_MENU_TEXT, new Color(243, 243, 243));
            this.set(RIGHT_MENU_BACKGROUND, new Color(60, 73, 81));
        }
    }

    LightTheme() {
        // Calendar general
        this.calendarColor = new LightCalendar();
        // Sidebar
        this.sidebarColor = new LightSideBar();
        // Deadline
        this.deadlineColor = new LightDeadline();
    }
}

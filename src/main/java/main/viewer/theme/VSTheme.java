package main.viewer.theme;

import java.awt.Color;

class VSTheme extends Theme {
    static final class VSSideBar extends SideBarColor {
        VSSideBar() {
            super();
            this.set(SIDEBAR_BACKGROUND, new Color(89, 44, 141));
            this.set(SIDEBAR_TEXT, new Color(0xc7c7c7));
            this.set(HOVER_OVER_SIDEBAR, new Color(0x3d1149));
        }
    }

    public static final class VSCalendar extends CalendarColor {
        VSCalendar() {
            super();
            this.set(CAL_BACKGROUND, new Color(37, 37, 38));
            this.set(CAL_DATE_TEXT, new Color(197, 197, 197));
            this.set(CAL_TITLE_TEXT, new Color(197, 197, 197));
            this.set(REFRESH_BUTTON, new Color(197, 197, 197));
            this.set(CAL_TITLE_SEP, new Color(30, 30, 30));
            this.set(CAL_BACKGROUND_INVALID, new Color(30, 30, 30));
            this.set(HOVER_OVER_CAL, new Color(100, 64, 128));
            this.set(HOVER_OVER_REFRESH, new Color(100, 64, 128));
            this.set(TODAY, new Color(38,38,38));
            this.set(TODAY_TEXT, new Color(100, 64, 128));
            this.set(HIGHLIGHT, new Color(149, 96, 191));
        }
    }

    VSTheme() {
        // Calendar general
        this.calendarColor = new VSCalendar();
        // Sidebar
        this.sidebarColor = new VSSideBar();
        // Deadline
        this.deadlineColor = new DarkTheme.DarkDeadline();
    }

}

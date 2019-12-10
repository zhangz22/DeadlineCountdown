package main.viewer.theme;

import java.awt.Color;

class HighContrastTheme extends Theme{
    HighContrastTheme() {
        this.sidebarColor = new DarkTheme.DarkSideBar();
        this.sidebarColor.set(Theme.HOVER_OVER_SIDEBAR, new Color(0, 0, 102));
        this.calendarColor = new LightTheme.LightCalendar();
        this.calendarColor.set(Theme.CAL_BACKGROUND, new Color(243, 242, 240));
        this.calendarColor.set(Theme.CAL_BACKGROUND_INVALID, new Color(19, 19, 20));
        this.calendarColor.set(Theme.CAL_TITLE_TEXT, new Color(19,19,20));
        this.calendarColor.set(Theme.CAL_DATE_TEXT, new Color(19,19,20));
        this.calendarColor.set(Theme.REFRESH_BUTTON, new Color(19,19,20));
        this.deadlineColor = new LightTheme.LightDeadline();
    }
}

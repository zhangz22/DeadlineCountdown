package main.viewer.theme;

import java.awt.Color;

class SubmittyTheme extends Theme {
    SubmittyTheme(){
        this.sidebarColor = new DarkTheme.DarkSideBar();
        this.sidebarColor.set(Theme.SIDEBAR_BACKGROUND, new Color(0, 99, 152));
        this.sidebarColor.set(Theme.HOVER_OVER_SIDEBAR, new Color(0, 114, 158));
        this.calendarColor = new LightTheme.LightCalendar();
        this.calendarColor.set(Theme.CAL_BACKGROUND, new Color(255, 255, 255));
        this.calendarColor.set(Theme.CAL_BACKGROUND_INVALID, new Color(246, 251, 254));
        this.calendarColor.set(Theme.CAL_TITLE_TEXT, new Color(0, 99, 152));
        this.calendarColor.set(Theme.TODAY, new Color(0, 99, 152));
        this.calendarColor.set(Theme.HOVER_OVER_REFRESH, new Color(217, 225, 226));
        this.deadlineColor = new LightTheme.LightDeadline();
    }
}

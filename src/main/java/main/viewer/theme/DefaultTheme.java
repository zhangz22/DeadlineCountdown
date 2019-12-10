package main.viewer.theme;

/**
 * This class represents the default theme that will be used in main
 */
public class DefaultTheme extends Theme {
    public DefaultTheme() {
        this.sidebarColor = new DarkTheme.DarkSideBar();
        this.calendarColor = new LightTheme.LightCalendar();
        this.deadlineColor = new LightTheme.LightDeadline();
    }
}

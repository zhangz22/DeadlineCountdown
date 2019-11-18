package main.viewer.theme;

import java.awt.Color;

class BlueTheme extends Theme {
    BlueTheme() {
        this.sidebarColor = new DarkTheme.DarkSideBar();
        this.sidebarColor.set(Theme.SIDEBAR_BACKGROUND, new Color(0x0078d7));
        this.sidebarColor.set(Theme.HOVER_OVER_SIDEBAR, new Color(0x0066b4));
        this.calendarColor = new LightTheme.LightCalendar();
        this.calendarColor.set(Theme.CAL_BACKGROUND, new Color(243, 242, 240));
        this.calendarColor.set(Theme.CAL_BACKGROUND_INVALID, new Color(224, 224, 224));
        this.calendarColor.set(Theme.HOVER_OVER_REFRESH, new Color(210, 210, 210));
        this.deadlineColor = new LightTheme.LightDeadline();
    }
}

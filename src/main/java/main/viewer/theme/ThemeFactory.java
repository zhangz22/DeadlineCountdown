package main.viewer.theme;

/**
 * This is a factory class to create themes for main
 *
 * Note: currently available base themes are
 * .-----------------.-----------------.-----------------.
 * |  SideBarThemme  |  CalendarTheme  |  DeadlineTheme  |
 * |-----------------|-----------------|-----------------|
 * |  LightSideBar   |  LightCalendar  |  LightDeadline  |
 * |  DarkSideBar    |  DarkCalendar   |  DarkDeadline   |
 * |  VSSideBar      |  VSCalendar     |                 |
 * '-----------------'-----------------'-----------------'
 */
public class ThemeFactory {
    public static final String[] ALL_THEME = {"default_theme", "light_theme",
            "dark_theme", "high_contrast_theme", "blue_theme", "submitty_theme", "vs_theme"};

    /**
     * This method returns a theme based on the given name
     * @param themeName the name for the theme
     * @requires None
     * @modifies None
     * @effects None
     * @return a theme instance
     */
    public static Theme createTheme(String themeName) {
        switch (themeName) {
            case "dark_theme":
                return new DarkTheme();
            case "light_theme":
                return new LightTheme();
            case "blue_theme":
                return new BlueTheme();
            case "submitty_theme":
                return new SubmittyTheme();
            case "vs_theme":
                return new VSTheme();
            case "high_contrast_theme":
                return new HighContrastTheme();
            default:
                return new DefaultTheme();
        }
    }
}

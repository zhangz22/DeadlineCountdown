package main.controller;

import main.viewer.Log;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;

import static main.viewer.Log.ANSI_CYAN;
import static main.viewer.Log.ANSI_RESET;

/**
 * This class represents the settings for main
 */
public class Settings {
    // all supported settings item
    public static final class SUPPORTED {
        public static final String START_FROM_SUNDAY = "start_from_sunday";
        public static final String NOTIFICATION_ENABLED = "notification_enabled";
        public static final String SHOW_PAST_DEADLINES = "show_past_deadlines";
        public static final String AUTO_SAVE_AFTER_REFRESH = "auto_save_after_refresh";
        public static final String START_ON_LOGIN = "start_on_login";
        public static final String SAVE_LOCAL_FILE = "save_local_file";
        public static final String[] ALL_SETTINGS =
                {START_FROM_SUNDAY, NOTIFICATION_ENABLED, SHOW_PAST_DEADLINES, AUTO_SAVE_AFTER_REFRESH};
    }
    // settings
    private HashMap<String, Boolean> settings;
    private String language;
    private String theme;
    private int themeRed;
    private int themeGreen;
    private int themeBlue;

    /**
     * Constructor
     * @requires None
     * @modifies settings, language, theme, themeRed, themeGreen, themeBlue
     * @effects create a Settings instance
     */
    public Settings() {
        this.settings = new HashMap<>();
        this.setDefaultSettings();
    }

    /**
     * Copy constructor
     * @param oldSettings the old Settings instance
     * @requires oldSettings != null
     * @effects create a new Settings instance and copy the saved settings
     */
    public Settings(Settings oldSettings) {
        this.settings = new HashMap<>(oldSettings.settings);
        this.language = oldSettings.language;
        this.theme = oldSettings.theme;
        this.themeRed = oldSettings.themeRed;
        this.themeGreen = oldSettings.themeGreen;
        this.themeBlue = oldSettings.themeBlue;
        this.settings.put(SUPPORTED.SAVE_LOCAL_FILE, true);
    }

    /**
     * This function sets the settings to default settings
     * @requires None
     * @modifies settings, language, theme, themeRed, themeGreen, themeBlue
     * @effects set default settings
     */
    public void setDefaultSettings() {
        this.settings.put(SUPPORTED.AUTO_SAVE_AFTER_REFRESH, true);
        this.settings.put(SUPPORTED.SHOW_PAST_DEADLINES, true);
        this.settings.put(SUPPORTED.START_ON_LOGIN, false);
        this.settings.put(SUPPORTED.NOTIFICATION_ENABLED, true);
        this.settings.put(SUPPORTED.START_FROM_SUNDAY, true);
        this.settings.put(SUPPORTED.SAVE_LOCAL_FILE, true);
        this.language = "en_US";
        this.theme = "default";
        this.themeRed = 34;
        this.themeGreen = 34;
        this.themeBlue = 34;
    }

    /**
     * This function returns a copy of all settings items for saving purpose
     * @requires None
     * @modifies None
     * @effects return all settings
     * @return a copy of all settings
     */
    public HashMap<String, Boolean> getAllSettings() {
        return new HashMap<>(this.settings);
    }

    /**
     * This function returns a specified setting
     * @param key the key for the setting
     * @requires None
     * @modifies None
     * @effects return a setting
     * @return the value for the setting corresponded to the key;
     *         null if the required key is not supported
     */
    public Boolean get(String key) {
        return this.settings.get(key);
    }

    /**
     * This function returns true if the program would save all deadlines locally and
     * automatically after login
     * @requires None
     * @modifies None
     * @effects check if the program will save deadlines automatically
     * @return true if the program will save deadlines automatically and false otherwise
     */
    public boolean isAutoSaveAfterRefresh() {
        return this.get(SUPPORTED.AUTO_SAVE_AFTER_REFRESH);
    }

    /**
     * This function returns true if the program would show past deadlines
     * @requires None
     * @modifies None
     * @effects check if the program shows past deadlines
     * @return true if the program shows past deadlines
     */
    public boolean isShowPastDeadlines() {
        return this.get(SUPPORTED.SHOW_PAST_DEADLINES);
    }

    /**
     * This function returns true if the program would allow notifications
     * @requires None
     * @modifies None
     * @effects check if the program allows notifications
     * @return true if the program allows notifications
     */
    public boolean isNotificationEnabled() {
        return this.get(SUPPORTED.NOTIFICATION_ENABLED);
    }

    /**
     * This function returns true if the program is set to be started from Sunday
     * @requires None
     * @modifies None
     * @effects check if the program starts from Sunday
     * @return true if the program starts from Sunday
     */
    public boolean isStartFromSunday() {
        return this.get(SUPPORTED.START_FROM_SUNDAY);
    }

    /**
     * This function returns true if the program can save local files
     * @requires None
     * @modifies None
     * @effects check if the program can save local files
     * @return true if the program can save local files
     */
    public boolean isSaveLocalUnavailable() {
        return !this.get(SUPPORTED.SAVE_LOCAL_FILE);
    }

    /**
     * This function returns the language setting for the program
     * @requires None
     * @modifies None
     * @effects return language settings
     * @return language
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * This function returns the theme setting for the program
     * @requires None
     * @modifies None
     * @effects return theme settings
     * @return theme
     */
    public String getTheme() {
        return this.theme;
    }

    /**
     * This function returns the main color setting for the program
     * @requires None
     * @modifies None
     * @effects return main color
     * @return color
     */
    public Color getThemeColor() {
        return new Color(this.themeRed, this.themeGreen, this.themeBlue);
    }

    /**
     * This function modifies a setting item
     * @param key the key for the setting item
     * @param value the value for the setting item
     * @requires None
     * @modifies this.settings
     * @effects change the value of key in the settings map
     */
    public void put(String key, Boolean value) {
        if (Arrays.asList(SUPPORTED.ALL_SETTINGS).contains(key)) {
            this.settings.put(key, value);
        }
    }

    /**
     * This function changes the language for the program
     * @param language the new language set for the program
     * @param parent the main component of main. For notification purpose
     * @param alert whether the program alerts the user about the result
     * @requires None
     * @modifies this.language
     * @effects change the language
     */
    void setLanguage(String language, GUIController parent, boolean alert) {
        Log.debug(ANSI_CYAN + "DEBUG: [Settings]      Language set as " + ANSI_RESET + language);
        this.language = language;
        if (parent != null && alert) {
            SwingUtilities.invokeLater(() -> {
                parent.saveSettings(false);
                parent.alert(parent.getFrame().getText("restart_alert"));
            });
        }
    }

    /**
     * This function changes the theme for the program
     * @param theme the new theme set for the program
     * @param parent the main component of main. For notification purpose
     * @param mainColor the main theme color
     * @requires None
     * @modifies this.theme
     * @effects change the theme
     */
    void setTheme(String theme, GUIController parent, Color mainColor) {
        Log.debug("DEBUG: [Settings]      Theme set as \"" + theme + "\"");
        this.theme = theme;
        this.themeRed = mainColor.getRed();
        this.themeGreen = mainColor.getGreen();
        this.themeBlue = mainColor.getBlue();
    }
}

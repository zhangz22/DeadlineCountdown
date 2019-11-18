package main;

import main.controller.GUIController;

import javax.swing.SwingUtilities;
import java.util.Locale;

public class Main {
    /**
     * This function returns the user home directory path based on user's platform
     * @requires None
     * @modifies None
     * @effects None
     * @return the user home directory path
     */
    private static String getUserAppDirectory() {
        if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("mac"))
            return System.getProperty("user.home") + "/Library/Application Support/main";
        else if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("win"))
            return System.getenv("APPDATA") + "/main";
        else
            return "./";
    }

    public static void main(String[] args) {
        System.setProperty("app.profile", getUserAppDirectory());
        SwingUtilities.invokeLater(() -> {
            GUIController window = new GUIController();
            window.run(args);
        });
    }
}
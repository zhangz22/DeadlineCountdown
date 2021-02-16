package main;

import main.controller.GUIController;
import main.viewer.Log;

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
            return System.getProperty("user.home") + "/Library/Application Support/DeadlineCountdown";
        else if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("win"))
            return System.getenv("APPDATA") + "/DeadlineCountdown";
        else
            return "./";
    }

    public static void main(String[] args) {
        System.setProperty("app.profile", getUserAppDirectory());
        Log.error("TEST: Set " + getUserAppDirectory() + " to save logs");
        SwingUtilities.invokeLater(() -> {
            // try {
                GUIController window = new GUIController();
                window.run(args);
            // } catch (Exception e) {
            //     Log.error("Unhandled bug: ", e);
            // }
        });
    }
}
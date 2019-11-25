package main.viewer;

/**
 * This class uses System.out to send debug information
 */
public class ConsoleDebug {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    /**
     * A function to set debug information to the console
     * @param debugInformation a string representing the debug information
     * @requires debugInformation != null
     * @modifies None
     * @effects send a debug output
     */
    public static void debug(Object debugInformation) {
        System.out.println(debugInformation);
    }

    /**
     * A function to set debug information to the console
     * @param debugInformation a string representing the debug information
     * @requires debugInformation != null
     * @modifies None
     * @effects send a debug output
     */
    public static void debug(Object debugInformation, String color) {
        System.out.println(color + debugInformation + ANSI_RESET);
    }
}

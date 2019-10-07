package localParser;

import model.Course;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a parser for local IO operations
 */
public abstract class Parser {
    ConcurrentHashMap<String, Course> allCourses;
    public static final String[] SUPPORTED_EXTIONSION = {"json", "txt", "csv", "ics"};
    public static final String[] SUPPORTED_EXTIONSION_DESCRIPTION =
            {"JavaScript Object Notation (*.json)",
                    "Pure OneSecondFont (*.txt)",
                    "Comma-Separated Values (*.csv)",
                    "Universal Calendar Format File (*.ics)"};
    protected static String errMsg = "";

    /**
     * This method would save data to a local JSON file.
     *
     * @requires file != null
     * @modifies None
     * @effects save to / load from local file
     * @return result: true -> successful; false -> failed
     */
    public abstract boolean Json();

    /**
     * This method would save data to a local ICS file.
     *
     * @requires file != null
     * @modifies None
     * @effects save to / load from local file
     * @return result: true -> successful; false -> failed
     */
    public abstract boolean Ics();

    /**
     * This method would save data to a local CSV file.
     *
     * @requires file != null
     * @modifies None
     * @effects save to / load from local file
     * @return result: true -> successful; false -> failed
     */
    public abstract boolean Csv();

    /**
     * This function generates a new Thread running the parser
     *
     * @param parser the parser type
     * @param extension the file extension
     * @requires parser != null
     * @modifies None
     * @effects None
     * @return a new Java Thread
     */
    public static Thread getParserThread(Parser parser, String extension) {
        return new Thread(() -> {
            boolean result = true;
            switch (extension.toUpperCase()) {
                case "JSON":
                    result = parser.Json();
                    break;
                case "ICS":
                    result = parser.Ics();
                    break;
                case "CSV":
                case "TXT":
                    result = parser.Csv();
                    break;
            }
        });
    }
}


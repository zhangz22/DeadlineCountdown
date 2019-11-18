package localParser;

import model.Course;
import main.controller.AbstractController;
import main.controller.GUIController;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a parser for local IO operations
 */
public abstract class Parser {
    ConcurrentHashMap<String, Course> allCourses;
    public static final String[] SUPPORTED_EXTIONSION = {"json", "txt", "csv", "ics"};
    public static final String[] SUPPORTED_EXTIONSION_DESCRIPTION =
            {"JavaScript Object Notation (*.json)",
                    "Pure Text (*.txt)",
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
     * @param parent the main process
     * @param showDialog if the program should show a dialog indicator
     * @requires parser != null
     * @modifies None
     * @effects None
     * @return a new Java Thread
     */
    public static Thread getParserThread(Parser parser, String extension,
                                         AbstractController parent, boolean showDialog) {
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
            // alerts
            if (parent instanceof GUIController) {
                if (!result && showDialog) {
                    String parserType = (parser instanceof Save) ?
                            ((GUIController) parent).getFrame().getText("saving_to") :
                            ((GUIController) parent).getFrame().getText("loading_from");
                    ((GUIController) parent).notification(parserType,
                            ((!(errMsg == null) && !errMsg.equals("")) ?
                                    ((GUIController) parent).getFrame().getText("error_code")
                                    : "") + " " + errMsg, "");

                }
            }
        });
    }
}


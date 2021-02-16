package main.viewer.util;

import javafx.util.Pair;
import model.Course;
import model.Deadline;
import main.controller.GUIController;
import main.viewer.DeadlineCountdownFactory;
import main.viewer.Log;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public class DeadlineExporter {
    private Deadline deadline;
    private GUIController controller;
    private String caller;

    /**
     * Constructor
     * @param deadline the deadline being exported
     * @param controller the main GUIController for notifications and text formats
     * @param caller the class calling this method
     * @requires deadline != null, mainmain != null, caller != null
     * @modifies deadline, mainmain, caller
     * @effects create a new DeadlineExporter instance
     */
    public DeadlineExporter(Deadline deadline, GUIController controller, String caller) {
        this.deadline = deadline;
        this.controller = controller;
        this.caller = caller;
    }

    /**
     * This function would export the deadline that current DeadlineBlock is
     * representing
     * @requires None
     * @modifies None
     * @effects export deadline
     */
    public void export() {
        Log.debug("Debug: [" + this.caller + "] {"
                + this.deadline.toString() + "} will be exported.");
        // save to
        JFileChooser fileChooser = DeadlineCountdownFactory.createFileChooser(
                this.deadline.getName(),
                this.controller.getFrame().getTextResource());
        int result = fileChooser.showSaveDialog(this.controller.getFrame());

        if (result == JFileChooser.APPROVE_OPTION) {
            Pair<File, String> choice = DeadlineCountdownFactory.getFileFromFileChooser(fileChooser);
            if (choice == null) return;
            File file = choice.getKey();
            String extSelected = choice.getValue();

            if (file == null) {
                Log.debug("Debug: [" + this.caller + "] trying to export to a null file.");
                this.controller.notification(
                        this.controller.getFrame().getText("export_failed"), "", "");
                return;
            }
            Log.debug("Debug: [" + this.caller + "] "
                    + deadline.toString() + " will be exported to " + file.getName());
            ConcurrentHashMap<String, Course> courseMap = new ConcurrentHashMap<>();
            Course currCourse = new Course(this.deadline.getCourseName());
            currCourse.addDeadline(this.deadline);
            courseMap.put(currCourse.getCourseName(), currCourse);
            if (!file.isDirectory()) {
                PrintWriter writer;
                try {
                    writer = new PrintWriter(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    this.controller.notification(this.controller.getFrame().getText("export_failed"),
                            this.controller.getFrame().getText("saving_to")
                            + "\n"
                            + this.controller.getFrame().getText("error_code")
                            + e.getMessage(), "");
                    return;
                }
                // save local files
                if (this.controller.getSettings().isSaveLocalUnavailable()) {
                    Log.error("DEBUG: [" + caller + "] Settings return " +
                            "'isSaveLocalUnavailable'");
                    this.controller.notification(this.controller.getFrame().getText("export_failed"),
                            this.controller.getFrame().getText("export_access_denied") + "\n" +
                            this.controller.getFrame().getText("export_please_restart"),"");
                    return;
                }
                localParser.Parser save = new localParser.Save(courseMap, writer);
                Thread thread = localParser.Parser.getParserThread(save, extSelected, this.controller, true);
                thread.run();
            } else {
                Log.debug("Debug: [" + this.caller + "] trying to export to a directory file.");
                this.controller.notification(this.controller.getFrame().getText("export_failed"),
                        file.getName() + " " + this.controller.getFrame().getText("export_is_directory"),
                        "");
            }
        }
    }
}

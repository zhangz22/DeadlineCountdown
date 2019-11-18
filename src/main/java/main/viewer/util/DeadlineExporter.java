package main.viewer.util;

import javafx.util.Pair;
import model.Course;
import model.Deadline;
import main.controller.GUIController;
import main.viewer.mainFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public class DeadlineExporter {
    private Deadline deadline;
    private GUIController mainmain;
    private String caller;

    /**
     * Constructor
     * @param deadline the deadline being exported
     * @param mainmain the main GUIController for notifications and text formats
     * @param caller the class calling this method
     * @requires deadline != null, mainmain != null, caller != null
     * @modifies deadline, mainmain, caller
     * @effects create a new DeadlineExporter instance
     */
    public DeadlineExporter(Deadline deadline, GUIController mainmain, String caller) {
        this.deadline = deadline;
        this.mainmain = mainmain;
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
        System.out.println("Debug: [" + this.caller + "] {"
                + this.deadline.toString() + "} will be exported.");
        // save to
        JFileChooser fileChooser = mainFactory.createFileChooser(
                this.deadline.getName(),
                this.mainmain.getFrame().getTextResource());
        int result = fileChooser.showSaveDialog(this.mainmain.getFrame());

        if (result == JFileChooser.APPROVE_OPTION) {
            Pair<File, String> choice = mainFactory.getFileFromFileChooser(fileChooser);
            if (choice == null) return;
            File file = choice.getKey();
            String extSelected = choice.getValue();

            if (file == null) {
                System.out.println("Debug: [" + this.caller + "] trying to export to a null file.");
                this.mainmain.notification(
                        this.mainmain.getFrame().getText("export_failed"), "", "");
                return;
            }
            System.out.println("Debug: [" + this.caller + "] "
                    + deadline.toString() + " will be exported to " + file.getName());
            ConcurrentHashMap<String, Course> courseMap = new ConcurrentHashMap<>();
            Course currCourse = new Course(this.deadline.getCourse());
            currCourse.addDeadline(this.deadline);
            courseMap.put(currCourse.getCourseName(), currCourse);
            if (!file.isDirectory()) {
                PrintWriter writer;
                try {
                    writer = new PrintWriter(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    this.mainmain.notification(this.mainmain.getFrame().getText("export_failed"),
                            this.mainmain.getFrame().getText("saving_to")
                            + "\n"
                            + this.mainmain.getFrame().getText("error_code")
                            + e.getMessage(), "");
                    return;
                }
                // save local files
                localParser.Parser save = new localParser.Save(courseMap, writer);
                Thread thread = localParser.Parser.getParserThread(save, extSelected, this.mainmain, true);
                thread.run();
            } else {
                System.out.println("Debug: [" + this.caller + "] trying to export to a directory file.");
                this.mainmain.notification(this.mainmain.getFrame().getText("export_failed"),
                        file.getName() + " " + this.mainmain.getFrame().getText("export_is_directory"),
                        "");
            }
        }
    }
}
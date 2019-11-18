package main.viewer.calendarPanel;

import model.CalendarWrapper;
import model.Deadline;
import main.viewer.*;
import main.viewer.util.DeadlineExporter;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * This is the deadline preview block inside each date's block
 */
class DeadlineBlock extends JTextArea implements DeadlineBlockInterface {
    private final CalendarPanel parent;
    private Deadline deadline;

    /**
     * Constructor
     * @param parent       the calendarPanel that contains current DeadlineBlock
     * @param deadline     the deadline that will be add to this DeadlineBlock
     * @requires parent != null, Deadline != null
     */
    DeadlineBlock(CalendarPanel parent, Deadline deadline) {
        super(deadline.getName());
        this.parent = parent;
        this.deadline = deadline;

        // set color based on the status of the deadline
        this.setEnabled(false);

        // set font
        this.setDisabledTextColor(Color.WHITE);
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setMargin(new Insets(2, 5, 2, 2));
        super.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        // set tool tip message: course name + deadline name + special status
        String tooltipText = "<html>" + deadline.getCourse() + ":<br>" + deadline.getName() + "<br>(due "
                + CalendarWrapper.getTimeStr(deadline.getYear(), deadline.getMonth(), deadline.getDay(), deadline.getHour(), deadline.getMinute()) + ")";
        tooltipText += "</html>";
        super.setToolTipText(tooltipText);

        // right-click menu
        JPopupMenu menu = mainFactory.createDeadlineBlockRightMenu(this,
                parent.getMainmainGUI().getFrame().getTheme().SIDEBAR_TEXT(),
                parent.getMainmainGUI().getFrame().getTheme().SIDEBAR_BACKGROUND());
        this.setComponentPopupMenu(menu);
        this.addMouseListener(mainFactory.createRightClickMenuAction(menu));
    }

    /**
     * This method returns the course name of current deadline
     * @requires None
     * @modifies None
     * @effects None
     * @return this.courseName
     */
    @Override
    public String getCourseName() {
        return this.deadline.getCourse();
    }

    /**
     * This method returns the current deadline
     *
     * @return this.deadline
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public Deadline getDeadline() {
        return this.deadline;
    }

    /**
     * This method returns the deadline name of current deadline
     * @requires None
     * @modifies None
     * @effects None
     * @return this.deadlineName
     */
    @Override
    public String getDeadlineName() {
        return this.deadline.getName();
    }

    /**
     * This method returns the year number of current deadline
     * @requires None
     * @modifies None
     * @effects None
     * @return this.year
     */
    @Override
    public int getYear() {
        return this.deadline.getYear();
    }

    /**
     * This method returns the month number of current deadline
     * @requires None
     * @modifies None
     * @effects None
     * @return this.month
     */
    @Override
    public int getMonth() {
        return this.deadline.getMonth();
    }

    /**
     * This method returns the date number of current deadline
     * @requires None
     * @modifies None
     * @effects None
     * @return this.date
     */
    @Override
    public int getDate() {
        return this.deadline.getDay();
    }

    /**
     * This function would delete the deadline that current DeadlineBlock is
     * representing
     * @requires None
     * @modifies this.parent
     * @effects delete current deadline
     */
    @Override
    public void delete() {
        this.parent.handleRemoveSignal(this.getCourseName(), this.getDeadlineName(), this.getYear(),
                this.getMonth(), this.getDate());
    }

    /**
     * This function would edit the deadline that current DeadlineBlock is
     * representing
     * @requires None
     * @modifies this.parent
     * @effects edit current deadline
     */
    @Override
    public void edit() {
        this.parent.handleEditSignal(this.deadline);
    }

    /**
     * This function would export the deadline that current DeadlineBlock is
     * representing
     * @requires None
     * @modifies None
     * @effects export current deadline
     */
    @Override
    public void export() {
        DeadlineExporter exporter = new DeadlineExporter(this.deadline, parent.getMainmainGUI(), "DeadlineBlock");
        exporter.export();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this deadline is before,
     * equal to or after than the specified deadline.
     * @param o the other deadline to be compared.
     * @requires None
     * @modifies None
     * @effects None
     * @return a negative integer, zero, or a positive integer as this current deadline
     * is earlier than, equal to, or latter than the specified deadline.
     * @throws NullPointerException if the specified object is null
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeadlineBlock that = (DeadlineBlock) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(deadline, that.deadline);
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables
     * @requires None
     * @modifies None
     * @effects None
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(parent, deadline);
    }
}

package main.viewer;

import model.Deadline;

/**
 * This interface defines the general behaviour of all DeadlineBlocks
 */
public interface DeadlineBlockInterface {
    /**
     * This method returns the course name of current deadline
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return this.courseName
     */
    String getCourseName();

    /**
     * This method returns the current deadline
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return this.deadline
     */
    Deadline getDeadline();

    /**
     * This method returns the year number of current deadline
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return this.year
     */
    int getYear();

    /**
     * This method returns the month number of current deadline
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return this.month
     */
    int getMonth();

    /**
     * This method returns the date number of current deadline
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return this.date
     */
    int getDate();

    /**
     * This method returns the deadline name of current deadline
     *
     * @requires None
     * @modifies None
     * @effects None
     * @return this.deadlineName
     */
    String getDeadlineName();

    /**
     * This function would delete the deadline that current DeadlineBlock is
     * representing
     *
     * @requires None
     * @modifies this.parent
     * @effects delete current deadline
     */
    void delete();

    /**
     * This function would edit the deadline that current DeadlineBlock is
     * representing
     *
     * @requires None
     * @modifies this.parent
     * @effects edit current deadline
     */
    void edit();

    /**
     * This function would export the deadline that current DeadlineBlock is
     * representing
     *
     * @requires None
     * @modifies this.parent
     * @effects export current deadline
     */
    void export();
}

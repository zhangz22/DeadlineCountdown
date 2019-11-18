package main.viewer.calendarPanel;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * This is an extended JPanel that contains two parts: a title bar and a center panel
 */
class RectangleWithTitle extends JPanel {
    private JPanel upperPart;
    private JPanel lowerPart;

    /**
     * Constructor
     * @param upper a JPanel which would be the title part
     * @param lower a JPanel which would be the center part
     * @requires upper != null, lower != null
     * @modifies this.upperPart, this.lowerPart
     * @effects create an instance of RectangleWithTitle
     */
    RectangleWithTitle(JPanel upper, JPanel lower) {
        super();
        super.setLayout(new BorderLayout());
        this.upperPart = upper;
        this.lowerPart = lower;
        super.add(this.upperPart, BorderLayout.NORTH);
        super.add(this.lowerPart, BorderLayout.CENTER);
    }

    /**
     * This function returns the title part
     * @requires None
     * @modifies None
     * @effects None
     * @return this.upperPart
     */
    JPanel getUpperPart() {
        return upperPart;
    }

    /**
     * This function returns the lower part
     * @requires None
     * @modifies None
     * @effects None
     * @return this.lowerPart
     */
    JPanel getLowerPart() {
        return this.lowerPart;
    }

    /**
     * This function would set the background of this component, both the title bar
     * and the center panel
     * @param color the required background color
     * @requires None
     * @modifies upperPart, lowerPart
     * @effects None
     */
    void setColor(Color color) {
        this.upperPart.setBackground(color);
        this.lowerPart.setBackground(color);
    }
}

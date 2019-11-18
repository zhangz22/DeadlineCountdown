package main.viewer.calendarPanel;

import model.CalendarWrapper;
import main.viewer.GUIViewer;
import main.viewer.textFormat.BaseText;

import javax.swing.JTextPane;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;

/**
 * This class is the calendar title which appears above the calendat and display
 * current month and year.
 */
public class TitlePanel extends JTextPane {
    private final BaseText textFormat;

    /**
     * Constructor
     * @param t current text format
     * @requires None
     * @modifies this, this.textFormat
     * @effects create a TitlePanel
     */
    public TitlePanel(BaseText t, GUIViewer parent) {
        super();
        this.textFormat = t;
        this.setMonthYear(null, null);
        this.setEnabled(false);
        this.setDisabledTextColor(parent.getTheme().CAL_TITLE_TEXT());
        this.setFont(new Font("Xhei", Font.BOLD, 24));
        this.setBackground(parent.getTheme().CAL_BACKGROUND());
    }

    /**
     * This function would let the title display a specified month and year
     * @param month an Integer to represent a specified month, start from 0 (January)
     *              use null to display current month
     * @param year an Integer to represent specified year
     *             use null to display current year
     * @requires None
     * @modifies this
     * @effects change the month and year that the label is currently displaying
     */
    public void setMonthYear(Integer month, Integer year) {
        if (month == null || year == null) {
            month = CalendarWrapper.now().getMonth();
            year = CalendarWrapper.now().getYear();
        }
        String monthStr = textFormat.monthYearFormat(textFormat.monthFormat(month), textFormat.yearFormat(year));
        setText(monthStr);
        setMargin(new Insets(4, 10, 10, 5));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}

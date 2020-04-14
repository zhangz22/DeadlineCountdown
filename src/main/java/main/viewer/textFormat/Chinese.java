package main.viewer.textFormat;

import model.CalendarWrapper;
import model.Deadline;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ResourceBundle;

/**
 * This class represents an Chinese text format
 */
public class Chinese extends BaseText {
    /**
     * Constructor
     * @param resource a ResourceBundle containing string for a specified locale
     * @requires resource != null
     * @modifies textResource
     * @effects create a new Chinese instance
     */
    Chinese(ResourceBundle resource) {
        super(resource);
    }

    /**
     * The format for representing a year and a month
     *
     * @param month a decimal to represent the month
     * @param year  a decimal to represent the year
     * @return a string to represent the year and the month
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public String monthYearFormat(String month, String year) {
        return year + "年" + month;
    }

    /**
     * The format for representing a date
     *
     * @param year  a decimal to represent the year
     * @param month a decimal to represent the month
     * @param day   a decimal to represent the day
     * @return a string to represent the date
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public String dateFormat(int year, int month, int day) {
        return yearFormat(year) + "/" + month + "/" +
                (dayFormat(day).length() < 2 ? "0" : "") + dayFormat(day);
    }

    /**
     * This function returns true if current locale should set Sunday as the first day
     * of week
     *
     * @return 1 for true and 0 for false
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int startsFromSunday() {
        return 0;
    }

    /**
     * This function returns a string to represent the user agreement
     *
     * @return the user agreement string
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public String userAgreement() {
        return "<html>" + getText("user_agreement1") +  "<br>" +
                getText("user_agreement2") +  "<br>" +
                getText("user_agreement3") +  "<br>" +
                getText("user_agreement4") +  "<br><br></html>";
    }

    /**
     * This function returns a string to represent the remaining time to a given deadline
     *
     * @param deadline      the given deadline
     * @param currentTime   the current time
     * @param showIndicator whether this string should include "later/ago" indicator
     * @return a string to represent the remaining time to a given deadline
     * @requires deadline != null; currentTime != null
     * @modifies None
     * @effects None
     */
    @Override
    public String getRemainingText(Deadline deadline, CalendarWrapper currentTime, boolean showIndicator) {
        if (currentTime == null) currentTime = CalendarWrapper.now();
        Pair<Period, Boolean> remain = deadline.getRemainPeriod(currentTime);
        Period pd = remain.getKey();
        String month;
        if (pd.getMonths() > 0) {
            month = String.format("%02d %s ", pd.getMonths(), getText("month_remain"));
        } else {
            month = "";
        }
        String remainingText = "";
        if (showIndicator) {
            if (remain.getValue().equals(true)) remainingText += (getText("due_in") + " ");
            else remainingText += (getText("ago") + " ");
        }
        remainingText += month + String.format("%02d %s %02d %s %02d %s",
                pd.getWeeks()*7+pd.getDays(), getText("day_remain"),
                pd.getHours(), getText("hour_remain"),
                pd.getMinutes(), getText("minute_remain"));
        return remainingText;
    }
}

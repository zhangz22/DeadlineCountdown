package main.viewer.textFormat;

import model.CalendarWrapper;
import model.Deadline;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ResourceBundle;

/**
 * This class represents an English text format
 */
public class English extends BaseText {
    /**
     * Constructor
     * @param resource a ResourceBundle containing string for a specified locale
     * @requires resource != null
     * @modifies textResource
     * @effects create a new English instance
     */
    English(ResourceBundle resource) {
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
        return month + " " + year;
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
    public final String dateFormat(int year, int month, int day) {
        return month + "/" +
                (dayFormat(day).length() < 2 ? "0" : "") + dayFormat(day) + "/" +
                yearFormat(year);
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
    public final int startsFromSunday() {
        return 1;
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
    public final String userAgreement() {
        return "<html>" + getText("user_agreement1") + "<br>" +
                getText("user_agreement2") + "<br>" +
                getText("user_agreement3") + "<br>" +
                getText("user_agreement4") + "<br>" +
                getText("user_agreement5") + "<br>" +
                getText("user_agreement6") + "<br>" +
                getText("user_agreement7") + "<br><br></html>";
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
    @SuppressWarnings("Duplicates")
    public String getRemainingText(Deadline deadline, CalendarWrapper currentTime, boolean showIndicator) {
        if (currentTime == null) currentTime = CalendarWrapper.now();
        Pair<Period, Boolean> remain = deadline.getRemainPeriod(currentTime);
        Period pd = remain.getKey();
        String month;
        if (pd.getMonths() > 0) {
            month = String.format("%02d %s ", pd.getMonths(), pd.getMonths() == 1 ? getText("month_remain") : getText("months_remain"));
        } else {
            month = "";
        }
        String remainingText = month + String.format("%02d %s %02d %s %02d %s ",
                pd.getWeeks()*7+pd.getDays(), pd.getWeeks()*7+pd.getDays() == 1 ? getText("day_remain") : getText("days_remain"),
                pd.getHours(), pd.getHours() == 1 ? getText("hour_remain") : getText("hours_remain"),
                pd.getMinutes(), pd.getMinutes() == 1 ? getText("minute_remain") : getText("minutes_remain"));
        if (showIndicator) {
            if (remain.getValue().equals(true)) remainingText += (getText("left") + ".");
            else remainingText += (getText("ago") + ".");
        }
        return remainingText;
    }
}

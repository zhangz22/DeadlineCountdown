package main.viewer.textFormat;

import model.Deadline;
import org.joda.time.DateTime;

import java.util.ResourceBundle;

/**
 * This class represents an abstract basic text format
 */
public abstract class BaseText {
    private final ResourceBundle textResource;
    private String[] months;

    /**
     * Constructor
     * @param resource a ResourceBundle containing string for a specified locale
     * @requires resource != null
     * @modifies textResource
     * @effects create a new BaseText instance
     */
    BaseText(ResourceBundle resource) {
        this.textResource = resource;
        months = new String[]{"", getText("jan"),
                               getText("feb"),
                               getText("mar"),
                               getText("apr"),
                               getText("may"),
                               getText("jun"),
                               getText("jul"),
                               getText("aug"),
                               getText("sep"),
                               getText("oct"),
                               getText("nov"),
                               getText("dec")};
    }

    /**
     * This function returns a text string contained within the ResourceBundle
     * @param textName the string need to be found
     * @requires None
     * @modifies None
     * @effects None
     * @return a string in required locale
     */
    protected String getText(String textName) {
        return textResource.getString(textName);
    }

    /**
     * The format for representing a year
     * @param year a decimal to represent the year
     * @requires None
     * @modifies None
     * @effects None
     * @return a string to represent the year
     */
    public String yearFormat(int year) {
        return String.valueOf(year);
    }

    /**
     * The format for representing a month
     * @param month a decimal to represent the month
     * @requires None
     * @modifies None
     * @effects None
     * @return a string to represent the month
     */
    public String monthFormat(int month) {
        return months[month];
    }

    /**
     * The format for representing a year and a month
     * @param month a decimal to represent the month
     * @param year a decimal to represent the year
     * @requires None
     * @modifies None
     * @effects None
     * @return a string to represent the year and the month
     */
    public abstract String monthYearFormat(String month, String year);

    /**
     * The format for representing a day
     * @param day a decimal to represent the day
     * @requires None
     * @modifies None
     * @effects None
     * @return a string to represent the day
     */
    String dayFormat(int day) {
        return String.valueOf(day);
    }

    /**
     * The format for representing a weekday
     * @param weekday a decimal to represent the month
     * @requires None
     * @modifies None
     * @effects None
     * @return a string to represent the weekday
     */
    public String weekFormat(int weekday) {
        switch (weekday) {
            case 1:
                return getText("mon") + " ";
            case 2:
                return getText("tue") + " ";
            case 3:
                return getText("wed") + " ";
            case 4:
                return getText("thur") + " ";
            case 5:
                return getText("fri") + " ";
            case 6:
                return getText("sat") + " ";
        }
        return getText("sun") + " ";
    }

    /**
     * The format for representing a date
     * @param year a decimal to represent the year
     * @param month a decimal to represent the month
     * @param day a decimal to represent the day
     * @requires None
     * @modifies None
     * @effects None
     * @return a string to represent the date
     */
    public abstract String dateFormat(int year, int month, int day);

    /**
     * This function returns true if current locale should set Sunday as the first day
     * of week
     * @requires None
     * @modifies None
     * @effects None
     * @return 1 for true and 0 for false
     */
    public abstract int startsFromSunday();

    /**
     * This function returns a string to represent the user agreement
     * @requires None
     * @modifies None
     * @effects None
     * @return the user agreement string
     */
    public abstract String userAgreement();

    /**
     * This function returns a string to represent the title of user agreement
     * @requires None
     * @modifies None
     * @effects None
     * @return the user agreement title string
     */
    public String userAgreementTitle() {
        return getText("user_agreement");
    }

    /**
     * This function returns a string to represent the remaining time to a given deadline
     * @param deadline the given deadline
     * @param currentTime the current time
     * @param showIndicator whether this string should include "later/ago" indicator
     * @requires deadline != null; currentTime != null
     * @modifies None
     * @effects None
     * @return a string to represent the remaining time to a given deadline
     */
    public abstract String getRemainingText(Deadline deadline, DateTime currentTime, boolean showIndicator);
}

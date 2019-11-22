package main.viewer.textFormat;

import java.util.ResourceBundle;

/**
 * This class will automatically create a BaseText subclass instance based on a given
 * locale string
 */
public class TextFactory {
    /**
     * This method automatically creates a BaseText subclass instance based on a given
     * locale string
     * @param locale a string to represent the locale
     * @param textResource a ResourceBundle that contains strings in main
     * @requires textResource != null
     * @modifies None
     * @effects None
     * @return a new BaseText instance
     */
    public static BaseText createTextFormat(String locale, ResourceBundle textResource) {
        switch (locale) {
            case "zh_Hant":
            case "zh_CN":
                return new Chinese(textResource);
            default:
                return new English(textResource);
        }
    }
}

package main.viewer.theme;

import java.awt.Color;

public interface ThemeInterface {
    /**
     * This method changes a specified color of one component
     *
     * @param component the component being changed
     * @param colorKey  the key of the item being changed
     * @param color     the expected color
     * @requires component != null, colorKey != null, color != null
     * @modifies theme
     * @effects change a key
     */
    void set(int component, String colorKey, Color color);

    /**
     * This method gets a specified color of one component
     *
     * @param component the component being changed
     * @param colorKey  the key of the item being changed
     * @requires component != null, colorKey != null, color != null
     * @modifies theme
     * @effects return a key
     * @return the specified color
     */
    Color get(int component, String colorKey);
}

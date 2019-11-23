package main.viewer.util;

import main.viewer.Log;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class handles operations related to a web page, such as opening.
 */
public class Webpage {
    /**
     * This function opens a web page in user's default browser
     * @param uri the uri to the web page
     * @requires None
     * @modifies None
     * @effects open a web page
     * @return true if the web page is opened successfully
     */
    private static boolean open(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
            Log.error("[Webpage] error when opening " + uri, e);
            }
        }
        return false;
    }

    /**
     * This function opens a web page in user's default browser
     * @param url the url to the web page
     * @requires None
     * @modifies None
     * @effects open a web page
     * @return true if the web page is opened successfully
     */
    private static boolean open(URL url) {
        try {
            return Webpage.open(url.toURI());
        } catch (URISyntaxException e) {
            Log.error("[Webpage] error when opening " + url, e);
        }
        return false;
    }

    /**
     * This function opens a web page in user's default browser
     * @param url the url to the web page
     * @requires None
     * @modifies None
     * @effects open a web page
     * @return true if the web page is opened successfully
     */
    public static boolean open(String url) {
        try {
            return Webpage.open(new URL(url));
        } catch (MalformedURLException e) {
            Log.error("[Webpage] error when opening " + url, e);
        }
        return false;
    }
}

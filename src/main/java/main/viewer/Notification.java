package main.viewer;

import com.sun.javafx.runtime.SystemProperties;
import model.Deadline;
import main.controller.AbstractController;
import main.controller.GUIController;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

/**
 * This class handles the system tray icon and notifications that will be sent to the user
 */
public class Notification {
    private SystemTray tray;
    private TrayIcon trayIcon;
    private Timer timer;
    private GUIController parent;

    /**
     * Constructor
     *
     * @requires None
     * @modifies tray, trayIcon
     * @effects create a new Notification instance
     */
    public Notification(GUIController parent) {
        if (!SystemTray.isSupported()) {
            return;
        }

        // obtain only one instance of the SystemTray object
        this.tray = SystemTray.getSystemTray();
        this.parent = parent;

        // if the icon is a file
        URL iconPath = Notification.class.getResource("/images/icon.png");
        ImageIcon icon = null;
        if (iconPath != null) {
            icon = new ImageIcon(iconPath);
        }
        Image image;
        if (icon == null) {
            image = Toolkit.getDefaultToolkit().createImage("icon.png");
        } else {
            image = icon.getImage();
        }
        this.trayIcon = new TrayIcon(image, "main");

        // let the system resize the image if needed
        this.trayIcon.setImageAutoSize(true);

        // set tooltip text for the tray icon
        this.trayIcon.setToolTip("main");
        this.timer = new Timer(6000, (e) -> {
            updateToolTip();
        });
        this.timer.start();
        try {
            this.tray.add(this.trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        if (!System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("win")) {
            return;
        }

        // create popup menu for the tray icon
        PopupMenu popupMenu = new PopupMenu();

        // tray icon actions
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 || e.getClickCount() == 2 && !e.isPopupTrigger()) {
                    parent.getFrame().showFront();
                }

            }
        });

        MenuItem closeMenu = new MenuItem("Close main");
        closeMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    close();
					System.exit(0);
				}
			});
        popupMenu.add(closeMenu);
        // add popup menu to the tray icon
        trayIcon.setPopupMenu(popupMenu);
    }

    /**
     * This function will check the user system type and send notification in the
     * corresponding method
     *
     * @param title    the title of the notification
     * @param message  the message
     * @param subtitle the subtitle of the notification
     * @requires None
     * @modifies None
     * @effects send a message
     */
    void send(String title, String message, String subtitle) {
        String OsName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (OsName.contains("win")) {
            if (SystemTray.isSupported() && this.trayIcon != null) {
                windows(title, message, subtitle);
            }
        } else if (OsName.contains("mac") || OsName.contains("darwin")) {
            mac(title, message, subtitle);
        // } else if (OsName.contains("nux")) {   there is no platform supported notification
        } else {
            if (parent instanceof GUIController) {
                ((GUIController) parent).alert("<html>" + title + "<br>" + message + "</html>");
            } else {
                System.out.println(title + System.getProperty("line.separator") + message);
            }
        }
    }

    /**
     * This function will send a notification on a Windows 10 platform by tray icon
     *
     * @param title    the title of the notification
     * @param message  the message
     * @param subtitle the subtitle of the notification
     * @requires None
     * @modifies None
     * @effects send a message
     */
    private void windows(String title, String message, String subtitle) {
        Thread thread = new Thread(() -> trayIcon.displayMessage(title, message, TrayIcon.MessageType.NONE));
        thread.run();
    }

    /**
     * This function will send a notification on a macOS platform by osascript
     *
     * @param title    the title of the notification
     * @param message  the message
     * @param subtitle the subtitle of the notification
     * @requires None
     * @modifies None
     * @effects send a message
     */
    private void mac(String title, String message, String subtitle) {
        try {
            Runtime.getRuntime().exec(new String[]
                    { "osascript", "-e", "display notification " + message + " with title " + title + " subtitle " + subtitle + "" });
        } catch (IOException e) {
            System.err.println("Error sending notifcation on macOS" + e);
        }
    }

    /**
     * This method closes the icon
     *
     * @requires None
     * @modifies trayIcon
     * @effects close the icon
     */
    void close() {
        this.timer.stop();
        this.tray.remove(this.trayIcon);
    }

    /**
     * This method updates the tooltip text
     *
     * @requires None
     * @modifies trayIcon
     * @effects set new tool tip text
     */
    void updateToolTip() {
        Deadline closest = parent.getClosestDeadline();
        if (closest == null) {
            trayIcon.setToolTip("main");
        } else {
            trayIcon.setToolTip(closest.getName() + "\n"
                    + "(" + closest.getCourse() + "): \n"
                    + parent.getFrame().getTextFormat().getRemainingText(closest, DateTime.now(), true));
        }
    }
}

package main.viewer.calendarPanel;

import javax.swing.JButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Font;

/**
 * This class is a JButton with white background and hover effect. It is created for
 * the next/prev month and refresh button on the TitleBar.
 */
public class TitleButton extends JButton {
    /**
     * Constructor
     * @param text the text that will appear on the button
     * @param backgroundColor the color of the background
     * @param textColor the color of the text
     * @param hoverColor the color when user's mouse is over the button
     * @requires text != null, textColor != null, hoverColor != null
     * @modifies this
     * @effects create an instance of TitleButton
     */
    public TitleButton(String text, Color backgroundColor, Color textColor, Color hoverColor) {
        super(text);
        LookAndFeel oldTheme = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException |
                IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        setBackground(backgroundColor);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(textColor);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(backgroundColor);
            }
        });
        try {
            UIManager.setLookAndFeel(oldTheme);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}

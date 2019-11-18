package main.viewer.util;

import main.controller.GUIController;
import main.viewer.calendarPanel.TitleButton;
import main.viewer.mainFactory;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * This is the loading dialog when the program tries to connect to submitty
 */
public class LoadingDialog extends JDialog {
    private JLabel currProgressLabel;

    /**
     * Constructor
     * @param parent the GUIController main part
     * @requires parent != null
     * @modifies currProgressLabel
     * @effects create a new LoadingDialog instance
     */
    public LoadingDialog(GUIController parent) {
        super();

        // progress
        this.currProgressLabel = new JLabel("Accessing Submitty...");
        this.currProgressLabel.setOpaque(false);

        String loadingText = parent.getFrame().getText("submitty_loading");

        // create waiting panel and loading animation
        JPanel loadingPanel = new JPanel();
        loadingPanel.setLayout(new BorderLayout());
        loadingPanel.setBackground(Color.WHITE);
        loadingPanel.setMinimumSize(new Dimension(300,200));
        loadingPanel.setPreferredSize(new Dimension(300,200));
        loadingPanel.setBorder(BorderFactory.createLineBorder(parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), 2));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(2,1,0,5));

        ImageIcon loadingPacman = null;
        URL iconPath = getClass().getResource("/images/ajax-loader_2.gif");
        if (iconPath != null) {
            loadingPacman = new ImageIcon(iconPath);
        }

        ImageIcon loadingProgress = null;
        URL iconPath2 = getClass().getResource("/images/ajax-loader_3.gif");
        if (iconPath2 != null) {
            loadingProgress = new ImageIcon(iconPath2);
        }

        JLabel loadingLabel;
        if (loadingPacman != null) {
            loadingLabel = new JLabel("  " + loadingText + "  ", loadingPacman, JLabel.CENTER);
        } else {
            loadingLabel = new JLabel("  " + loadingText + "  ");
        }
        labelPanel.setPreferredSize(new Dimension(250,55));
        labelPanel.add(loadingLabel);
        if (loadingProgress != null) {
            labelPanel.add(new JLabel(loadingProgress));
        }
        loadingPanel.add(labelPanel, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel();
        progressPanel.setPreferredSize(new Dimension(250,55));
        progressPanel.add(currProgressLabel, BorderLayout.CENTER);
        loadingPanel.add(progressPanel, BorderLayout.SOUTH);

        JToolBar title = mainFactory.createToolbar();
        JButton close = new TitleButton("✖",
                parent.getFrame().getTheme().CAL_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_BACKGROUND(),
                parent.getFrame().getTheme().HOVER_OVER_CAL());
        close.setBorder(BorderFactory.createLineBorder(parent.getFrame().getTheme().SIDEBAR_BACKGROUND(),1));
        close.setFocusPainted(false);
        close.setOpaque(false);
        close.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects hide the dialog
             */
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                synchronized (this) {
                    setVisible(false);
                }
            }
        });
        title.add(Box.createHorizontalGlue());
        title.add(close);
        loadingPanel.add(title, BorderLayout.NORTH);

        // create a blocking dialog
        setModal(true);
        setSize(300,200);
        setFocusableWindowState(false);
        setUndecorated(true);
        setContentPane(loadingPanel);
        setLocationRelativeTo(parent.getFrame().getCalendarPanel());
    }

    /**
     * This method makes the dialog visible and block user input to the main main
     * components and this dialog is closed by user or by the accessor
     * @requires None
     * @modifies this
     * @effects show itself
     */
    public void run() {
        this.pack();
        this.setVisible(true);
    }

    /**
     * This function returns the curr progress label so that the accessor that modify
     * the text to show current progress
     * @requires None
     * @modifies None
     * @effects return the currProgressLabel
     * @return this.currProgressLabel
     */
    public JLabel getCurrProgressLabel() {
        return this.currProgressLabel;
    }
}

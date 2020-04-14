package main.viewer.sideBarPanel;

import main.controller.GUIController;
import main.controller.Settings;
import main.viewer.Log;
import main.viewer.DeadlineCountdownFactory;
import main.viewer.textFormat.ViewerFont;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This panel would appear only after user clicks the "settings" button
 */
class SettingsPanel extends JPanel {
    private final GUIController parent;
    private HashMap<String, JCheckBox> allCheckBoxes;

    /**
     * Constructor
     * @param parent the GUIController main component
     * @requires parent != null
     * @modifies this, parent, allCourseName, constraint, courseNameBox
     *           deadlineNameField, yearField, monthField, dayField, minuteField,
     *           hourField
     * @effects create an instance of addNewDeadlinePanel
     */
    SettingsPanel(GUIController parent) {
        this.parent = parent;
        this.setBackground(parent.getFrame().getTheme().SIDEBAR_BACKGROUND());
        this.setBounds(0,0,0,0);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH-5, Integer.MAX_VALUE));
        this.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        // create checkboxes
        this.allCheckBoxes = new HashMap<>();
        JPanel checkBoxPanels = new JPanel();
        checkBoxPanels.setBounds(0,0,0,0);
        checkBoxPanels.setBorder(BorderFactory.createEmptyBorder());
        checkBoxPanels.setLayout(new BoxLayout(checkBoxPanels, BoxLayout.Y_AXIS));
        checkBoxPanels.setOpaque(false);
        checkBoxPanels.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        for (String key: Settings.SUPPORTED.ALL_SETTINGS) {
            JCheckBox settingsBox;
            settingsBox = SideBarFactory.createCheckBox(parent.getFrame().getText(key),
                    parent.getFrame().getTheme().SIDEBAR_TEXT());
            settingsBox.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 17));
            settingsBox.addItemListener(e -> {
                parent.setSettings(key, (e.getStateChange() == ItemEvent.SELECTED));
                Log.debug("DEBUG: [SettingsPanel] " +
                        key + " set as " + (e.getStateChange() == ItemEvent.SELECTED));
                if (key.startsWith("show")) {
                    parent.refresh();
                } else if (key.equals(Settings.SUPPORTED.START_FROM_SUNDAY)) {
                    parent.getFrame().refreshCalendar();
                } else if (key.equals(Settings.SUPPORTED.NOTIFICATION_ENABLED)) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        Log.debug("DEBUG: [SettingsPanel] All timers resumed");
                    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        Log.debug("DEBUG: [SettingsPanel] All timers stopped");
                    }
                    parent.setTimerStatus(e.getStateChange() == ItemEvent.SELECTED);
                }
            });
            checkBoxPanels.add(settingsBox);
            this.allCheckBoxes.put(key, settingsBox);
        }
        this.add(checkBoxPanels);

        // language combo box
        JComboBox<String> languageSupport = SideBarFactory.createComboBox(parent.getFrame().getTheme().SIDEBAR_HOVER(),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        languageSupport.addItem(parent.getFrame().getText("en_US"));
        languageSupport.addItem(parent.getFrame().getText("zh_CN"));
        languageSupport.addItem(parent.getFrame().getText("zh_Hant"));
        languageSupport.setSelectedItem(parent.getFrame().getText(parent.getSettings().getLanguage()));
        languageSupport.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object source = e.getSource();
                if (source instanceof JComboBox) {
                    JComboBox cb = (JComboBox) source;
                    Object selectedItem = cb.getSelectedItem();
                    if (parent.getFrame().getText("en_US").equals(selectedItem)) {
                        parent.setLanguage("en_US",false);
                    } else if (parent.getFrame().getText("zh_CN").equals(selectedItem)) {
                        parent.setLanguage("zh_CN",false);
                    } else if (parent.getFrame().getText("zh_Hant").equals(selectedItem)) {
                        parent.setLanguage("zh_Hant",false);
                    } else {
                        Log.debug("DEBUG: [Language support box] selected " + selectedItem);
                    }
                    parent.restart();
                    parent.getFrame().getSideBar().showPanel(SideBarPanel.PANEL.SETTINGS_PANEL);
                }
            }
        });
        languageSupport.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,30));
        languageSupport.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,30));
        languageSupport.setBounds(0,0,SideBarPanel.SIDEBAR_WIDTH,30);

        JPanel languagePanel = new JPanel();
        languagePanel.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,35));
        languagePanel.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH*2,35));
        languagePanel.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,35));
        languagePanel.setBorder(BorderFactory.createEmptyBorder());
        languagePanel.setLayout(null);
        languagePanel.setOpaque(false);
        languagePanel.add(languageSupport);
        this.add(languagePanel);

        // load and save buttons
        this.add(Box.createVerticalStrut(3));
        this.add(SideBarFactory.createLineSeparator(parent.getFrame().getTheme().SIDEBAR_TEXT()));
        this.add(Box.createVerticalStrut(3));
        JButton version = DeadlineCountdownFactory.createSimpleButton(parent.getFrame().getText("version_settings"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        version.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 20));
        version.addMouseListener(DeadlineCountdownFactory.getHoverEffect(version, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        version.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let the side bar show the theme panel
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                parent.notification("Welcome to Deadline Countdown.","You are running version " + GUIController.VERSION,"");
            }
        });
        JButton theme = DeadlineCountdownFactory.createSimpleButton(parent.getFrame().getText("theme_settings"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        theme.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 20));
        theme.addMouseListener(DeadlineCountdownFactory.getHoverEffect(theme, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        theme.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let the side bar show the theme panel
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                parent.getFrame().getSideBar().showPanel(SideBarPanel.PANEL.THEME_PANEL);
            }
        });
        JButton save = DeadlineCountdownFactory.createSimpleButton(parent.getFrame().getText("save_settings"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        save.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 20));
        save.addMouseListener(DeadlineCountdownFactory.getHoverEffect(save, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        save.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let main save settings to a local file
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                parent.saveSettings(true);
            }
        });
        JButton load = DeadlineCountdownFactory.createSimpleButton(parent.getFrame().getText("load_settings"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        load.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 20));
        load.addMouseListener(DeadlineCountdownFactory.getHoverEffect(load, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        load.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let main load settings from a local file
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                parent.loadSettings(true);
            }
        });
        JButton clear = DeadlineCountdownFactory.createSimpleButton(parent.getFrame().getText("clear_settings"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        clear.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 20));
        clear.addMouseListener(DeadlineCountdownFactory.getHoverEffect(clear, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        clear.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let main clear local settings files
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                File file = new File("settings.ini");
                int reply = JOptionPane.showConfirmDialog(null,
                        parent.getFrame().getText("clear_settings_message"),
                        parent.getFrame().getText("clear_settings_title"),
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    file.delete();
                    parent.setTheme("default_theme", new Color(34,34,34));
                    parent.resetSettings();
                    parent.saveSettings(false);
                    parent.restart();
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0,0,0,0);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        this.add(Box.createVerticalStrut(4));
        buttonPanel.add(version);
        buttonPanel.add(theme);
        buttonPanel.add(save);
        this.add(Box.createVerticalStrut(4));
        buttonPanel.add(load);
        buttonPanel.add(clear);
        this.add(buttonPanel);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Repaints this component and setting the status of the checkboxes
     * @requires None
     * @modifies allCheckBoxes
     * @effects None
     */
    @Override
    public void repaint() {
        super.repaint();
        if (parent == null || this.allCheckBoxes == null) return;
        for (Map.Entry<String, Boolean> item : parent.getSettings().getAllSettings().entrySet()) {
            JCheckBox settingsBox = this.allCheckBoxes.get(item.getKey());
            if (settingsBox == null) continue;
            settingsBox.setSelected(item.getValue());
        }
    }
}

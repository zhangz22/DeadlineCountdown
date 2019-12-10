package main.viewer.sideBarPanel;

import com.bric.colorpicker.ColorPicker;
import main.controller.GUIController;
import main.viewer.ViewerPanelFactory;
import main.viewer.textFormat.ViewerFont;
import main.viewer.theme.DefaultTheme;
import main.viewer.theme.Theme;
import main.viewer.theme.ThemeFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemePanel extends JPanel {
    private Theme theme;
    private String selected;

    /**
     * Constructor
     * @param parent the GUIController main component
     * @requires parent != null
     * @modifies this, parent
     * @effects create an instance of ThemePanel
     */
    ThemePanel(GUIController parent) {
        this.setBackground(parent.getFrame().getTheme().SIDEBAR_BACKGROUND());
        this.setBounds(0,0,0,0);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH-5, Integer.MAX_VALUE));
        this.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        LookAndFeel oldJavaTheme = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException |
                IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        try {
            this.theme = parent.getFrame().getTheme().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            this.theme = new DefaultTheme();
        }
        this.selected = parent.getSettings().getTheme();

        Object oldFont = UIManager.get("Label.font");
        Object oldLabelColor = UIManager.get("Label.foreground");
        UIManager.put("Label.font", new Font(ViewerFont.XHEI, Font.PLAIN, 12));
        UIManager.put("Label.foreground", this.theme.SIDEBAR_TEXT());

        ColorPicker colorPicker = new ColorPicker();

        UIManager.put("Label.font", oldFont);
        UIManager.put("Label.foreground", oldLabelColor);

        // language combo box
        JComboBox<String> chooseTheme = SideBarFactory.createComboBox(parent.getFrame().getTheme().SIDEBAR_HOVER(),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        for (String currTheme: ThemeFactory.ALL_THEME) {
            chooseTheme.addItem(parent.getFrame().getText(currTheme));
        }
        chooseTheme.setSelectedItem(parent.getFrame().getText(parent.getSettings().getTheme()));
        chooseTheme.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object source = e.getSource();
                if (source instanceof JComboBox) {
                    JComboBox cb = (JComboBox) source;
                    Object selectedItem = cb.getSelectedItem();
                    for (String currTheme: ThemeFactory.ALL_THEME) {
                        if (parent.getFrame().getText(currTheme).equals(selectedItem)) {
                            this.theme = ThemeFactory.createTheme(currTheme);
                            this.selected = currTheme;
                            break;
                        }
                    }
                    colorPicker.setRGB(this.theme.SIDEBAR_BACKGROUND().getRed(),
                            this.theme.SIDEBAR_BACKGROUND().getGreen(),
                            this.theme.SIDEBAR_BACKGROUND().getBlue());
                }
            }
        });
        chooseTheme.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,30));
        chooseTheme.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,30));
        chooseTheme.setBounds(0,0,SideBarPanel.SIDEBAR_WIDTH,30);

        JPanel themePanel = new JPanel();
        themePanel.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,35));
        themePanel.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH*2,35));
        themePanel.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,35));
        themePanel.setBorder(BorderFactory.createEmptyBorder());
        themePanel.setLayout(null);
        themePanel.setOpaque(false);
        themePanel.add(chooseTheme);
        this.add(themePanel);
        this.add(Box.createHorizontalStrut(2));

        JLabel colorPickerLabel = SideBarFactory.createSimpleLabel(parent.getFrame().getText("color_picker_label"),
                18, parent.getFrame().getTheme().SIDEBAR_TEXT());
        colorPickerLabel.setBounds(0,0,SideBarPanel.SIDEBAR_WIDTH,18);

        JPanel labelPanel = new JPanel();
        labelPanel.setSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,19));
        labelPanel.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH*2,19));
        labelPanel.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH,19));
        labelPanel.setBorder(BorderFactory.createEmptyBorder());
        labelPanel.setLayout(null);
        labelPanel.setOpaque(false);
        labelPanel.add(colorPickerLabel);
        this.add(Box.createVerticalStrut(2));
        this.add(labelPanel);

        // color picker
        colorPicker.setRGB(this.theme.SIDEBAR_BACKGROUND().getRed(),
                this.theme.SIDEBAR_BACKGROUND().getGreen(),
                this.theme.SIDEBAR_BACKGROUND().getBlue());
        this.add(colorPicker);

        JButton save = ViewerPanelFactory.createSimpleButton(parent.getFrame().getText("save_settings"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        save.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 20));
        save.addMouseListener(ViewerPanelFactory.getHoverEffect(save, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        save.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects let oneSecond save theme and settings to a local file
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                theme.set(DefaultTheme.SIDE_BAR_THEME, Theme.SIDEBAR_BACKGROUND,
                        new Color(colorPicker.getRGB()[0], colorPicker.getRGB()[1], colorPicker.getRGB()[2]));
                parent.setTheme(selected, theme.SIDEBAR_BACKGROUND());
                parent.saveSettings(false);
                parent.restart();

            }
        });
        JButton cancel = ViewerPanelFactory.createSimpleButton(parent.getFrame().getText("cancel"),
                parent.getFrame().getTheme().SIDEBAR_BACKGROUND(), parent.getFrame().getTheme().SIDEBAR_TEXT());
        cancel.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 20));
        cancel.addMouseListener(ViewerPanelFactory.getHoverEffect(cancel, parent.getFrame().getTheme().SIDEBAR_HOVER()));
        cancel.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects discard theme panel changes and let the side bar show the settins panel
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                parent.getFrame().getSideBar().showPanel(SideBarPanel.PANEL.SETTINGS_PANEL);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0,0,0,0);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(save);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancel);
        this.add(Box.createVerticalStrut(3));
        this.add(SideBarFactory.createLineSeparator(parent.getFrame().getTheme().SIDEBAR_TEXT()));
        this.add(Box.createVerticalStrut(2));
        this.add(buttonPanel);

        // restore Java theme
        try {
            UIManager.setLookAndFeel(oldJavaTheme);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}

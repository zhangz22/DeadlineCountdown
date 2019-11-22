package main.viewer.util;

import main.controller.GUIController;
import main.viewer.DeadlineCountdownFactory;
import main.viewer.textFormat.ViewerFont;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * This component is created represent the login dialog of main
 */
public class LoginDialog extends JDialog {
    private GridBagConstraints constraint;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String id;
    private String password;
    private JPanel mainPanel;

    /**
     * Constructor
     * @param parent the main main component
     * @param message the additional message
     * @param lastID the last saved id
     * @requires None
     * @modifies this, constraint, usernameField, passwordField, id, password, mainPanel
     * @effects create a new LoginDialog
     */
    public LoginDialog(GUIController parent, String message, String lastID) {
        super(parent.getFrame(), "", true);

        // initialize the variables
        this.id = null;
        this.password = null;
        this.mainPanel = new JPanel(new GridBagLayout());
        this.constraint = new GridBagConstraints();
        this.constraint.fill = GridBagConstraints.HORIZONTAL;

        // A message that tells the user what to do
        JTextArea hint = new JTextArea(parent.getFrame().getText("login_label"));
        hint.setBackground(null);
        hint.setOpaque(false);
        hint.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 16));
        hint.setForeground(new Color(102,102,102));
        hint.setEnabled(false);
        hint.setDisabledTextColor(Color.BLACK);
        hint.setMargin(new Insets(3, 0, 3, 0));
        this.add(hint, 0,0,3);

        // gap between the id part and the username part
        JPanel sep = DeadlineCountdownFactory.createEmptyArea(4, true);
        this.add(sep, 0,1,3);

        // ask for username
        JLabel usernameLabel = new JLabel(parent.getFrame().getText("username") +  ": ");
        usernameLabel.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 13));
        this.add(usernameLabel, 0, 2,1);

        this.usernameField = new JTextField(20);
        if (lastID != null) {
            this.usernameField.setText(lastID);
        }
        this.add(this.usernameField, 1,2,3);

        JPanel sep2 = DeadlineCountdownFactory.createEmptyArea(4, true);
        this.add(sep2, 0,3,3);

        // ask for password
        JLabel passwordLabel = new JLabel(parent.getFrame().getText("password") +  ": ");
        passwordLabel.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 13));
        this.add(passwordLabel, 0,4,1);

        this.passwordField = new JPasswordField(20);
        this.add(this.passwordField, 1,4,2);
        this.mainPanel.setBorder(new LineBorder(Color.GRAY));

        // additional message
        if (!message.equals("")) {
            JTextArea messageLabel = new JTextArea(message);
            messageLabel.setBackground(null);
            messageLabel.setOpaque(false);
            messageLabel.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 16));
            messageLabel.setForeground(new Color(102, 102, 102));
            messageLabel.setEnabled(false);
            messageLabel.setDisabledTextColor(Color.RED);
            messageLabel.setMargin(new Insets(3, 0, 3, 0));
            this.add(messageLabel, 0,5,3);
        }

        // Buttons
        JButton confirmBtn = new JButton(parent.getFrame().getText("login"));
        confirmBtn.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 14));
        confirmBtn.addActionListener(e -> {
            id = usernameField.getText().trim();
            password = new String(passwordField.getPassword());
            dispose();
        });

        JButton cancelBtn= new JButton(parent.getFrame().getText("cancel"));
        cancelBtn.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 14));
        cancelBtn.addActionListener(e -> {
            id = null;
            password = null;
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);

        this.getContentPane().add(this.mainPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);

        // Press Enter to confirm
        KeyAdapter enter = new KeyAdapter() {
            /**
             * Invoked when a key has been pressed.
             * @param e the key event
             * @requires None
             * @modifies confirmBtn
             * @effects clicks the confirm button
             */
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmBtn.doClick();
                }
            }
        };

        SwingUtilities.invokeLater(() -> addKeyListener(enter));
        usernameField.addKeyListener(enter);
        passwordField.addKeyListener(enter);

        // set visible
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(parent.getFrame());
    }

    /**
     * This function adds a component to the login dialog mainPanel's GridBagLayout
     * @param component the component being added
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the component
     * @requires component != null
     * @modifies constraint, mainPanel
     * @effects add a component to mainPanel
     */
    private void add(JComponent component, int x, int y, int width) {
        this.constraint.gridx = x;
        this.constraint.gridy = y;
        this.constraint.gridwidth = width;
        this.mainPanel.add(component, this.constraint);
    }

    /**
     * This function returns the username from user's input
     * @requires None
     * @modifies None
     * @effects None
     * @return the username
     */
    public String getUsername() {
        return id;
    }

    /**
     * This function returns the username from user's input
     * @requires None
     * @modifies None
     * @effects None
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
package main.viewer.calendarPanel;

import model.CalendarWrapper;
import main.viewer.textFormat.ViewerFont;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This component is created for representing each date in the calendar
 */
class DateBlock extends RectangleWithTitle {
    private final CalendarPanel parent;
    private JTextArea dateTextPanel;
    private ConcurrentHashMap<String, DeadlineBlock> allDeadlinkblocks;
    private boolean[] isMutexLock = {false};
    private int year;
    private int month;
    private int day;

    /**
     * Constructor
     * @param year a integer to represent the year
     * @param month a integer to represent the month
     * @param day a integer to represent the day
     * @param date a String representing the date
     * @param parent the calendarPanel main component
     * @requires None
     * @modifies this, dateTextPanel, super.upperPart
     * @effects create a DateBlock
     */
    DateBlock(int year, int month, int day, String date, CalendarPanel parent) {
        super(new JPanel(), new JPanel());
        this.getUpperPart().setBackground(parent.getTheme().CAL_BACKGROUND());
        this.getLowerPart().setBackground(parent.getTheme().CAL_BACKGROUND());
        this.parent = parent;
        this.dateTextPanel = new JTextArea(" " + date);
        this.dateTextPanel.setEnabled(false);
        this.dateTextPanel.setBackground(parent.getTheme().CAL_BACKGROUND());
        this.dateTextPanel.setDisabledTextColor(parent.getTheme().CAL_DATE_TEXT());
        this.dateTextPanel.setFont(new Font(ViewerFont.XHEI, Font.BOLD, 22));
        this.dateTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        super.getUpperPart().setLayout(new BorderLayout());
        super.getUpperPart().add(dateTextPanel, BorderLayout.WEST);

        // assign the dates
        this.year = year;
        this.month = month;
        this.day = day;

        // lower part
        this.allDeadlinkblocks = new ConcurrentHashMap<>();
        super.getLowerPart().setLayout(new BoxLayout(super.getLowerPart(), BoxLayout.Y_AXIS));
        super.getLowerPart().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));

        // create popup menu
        LookAndFeel oldTheme = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException |
                IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        JPopupMenu addDeadlineMenu = new JPopupMenu();
        JMenuItem addNewDeadline = new JMenuItem("Add new deadline");
        addNewDeadline.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_D) {
                    parent.handleEditSignal("","",0,0,Integer.valueOf(date),0,0,null);
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });
        addNewDeadline.setForeground(parent.getTheme().RIGHT_MENU_BACKGROUND());
        addNewDeadline.setBackground(parent.getTheme().RIGHT_MENU_BACKGROUND());
        addNewDeadline.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 14));
        addNewDeadline.addActionListener(e2 -> parent.handleEditSignal("","",0,0,Integer.valueOf(date),0,0,null));
        addDeadlineMenu.add(addNewDeadline);
        addDeadlineMenu.setForeground(parent.getTheme().SIDEBAR_TEXT());
        addDeadlineMenu.setBackground(parent.getTheme().SIDEBAR_BACKGROUND());
        addDeadlineMenu.setBorder(BorderFactory.createLineBorder(parent.getTheme().RIGHT_MENU_BACKGROUND(), 1));

        try {
            UIManager.setLookAndFeel(oldTheme);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // add mouse listener to show the popup menu
        super.getLowerPart().setComponentPopupMenu(addDeadlineMenu);
        super.getLowerPart().addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been pressed on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects show the popup menu
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    showPopupMenu(e);
            }

            /**
             * {@inheritDoc}
             * Invoked when a mouse button has been release on a component.
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects show the popup menu
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    showPopupMenu(e);
            }

            /**
             * This function would show the popup right-click menu
             * @param e the mouse event
             * @requires None
             * @modifies None
             * @effects show the popup menu
             */
            private void showPopupMenu(MouseEvent e) {
                addDeadlineMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    /**
     * This function would return the dateTextPanel
     * @return this.dateTextPanel
     * @requires None
     * @modifies None
     * @effects None
     */
    JTextArea getDateTextPanel() {
        return this.dateTextPanel;
    }

    /**
     * This function would set the background of this component, both the title bar
     * and the center panel
     * @param color the required background color
     * @requires None
     * @modifies upperPart, lowerPart
     * @effects None
     */
    @Override
    public synchronized void setColor(Color color) {
        super.setColor(color);
        this.dateTextPanel.setBackground(color);
    }

    /**
     * This function checks if the date the block is representing is the date for the day
     * which the user currently is in.
     * @requires None
     * @modifies None
     * @effects None
     * @return True if the date == today.date
     */
    private boolean isToday() {
        return this.year == CalendarWrapper.now().getYear()
                && this.month == CalendarWrapper.now().getMonth()
                && this.day == CalendarWrapper.now().getDay();
    }

    /**
     * This function returns the color of the current date block from the current theme
     * @requires None
     * @modifies None
     * @effects None
     * @return the background color
     */
    private Color getThemeBackgroundColor() {
        if (this.isToday()) {
            return parent.getTheme().TODAY();
        }
        return parent.getTheme().CAL_BACKGROUND();
    }

    /**
     * This function will highlight current deadline block by changing the color of
     * current block and then changing it back
     * @requires None
     * @modifies None
     * @effects highlight current block
     */
    void highlight() {
        synchronized(this) {
            Runnable fade = new Runnable() {
                @Override
                public void run() {
                    Color old = getThemeBackgroundColor();
                    Color highlight = parent.getTheme().HIGHLIGHT();
                    setColor(highlight);
                    final int[] rgb = {highlight.getRed(), highlight.getGreen(), highlight.getBlue()};
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // the differential: positive value means the block is turning white to normal
                    int dr = (old.getRed() - highlight.getRed()) / 17;
                    int dg = (old.getGreen() - highlight.getGreen()) / 17;
                    int db = (old.getBlue() - highlight.getBlue()) / 17;
                    synchronized (this) {
                        if (!isMutexLock[0]) {
                            isMutexLock[0] = true;
                            Timer t = new Timer(20, e -> {
                                boolean move = false;
                                if ((dr > 0 && rgb[0] < old.getRed()) || (dr < 0 && rgb[0] > old.getRed())) {
                                    rgb[0] += dr;
                                    move = true;
                                }
                                if ((dg > 0 && rgb[1] < old.getGreen()) || (dg < 0 && rgb[1] > old.getGreen())) {
                                    rgb[1] += dg;
                                    move = true;
                                }
                                if ((db > 0 && rgb[2] < old.getBlue()) || (db < 0 && rgb[2] > old.getBlue())) {
                                    rgb[2] += db;
                                    move = true;
                                }
                                if (dr > 0 && rgb[0] > old.getRed())
                                    rgb[0] = old.getRed();
                                else if (dr < 0 && rgb[0] < old.getRed())
                                    rgb[0] = old.getRed();
                                if (dg > 0 && rgb[1] > old.getGreen())
                                    rgb[1] = old.getGreen();
                                else if (dr < 0 && rgb[1] < old.getGreen())
                                    rgb[1] = old.getGreen();
                                if (db > 0 && rgb[2] > old.getBlue())
                                    rgb[2] = old.getBlue();
                                else if (dr < 0 && rgb[2] < old.getBlue())
                                    rgb[2] = old.getBlue();
                                setColor(new Color(rgb[0], rgb[1], rgb[2]));
                                if ((rgb[0] == 255 && rgb[1] == 255 && rgb[2] == 255) ||
                                        (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0) ||
                                        (rgb[0] == old.getRed() && rgb[1] == old.getGreen() && rgb[2] == old.getBlue()) ||
                                        !move) {
                                    ((Timer) e.getSource()).stop();
                                    setColor(old);
                                    isMutexLock[0] = false;
                                }
                            });
                            t.start();
                        }
                    }
                }
            };
            Thread thread = new Thread(fade);
            thread.start();
        }
    }

    /**
     * This function would add a new deadline block
     * @param block the new deadline block
     * @requires block != null
     * @modifies this.allDeadlinkblocks, super.lowerPart
     * @effects add a new deadline block
     */
    public void addDeadline(DeadlineBlock block) {
        if (this.allDeadlinkblocks.containsKey(block.getCourseName() + block.getDeadlineName())) {
            this.removeDeadline(block);
        }
        this.allDeadlinkblocks.put(block.getCourseName() + block.getDeadlineName(),  block);
        this.getLowerPart().add(block);
    }

    /**
     * This function would remove a new deadline block
     * @param block the new deadline block
     * @requires block != null
     * @modifies this.allDeadlinkblocks, super.lowerPart
     * @effects remove a new deadline block
     */
    public void removeDeadline(DeadlineBlock block) {
        this.allDeadlinkblocks.remove(block.getCourseName() + block.getDeadlineName());
        Component[] components = this.getLowerPart().getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof DeadlineBlock) {
                DeadlineBlock d = (DeadlineBlock) components[i];
                if (d.getCourseName().equals(block.getCourseName()) && d.getDeadlineName().equals(block.getDeadlineName())) {
                    this.getLowerPart().remove(d);
                    if (i < components.length - 1 && components[i + 1] instanceof Box.Filler) {
                        this.getLowerPart().remove(components[i + 1]);
                    }
                }
            }
        }
    }
}

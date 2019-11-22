package main.viewer.sideBarPanel;

import model.CalendarWrapper;
import model.Deadline;
import main.viewer.DeadlineBlockInterface;
import main.viewer.DeadlineCountdownFactory;
import main.viewer.textFormat.ViewerFont;
import main.viewer.util.DeadlineExporter;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * This component is created to represent a single deadline in the summary part of
 * the side bar. Each DeadlineInfoBlock contains three parts: the deadline name,
 * an empty area and the remaining time information
 */
class DeadlineInfoBlock extends JPanel implements DeadlineBlockInterface {
    private final SideBarPanel parent;
    private JTextArea remainTime;
    private String blockName;
    private Deadline deadline;

    /**
     * Constructor
     * @param parent the sideBarPanel main component
     * @param name the course name with the deadline name, which would appear as the
     *             title of DeadlineInfoBlock
     * @param d the deadline object
     * @requires parent != null, d != null,
     * @modifies this, remainTime, blockName, parent, deadline
     * @effects create an instance of DeadlineInfoBlock
     */
    DeadlineInfoBlock(SideBarPanel parent, String name, Deadline d) {
        super();
        this.deadline = d;
        this.setLayout(new BorderLayout());
        this.setBackground(null);
        this.setOpaque(false);
        this.addMouseListener(DeadlineCountdownFactory.getHoverEffect(this, parent.getTheme().SIDEBAR_HOVER()));
        this.parent = parent;
        this.blockName = name;

        // an area to display the deadline name
        JTextArea deadlineName = SideBarFactory.createSimpleTextArea(name, null,
                parent.getTheme().SIDEBAR_TEXT());
        deadlineName.setAlignmentX(Component.LEFT_ALIGNMENT);
        deadlineName.setFont(new Font(ViewerFont.XHEI, Font.BOLD, 18));
        deadlineName.setMargin(new Insets(2, 5, 2, 5));
        deadlineName.setLineWrap(true);
        deadlineName.setWrapStyleWord(true);
        deadlineName.addMouseListener(DeadlineCountdownFactory.getHoverEffect(this,
                parent.getTheme().SIDEBAR_HOVER()));
        this.add(deadlineName, BorderLayout.NORTH);

        // an area to display the remaining time
        remainTime = SideBarFactory.createSimpleTextArea(
                parent.getMainmainGUI().getFrame().getTextFormat().getRemainingText(deadline, null, true),
                null, parent.getTheme().SIDEBAR_TEXT());
        remainTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        remainTime.setFont(new Font(ViewerFont.XHEI, Font.PLAIN, 18));
        remainTime.setMargin(new Insets(2, 5, 2, 5));
        remainTime.setLineWrap(true);
        remainTime.setWrapStyleWord(true);
        remainTime.addMouseListener(DeadlineCountdownFactory.getHoverEffect(this,
                parent.getTheme().SIDEBAR_HOVER()));
        this.add(remainTime, BorderLayout.SOUTH);

        JPanel middleBlank = DeadlineCountdownFactory.createEmptyArea(2, true);
        middleBlank.addMouseListener(DeadlineCountdownFactory.getHoverEffect(this,
                parent.getTheme().SIDEBAR_HOVER()));

        this.setMaximumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH - 20,
                (getWrappedLines(deadlineName) + getWrappedLines(remainTime)) * 27));
        this.setMinimumSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH - 20,
                (getWrappedLines(deadlineName) + getWrappedLines(remainTime)) * 27));
        this.setPreferredSize(new Dimension(SideBarPanel.SIDEBAR_WIDTH - 20,
                (getWrappedLines(deadlineName) + getWrappedLines(remainTime)) * 27));

        // add the right-click menu
        JPopupMenu menu = DeadlineCountdownFactory.createDeadlineBlockRightMenu(this,
                parent.getMainmainGUI().getFrame().getTheme().SIDEBAR_TEXT(),
                parent.getMainmainGUI().getFrame().getTheme().SIDEBAR_BACKGROUND());
        this.setComponentPopupMenu(menu);
        deadlineName.setComponentPopupMenu(menu);
        this.remainTime.setComponentPopupMenu(menu);
        middleBlank.setComponentPopupMenu(menu);
        this.addMouseListener(DeadlineCountdownFactory.createRightClickMenuAction(menu));
        deadlineName.addMouseListener(DeadlineCountdownFactory.createRightClickMenuAction(menu));
        this.remainTime.addMouseListener(DeadlineCountdownFactory.createRightClickMenuAction(menu));
        middleBlank.addMouseListener(DeadlineCountdownFactory.createRightClickMenuAction(menu));

        // click to jump
        MouseAdapter jumpToDate = new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    parent.getMainmainGUI().getFrame().highlightDate(deadline.getMonth(), deadline.getYear(), deadline.getDay());
                }
            }
        };
        this.addMouseListener(jumpToDate);
        this.remainTime.addMouseListener(jumpToDate);
        deadlineName.addMouseListener(jumpToDate);
        middleBlank.addMouseListener(jumpToDate);

        if (!deadline.isAfter(CalendarWrapper.now())) {
            deadlineName.setDisabledTextColor(new Color(0x6F6F6F));
            remainTime.setDisabledTextColor(new Color(0x6F6F6F));
        }
    }

    /**
     * This function would return the blockName
     * @requires None
     * @modifies None
     * @effects None
     * @return blockName
     */
    String getBlockName() {
        return this.blockName;
    }

    /**
     * This function would update the remaining time text
     * @param remainingText the next remaining time
     * @requires None
     * @modifies this.remainTime
     * @effects update the text
     */
    void updateRemainingText(String remainingText) {
        this.remainTime.setText(remainingText);
    }

    /**
     * This method returns the course name of current deadline
     * @return this.courseName
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public String getCourseName() {
        return this.deadline.getCourseName();
    }

    /**
     * This method returns the current deadline
     *
     * @return this.deadline
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public Deadline getDeadline() {
        return this.deadline;
    }

    /**
     * This method returns the year number of current deadline
     * @return this.year
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getYear() {
        return this.deadline.getYear();
    }

    /**
     * This method returns the month number of current deadline
     * @return this.month
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getMonth() {
        return this.deadline.getMonth();
    }

    /**
     * This method returns the date number of current deadline
     * @return this.date
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public int getDate() {
        return this.deadline.getDay();
    }

    /**
     * This method returns the deadline name of current deadline
     * @return this.deadlineName
     * @requires None
     * @modifies None
     * @effects None
     */
    @Override
    public String getDeadlineName() {
        return this.deadline.getName();
    }

    /**
     * This function would delete the deadline that current deadlineInfoBlock is
     * representing
     * @requires None
     * @modifies this.parent
     * @effects delete current deadline
     */
    @Override
    public void delete() {
        this.parent.handleRemoveSignal(this.deadline.getCourseName(),
                this.deadline.getName(),
                this.deadline.getYear(),
                this.deadline.getMonth(),
                this.deadline.getDay());
    }

    /**
     * This function would edit the deadline that current DeadlineBlock is
     * representing
     * @requires None
     * @modifies this.parent
     * @effects edit current deadline
     */
    @Override
    public void edit() {
        this.parent.handleEditSignal(this.deadline);
    }

    /**
     * This function would export the deadline that current DeadlineBlock is
     * representing
     * @requires None
     * @modifies this.parent
     * @effects export current deadline
     */
    @Override
    public void export() {
        DeadlineExporter exporter = new DeadlineExporter(this.deadline, parent.getMainmainGUI(), "DeadlineBlock");
        exporter.export();
    }

    /**
     * This method is created in order to count the actual lines including those
     * cause by auto-wrap since JDK doesn't provide with such function. It counts
     * the total length of characters and compare it with the maximum width of the
     * component.
     * Note: This method is from
     * <url>https://stackoverflow.com/questions/6366776/how-to-count-the-number-of-lines-in-a-jtextarea-including-those-caused-by-wrapp</url>
     * @param textArea the JTextArea
     * @requires None
     * @modifies None
     * @effects None
     * @return the number of physical lines in this JTextArea
     */
    private static int getWrappedLines(JTextArea textArea) {
        AttributedString text = new AttributedString(textArea.getText());
        text.addAttribute(TextAttribute.FONT, textArea.getFont());
        FontRenderContext frc = textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();
        AttributedCharacterIterator charIt = text.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
        float formatWidth = SideBarPanel.SIDEBAR_WIDTH - 30;
        lineMeasurer.setPosition(charIt.getBeginIndex());

        int noLines = 0;
        while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
            lineMeasurer.nextLayout(formatWidth);
            noLines++;
        }
        return noLines;

    }
}

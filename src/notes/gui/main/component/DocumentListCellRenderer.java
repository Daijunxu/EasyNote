package notes.gui.main.component;

import javax.swing.*;
import java.awt.*;

/**
 * Defines the cell renderer for the documents' JList.
 *
 * Author: Rui Du
 */
public class DocumentListCellRenderer extends DefaultListCellRenderer {
    private final String HTML_1 = "<html><body style='width: ";
    private final String HTML_2 = "'>";
    private final String HTML_3 = "</html>";
    private int width;

    /**
     * Constructs an instance of {@code DocumentListCellRenderer}.
     *
     * @param width The width of the cell.
     */
    public DocumentListCellRenderer(int width) {
        this.width = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        String text = HTML_1 + String.valueOf(width) + HTML_2 + value.toString() + HTML_3;
        JLabel label = (JLabel) super.getListCellRendererComponent(list, text, index, isSelected,
                cellHasFocus);
        if (!isSelected) {
            label.setFont(new Font("Times", Font.BOLD, 14));
        } else {
            label.setFont(new Font("Times", Font.BOLD, 14));
        }
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(label);
        return panel;
    }
}
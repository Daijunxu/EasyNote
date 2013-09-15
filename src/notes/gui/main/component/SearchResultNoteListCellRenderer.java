/**
 *
 */
package notes.gui.main.component;

import notes.bean.BookHome;
import notes.book.BookNote;
import notes.entity.Note;

import javax.swing.*;
import java.awt.*;

/**
 * Defines the cell renderer for the notes' JList in note search dialog.
 *
 * @author Rui Du
 * @version 1.0
 */
public class SearchResultNoteListCellRenderer extends DefaultListCellRenderer {
    private final String HTML_1 = "<html><body style='width: ";
    private final String HTML_2 = "'>";
    private final String HTML_3 = "</body></html>";
    private int width;

    /**
     * Creates an instance of {@code SearchResultNoteListCellRenderer}.
     *
     * @param width The width of the cell.
     */
    public SearchResultNoteListCellRenderer(int width) {
        this.width = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        Note note = (Note) value;

        JPanel documentPanel = new JPanel();
        documentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        String documentTitle = BookHome.get().getBookNoteDAO()
                .findDocumentById(note.getDocumentId()).getDocumentTitle();
        JLabel documentLabel = new JLabel();
        if (note instanceof BookNote) {
            documentLabel.setText(documentTitle + " - Chapter " + ((BookNote) note).getChapterId());
        } else {
            documentLabel.setText(documentTitle);
        }
        documentLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        JPanel innerDocumentPanel = new JPanel();
        innerDocumentPanel.setLayout(new BoxLayout(innerDocumentPanel, BoxLayout.PAGE_AXIS));
        innerDocumentPanel.setBackground(Color.WHITE);
        innerDocumentPanel.add(documentLabel);
        documentPanel.add(documentLabel);

        String noteText = note.getNoteText();
        noteText = noteText.replaceAll("&", "&amp;");
        noteText = noteText.replaceAll("<", "&lt;");
        noteText = noteText.replaceAll("\n", "<br/>");
        noteText = noteText.replaceAll("  ", "&emsp;");
        noteText = noteText.replaceAll("\t", "&emsp;&emsp;");
        String text = HTML_1 + String.valueOf(width) + HTML_2 + noteText + HTML_3;
        JLabel noteLabel = (JLabel) super.getListCellRendererComponent(list, text, index,
                isSelected, cellHasFocus);
        noteLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        noteLabel.setAlignmentX(documentPanel.getAlignmentX());

        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (Long tagId : note.getTagIds()) {
            String tagText = BookHome.get().getBookNoteDAO().findTagById(tagId).getTagText();
            JLabel tagLabel = new JLabel(tagText);
            tagLabel.setFont(new Font("Helvetica", Font.PLAIN, 11));
            tagLabel.setForeground(Color.WHITE);
            tagLabel.setBackground(new Color(119, 136, 153));
            tagLabel.setOpaque(true);
            tagLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            JPanel innerTagPanel = new JPanel();
            innerTagPanel.setLayout(new BoxLayout(innerTagPanel, BoxLayout.PAGE_AXIS));
            innerTagPanel.setForeground(new Color(696969));
            innerTagPanel.add(tagLabel);
            tagPanel.add(innerTagPanel);
        }
        tagPanel.setAlignmentX(noteLabel.getAlignmentX());

        documentPanel.setBackground(noteLabel.getBackground());
        tagPanel.setBackground(noteLabel.getBackground());

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));
        innerPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        innerPanel.add(documentPanel);
        innerPanel.add(noteLabel);
        innerPanel.add(tagPanel);

        JPanel outPanel = new JPanel();
        outPanel.setLayout(new BoxLayout(outPanel, BoxLayout.PAGE_AXIS));
        outPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.WHITE));
        outPanel.add(innerPanel);

        if (!isSelected) {
            documentLabel.setFont(new Font("Times", Font.ITALIC, 12));
            documentLabel.setForeground(Color.GRAY);
            noteLabel.setFont(new Font("Times", Font.PLAIN, 14));
        } else {
            documentLabel.setFont(new Font("Times", Font.ITALIC, 12));
            documentLabel.setForeground(Color.WHITE);
            noteLabel.setForeground(Color.RED);
            noteLabel.setFont(new Font("Times", Font.PLAIN, 14));
        }

        return outPanel;
    }
}

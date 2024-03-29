package notes.gui.main.component;

import notes.dao.impl.DocumentNoteDAO;
import notes.businessobjects.Note;

import javax.swing.*;
import java.awt.*;

/**
 * Defines the cell renderer for the notes' JList.
 *
 * Author: Rui Du
 */
public class NoteListCellRenderer extends DefaultListCellRenderer {
    private int width;

    /**
     * Constructs an instance of {@code NoteListCellRenderer}.
     *
     * @param width The width of the cell.
     */
    public NoteListCellRenderer(int width) {
        this.width = width;
    }

    /**
     * Gets the color for the note that is displayed in the note cell.
     *
     * @param note The note.
     * @return {@code Color} The color for the note.
     */
    protected Color getNoteColorOverride(Note note) {
        return null;
    }

    /**
     * Setup the appearance when the renderer is selected.
     *
     * @param renderer The renderer of the note.
     */
    protected void setupWhenSelected(JLabel renderer) {
        renderer.setFont(new Font("Times", Font.PLAIN, 14));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        Note note = (Note) value;
        String noteText = note.getNoteText();
        noteText = noteText.replaceAll("&", "&amp;");
        noteText = noteText.replaceAll("<", "&lt;");
        noteText = noteText.replaceAll("\n", "<br/>");
        noteText = noteText.replaceAll("  ", "&emsp;");
        noteText = noteText.replaceAll("\t", "&emsp;&emsp;");

        final String HTML_1 = "<html><body style='width: ";
        final String HTML_2 = "'>";
        final String HTML_3 = "</body></html>";

        String text = HTML_1 + String.valueOf(width) + HTML_2 + noteText + HTML_3;
        JLabel renderer = (JLabel) super.getListCellRendererComponent(list, text, index,
                isSelected, cellHasFocus);
        renderer.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        if (!isSelected) {
            renderer.setFont(new Font("Times", Font.PLAIN, 14));
        } else {
            setupWhenSelected(renderer);
        }

        Color noteColorOverride = getNoteColorOverride(note);
        if (noteColorOverride != null) {
            renderer.setBackground(noteColorOverride);
        }

        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (Long tagId : note.getTagIds()) {
            String tagText = DocumentNoteDAO.get().findTagById(tagId).getTagText();
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
        tagPanel.setAlignmentX(renderer.getAlignmentX());
        tagPanel.setBackground(renderer.getBackground());

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));
        innerPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        innerPanel.add(renderer);
        innerPanel.add(tagPanel);

        JPanel outPanel = new JPanel();
        outPanel.setLayout(new BoxLayout(outPanel, BoxLayout.PAGE_AXIS));
        outPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.WHITE));
        outPanel.add(innerPanel);

        return outPanel;
    }
}

package notes.gui.workset.component;

import notes.businessobjects.Note;
import notes.businessobjects.workset.WorksheetNote;
import notes.gui.main.component.NoteListCellRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * Defines the cell renderer for worksheet notes' JList.
 * <p/>
 * Author: Rui Du
 * Date: 10/4/13
 * Time: 6:43 AM
 */
public class WorksheetNoteListCellRenderer extends NoteListCellRenderer {

    /**
     * Constructs an instance of {@code WorksheetNoteListCellRenderer}.
     *
     * @param width The width of the cell.
     */
    public WorksheetNoteListCellRenderer(int width) {
        super(width);
    }

    /**
     * {@inheritDoc}
     */
    protected Color getNoteColorOverride(Note note) {
        return ((WorksheetNote) note).getNoteStatus().getNoteColor();
    }

    /**
     * {@inheritDoc}
     */
    protected void setupWhenSelected(JLabel renderer) {
        renderer.setFont(new Font("Times", Font.PLAIN, 14));
        // The color will not change when the cell is selected.
        renderer.setBackground(UIManager.getDefaults().getColor("List.background"));
    }
}

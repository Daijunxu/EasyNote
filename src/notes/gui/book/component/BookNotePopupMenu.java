package notes.gui.book.component;

import notes.bean.BookHome;
import notes.gui.book.event.DeleteBookNoteActionListener;
import notes.gui.book.event.EditBookNoteActionListener;
import notes.gui.book.event.NewBookNoteActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the book note panel.
 *
 * @author Rui Du
 */
public class BookNotePopupMenu extends JPopupMenu {

    private final JMenuItem newItem;
    private final JMenuItem editItem;
    private final JMenuItem deleteItem;

    /**
     * Creates an instance of {@code BookNotePopupMenu}.
     */
    public BookNotePopupMenu() {
        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewBookNoteActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditBookNoteActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteBookNoteActionListener());
        if (BookHome.get().getCurrentBookNote() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        if (BookHome.get().getCurrentChapter() == null) {
            newItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(deleteItem);
    }

}

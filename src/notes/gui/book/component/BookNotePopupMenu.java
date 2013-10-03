package notes.gui.book.component;

import notes.bean.BookHome;
import notes.gui.book.event.DeleteBookNoteActionListener;
import notes.gui.book.event.EditBookNoteActionListener;
import notes.gui.book.event.NewBookNoteActionListener;
import notes.gui.book.event.ViewBookNoteActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the book note panel.
 *
 * @author Rui Du
 */
public class BookNotePopupMenu extends JPopupMenu {

    private final JMenuItem newItem;
    private final JMenuItem viewItem;
    private final JMenuItem editItem;
    private final JMenuItem deleteItem;

    /**
     * Creates an instance of {@code BookNotePopupMenu}.
     */
    public BookNotePopupMenu() {
        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewBookNoteActionListener());
        viewItem = new JMenuItem("View");
        viewItem.addActionListener(new ViewBookNoteActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditBookNoteActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteBookNoteActionListener());
        if (BookHome.get().getCurrentBookNote() == null) {
            viewItem.setEnabled(false);
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        if (BookHome.get().getCurrentChapter() == null) {
            newItem.setEnabled(false);
        }
        add(newItem);
        add(viewItem);
        add(editItem);
        add(deleteItem);
    }

}

/**
 *
 */
package notes.gui.book.component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import notes.bean.BookHome;
import notes.gui.book.event.DeleteBookNoteActionListener;
import notes.gui.book.event.EditBookNoteActionListener;
import notes.gui.book.event.NewBookNoteActionListener;
import notes.gui.book.event.ViewBookNoteActionListener;

/**
 * Pops up when right clicking a note in the book note panel.
 *
 * @author Rui Du
 * @version 1.0
 */
public class BookNotePopupMenu extends JPopupMenu {

    private static final long serialVersionUID = 8528670988811138651L;
    private JMenuItem newItem;
    private JMenuItem viewItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;

    /**
     * Creates an instance of {@code BookNotePopupMenu}.
     */
    public BookNotePopupMenu() {
        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewBookNoteActionListener());
        viewItem = new JMenuItem("Details");
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

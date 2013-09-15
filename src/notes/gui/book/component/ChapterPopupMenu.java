/**
 *
 */
package notes.gui.book.component;

import notes.bean.BookHome;
import notes.gui.book.event.DeleteChapterActionListener;
import notes.gui.book.event.EditChapterActionListener;
import notes.gui.book.event.NewChapterActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a chapter in the chapter panel.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ChapterPopupMenu extends JPopupMenu {

    private JMenuItem newItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;

    /**
     * Creates an instance of {@code ChapterPopupMenu}.
     */
    public ChapterPopupMenu() {
        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewChapterActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditChapterActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteChapterActionListener());
        if (BookHome.get().getCurrentChapter() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(deleteItem);
    }
}

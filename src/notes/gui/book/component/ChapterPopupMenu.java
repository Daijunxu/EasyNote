package notes.gui.book.component;

import notes.businesslogic.BookBusinessLogic;
import notes.gui.book.event.DeleteChapterActionListener;
import notes.gui.book.event.EditChapterActionListener;
import notes.gui.book.event.NewChapterActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a chapter in the chapter panel.
 *
 * Author: Rui Du
 */
public class ChapterPopupMenu extends JPopupMenu {

    private final JMenuItem newItem;
    private final JMenuItem editItem;
    private final JMenuItem deleteItem;

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
        if (BookBusinessLogic.get().getCurrentChapter() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(deleteItem);
    }
}

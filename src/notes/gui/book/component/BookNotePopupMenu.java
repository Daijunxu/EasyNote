package notes.gui.book.component;

import notes.businesslogic.BookBusinessLogic;
import notes.businessobjects.Note;
import notes.gui.book.event.DeleteBookNoteActionListener;
import notes.gui.book.event.EditBookNoteActionListener;
import notes.gui.book.event.NewBookNoteActionListener;
import notes.gui.main.event.note.MoveNoteDownActionListener;
import notes.gui.main.event.note.MoveNoteToBottomActionListener;
import notes.gui.main.event.note.MoveNoteToTopActionListener;
import notes.gui.main.event.note.MoveNoteUpActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the book note panel.
 * <p/>
 * Author: Rui Du
 */
public class BookNotePopupMenu extends JPopupMenu {

    /**
     * Creates an instance of {@code BookNotePopupMenu}.
     */
    public BookNotePopupMenu() {
        BookBusinessLogic logic = BookBusinessLogic.get();

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(new NewBookNoteActionListener());
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditBookNoteActionListener());
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteBookNoteActionListener());

        JMenuItem moveUpItem = new JMenuItem("Move up");
        moveUpItem.addActionListener(new MoveNoteUpActionListener());
        JMenuItem moveDownItem = new JMenuItem("Move down");
        moveDownItem.addActionListener(new MoveNoteDownActionListener());
        JMenuItem moveToTopItem = new JMenuItem("Move to top");
        moveToTopItem.addActionListener(new MoveNoteToTopActionListener());
        JMenuItem moveToBottomItem = new JMenuItem("Move to bottom");
        moveToBottomItem.addActionListener(new MoveNoteToBottomActionListener());

        if (BookBusinessLogic.get().getCurrentChapter() == null) {
            newItem.setEnabled(false);
        }

        Note currentNote = BookBusinessLogic.get().getCurrentNote();
        if (currentNote == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }

        if (currentNote == null || !logic.canMoveCurrentNoteUp()) {
            moveUpItem.setEnabled(false);
            moveToTopItem.setEnabled(false);
        }

        if (currentNote == null || !logic.canMoveCurrentNoteDown()) {
            moveDownItem.setEnabled(false);
            moveToBottomItem.setEnabled(false);
        }

        add(newItem);
        add(editItem);
        add(deleteItem);
        addSeparator();
        add(moveUpItem);
        add(moveDownItem);
        add(moveToTopItem);
        add(moveToBottomItem);
    }

}

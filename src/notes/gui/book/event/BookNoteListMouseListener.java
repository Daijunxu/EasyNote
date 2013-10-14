package notes.gui.book.event;

import notes.bean.BookHome;
import notes.gui.book.component.BookNotePopupMenu;
import notes.gui.book.component.EditBookNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for notes' JList in book panel.
 *
 * @author Rui Du
 */
public class BookNoteListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event) {
        BookNotePopupMenu menu = new BookNotePopupMenu();
        menu.show(event.getComponent(), event.getX(), event.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        BookHome home = BookHome.get();
        if (event.getClickCount() == 2 && home.getCurrentBook() != null
                && home.getCurrentChapter() != null
                && home.getCurrentBookNote() != null) {
            SoundFactory.playPopup();
            new EditBookNoteDialog(home.getCurrentBook(), home.getCurrentChapter(), home.getCurrentBookNote());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            JList noteList = (JList) event.getSource();
            int row = noteList.locationToIndex(event.getPoint());
            noteList.setSelectedIndex(row);
            SoundFactory.playNavigation();
            doPop(event);
        }
    }

}
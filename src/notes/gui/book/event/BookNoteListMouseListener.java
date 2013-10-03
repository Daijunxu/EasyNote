package notes.gui.book.event;

import notes.bean.BookHome;
import notes.data.cache.Property;
import notes.gui.book.component.BookNotePopupMenu;
import notes.gui.book.component.ViewBookNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

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
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }
            new ViewBookNoteDialog(home.getCurrentBook(), home.getCurrentChapter(), home.getCurrentBookNote());
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
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            doPop(event);
        }
    }

}
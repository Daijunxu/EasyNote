package notes.gui.book.event;

import notes.data.cache.Property;
import notes.gui.book.component.ChapterPopupMenu;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for chapters' list in book panel.
 *
 * @author Rui Du
 */
public class ChapterListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event) {
        ChapterPopupMenu menu = new ChapterPopupMenu();
        menu.show(event.getComponent(), event.getX(), event.getY());
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

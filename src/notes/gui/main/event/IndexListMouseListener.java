package notes.gui.main.event;

import notes.entity.SystemMode;
import notes.gui.book.component.ChapterPopupMenu;
import notes.gui.main.component.MainPanel;
import notes.gui.workset.component.WorksheetPopupMenu;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: rui
 * Date: 10/20/13
 * Time: 4:45 AM
 */
public class IndexListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event) {
        JPopupMenu menu;
        SystemMode systemMode = MainPanel.get().getCurrentMode();
        if (systemMode.equals(SystemMode.WORKSET)) {
            menu = new WorksheetPopupMenu();
        } else {
            menu = new ChapterPopupMenu();
        }
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
            SoundFactory.playNavigation();
            doPop(event);
        }
    }
}

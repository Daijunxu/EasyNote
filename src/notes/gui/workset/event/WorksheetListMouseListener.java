package notes.gui.workset.event;

import notes.gui.workset.component.WorksheetPopupMenu;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for worksheets' list in workset panel.
 *
 * @author Rui Du
 */
public class WorksheetListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event) {
        WorksheetPopupMenu menu = new WorksheetPopupMenu();
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

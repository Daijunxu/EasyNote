/**
 *
 */
package notes.gui.workset.event;

import notes.data.cache.Property;
import notes.gui.workset.component.WorksheetPopupMenu;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for worksheets' list in workset panel.
 *
 * @author Rui Du
 * @version 1.0
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
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            doPop(event);
        }
    }
}

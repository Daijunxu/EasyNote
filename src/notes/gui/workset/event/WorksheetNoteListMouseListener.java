package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.gui.workset.component.EditWorksheetNoteDialog;
import notes.gui.workset.component.WorksheetNotePopupMenu;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for notes' JList in workset panel.
 *
 * @author Rui Du
 */
public class WorksheetNoteListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event) {
        WorksheetNotePopupMenu menu = new WorksheetNotePopupMenu();
        menu.show(event.getComponent(), event.getX(), event.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        WorksetHome home = WorksetHome.get();
        if (event.getClickCount() == 2 && home.getCurrentWorkset() != null
                && home.getCurrentWorksheet() != null
                && home.getCurrentWorksheetNote() != null) {
            SoundFactory.playPopup();
            new EditWorksheetNoteDialog(home.getCurrentWorkset(), home.getCurrentWorksheet(),
                    home.getCurrentWorksheetNote());
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
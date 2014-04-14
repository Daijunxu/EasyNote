package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The event listener for worksheet list. Triggers when selected item in the list changes.
 *
 * Author: Rui Du
 */
public class WorksheetListSelectionListener implements ListSelectionListener {

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {
        // When the user release the mouse button and completes the selection,
        // getValueIsAdjusting() becomes false.
        if (!event.getValueIsAdjusting()) {
            MainPanel frame = MainPanel.get();
            frame.remove(frame.getNotesPanel());

            // Clears the temporary data.
            WorksetBusinessLogic.get().clearTemporaryDataWhenWorksheetChanged();

            JList list = (JList) event.getSource();
            int selected = list.getSelectedIndex();

            // Get the worksheet's note data.
            Long worksheetId = WorksetBusinessLogic.get().getCurrentWorkset().getWorksheetIdsList().get(selected);
            Worksheet worksheet = WorksetBusinessLogic.get().getCurrentWorkset().getWorksheetsMap().get(worksheetId);
            frame.updateWorksheetNotePanel(worksheet, null);

            SoundFactory.playNavigation();
        }
    }
}

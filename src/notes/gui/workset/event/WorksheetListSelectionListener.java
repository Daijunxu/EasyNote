package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.entity.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The event listener for worksheet list. Triggers when selected item in the list changes.
 *
 * @author Rui Du
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
            WorksetHome.get().clearTemporaryDataWhenWorksheetChanged();

            JList list = (JList) event.getSource();
            int selected = list.getSelectedIndex();

            // Get the worksheet's note data.
            Long worksheetId = WorksetHome.get().getCurrentWorkset().getWorksheetIdsList().get(selected);
            Worksheet worksheet = WorksetHome.get().getCurrentWorkset().getWorksheetsMap().get(worksheetId);
            frame.updateWorksheetNotePanel(worksheet);

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
        }
    }
}
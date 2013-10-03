package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a worksheet note.
 *
 * @author Rui Du
 */
public class DeleteWorksheetNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        WorksetHome home = WorksetHome.get();
        MainPanel frame = MainPanel.get();
        try {
            WorksheetNote worksheetNote = home.getCurrentWorksheetNote();
            if (worksheetNote == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNotify();
            }
            int result = JOptionPane.showConfirmDialog(null, "Delete this note?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected worksheet note.
                home.getWorksheetNoteDAO().deleteNote(worksheetNote);
                // Update temporary data in the WorksetHome.
                home.updateTemporaryData(home.getCurrentWorkset().getDocumentId(), home
                        .getCurrentWorksheet().getWorksheetId(), null);
                // Update the note panel.
                frame.updateWorksheetNotePanel(null);
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playDelete();
                }
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playNavigation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

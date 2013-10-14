package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

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
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            SoundFactory.playNotify();
            int result = JOptionPane.showConfirmDialog(null, "Delete this note?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected worksheet note.
                home.getWorksheetNoteDAO().deleteNote(worksheetNote);
                // Update the note panel.
                frame.updateWorksheetNotePanel(home.getCurrentWorksheet(), null);
                SoundFactory.playDelete();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

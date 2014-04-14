package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.workset.Workset;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a workset.
 *
 * Author: Rui Du
 */
public class DeleteWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        WorksetBusinessLogic logic = WorksetBusinessLogic.get();
        MainPanel frame = MainPanel.get();
        try {
            Workset workset = logic.getCurrentWorkset();
            if (workset == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            SoundFactory.playNotify();
            int result = JOptionPane.showConfirmDialog(null,
                    "Delete this workset and all the notes in it?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected workset.
                logic.getWorksheetNoteDAO().deleteDocument(workset);
                // Clear all temporary data.
                logic.clearAllTemporaryData();
                // Set up the default panel.
                frame.setDefaultPanel();
                SoundFactory.playDelete();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

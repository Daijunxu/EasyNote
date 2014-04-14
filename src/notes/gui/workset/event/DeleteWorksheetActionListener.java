package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a worksheet.
 *
 * Author: Rui Du
 */
public class DeleteWorksheetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        WorksetBusinessLogic logic = WorksetBusinessLogic.get();
        MainPanel frame = MainPanel.get();
        try {
            Worksheet worksheet = logic.getCurrentWorksheet();
            if (worksheet == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No worksheet is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            SoundFactory.playNotify();
            int result = JOptionPane.showConfirmDialog(null,
                    "Delete this worksheet and all the notes in it?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected worksheet.
                logic.getWorksheetNoteDAO().deleteWorksheet(worksheet, logic.getCurrentWorkset().getDocumentId());
                // Update the worksheet and note panel.
                frame.updateWorksetPanel(logic.getCurrentWorkset().getDocumentId(), null, null);
                SoundFactory.playDelete();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

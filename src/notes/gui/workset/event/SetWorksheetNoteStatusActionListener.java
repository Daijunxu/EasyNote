package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.workset.WorksheetNoteStatus;
import notes.businessobjects.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of setting the worksheet note status.
 * <p/>
 * Author: Rui Du
 * Date: 4/22/14
 * Time: 11:03 PM
 */
public class SetWorksheetNoteStatusActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            WorksheetNote currentWorksheetNote = WorksetBusinessLogic.get().getCurrentNote();
            if (currentWorksheetNote == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JMenuItem source = (JMenuItem) event.getSource();
                WorksheetNoteStatus statusToSet = WorksheetNoteStatus.getNoteStatusFromDescription(source.getText());
                currentWorksheetNote.setNoteStatus(statusToSet);

                // Update the note panel.
                MainPanel.get().updateWorksheetNotePanel(WorksetBusinessLogic.get().getCurrentWorksheet(), null);
                SoundFactory.playUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

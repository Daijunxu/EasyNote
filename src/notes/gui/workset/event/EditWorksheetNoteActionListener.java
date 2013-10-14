package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.gui.workset.component.EditWorksheetNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing a worksheet note.
 *
 * @author Rui Du
 */
public class EditWorksheetNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            WorksetHome home = WorksetHome.get();
            if (home.getCurrentWorkset() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (home.getCurrentWorksheet() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No worksheet is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (home.getCurrentWorksheetNote() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new EditWorksheetNoteDialog(home.getCurrentWorkset(), home.getCurrentWorksheet(),
                        home.getCurrentWorksheetNote());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

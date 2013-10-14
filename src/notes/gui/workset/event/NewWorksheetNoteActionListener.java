package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.gui.workset.component.NewWorksheetNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new worksheet note.
 *
 * @author Rui Du
 */
public class NewWorksheetNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (WorksetHome.get().getCurrentWorkset() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (WorksetHome.get().getCurrentWorksheet() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No worksheet is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new NewWorksheetNoteDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

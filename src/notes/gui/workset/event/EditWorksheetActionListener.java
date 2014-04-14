package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.gui.workset.component.EditWorksheetDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing a worksheet in a workset.
 *
 * Author: Rui Du
 */
public class EditWorksheetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (WorksetBusinessLogic.get().getCurrentWorkset() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (WorksetBusinessLogic.get().getCurrentWorksheet() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No worksheet is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new EditWorksheetDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

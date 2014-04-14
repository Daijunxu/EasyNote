package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.gui.workset.component.NewWorksheetDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new worksheet.
 *
 * Author: Rui Du
 */
public class NewWorksheetActionListener implements ActionListener {

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
            } else {
                SoundFactory.playPopup();
                new NewWorksheetDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

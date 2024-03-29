package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.gui.workset.component.EditWorksetDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing a workset.
 *
 * Author: Rui Du
 */
public class EditWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (WorksetBusinessLogic.get().getCurrentWorkset() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No book is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new EditWorksetDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

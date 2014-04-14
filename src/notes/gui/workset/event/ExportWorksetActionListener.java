package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.gui.workset.component.ExportWorksetDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for exporting current workset.
 *
 * Author: Rui Du
 */
public class ExportWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (WorksetBusinessLogic.get().getCurrentWorkset() == null) {
            SoundFactory.playError();
            JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            SoundFactory.playPopup();
            new ExportWorksetDialog();
        }
    }
}

package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.gui.workset.component.ExportWorksetDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for exporting current workset.
 *
 * @author Rui Du
 */
public class ExportWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (WorksetHome.get().getCurrentWorkset() == null) {
            SoundFactory.playError();
            JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            SoundFactory.playPopup();
            new ExportWorksetDialog();
        }
    }
}

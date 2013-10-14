package notes.gui.workset.event;

import notes.gui.workset.component.NewWorksetDialog;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new workset.
 *
 * @author Rui Du
 */
public class NewWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            SoundFactory.playPopup();
            new NewWorksetDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

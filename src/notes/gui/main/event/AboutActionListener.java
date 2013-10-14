package notes.gui.main.event;

import notes.gui.main.component.AboutDialog;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for showing system's information.
 *
 * @author Rui Du
 */
public class AboutActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        SoundFactory.playPopup();
        new AboutDialog();
    }
}

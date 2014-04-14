package notes.gui.main.event;

import notes.gui.main.component.OpenDocumentDialog;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for opening a document.
 *
 * Author: Rui Du
 */
public class OpenDocumentActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        SoundFactory.playPopup();
        new OpenDocumentDialog();
    }
}
package notes.gui.book.event;

import notes.businesslogic.BookBusinessLogic;
import notes.gui.book.component.ExportBookDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for exporting current book.
 *
 * Author: Rui Du
 */
public class ExportBookActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (BookBusinessLogic.get().getCurrentBook() == null) {
            SoundFactory.playError();
            JOptionPane.showMessageDialog(null, "No book is selected!", "Input error", JOptionPane.ERROR_MESSAGE);
        } else {
            SoundFactory.playPopup();
            new ExportBookDialog();
        }
    }
}

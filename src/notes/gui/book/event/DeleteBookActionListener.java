package notes.gui.book.event;

import notes.businesslogic.BookBusinessLogic;
import notes.businessobjects.book.Book;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a book.
 *
 * Author: Rui Du
 */
public class DeleteBookActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        BookBusinessLogic logic = BookBusinessLogic.get();
        MainPanel frame = MainPanel.get();
        try {
            Book book = logic.getCurrentBook();
            if (book == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No book is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            SoundFactory.playNotify();
            int result = JOptionPane.showConfirmDialog(null,
                    "Delete this book and all the notes in it?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected book.
                logic.getBookNoteDAO().deleteDocument(book);
                // Clear all temporary data.
                logic.clearAllTemporaryData();
                // Set up the default panel.
                frame.setDefaultPanel();
                SoundFactory.playDelete();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package notes.gui.book.event;

import notes.businesslogic.BookBusinessLogic;
import notes.businessobjects.book.BookNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a book note.
 *
 * Author: Rui Du
 */
public class DeleteBookNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        BookBusinessLogic logic = BookBusinessLogic.get();
        MainPanel frame = MainPanel.get();
        try {
            BookNote bookNote = logic.getCurrentBookNote();
            if (bookNote == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            SoundFactory.playNotify();
            int result = JOptionPane.showConfirmDialog(null, "Delete this note?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected book note.
                logic.getBookNoteDAO().deleteNote(bookNote);
                // Update the note panel.
                frame.updateBookNotePanel(logic.getCurrentChapter(), null);
                SoundFactory.playDelete();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

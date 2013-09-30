/**
 *
 */
package notes.gui.book.event;

import notes.bean.BookHome;
import notes.data.cache.Property;
import notes.entity.book.BookNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a book note.
 *
 * @author Rui Du
 * @version 1.0
 */
public class DeleteBookNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        BookHome home = BookHome.get();
        MainPanel frame = MainPanel.get();
        try {
            BookNote bookNote = home.getCurrentBookNote();
            if (bookNote == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNotify();
            }
            int result = JOptionPane.showConfirmDialog(null, "Delete this note?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected book note.
                home.getBookNoteDAO().deleteNote(bookNote);
                // Update temporary data in the BookHome.
                home.updateTemporaryData(home.getCurrentBook().getDocumentId(), home
                        .getCurrentChapter().getChapterId(), null);
                // Update the note panel.
                frame.updateBookNotePanel(null);
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playDelete();
                }
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playNavigation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

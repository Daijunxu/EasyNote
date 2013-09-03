/**
 *
 */
package notes.gui.book.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import notes.bean.BookHome;
import notes.data.cache.Property;
import notes.gui.book.component.ViewBookNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Defines event listener of viewing a book note.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewBookNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (BookHome.get().getCurrentBook() == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No book is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (BookHome.get().getCurrentChapter() == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No chapter is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (BookHome.get().getCurrentBookNote() == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playPopup();
                }
                new ViewBookNoteDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

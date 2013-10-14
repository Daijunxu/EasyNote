package notes.gui.book.event;

import notes.bean.BookHome;
import notes.gui.book.component.EditChapterDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing a chapter in a book.
 *
 * @author Rui Du
 */
public class EditChapterActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (BookHome.get().getCurrentBook() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No book is selected!", "Input error", JOptionPane.ERROR_MESSAGE);
            } else if (BookHome.get().getCurrentChapter() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No chapter is selected!", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new EditChapterDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

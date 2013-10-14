package notes.gui.book.event;

import notes.bean.BookHome;
import notes.gui.book.component.NewChapterDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new chapter.
 *
 * @author Rui Du
 */
public class NewChapterActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (BookHome.get().getCurrentBook() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No book is selected!", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new NewChapterDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

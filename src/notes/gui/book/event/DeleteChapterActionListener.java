package notes.gui.book.event;

import notes.bean.BookHome;
import notes.entity.book.Chapter;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a chapter.
 *
 * @author Rui Du
 */
public class DeleteChapterActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        BookHome home = BookHome.get();
        MainPanel frame = MainPanel.get();
        try {
            Chapter chapter = home.getCurrentChapter();
            if (chapter == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No chapter is selected!", "Input error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SoundFactory.playNotify();
            int result = JOptionPane.showConfirmDialog(null,
                    "Delete this chapter and all the notes in it?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected chapter.
                home.getBookNoteDAO().deleteChapter(chapter, home.getCurrentBook().getDocumentId());
                // Update the chapter and note panel.
                frame.updateBookPanel(home.getCurrentBook().getDocumentId(), null, null);
                SoundFactory.playDelete();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

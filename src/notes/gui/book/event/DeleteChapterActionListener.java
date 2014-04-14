package notes.gui.book.event;

import notes.businesslogic.BookBusinessLogic;
import notes.businessobjects.book.Chapter;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a chapter.
 *
 * Author: Rui Du
 */
public class DeleteChapterActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        BookBusinessLogic logic = BookBusinessLogic.get();
        MainPanel frame = MainPanel.get();
        try {
            Chapter chapter = logic.getCurrentChapter();
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
                logic.getBookNoteDAO().deleteChapter(chapter, logic.getCurrentBook().getDocumentId());
                // Update the chapter and note panel.
                frame.updateBookPanel(logic.getCurrentBook().getDocumentId(), null, null);
                SoundFactory.playDelete();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

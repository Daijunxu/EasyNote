package notes.gui.article.event;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businessobjects.article.ArticleNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting an article note.
 *
 * Author: Rui Du
 */
public class DeleteArticleNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        ArticleBusinessLogic logic = ArticleBusinessLogic.get();
        MainPanel frame = MainPanel.get();
        try {
            ArticleNote articleNote = logic.getCurrentArticleNote();
            if (articleNote == null) {
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
                SoundFactory.playDelete();
                // Delete the selected article note.
                logic.getArticleNoteDAO().deleteNote(articleNote);
                // Update the note panel.
                frame.updateArticleNotePanel(null);
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

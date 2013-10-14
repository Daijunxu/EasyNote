package notes.gui.article.event;

import notes.bean.ArticleHome;
import notes.entity.article.ArticleNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting an article note.
 *
 * @author Rui Du
 */
public class DeleteArticleNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        ArticleHome home = ArticleHome.get();
        MainPanel frame = MainPanel.get();
        try {
            ArticleNote articleNote = home.getCurrentArticleNote();
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
                home.getArticleNoteDAO().deleteNote(articleNote);
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

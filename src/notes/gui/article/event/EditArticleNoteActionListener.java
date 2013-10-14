package notes.gui.article.event;

import notes.bean.ArticleHome;
import notes.gui.article.component.EditArticleNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing an article note.
 *
 * @author Rui Du
 */
public class EditArticleNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            ArticleHome home = ArticleHome.get();
            if (home.getCurrentArticle() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No article is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (home.getCurrentArticleNote() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new EditArticleNoteDialog(home.getCurrentArticle(), home.getCurrentArticleNote());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package notes.gui.article.event;

import notes.businesslogic.ArticleBusinessLogic;
import notes.gui.article.component.EditArticleNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing an article note.
 *
 * Author: Rui Du
 */
public class EditArticleNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            ArticleBusinessLogic logic = ArticleBusinessLogic.get();
            if (logic.getCurrentArticle() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No article is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (logic.getCurrentArticleNote() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new EditArticleNoteDialog(logic.getCurrentArticle(), logic.getCurrentArticleNote());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

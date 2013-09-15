/**
 *
 */
package notes.gui.article.event;

import notes.article.Article;
import notes.article.ArticleNote;
import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.article.component.ViewArticleNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of viewing an article note.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewArticleNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (ArticleHome.get().getCurrentArticle() == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No article is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (ArticleHome.get().getCurrentArticleNote() == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playPopup();
                }
                ArticleNote selectedNote = ArticleHome.get().getCurrentArticleNote();
                Article selectedArticle = ArticleHome.get().getCurrentArticle();
                new ViewArticleNoteDialog(selectedArticle, selectedNote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

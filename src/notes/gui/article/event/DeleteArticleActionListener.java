package notes.gui.article.event;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businessobjects.article.Article;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting an article.
 *
 * Author: Rui Du
 */
public class DeleteArticleActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        ArticleBusinessLogic logic = ArticleBusinessLogic.get();
        MainPanel frame = MainPanel.get();
        try {
            Article article = logic.getCurrentArticle();
            if (article == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No article is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            SoundFactory.playNavigation();
            int result = JOptionPane.showConfirmDialog(null,
                    "Delete this article and all the notes in it?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                SoundFactory.playDelete();
                // Delete the selected article.
                logic.getArticleNoteDAO().deleteDocument(article);
                // Set up the default panel.
                frame.setDefaultPanel();
            } else {
                SoundFactory.playNavigation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/**
 *
 */
package notes.gui.article.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import notes.article.Article;
import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Defines event listener of deleting an article.
 *
 * @author Rui Du
 * @version 1.0
 */
public class DeleteArticleActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        ArticleHome home = ArticleHome.get();
        MainPanel frame = MainPanel.get();
        try {
            Article article = home.getCurrentArticle();
            if (article == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No article is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            int result = JOptionPane.showConfirmDialog(null,
                    "Delete this article and all the notes in it?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playDelete();
                }
                // Delete the selected article.
                home.getArticleNoteDAO().deleteDocument(article);
                // Clear all temporary data.
                home.clearAllTemporaryData();
                // Set up the default panel.
                frame.setDefaultPanel();
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playNavigation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

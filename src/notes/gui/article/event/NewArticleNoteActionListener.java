/**
 *
 */
package notes.gui.article.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.article.component.NewArticleNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Defines event listener of creating a new article note.
 *
 * @author Rui Du
 * @version 1.0
 */
public class NewArticleNoteActionListener implements ActionListener {

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
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playPopup();
                }
                new NewArticleNoteDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package notes.gui.article.event;

import notes.bean.ArticleHome;
import notes.gui.article.component.NewArticleNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new article note.
 *
 * @author Rui Du
 */
public class NewArticleNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (ArticleHome.get().getCurrentArticle() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No article is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SoundFactory.playPopup();
                new NewArticleNoteDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

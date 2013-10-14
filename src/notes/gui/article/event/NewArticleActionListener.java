package notes.gui.article.event;

import notes.gui.article.component.NewArticleDialog;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new article.
 *
 * @author Rui Du
 */
public class NewArticleActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            SoundFactory.playPopup();
            new NewArticleDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

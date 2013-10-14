package notes.gui.article.event;

import notes.bean.ArticleHome;
import notes.gui.article.component.ArticleNotePopupMenu;
import notes.gui.article.component.EditArticleNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for notes' JList in article panel.
 *
 * @author Rui Du
 */
public class ArticleNoteListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event) {
        ArticleNotePopupMenu menu = new ArticleNotePopupMenu();
        menu.show(event.getComponent(), event.getX(), event.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        ArticleHome home = ArticleHome.get();
        if (event.getClickCount() == 2 && home.getCurrentArticle() != null
                && home.getCurrentArticleNote() != null) {
            SoundFactory.playPopup();
            new EditArticleNoteDialog(home.getCurrentArticle(), home.getCurrentArticleNote());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            JList noteList = (JList) event.getSource();
            int row = noteList.locationToIndex(event.getPoint());
            noteList.setSelectedIndex(row);
            SoundFactory.playNavigation();
            doPop(event);
        }
    }

}
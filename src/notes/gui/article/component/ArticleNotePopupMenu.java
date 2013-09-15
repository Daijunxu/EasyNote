/**
 *
 */
package notes.gui.article.component;

import notes.bean.ArticleHome;
import notes.gui.article.event.DeleteArticleNoteActionListener;
import notes.gui.article.event.EditArticleNoteActionListener;
import notes.gui.article.event.NewArticleNoteActionListener;
import notes.gui.article.event.ViewArticleNoteActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the article note panel.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleNotePopupMenu extends JPopupMenu {
    /**
     * Creates an instance of {@code ArticleNotePopupMenu}.
     */
    public ArticleNotePopupMenu() {
        JMenuItem newItem;
        JMenuItem viewItem;
        JMenuItem editItem;
        JMenuItem deleteItem;

        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewArticleNoteActionListener());
        viewItem = new JMenuItem("Details");
        viewItem.addActionListener(new ViewArticleNoteActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditArticleNoteActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteArticleNoteActionListener());
        if (ArticleHome.get().getCurrentArticleNote() == null) {
            viewItem.setEnabled(false);
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        add(newItem);
        add(viewItem);
        add(editItem);
        add(deleteItem);
    }

}

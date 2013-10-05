package notes.gui.article.component;

import notes.bean.ArticleHome;
import notes.gui.article.event.DeleteArticleNoteActionListener;
import notes.gui.article.event.EditArticleNoteActionListener;
import notes.gui.article.event.NewArticleNoteActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the article note panel.
 *
 * @author Rui Du
 */
public class ArticleNotePopupMenu extends JPopupMenu {
    /**
     * Creates an instance of {@code ArticleNotePopupMenu}.
     */
    public ArticleNotePopupMenu() {
        final JMenuItem newItem;
        final JMenuItem editItem;
        final JMenuItem deleteItem;

        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewArticleNoteActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditArticleNoteActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteArticleNoteActionListener());
        if (ArticleHome.get().getCurrentArticleNote() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(deleteItem);
    }

}

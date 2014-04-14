package notes.gui.article.component;

import notes.businesslogic.ArticleBusinessLogic;
import notes.gui.article.event.DeleteArticleNoteActionListener;
import notes.gui.article.event.EditArticleActionListener;
import notes.gui.article.event.EditArticleNoteActionListener;
import notes.gui.article.event.NewArticleNoteActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the article note panel.
 *
 * Author: Rui Du
 */
public class ArticleNotePopupMenu extends JPopupMenu {
    /**
     * Creates an instance of {@code ArticleNotePopupMenu}.
     */
    public ArticleNotePopupMenu() {
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(new NewArticleNoteActionListener());
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditArticleNoteActionListener());
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteArticleNoteActionListener());
        JMenuItem viewArticleInfoItem = new JMenuItem("Article Info");
        viewArticleInfoItem.addActionListener(new EditArticleActionListener());
        if (ArticleBusinessLogic.get().getCurrentArticleNote() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(deleteItem);
        add(viewArticleInfoItem);
    }

}

/**
 *
 */
package notes.gui.article.component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import notes.bean.ArticleHome;
import notes.gui.article.event.DeleteArticleNoteActionListener;
import notes.gui.article.event.EditArticleNoteActionListener;
import notes.gui.article.event.NewArticleNoteActionListener;
import notes.gui.article.event.ViewArticleNoteActionListener;

/**
 * Pops up when right clicking a note in the article note panel.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleNotePopupMenu extends JPopupMenu {
    private static final long serialVersionUID = 1060788077636224251L;

    private JMenuItem newItem;
    private JMenuItem viewItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;

    /**
     * Creates an instance of {@code ArticleNotePopupMenu}.
     */
    public ArticleNotePopupMenu() {
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

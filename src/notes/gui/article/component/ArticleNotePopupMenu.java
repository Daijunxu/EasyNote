package notes.gui.article.component;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businessobjects.Note;
import notes.gui.article.event.DeleteArticleNoteActionListener;
import notes.gui.article.event.EditArticleActionListener;
import notes.gui.article.event.EditArticleNoteActionListener;
import notes.gui.article.event.NewArticleNoteActionListener;
import notes.gui.main.event.note.MoveNoteDownActionListener;
import notes.gui.main.event.note.MoveNoteToBottomActionListener;
import notes.gui.main.event.note.MoveNoteToTopActionListener;
import notes.gui.main.event.note.MoveNoteUpActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the article note panel.
 * <p/>
 * Author: Rui Du
 */
public class ArticleNotePopupMenu extends JPopupMenu {
    /**
     * Creates an instance of {@code ArticleNotePopupMenu}.
     */
    public ArticleNotePopupMenu() {
        ArticleBusinessLogic logic = ArticleBusinessLogic.get();

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(new NewArticleNoteActionListener());
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditArticleNoteActionListener());
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteArticleNoteActionListener());
        JMenuItem viewArticleInfoItem = new JMenuItem("Article Info");
        viewArticleInfoItem.addActionListener(new EditArticleActionListener());

        JMenuItem moveUpItem = new JMenuItem("Move up");
        moveUpItem.addActionListener(new MoveNoteUpActionListener());
        JMenuItem moveDownItem = new JMenuItem("Move down");
        moveDownItem.addActionListener(new MoveNoteDownActionListener());
        JMenuItem moveToTopItem = new JMenuItem("Move to top");
        moveToTopItem.addActionListener(new MoveNoteToTopActionListener());
        JMenuItem moveToBottomItem = new JMenuItem("Move to bottom");
        moveToBottomItem.addActionListener(new MoveNoteToBottomActionListener());

        Note currentNote = ArticleBusinessLogic.get().getCurrentNote();
        if (currentNote == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }

        if (currentNote == null || !logic.canMoveCurrentNoteUp()) {
            moveUpItem.setEnabled(false);
            moveToTopItem.setEnabled(false);
        }

        if (currentNote == null || !logic.canMoveCurrentNoteDown()) {
            moveDownItem.setEnabled(false);
            moveToBottomItem.setEnabled(false);
        }

        add(newItem);
        add(editItem);
        add(deleteItem);
        add(viewArticleInfoItem);
        addSeparator();
        add(moveUpItem);
        add(moveDownItem);
        add(moveToTopItem);
        add(moveToBottomItem);
    }

}

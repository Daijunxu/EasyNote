package notes.gui.main.event;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businesslogic.BookBusinessLogic;
import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.SystemMode;
import notes.gui.article.component.ArticleNotePopupMenu;
import notes.gui.article.component.EditArticleNoteDialog;
import notes.gui.book.component.BookNotePopupMenu;
import notes.gui.book.component.EditBookNoteDialog;
import notes.gui.main.component.MainPanel;
import notes.gui.workset.component.EditWorksheetNoteDialog;
import notes.gui.workset.component.WorksheetNotePopupMenu;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for notes' JList.
 * <p/>
 * Author: Rui Du
 * Date: 10/20/13
 * Time: 4:18 AM
 */
public class NoteListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event) {
        JPopupMenu menu;
        SystemMode systemMode = MainPanel.get().getCurrentMode();
        if (systemMode.equals(SystemMode.WORKSET)) {
            menu = new WorksheetNotePopupMenu();
        } else if (systemMode.equals(SystemMode.BOOK)) {
            menu = new BookNotePopupMenu();
        } else {
            menu = new ArticleNotePopupMenu();
        }
        menu.show(event.getComponent(), event.getX(), event.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() != 2) {
            return;
        }
        SystemMode systemMode = MainPanel.get().getCurrentMode();
        SoundFactory.playPopup();
        if (systemMode.equals(SystemMode.WORKSET) && WorksetBusinessLogic.get().getCurrentWorkset() != null
                && WorksetBusinessLogic.get().getCurrentWorksheet() != null && WorksetBusinessLogic.get().getWorksheetNoteDAO() != null) {
            new EditWorksheetNoteDialog(WorksetBusinessLogic.get().getCurrentWorkset(),
                    WorksetBusinessLogic.get().getCurrentWorksheet(), WorksetBusinessLogic.get().getCurrentWorksheetNote());
        } else if (systemMode.equals(SystemMode.BOOK) && BookBusinessLogic.get().getCurrentBook() != null
                && BookBusinessLogic.get().getCurrentChapter() != null && BookBusinessLogic.get().getCurrentBookNote() != null) {
            new EditBookNoteDialog(BookBusinessLogic.get().getCurrentBook(), BookBusinessLogic.get().getCurrentChapter(),
                    BookBusinessLogic.get().getCurrentBookNote());
        } else if (systemMode.equals(SystemMode.ARTICLE) && ArticleBusinessLogic.get().getCurrentArticle() != null
                && ArticleBusinessLogic.get().getCurrentArticleNote() != null) {
            new EditArticleNoteDialog(ArticleBusinessLogic.get().getCurrentArticle(), ArticleBusinessLogic.get().getCurrentArticleNote());
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

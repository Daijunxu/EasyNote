package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.entity.SystemMode;
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
        if (systemMode.equals(SystemMode.WORKSET) && WorksetHome.get().getCurrentWorkset() != null
                && WorksetHome.get().getCurrentWorksheet() != null && WorksetHome.get().getWorksheetNoteDAO() != null) {
            new EditWorksheetNoteDialog(WorksetHome.get().getCurrentWorkset(),
                    WorksetHome.get().getCurrentWorksheet(), WorksetHome.get().getCurrentWorksheetNote());
        } else if (systemMode.equals(SystemMode.BOOK) && BookHome.get().getCurrentBook() != null
                && BookHome.get().getCurrentChapter() != null && BookHome.get().getCurrentBookNote() != null) {
            new EditBookNoteDialog(BookHome.get().getCurrentBook(), BookHome.get().getCurrentChapter(),
                    BookHome.get().getCurrentBookNote());
        } else if (systemMode.equals(SystemMode.ARTICLE) && ArticleHome.get().getCurrentArticle() != null
                && ArticleHome.get().getCurrentArticleNote() != null) {
            new EditArticleNoteDialog(ArticleHome.get().getCurrentArticle(), ArticleHome.get().getCurrentArticleNote());
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

package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.entity.Note;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;
import notes.gui.article.component.EditArticleNoteDialog;
import notes.gui.book.component.EditBookNoteDialog;
import notes.gui.main.component.SearchNotePopupMenu;
import notes.gui.workset.component.EditWorksheetNoteDialog;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse event listener for notes' JList in search note panel.
 *
 * @author Rui Du
 */
public class SearchNoteListMouseListener extends MouseAdapter {

    private void doPop(MouseEvent event, Note selectedNote) {
        SearchNotePopupMenu menu = new SearchNotePopupMenu(selectedNote);
        menu.show(event.getComponent(), event.getX(), event.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        JList noteList = (JList) event.getSource();
        Note selectedNote = (Note) noteList.getSelectedValue();

        if (event.getClickCount() == 2) {
            SoundFactory.playPopup();
            if (selectedNote instanceof WorksheetNote) {
                Workset selectedWorkset = (Workset) WorksetHome.get().getWorksheetNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                Worksheet selectedWorksheet = selectedWorkset.getWorksheetsMap().get(((WorksheetNote) selectedNote).getWorksheetId());
                new EditWorksheetNoteDialog(selectedWorkset, selectedWorksheet, (WorksheetNote) selectedNote);
            } else if (selectedNote instanceof ArticleNote) {
                Article selectedArticle = (Article) ArticleHome.get().getArticleNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                new EditArticleNoteDialog(selectedArticle, (ArticleNote) selectedNote);
            } else if (selectedNote instanceof BookNote) {
                Book selectedBook = (Book) BookHome.get().getBookNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                Chapter selectedChapter = selectedBook.getChaptersMap().get(((BookNote) selectedNote).getChapterId());
                new EditBookNoteDialog(selectedBook, selectedChapter, (BookNote) selectedNote);
            }
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
            Note selectedNote = (Note) noteList.getSelectedValue();
            SoundFactory.playNavigation();
            doPop(event, selectedNote);
        }
    }
}

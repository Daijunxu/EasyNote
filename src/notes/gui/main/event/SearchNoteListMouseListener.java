package notes.gui.main.event;

import notes.dao.impl.DocumentNoteDAO;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;
import notes.businessobjects.book.Book;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.book.Chapter;
import notes.businessobjects.workset.Workset;
import notes.businessobjects.workset.Worksheet;
import notes.businessobjects.workset.WorksheetNote;
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
 * Author: Rui Du
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
            Document document = DocumentNoteDAO.get().findDocumentById(selectedNote.getDocumentId());
            if (selectedNote instanceof WorksheetNote) {
                Worksheet selectedWorksheet = ((Workset) document).getWorksheetsMap().get(((WorksheetNote) selectedNote).getWorksheetId());
                new EditWorksheetNoteDialog((Workset) document, selectedWorksheet, (WorksheetNote) selectedNote);
            } else if (selectedNote instanceof ArticleNote) {
                new EditArticleNoteDialog((Article) document, (ArticleNote) selectedNote);
            } else if (selectedNote instanceof BookNote) {
                Chapter selectedChapter = ((Book) document).getChaptersMap().get(((BookNote) selectedNote).getChapterId());
                new EditBookNoteDialog((Book) document, selectedChapter, (BookNote) selectedNote);
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

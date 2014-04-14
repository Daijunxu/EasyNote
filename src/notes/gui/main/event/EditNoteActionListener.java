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
import notes.gui.main.component.SearchNoteDialog;
import notes.gui.workset.component.EditWorksheetNoteDialog;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing a note. Notes can belong to different types.
 * <p/>
 * Author: Rui Du
 * Date: 9/15/13
 * Time: 5:07 PM
 */
public class EditNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            SoundFactory.playPopup();
            Note selectedNote = SearchNoteDialog.get().getSelectedResultNote();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

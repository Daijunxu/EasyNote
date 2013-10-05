package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.data.cache.Property;
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
import notes.gui.book.component.ViewBookNoteDialog;
import notes.gui.main.component.SearchNoteDialog;
import notes.gui.workset.component.ViewWorksheetNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of viewing a note. Notes can belong to different types.
 *
 * @author Rui Du
 */
public class ViewNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }

            Note selectedNote = SearchNoteDialog.get().getSelectedResultNote();

            if (selectedNote instanceof WorksheetNote) {
                Workset selectedWorkset = (Workset) WorksetHome.get().getWorksheetNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                Worksheet selectedWorksheet = selectedWorkset.getWorksheetsMap().get(((WorksheetNote) selectedNote).getWorksheetId());
                new ViewWorksheetNoteDialog(selectedWorkset, selectedWorksheet, (WorksheetNote) selectedNote);
            } else if (selectedNote instanceof ArticleNote) {
                Article selectedArticle = (Article) ArticleHome.get().getArticleNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                new EditArticleNoteDialog(selectedArticle, (ArticleNote) selectedNote);
            } else if (selectedNote instanceof BookNote) {
                Book selectedBook = (Book) BookHome.get().getBookNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                Chapter selectedChapter = selectedBook.getChaptersMap().get(((BookNote) selectedNote).getChapterId());
                new ViewBookNoteDialog(selectedBook, selectedChapter, (BookNote) selectedNote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

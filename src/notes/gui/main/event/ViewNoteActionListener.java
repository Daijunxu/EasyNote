/**
 *
 */
package notes.gui.main.event;

import notes.article.Article;
import notes.article.ArticleNote;
import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.book.Book;
import notes.book.BookNote;
import notes.book.Chapter;
import notes.data.cache.Property;
import notes.entity.Note;
import notes.gui.article.component.ViewArticleNoteDialog;
import notes.gui.book.component.ViewBookNoteDialog;
import notes.gui.main.component.SearchNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of viewing a note. Notes can belong to different types.
 *
 * @author Rui Du
 * @version 1.0
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

            if (selectedNote instanceof ArticleNote) {
                Article selectedArticle = (Article) ArticleHome.get().getArticleNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                new ViewArticleNoteDialog(selectedArticle, (ArticleNote) selectedNote);
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

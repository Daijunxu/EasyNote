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
import notes.gui.article.component.EditArticleNoteDialog;
import notes.gui.book.component.EditBookNoteDialog;
import notes.gui.main.component.SearchNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of editing a note. Notes can belong to different types.
 * <p/>
 * User: rui
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
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }

            Note selectedNote = SearchNoteDialog.get().getSelectedResultNote();

            if (selectedNote instanceof ArticleNote) {
                Article selectedArticle = (Article) ArticleHome.get().getArticleNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                new EditArticleNoteDialog(selectedArticle, (ArticleNote) selectedNote);
            } else if (selectedNote instanceof BookNote) {
                Book selectedBook = (Book) BookHome.get().getBookNoteDAO()
                        .findDocumentById(selectedNote.getDocumentId());
                Chapter selectedChapter = selectedBook.getChaptersMap().get(((BookNote) selectedNote).getChapterId());
                new EditBookNoteDialog(selectedBook, selectedChapter, (BookNote) selectedNote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package notes.businesslogic;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.ArticleNoteDAO;
import notes.data.cache.Cache;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;

import java.util.ArrayList;
import java.util.List;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 *
 * Author: Rui Du
 */
public class ArticleBusinessLogic {

    /**
     * The single instance of ArticleBusinessLogic.
     */
    private static final ArticleBusinessLogic instance = new ArticleBusinessLogic();
    /**
     * The article note DAO.
     */
    @Getter
    private ArticleNoteDAO articleNoteDAO;
    /**
     * The current selected article.
     */
    @Getter
    @Setter
    private Article currentArticle;
    /**
     * The current selected article note.
     */
    @Getter
    @Setter
    private ArticleNote currentArticleNote;

    /**
     * Constructs an instance of {@code ArticleBusinessLogic}.
     */
    private ArticleBusinessLogic() {
        articleNoteDAO = ArticleNoteDAO.get();
    }

    /**
     * Gets the instance of {@code ArticleBusinessLogic}.
     *
     * @return {@code ArticleBusinessLogic} The instance of {@code ArticleBusinessLogic}.
     */
    public static ArticleBusinessLogic get() {
        return instance;
    }

    /**
     * Clears all temporary data stored in ArticleBusinessLogic.
     */
    public void clearAllTemporaryData() {
        currentArticle = null;
        currentArticleNote = null;
    }

    /**
     * Updates the data members in ArticleBusinessLogic, which are acquired from Cache.
     *
     * @param documentId The current selected document ID.
     * @param noteId     The current selected note ID.
     */
    public void updateTemporaryData(Long documentId, Long noteId) {
        // Clear temporary data.
        clearAllTemporaryData();

        if (documentId != null) {
            // Update currentArticle.
            currentArticle = ((Article) articleNoteDAO.findDocumentById(documentId));
        }

        if (noteId != null) {
            // Update currentArticleNote.
            currentArticleNote = (ArticleNote) (articleNoteDAO.findNoteById(noteId));
        }
    }

    /**
     * Gets the list of notes for the current article.
     *
     * @return {@code List} The list of notes for the current article.
     */
    public List<ArticleNote> getAllNotesForCurrentArticle() {
        Cache cache = Cache.get();
        List<ArticleNote> articleNoteList = new ArrayList<ArticleNote>();
        for (Long articleNoteId : currentArticle.getNotesList()) {
            articleNoteList.add((ArticleNote) cache.getNoteCache().getNoteMap().get(articleNoteId));
        }
        return articleNoteList;
    }

    /**
     * Gets the index of the given note in the current article.
     *
     * @param noteIdToFind The ID of the note to find.
     * @return int The index of the note.
     */
    public int getIndexForNote(Long noteIdToFind) {
        return currentArticle.getNotesList().indexOf(noteIdToFind);
    }
}

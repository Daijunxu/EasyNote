package notes.bean;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.ArticleNoteDAO;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;

import java.util.ArrayList;
import java.util.List;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 *
 * @author Rui Du
 */
public class ArticleHome {

    /**
     * The single instance of ArticleHome.
     */
    private static final ArticleHome instance = new ArticleHome();
    /**
     * The article note DAO.
     */
    @Getter
    private ArticleNoteDAO articleNoteDAO;
    /**
     * The list of documents.
     */
    @Getter
    @Setter
    private List<Document> documentList;
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
     * Constructs an instance of {@code ArticleHome}.
     */
    private ArticleHome() {
        articleNoteDAO = new ArticleNoteDAO();
        documentList = articleNoteDAO.findAllDocuments();
    }

    /**
     * Gets the instance of {@code ArticleHome}.
     *
     * @return {@code ArticleHome} The instance of {@code ArticleHome}.
     */
    public static ArticleHome get() {
        return instance;
    }

    /**
     * Clears all temporary data stored in ArticleHome.
     */
    public void clearAllTemporaryData() {
        documentList.clear();
        currentArticle = null;
        currentArticleNote = null;
    }

    /**
     * Updates the data members in ArticleHome, which are acquired from Cache.
     *
     * @param documentId The current selected document ID.
     * @param noteId     The current selected note ID.
     */
    public void updateTemporaryData(Long documentId, Long noteId) {
        // Clear temporary data.
        clearAllTemporaryData();

        // Update documentList.
        documentList = articleNoteDAO.findAllDocuments();

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
}

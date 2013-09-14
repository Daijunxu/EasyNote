/**
 *
 */
package notes.bean;

import notes.article.Article;
import notes.article.ArticleNote;
import notes.dao.impl.ArticleNoteDAO;
import notes.entity.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleHome implements Serializable {

    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = 5485984267066020550L;
    /**
     * The single instance of ArticleHome.
     */
    private static final ArticleHome instance = new ArticleHome();
    /**
     * The article note DAO.
     */
    private ArticleNoteDAO articleNoteDAO;
    /**
     * The list of documents.
     */
    private List<Document> documentList;
    /**
     * The current selected article.
     */
    private Article currentArticle;
    /**
     * The current selected article note.
     */
    private ArticleNote currentArticleNote;
    /**
     * The list of article notes in current selected article.
     */
    private List<ArticleNote> currentArticleNotesList;

    /**
     * Constructs an instance of {@code ArticleHome}.
     */
    private ArticleHome() {
        articleNoteDAO = new ArticleNoteDAO();
        documentList = articleNoteDAO.findAllDocuments();
        currentArticleNotesList = new ArrayList<ArticleNote>();
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
        currentArticleNotesList.clear();
    }

    /**
     * Gets the article note DAO.
     *
     * @return {@code ArticleNoteDAO} The article note DAO.
     */
    public ArticleNoteDAO getArticleNoteDAO() {
        return articleNoteDAO;
    }

    /**
     * Gets the current selected article.
     *
     * @return {@code Article} The current selected article.
     */
    public Article getCurrentArticle() {
        return currentArticle;
    }

    /**
     * Sets the current selected article.
     *
     * @param currentArticle The current selected article to set.
     */
    public void setCurrentArticle(Article currentArticle) {
        this.currentArticle = currentArticle;
    }

    /**
     * Gets the current selected article note.
     *
     * @return {@code ArticleNote} The current selected article note.
     */
    public ArticleNote getCurrentArticleNote() {
        return currentArticleNote;
    }

    /**
     * Sets the current selected article note.
     *
     * @param currentArticleNote The current selected article note to set.
     */
    public void setCurrentArticleNote(ArticleNote currentArticleNote) {
        this.currentArticleNote = currentArticleNote;
    }

    /**
     * Gets the article list of notes in current selected article.
     *
     * @return {@code List<ArticleNote>} The list of article notes in current selected article.
     */
    public List<ArticleNote> getCurrentArticleNotesList() {
        return currentArticleNotesList;
    }

    /**
     * Sets the list of article notes in current selected article.
     *
     * @param currentArticleNotesList The list of article note in current selected article to set.
     */
    public void setCurrentArticleNotesList(List<ArticleNote> currentArticleNotesList) {
        this.currentArticleNotesList = currentArticleNotesList;
    }

    /**
     * Gets the list of documents.
     *
     * @return {@code List<Document>} The list of documents.
     */
    public List<Document> getDocumentList() {
        return documentList;
    }

    /**
     * Sets the list of documents.
     *
     * @param documentList The list of documents to set.
     */
    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
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

            // Update currentNotesList.
            currentArticleNotesList = new ArrayList<ArticleNote>();
            for (Long articleNoteId : currentArticle.getNotesList()) {
                currentArticleNotesList.add((ArticleNote) articleNoteDAO
                        .findNoteById(articleNoteId));
            }
        }

        if (noteId != null) {
            // Update currentArticleNote.
            currentArticleNote = (ArticleNote) (articleNoteDAO.findNoteById(noteId));
        }

    }
}

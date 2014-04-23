package notes.businesslogic;

import lombok.Getter;
import lombok.Setter;
import notes.businessobjects.Note;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;
import notes.dao.impl.ArticleNoteDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 * <p/>
 * Author: Rui Du
 */
public class ArticleBusinessLogic extends AbstractDocumentBusinessLogic {

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
    private ArticleNote currentNote;

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
        currentNote = null;
    }

    /**
     * Updates the data members in ArticleBusinessLogic, which are acquired from cache.
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
            // Update currentNote.
            currentNote = (ArticleNote) (articleNoteDAO.findNoteById(noteId));
        }
    }

    /**
     * Gets the list of notes for the current article.
     *
     * @return {@code List} The list of notes for the current article.
     */
    public List<Note> getAllNotesForCurrentArticle() {
        List<Note> allNotes = new ArrayList<Note>();

        // To keep notes in the order of the current article.
        for (Long noteId : currentArticle.getNotesList()) {
            allNotes.add(articleNoteDAO.findNoteById(noteId));
        }

        return allNotes;
    }

    @Override
    public int getIndexForNote(Long noteIdToFind) {
        return currentArticle.getNotesList().indexOf(noteIdToFind);
    }

    @Override
    protected List<Long> getCurrentNoteList() {
        return currentArticle.getNotesList();
    }
}

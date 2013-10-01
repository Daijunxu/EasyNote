/**
 *
 */
package notes.bean;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.BookNoteDAO;
import notes.entity.Document;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 *
 * @author Rui Du
 * @version 1.0
 */
public class BookHome {

    /**
     * The single instance of BookHome.
     */
    private static final BookHome instance = new BookHome();
    /**
     * The book note DAO.
     */
    @Getter
    private BookNoteDAO bookNoteDAO;
    /**
     * The list of documents.
     */
    @Getter
    @Setter
    private List<Document> documentList;
    /**
     * The current selected book.
     */
    @Getter
    @Setter
    private Book currentBook;
    /**
     * The current selected chapter.
     */
    @Getter
    @Setter
    private Chapter currentChapter;
    /**
     * The current selected book note.
     */
    @Getter
    @Setter
    private BookNote currentBookNote;
    /**
     * The list of chapters in current selected book.
     */
    @Getter
    @Setter
    private List<Chapter> currentChapterList;
    /**
     * The map from chapter IDs to notes in current selected book.
     */
    @Getter
    @Setter
    private Map<Long, List<BookNote>> currentChapterNotesMap;
    /**
     * The list of book notes in current selected chapter.
     */
    @Getter
    @Setter
    private List<BookNote> currentBookNotesList;

    /**
     * Constructs an instance of {@code BookHome}.
     */
    private BookHome() {
        bookNoteDAO = new BookNoteDAO();
        documentList = bookNoteDAO.findAllDocuments();
        currentChapterList = new ArrayList<Chapter>();
        currentChapterNotesMap = new HashMap<Long, List<BookNote>>();
        currentBookNotesList = new ArrayList<BookNote>();
    }

    /**
     * Gets the instance of {@code BookHome}.
     *
     * @return {@code BookHome} The instance of {@code BookHome}.
     */
    public static BookHome get() {
        return instance;
    }

    /**
     * Clears all temporary data stored in BookHome.
     */
    public void clearAllTemporaryData() {
        documentList.clear();
        currentBook = null;
        currentChapter = null;
        currentBookNote = null;
        currentChapterList.clear();
        currentChapterNotesMap.clear();
        currentBookNotesList.clear();
    }

    /**
     * Clears the corresponding temporary data when current chapter is changed.
     */
    public void clearTemporaryDataWhenChapterChanged() {
        currentChapter = null;
        currentBookNote = null;
    }

    /**
     * Updates the data members in BookHome, which are acquired from Cache.
     *
     * @param documentId The current selected document ID.
     * @param chapterId  The current selected chapter ID.
     * @param noteId     The current selected note ID.
     */
    public void updateTemporaryData(Long documentId, Long chapterId, Long noteId) {
        // Clear temporary data.
        clearAllTemporaryData();

        // Update documentList.
        documentList = bookNoteDAO.findAllDocuments();

        if (documentId != null) {
            // Update currentBook.
            currentBook = ((Book) bookNoteDAO.findDocumentById(documentId));

            // Update chaptersData.
            for (Map.Entry<Long, Chapter> entry : currentBook.getChaptersMap().entrySet()) {
                currentChapterList.add(entry.getValue());
            }

            // Update notesMap.
            currentChapterNotesMap = BookHome.get().getBookNoteDAO()
                    .findAllNotesByChapters(documentId);
        }

        if (chapterId != null) {
            // Update currentChapter.
            currentChapter = currentBook.getChaptersMap().get(chapterId);

            // Update currentNotesList.
            currentBookNotesList = currentChapterNotesMap.get(chapterId);
        }

        if (noteId != null) {
            // Update currentBookNote.
            currentBookNote = (BookNote) (bookNoteDAO.findNoteById(noteId));
        }

    }
}

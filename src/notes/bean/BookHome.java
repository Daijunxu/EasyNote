/**
 *
 */
package notes.bean;

import notes.book.Book;
import notes.book.BookNote;
import notes.book.Chapter;
import notes.dao.impl.BookNoteDAO;
import notes.entity.Document;

import java.io.Serializable;
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
public class BookHome implements Serializable {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -641364457490299907L;
    /**
     * The single instance of BookHome.
     */
    private static final BookHome instance = new BookHome();
    /**
     * The book note DAO.
     */
    private BookNoteDAO bookNoteDAO;
    /**
     * The list of documents.
     */
    private List<Document> documentList;
    /**
     * The current selected book.
     */
    private Book currentBook;
    /**
     * The current selected chapter.
     */
    private Chapter currentChapter;
    /**
     * The current selected book note.
     */
    private BookNote currentBookNote;
    /**
     * The list of chapters in current selected book.
     */
    private List<Chapter> currentChapterList;
    /**
     * The map from chapter IDs to notes in current selected book.
     */
    private Map<Long, List<BookNote>> currentChapterNotesMap;
    /**
     * The list of book notes in current selected chapter.
     */
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
     * Gets the book note DAO.
     *
     * @return {@code BookNoteDAO} The book note DAO.
     */
    public BookNoteDAO getBookNoteDAO() {
        return bookNoteDAO;
    }

    /**
     * Gets the current selected book.
     *
     * @return {@code Book} The current selected book.
     */
    public Book getCurrentBook() {
        return currentBook;
    }

    /**
     * Sets the current selected book.
     *
     * @param currentBook The current selected book to set.
     */
    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }

    /**
     * Gets the current selected book note.
     *
     * @return {@code BookNote} The current selected book note.
     */
    public BookNote getCurrentBookNote() {
        return currentBookNote;
    }

    /**
     * Sets the current selected book note.
     *
     * @param currentBookNote The current selected book note to set.
     */
    public void setCurrentBookNote(BookNote currentBookNote) {
        this.currentBookNote = currentBookNote;
    }

    /**
     * Gets the book list of notes in current selected chapter.
     *
     * @return {@code List<BookNote>} The list of book notes in current selected chapter.
     */
    public List<BookNote> getCurrentBookNotesList() {
        return currentBookNotesList;
    }

    /**
     * Sets the list of book notes in current selected chapter.
     *
     * @param currentBookNotesList The list of book note in current selected chapter to set.
     */
    public void setCurrentBookNotesList(List<BookNote> currentBookNotesList) {
        this.currentBookNotesList = currentBookNotesList;
    }

    /**
     * Gets the current selected chapter.
     *
     * @return {@code Chapter} The current selected chapter.
     */
    public Chapter getCurrentChapter() {
        return currentChapter;
    }

    /**
     * Sets the current selected chapter.
     *
     * @param currentChapter The current selected chapter to set.
     */
    public void setCurrentChapter(Chapter currentChapter) {
        this.currentChapter = currentChapter;
    }

    /**
     * Gets the list of chapters in current selected book.
     *
     * @return {@code List<Chapter>} The list of chapters in current selected book.
     */
    public List<Chapter> getCurrentChapterList() {
        return currentChapterList;
    }

    /**
     * Sets the list of chapters in current selected book.
     *
     * @param currentChapterList The list of chapters in current selected book to set.
     */
    public void setCurrentChapterList(List<Chapter> currentChapterList) {
        this.currentChapterList = currentChapterList;
    }

    /**
     * Gets the map from chapter IDs to notes in current selected book.
     *
     * @return {@code Map<Long, List<BookNote>>} The map from chapter IDs to notes in current
     *         selected book.
     */
    public Map<Long, List<BookNote>> getCurrentChapterNotesMap() {
        return currentChapterNotesMap;
    }

    /**
     * Sets the map from chapter IDs to notes in current selected book.
     *
     * @param currentChapterNotesMap The map from chapter IDs to notes in current selected book to set.
     */
    public void setCurrentChapterNotesMap(Map<Long, List<BookNote>> currentChapterNotesMap) {
        this.currentChapterNotesMap = currentChapterNotesMap;
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

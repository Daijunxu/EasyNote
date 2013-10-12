package notes.bean;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.BookNoteDAO;
import notes.data.cache.Cache;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 *
 * @author Rui Du
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
     * Constructs an instance of {@code BookHome}.
     */
    private BookHome() {
        bookNoteDAO = new BookNoteDAO();
        currentChapterList = new ArrayList<Chapter>();
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
        currentBook = null;
        currentChapter = null;
        currentBookNote = null;
        currentChapterList.clear();
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

        if (documentId != null) {
            // Update currentBook.
            currentBook = ((Book) bookNoteDAO.findDocumentById(documentId));

            // Update chaptersData.
            for (Map.Entry<Long, Chapter> entry : currentBook.getChaptersMap().entrySet()) {
                currentChapterList.add(entry.getValue());
            }
        }

        if (chapterId != null) {
            // Update currentChapter.
            currentChapter = currentBook.getChaptersMap().get(chapterId);
        }

        if (noteId != null) {
            // Update currentBookNote.
            currentBookNote = (BookNote) (bookNoteDAO.findNoteById(noteId));
        }

    }

    /**
     * Gets the list of notes for the current chapter.
     *
     * @return {@code List} The list of notes for the current chapter.
     */
    public List<BookNote> getAllNotesForCurrentChapter() {
        Cache cache = Cache.get();
        List<BookNote> bookNoteList = new ArrayList<BookNote>();
        for (Long bookNoteId : currentChapter.getNotesList()) {
            bookNoteList.add((BookNote) cache.getNoteCache().getNoteMap().get(bookNoteId));
        }
        return bookNoteList;
    }
}

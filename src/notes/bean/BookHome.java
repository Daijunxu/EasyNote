package notes.bean;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.BookNoteDAO;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
     * Constructs an instance of {@code BookHome}.
     */
    private BookHome() {
        bookNoteDAO = new BookNoteDAO();
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
     * Gets the list of chapters for the current book.
     *
     * @return {@code List} The list of chapters for the current book.
     */
    public List<Chapter> getChaptersListForCurrentBook() {
        List<Chapter> chaptersList = new ArrayList<Chapter>();
        TreeMap<Long, Chapter> chaptersMap = currentBook.getChaptersMap();
        for (Long chapterId : chaptersMap.keySet()) {
            chaptersList.add(chaptersMap.get(chapterId));
        }
        return chaptersList;
    }

    /**
     * Gets the list of notes for the current chapter.
     *
     * @return {@code List} The list of notes for the current chapter.
     */
    public List<BookNote> getNotesListForCurrentChapter() {
        List<BookNote> notesList = new ArrayList<BookNote>();
        for (Long bookNoteId : currentChapter.getNotesList()) {
            notesList.add((BookNote) bookNoteDAO.findNoteById(bookNoteId));
        }
        return notesList;
    }
}

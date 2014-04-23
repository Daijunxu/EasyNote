package notes.businesslogic;

import lombok.Getter;
import lombok.Setter;
import notes.businessobjects.book.Book;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.book.Chapter;
import notes.dao.impl.BookNoteDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 * <p/>
 * Author: Rui Du
 */
public class BookBusinessLogic extends AbstractDocumentBusinessLogic {

    /**
     * The single instance of BookBusinessLogic.
     */
    private static final BookBusinessLogic instance = new BookBusinessLogic();
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
    private BookNote currentNote;

    /**
     * Constructs an instance of {@code BookBusinessLogic}.
     */
    private BookBusinessLogic() {
        bookNoteDAO = BookNoteDAO.get();
    }

    /**
     * Gets the instance of {@code BookBusinessLogic}.
     *
     * @return {@code BookBusinessLogic} The instance of {@code BookBusinessLogic}.
     */
    public static BookBusinessLogic get() {
        return instance;
    }

    /**
     * Clears all temporary data stored in BookBusinessLogic.
     */
    public void clearAllTemporaryData() {
        currentBook = null;
        currentChapter = null;
        currentNote = null;
    }

    /**
     * Clears the corresponding temporary data when current chapter is changed.
     */
    public void clearTemporaryDataWhenChapterChanged() {
        currentChapter = null;
        currentNote = null;
    }

    /**
     * Updates the data members in BookBusinessLogic, which are acquired from CacheDelegate.
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
            // Update currentNote.
            currentNote = (BookNote) (bookNoteDAO.findNoteById(noteId));
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

        // To keep notes in the order of the current chapter.
        for (Long bookNoteId : currentChapter.getNotesList()) {
            notesList.add((BookNote) bookNoteDAO.findNoteById(bookNoteId));
        }

        return notesList;
    }

    /**
     * Gets the index of the given chapter in the current book.
     *
     * @param chapterIdToFind The ID of the chapter to find.
     * @return int The index of the chapter.
     */
    public int getIndexForChapter(Long chapterIdToFind) {
        int index = 0;
        for (Long chapterId : currentBook.getChaptersMap().keySet()) {
            if (!chapterId.equals(chapterIdToFind)) {
                index++;
            } else {
                break;
            }
        }
        return index;
    }

    @Override
    public int getIndexForNote(Long noteIdToFind) {
        return currentChapter.getNotesList().indexOf(noteIdToFind);
    }

    @Override
    protected List<Long> getCurrentNoteList() {
        return currentChapter.getNotesList();
    }
}

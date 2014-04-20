package notes.dao.impl;

import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.book.Book;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.book.Chapter;
import notes.dao.DuplicateRecordException;
import notes.data.cache.CacheDelegate;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Data access object for book notes.
 * <p/>
 * Author: Rui Du
 */
public class BookNoteDAO extends DocumentNoteDAO {

    private static final BookNoteDAO INSTANCE = new BookNoteDAO();
    private static final CacheDelegate CACHE = CacheDelegate.get();

    private BookNoteDAO() {
    }

    public static BookNoteDAO get() {
        return INSTANCE;
    }

    /**
     * Deletes a chapter and all its notes.
     *
     * @param chapter    The chapter to delete.
     * @param documentId The document ID of the document that the chapter belongs to.
     */
    public void deleteChapter(Chapter chapter, Long documentId) {
        Book cachedBook = (Book) CACHE.getDocumentCache().find(documentId);
        Chapter cachedChapter = cachedBook.getChaptersMap().get(chapter.getChapterId());

        // Remove all notes in the chapter.
        for (Long noteId : cachedChapter.getNotesList()) {
            CACHE.getNoteCache().remove(noteId);
        }

        // Remove the chapter.
        cachedBook.getChaptersMap().remove(chapter.getChapterId());

        // Update book's last updated time.
        cachedBook.setLastUpdatedTime(new Date());
    }

    @Override
    public void deleteDocument(Document document) {
        Book cachedBook = (Book) CACHE.getDocumentCache().find(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Chapter> chaptersMap = cachedBook.getChaptersMap();
        for (Chapter chapter : chaptersMap.values()) {
            for (Long noteId : chapter.getNotesList()) {
                CACHE.getNoteCache().remove(noteId);
            }
        }

        // Remove the document in the document CACHE.
        CACHE.getDocumentCache().remove(cachedBook.getDocumentId());
    }

    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Book book = (Book) CACHE.getDocumentCache().find(note.getDocumentId());
        Chapter chapter = book.getChaptersMap().get(((BookNote) note).getChapterId());
        chapter.getNotesList().remove(noteId);

        // Remove the note in the note CACHE.
        CACHE.getNoteCache().remove(noteId);

        // Update book's last updated time.
        book.setLastUpdatedTime(new Date());
    }

    /**
     * Finds all books.
     *
     * @return {@code Set<Long>} The set of document IDs.
     */
    public Set<Long> findAllBooks() {
        Set<Long> resultSet = new HashSet<Long>();
        for (Document document : CACHE.getDocumentCache().findAll()) {
            if (document instanceof Book) {
                resultSet.add(document.getDocumentId());
            }
        }
        return resultSet;
    }

    /**
     * Finds all notes in the document, grouped by chapter IDs.
     *
     * @param documentId The document ID.
     * @return {@code Map} All notes in the document grouped by chapter IDs.
     */
    public Map<Long, List<BookNote>> findAllNotesByChapters(Long documentId) {
        Book book = (Book) (CACHE.getDocumentCache().find(documentId));
        Map<Long, List<BookNote>> noteMapByChapters = new HashMap<Long, List<BookNote>>();

        for (Chapter chapter : book.getChaptersMap().values()) {
            List<BookNote> notesList = new ArrayList<BookNote>();
            for (Long noteId : chapter.getNotesList()) {
                notesList.add((BookNote) CACHE.getNoteCache().find(noteId));
            }
            Collections.sort(notesList);
            noteMapByChapters.put(chapter.getChapterId(), notesList);
        }
        return noteMapByChapters;
    }

    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        Book book = (Book) (CACHE.getDocumentCache().find(documentId));
        List<Note> noteList = new ArrayList<Note>();

        for (Chapter chapter : book.getChaptersMap().values()) {
            for (Long noteId : chapter.getNotesList()) {
                noteList.add(CACHE.getNoteCache().find(noteId));
            }
        }
        Collections.sort(noteList);
        return noteList;
    }

    /**
     * Updates a chapter.
     *
     * @param chapter    The chapter to update.
     * @param documentId The document ID of the book that the chapter belongs to.
     * @return {@code Chapter} The updated chapter object.
     */
    public Chapter updateChapter(Chapter chapter, Long documentId) {
        try {
            Book cachedBook = (Book) (CACHE.getDocumentCache().find(documentId));
            TreeMap<Long, Chapter> chaptersMap = cachedBook.getChaptersMap();
            Chapter cachedChapter = cachedBook.getChaptersMap().get(chapter.getChapterId());
            Long oldChapterId = cachedChapter.getChapterId();
            Long updatedChapterId = chapter.getChapterId();

            cachedChapter.setChapterTitle(chapter.getChapterTitle());
            cachedChapter.setNotesList(chapter.getNotesList());

            if (!oldChapterId.equals(updatedChapterId)) {
                // Chapter id is changed.
                if (chaptersMap.containsKey(updatedChapterId)) {
                    throw new InvalidKeyException("The updated chapter id is already taken by another chapter.");
                }
                cachedChapter.setChapterId(updatedChapterId);
                chaptersMap.remove(oldChapterId);
                chaptersMap.put(updatedChapterId, cachedChapter);
            }

            // Update book's last updated time.
            cachedBook.setLastUpdatedTime(new Date());

            return cachedChapter;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Note updateNote(Note note) {
        BookNote cachedNote = (BookNote) (CACHE.getNoteCache().find(note.getNoteId()));
        Long oldChapterId = cachedNote.getChapterId();
        Book book = (Book) (CACHE.getDocumentCache().find(note.getDocumentId()));

        cachedNote = (BookNote) CACHE.getNoteCache().update(note);

        // Update chapters' notes list.
        if (!oldChapterId.equals(((BookNote) note).getChapterId())) {
            Chapter oldChapter = book.getChaptersMap().get(oldChapterId);
            oldChapter.getNotesList().remove(note.getNoteId());
            Chapter newChapter = book.getChaptersMap().get(cachedNote.getChapterId());
            newChapter.getNotesList().add(note.getNoteId());
        }

        // Update book's last updated time.
        book.setLastUpdatedTime(new Date());

        return cachedNote;
    }

    /**
     * Saves a chapter.
     *
     * @param chapter    The chapter to save.
     * @param documentId The document ID of the book that the new chapter belongs to.
     * @return {@code Chapter} The saved chapter, NULL if exception occurred.
     */
    public Chapter saveChapter(Chapter chapter, Long documentId) {
        try {
            Book cachedBook = (Book) CACHE.getDocumentCache().find(documentId);
            TreeMap<Long, Chapter> chapterMap = cachedBook.getChaptersMap();
            if (chapterMap.containsKey(chapter.getChapterId())) {
                throw new DuplicateRecordException("Duplicate chapter ID when saving a new chapter!");
            }
            if (chapter.getNotesList() == null) {
                chapter.setNotesList(new ArrayList<Long>());
            }
            chapterMap.put(chapter.getChapterId(), chapter);

            // Update book's last updated time.
            cachedBook.setLastUpdatedTime(new Date());

            return chapter;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Note saveNote(Note note) {
        if (note instanceof BookNote) {
            try {
                BookNote newNote = (BookNote) CACHE.getNoteCache().insert(note);

                // Add the note ID to corresponding notes list in the chapter.
                Book book = (Book) (CACHE.getDocumentCache().find(newNote.getDocumentId()));
                Chapter chapter = book.getChaptersMap().get(newNote.getChapterId());
                chapter.getNotesList().add(newNote.getNoteId());

                // Update book's last updated time.
                book.setLastUpdatedTime(new Date());

                return newNote;
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

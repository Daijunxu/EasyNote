package notes.dao.impl;

import notes.dao.DuplicateRecordException;
import notes.data.cache.Cache;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.book.Book;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.book.Chapter;

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
    private static final Cache CACHE = Cache.get();

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
        Book cachedBook = (Book) CACHE.getDocumentCache().getDocumentMap().get(documentId);
        Chapter cachedChapter = cachedBook.getChaptersMap().get(chapter.getChapterId());
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();

        // Remove all notes in the chapter.
        for (Long noteId : cachedChapter.getNotesList()) {
            noteMap.remove(noteId);
        }

        // Remove the chapter.
        cachedBook.getChaptersMap().remove(chapter.getChapterId());

        // Update book's last updated time.
        cachedBook.setLastUpdatedTime(new Date());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocument(Document document) {
        Book cachedBook = (Book) CACHE.getDocumentCache().getDocumentMap()
                .get(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        Map<Long, Chapter> chaptersMap = cachedBook.getChaptersMap();
        for (Chapter chapter : chaptersMap.values()) {
            for (Long noteId : chapter.getNotesList()) {
                noteMap.remove(noteId);
            }
        }

        // Remove the document in the document CACHE.
        CACHE.getDocumentCache().getDocumentMap().remove(cachedBook.getDocumentId());
        CACHE.getDocumentCache().getDocumentTitleIdMap()
                .remove(cachedBook.getDocumentTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Book book = (Book) CACHE.getDocumentCache().getDocumentMap()
                .get(note.getDocumentId());
        Chapter chapter = book.getChaptersMap().get(((BookNote) note).getChapterId());
        chapter.getNotesList().remove(noteId);

        // Remove the note in the note CACHE.
        CACHE.getNoteCache().getNoteMap().remove(noteId);

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
        for (Document document : CACHE.getDocumentCache().getDocumentMap().values()) {
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
        Book book = (Book) (CACHE.getDocumentCache().getDocumentMap().get(documentId));
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        Map<Long, List<BookNote>> noteMapByChapters = new HashMap<Long, List<BookNote>>();

        for (Chapter chapter : book.getChaptersMap().values()) {
            List<BookNote> notesList = new ArrayList<BookNote>();
            for (Long noteId : chapter.getNotesList()) {
                notesList.add((BookNote) (noteMap.get(noteId)));
            }
            Collections.sort(notesList);
            noteMapByChapters.put(chapter.getChapterId(), notesList);
        }
        return noteMapByChapters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        Book book = (Book) (CACHE.getDocumentCache().getDocumentMap().get(documentId));
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        List<Note> noteList = new ArrayList<Note>();

        for (Chapter chapter : book.getChaptersMap().values()) {
            for (Long noteId : chapter.getNotesList()) {
                noteList.add(noteMap.get(noteId));
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
            Book cachedBook = (Book) (CACHE.getDocumentCache().getDocumentMap().get(documentId));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Document updateDocument(Document document) {
        Book updateBook = (Book) (CACHE.getDocumentCache().getDocumentMap().get(document
                .getDocumentId()));
        if (updateBook != null) {
            CACHE.getDocumentCache().getDocumentTitleIdMap()
                    .remove(updateBook.getDocumentTitle());
            updateBook.setDocumentTitle(document.getDocumentTitle());
            updateBook.setAuthorsList(((Book) document).getAuthorsList());
            updateBook.setComment(document.getComment());
            updateBook.setEdition(((Book) document).getEdition());
            updateBook.setPublishedYear(((Book) document).getPublishedYear());
            updateBook.setIsbn(((Book) document).getIsbn());
            updateBook.setChaptersMap(((Book) document).getChaptersMap());
            if (((Book) document).getLastUpdatedTime() == null) {
                updateBook.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                updateBook.setLastUpdatedTime(((Book) document).getLastUpdatedTime());
            }
            CACHE.getDocumentCache().getDocumentTitleIdMap()
                    .put(updateBook.getDocumentTitle(), updateBook.getDocumentId());
            return updateBook;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note updateNote(Note note) {
        Book book = (Book) (CACHE.getDocumentCache().getDocumentMap().get(note
                .getDocumentId()));
        BookNote cachedNote = (BookNote) (CACHE.getNoteCache().getNoteMap().get(note
                .getNoteId()));
        if (cachedNote != null) {
            cachedNote.setDocumentId(note.getDocumentId());
            Long oldChapterId = cachedNote.getChapterId();
            cachedNote.setChapterId(((BookNote) note).getChapterId());
            cachedNote.setTagIds(note.getTagIds());
            cachedNote.setNoteText(note.getNoteText());

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
        return null;
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
            Book cachedBook = (Book) CACHE.getDocumentCache().getDocumentMap()
                    .get(documentId);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Document saveDocument(Document document) {
        if (document instanceof Book) {
            Book newBook = new Book();
            if (document.getDocumentId() == null) {
                newBook.setDocumentId(CACHE.getDocumentCache().getMaxDocumentId() + 1L);
            } else {
                newBook.setDocumentId(document.getDocumentId());
            }
            newBook.setDocumentTitle(document.getDocumentTitle());
            newBook.setAuthorsList(((Book) document).getAuthorsList());
            newBook.setComment(document.getComment());
            newBook.setEdition(((Book) document).getEdition());
            newBook.setPublishedYear(((Book) document).getPublishedYear());
            newBook.setIsbn(((Book) document).getIsbn());
            newBook.setChaptersMap(((Book) document).getChaptersMap());
            if (((Book) document).getCreatedTime() == null) {
                newBook.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newBook.setCreatedTime(((Book) document).getCreatedTime());
            }
            if (((Book) document).getLastUpdatedTime() == null) {
                newBook.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                newBook.setLastUpdatedTime(((Book) document).getLastUpdatedTime());
            }

            // Add the document to document CACHE.
            try {
                if (CACHE.getDocumentCache().getDocumentMap()
                        .containsKey(newBook.getDocumentId())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document ID!");
                }
                if (CACHE.getDocumentCache().getDocumentTitleIdMap()
                        .containsKey(newBook.getDocumentTitle())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document title!");
                }

                CACHE.getDocumentCache().getDocumentMap()
                        .put(newBook.getDocumentId(), newBook);
                CACHE.getDocumentCache().getDocumentTitleIdMap()
                        .put(newBook.getDocumentTitle(), newBook.getDocumentId());

                // Update the max document ID in document CACHE.
                if (CACHE.getDocumentCache().getMaxDocumentId() < newBook.getDocumentId()) {
                    CACHE.getDocumentCache().setMaxDocumentId(newBook.getDocumentId());
                }

                return newBook;
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note saveNote(Note note) {
        if (note instanceof BookNote) {
            BookNote newNote = new BookNote();
            if (note.getNoteId() == null) {
                newNote.setNoteId(CACHE.getNoteCache().getMaxNoteId() + 1L);
            } else {
                newNote.setNoteId(note.getNoteId());
            }
            newNote.setDocumentId(note.getDocumentId());
            newNote.setChapterId(((BookNote) note).getChapterId());
            newNote.setTagIds(note.getTagIds());
            newNote.setNoteText(note.getNoteText());
            if (note.getCreatedTime() == null) {
                newNote.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newNote.setCreatedTime(note.getCreatedTime());
            }

            // Add the note to note CACHE.
            try {
                if (CACHE.getNoteCache().getNoteMap().containsKey(newNote.getNoteId())) {
                    throw new DuplicateRecordException("Duplicate note exception: same note ID!");
                }
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
            CACHE.getNoteCache().getNoteMap().put(newNote.getNoteId(), newNote);

            // Update the max note ID in note CACHE.
            if (CACHE.getNoteCache().getMaxNoteId() < newNote.getNoteId()) {
                CACHE.getNoteCache().setMaxNoteId(newNote.getNoteId());
            }

            // Add the note ID to corresponding notes list.
            Book book = (Book) (CACHE.getDocumentCache().getDocumentMap().get(newNote
                    .getDocumentId()));
            Chapter chapter = book.getChaptersMap().get(newNote.getChapterId());
            chapter.getNotesList().add(newNote.getNoteId());

            // Update book's last updated time.
            book.setLastUpdatedTime(new Date());

            return newNote;
        }
        return null;
    }
}

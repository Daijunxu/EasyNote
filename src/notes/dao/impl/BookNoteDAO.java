package notes.dao.impl;

import notes.dao.DuplicateRecordException;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Data access object for book notes.
 *
 * @author Rui Du
 */
public class BookNoteDAO extends AbstractNoteDAO {

    /**
     * Deletes a chapter and all its notes.
     *
     * @param chapter    The chapter to delete.
     * @param documentId The document ID of the document that the chapter belongs to.
     */
    public void deleteChapter(Chapter chapter, Long documentId) {
        Book cachedBook = (Book) Cache.get().getDocumentCache().getDocumentMap().get(documentId);
        Chapter cachedChapter = cachedBook.getChaptersMap().get(chapter.getChapterId());
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();

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
        Book cachedBook = (Book) Cache.get().getDocumentCache().getDocumentMap()
                .get(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
        Map<Long, Chapter> chaptersMap = cachedBook.getChaptersMap();
        for (Chapter chapter : chaptersMap.values()) {
            for (Long noteId : chapter.getNotesList()) {
                noteMap.remove(noteId);
            }
        }

        // Remove the document in the document cache.
        Cache.get().getDocumentCache().getDocumentMap().remove(cachedBook.getDocumentId());
        Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .remove(cachedBook.getDocumentTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Book book = (Book) Cache.get().getDocumentCache().getDocumentMap()
                .get(note.getDocumentId());
        Chapter chapter = book.getChaptersMap().get(((BookNote) note).getChapterId());
        chapter.getNotesList().remove(noteId);

        // Remove the note in the note cache.
        Cache.get().getNoteCache().getNoteMap().remove(noteId);

        // Update book's last updated time.
        book.setLastUpdatedTime(new Date());
    }

    /**
     * Finds all notes in the document, grouped by chapter IDs.
     *
     * @param documentId The document ID.
     * @return {@code Map} All notes in the document grouped by chapter IDs.
     */
    public Map<Long, List<BookNote>> findAllNotesByChapters(Long documentId) {
        Book book = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
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
        Book book = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
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
     * Merges a chapter.
     *
     * @param chapter    The chapter to merge.
     * @param documentId The document ID of the book that the chapter belongs to.
     * @return {@code Chapter} The merged chapter object.
     */
    public Chapter mergeChapter(Chapter chapter, Long documentId) {
        try {
            Book cachedBook = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
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
    public Document mergeDocument(Document document) {
        Book updateBook = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(document
                .getDocumentId()));
        if (updateBook != null) {
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
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
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
                    .put(updateBook.getDocumentTitle(), updateBook.getDocumentId());
            return updateBook;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note mergeNote(Note note) {
        Book book = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(note
                .getDocumentId()));
        BookNote cachedNote = (BookNote) (Cache.get().getNoteCache().getNoteMap().get(note
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
            Book cachedBook = (Book) Cache.get().getDocumentCache().getDocumentMap()
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
                newBook.setDocumentId(Cache.get().getDocumentCache().getMaxDocumentId() + 1L);
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

            // Add the document to document cache.
            try {
                if (Cache.get().getDocumentCache().getDocumentMap()
                        .containsKey(newBook.getDocumentId())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document ID!");
                }
                if (Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .containsKey(newBook.getDocumentTitle())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document title!");
                }

                Cache.get().getDocumentCache().getDocumentMap()
                        .put(newBook.getDocumentId(), newBook);
                Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .put(newBook.getDocumentTitle(), newBook.getDocumentId());

                // Update the max document ID in document cache.
                if (Cache.get().getDocumentCache().getMaxDocumentId() < newBook.getDocumentId()) {
                    Cache.get().getDocumentCache().setMaxDocumentId(newBook.getDocumentId());
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
                newNote.setNoteId(Cache.get().getNoteCache().getMaxNoteId() + 1L);
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

            // Add the note to note cache.
            try {
                if (Cache.get().getNoteCache().getNoteMap().containsKey(newNote.getNoteId())) {
                    throw new DuplicateRecordException("Duplicate note exception: same note ID!");
                }
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
            Cache.get().getNoteCache().getNoteMap().put(newNote.getNoteId(), newNote);

            // Update the max note ID in note cache.
            if (Cache.get().getNoteCache().getMaxNoteId() < newNote.getNoteId()) {
                Cache.get().getNoteCache().setMaxNoteId(newNote.getNoteId());
            }

            // Add the note ID to corresponding notes list.
            Book book = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(newNote
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

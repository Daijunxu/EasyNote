package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
import notes.businessobjects.book.Book;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.book.Chapter;
import notes.dao.DuplicateRecordException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code BookNoteDAO}.
 * <p/>
 * Author: Rui Du
 */
public class BookNoteDAOUnitTest extends EasyNoteUnitTestCase {

    private BookNoteDAO dao = BookNoteDAO.get();

    @Test
    public void testDeleteChapter() {
        UnitTestData testData = new UnitTestData();
        Book book = testData.getBook();
        Long documentId = book.getDocumentId();
        Chapter deleteChapter = testData.getChapter();

        dao.deleteChapter(deleteChapter, documentId);
        assertNotNull(CACHE.getDocumentCache().find(documentId));
        Book cachedBook = (Book) CACHE.getDocumentCache().find(documentId);
        assertFalse(cachedBook.getChaptersMap().isEmpty());
        assertFalse(cachedBook.getChaptersMap().containsKey(documentId));
        assertNull(CACHE.getNoteCache().find(documentId));
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testDeleteDocument() {
        UnitTestData testData = new UnitTestData();
        Book deleteDocument = testData.getBook();
        BookNote bookNote = testData.getBookNote();
        dao.deleteDocument(deleteDocument);
        assertFalse(CACHE.getDocumentCache().findAll().isEmpty());
        assertNull(CACHE.getDocumentCache().find(deleteDocument.getDocumentId()));
        assertNull(CACHE.getNoteCache().find(bookNote.getNoteId()));
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testDeleteNote() {
        UnitTestData testData = new UnitTestData();
        BookNote deleteNote = testData.getBookNote();
        dao.deleteNote(deleteNote);
        assertFalse(CACHE.getNoteCache().findAll().isEmpty());
        assertNull(CACHE.getNoteCache().find(deleteNote.getNoteId()));
        Book book = (Book) CACHE.getDocumentCache().find(deleteNote.getDocumentId());
        assertFalse(book.getChaptersMap().get(deleteNote.getChapterId()).getNotesList()
                .contains(deleteNote.getNoteId()));
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testFindAllNotesByChapters() {
        UnitTestData testData = new UnitTestData();
        Book book = testData.getBook();
        Map<Long, List<BookNote>> noteMap = dao.findAllNotesByChapters(book.getDocumentId());
        assertNotNull(noteMap);
        assertTrue(noteMap.size() == book.getChaptersMap().size());
        assertTrue(noteMap.containsKey(1L));
        assertNotNull(noteMap.get(1L));
        assertTrue(noteMap.get(1L).size() == 1);
        assertTrue(noteMap.get(2L).isEmpty());
        assertEquals(testData.noteMap.get(noteMap.get(1L).get(0).getNoteId()),
                noteMap.get(1L).get(0));
    }

    @Test
    public void testFindAllNotesByDocumentId() {
        UnitTestData testData = new UnitTestData();
        List<Note> noteList = dao.findAllNotesByDocumentId(1L);
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));
    }

    @Test
    public void testUpdateChapter() {
        UnitTestData testData = new UnitTestData();
        Book testBook = testData.getBook();
        Chapter testChapter = testData.getChapter();
        Chapter updateChapter = new Chapter();
        updateChapter.setChapterId(testChapter.getChapterId());
        updateChapter.setChapterTitle("Another chapter title");
        updateChapter.setNotesList(testChapter.getNotesList());
        Chapter updatedChapter = dao.updateChapter(updateChapter, testBook.getDocumentId());

        assertNotNull(updatedChapter);
        Book cachedBook = (Book) CACHE.getDocumentCache().find(testBook.getDocumentId());
        assertEquals(updatedChapter, cachedBook.getChaptersMap().get(updatedChapter.getChapterId()));
        assertFalse(updatedChapter.equals(testChapter));
        assertEquals(updatedChapter.getChapterId(), updateChapter.getChapterId());
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testUpdateDocument() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        Book testBook = testData.getBook();
        Book newBook = new Book();
        newBook.setDocumentId(testBook.getDocumentId());
        newBook.setDocumentTitle(testBook.getDocumentTitle());
        newBook.setAuthorsList(testBook.getAuthorsList());
        newBook.setComment("This book is not worth reading!");
        newBook.setEdition(testBook.getEdition());
        newBook.setPublishedYear(testBook.getPublishedYear());
        newBook.setIsbn(testBook.getIsbn());
        newBook.setChaptersMap(testBook.getChaptersMap());
        newBook.getChaptersMap().put(2L, new Chapter(2L, "Second Chapter", new ArrayList<Long>()));
        Book updatedBook = (Book) dao.updateDocument(newBook);

        assertNotNull(updatedBook);
        assertEquals(updatedBook, CACHE.getDocumentCache().find(newBook.getDocumentId()));
        assertFalse(updatedBook.equals(testBook));
        assertEquals(updatedBook.getComment(), newBook.getComment());
        assertEquals(updatedBook.getChaptersMap(), newBook.getChaptersMap());
        assertEquals(testBook.getDocumentId(), updatedBook.getDocumentId());
        assertEquals(testBook.getCreatedTime(), updatedBook.getCreatedTime());
        assertTrue(testBook.getLastUpdatedTime().compareTo(updatedBook.getLastUpdatedTime()) < 0);
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testUpdateNote() {
        UnitTestData testData = new UnitTestData();
        BookNote testBookNote = testData.getBookNote();
        BookNote newBookNote = new BookNote();
        newBookNote.setNoteId(testBookNote.getNoteId());
        newBookNote.setDocumentId(testBookNote.getDocumentId());
        newBookNote.setChapterId(2L);
        newBookNote.setTagIds(testBookNote.getTagIds());
        newBookNote.setNoteText(testBookNote.getNoteText());
        BookNote updatedBookNote = (BookNote) dao.updateNote(newBookNote);

        assertNotNull(updatedBookNote);
        assertEquals(updatedBookNote, CACHE.getNoteCache().find(newBookNote.getNoteId()));
        assertFalse(updatedBookNote.equals(testBookNote));
        assertEquals(updatedBookNote.getChapterId(), newBookNote.getChapterId());
        assertFalse(updatedBookNote.getChapterId().equals(testBookNote.getChapterId()));
        assertEquals(testBookNote.getNoteId(), updatedBookNote.getNoteId());
        assertEquals(testBookNote.getCreatedTime(), updatedBookNote.getCreatedTime());
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testSaveChapter() {
        Chapter newChapter = new Chapter();
        newChapter.setChapterId(3L);
        newChapter.setChapterTitle("Chapter 3");
        Chapter savedChapter = dao.saveChapter(newChapter, 1L);
        Book book = (Book) CACHE.getDocumentCache().find(1L);

        assertEquals(savedChapter, book.getChaptersMap().get(3L));
        assertNotNull(savedChapter.getNotesList());
        assertTrue(savedChapter.getNotesList().isEmpty());
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testSaveDocument() throws DuplicateRecordException {
        Book newBook = new Book();
        newBook.setDocumentId(4L);
        newBook.setDocumentTitle("Data Mining");
        newBook.setAuthorsList(new ArrayList<String>(Arrays.asList("Author")));
        newBook.setComment("Good book.");
        newBook.setEdition(2);
        newBook.setPublishedYear(2007);
        newBook.setIsbn("111-1-1111-1111-1");
        TreeMap<Long, Chapter> chaptersMap = new TreeMap<Long, Chapter>();
        Chapter chapter1 = new Chapter(1L, "Chapter 1", new ArrayList<Long>());
        chapter1.getNotesList().add(1L);
        chaptersMap.put(1L, chapter1);
        newBook.setChaptersMap(chaptersMap);
        Book savedBook = (Book) dao.saveDocument(newBook);

        assertEquals(savedBook, CACHE.getDocumentCache().find(newBook.getDocumentId()));
        assertNotNull(savedBook.getCreatedTime());
        assertNotNull(savedBook.getLastUpdatedTime());
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testSaveNote() {
        BookNote newBookNote = new BookNote();
        newBookNote.setNoteId(4L);
        newBookNote.setDocumentId(1L);
        newBookNote.setChapterId(2L);
        newBookNote.setTagIds(new ArrayList<Long>(Arrays.asList(2L)));
        newBookNote.setNoteText("New note.");
        BookNote savedBookNote = (BookNote) dao.saveNote(newBookNote);

        assertEquals(savedBookNote, CACHE.getNoteCache().find(newBookNote.getNoteId()));
        assertNotNull(savedBookNote.getCreatedTime());
        Book book = (Book) CACHE.getDocumentCache().find(savedBookNote.getDocumentId());
        Chapter chapter = book.getChaptersMap().get(savedBookNote.getChapterId());
        assertTrue(chapter.getNotesList().contains(savedBookNote.getNoteId()));
        assertTrue(CACHE.isCacheChanged());
    }
}

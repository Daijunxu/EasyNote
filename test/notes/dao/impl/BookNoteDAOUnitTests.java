/**
 *
 */
package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.book.Book;
import notes.book.BookNote;
import notes.book.Chapter;
import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.entity.Note;
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
 *
 * @author Rui Du
 * @version 1.0
 */
public class BookNoteDAOUnitTests extends EasyNoteUnitTestCase {

    /**
     * Data required for unit tests. NOTE: A new instance should be created for each unit test.
     */
    public static class UnitTestData extends CacheUnitTests.UnitTestData {
    }

    /**
     * The data access object for the {@code BookNoteDAO}.
     */
    private BookNoteDAO dao = new BookNoteDAO();

    /**
     * Test method for
     * {@link notes.dao.impl.BookNoteDAO#deleteChapter(notes.book.Chapter, java.lang.Long)}.
     */
    @Test
    public void testDeleteChapter() {
        UnitTestData testData = new UnitTestData();
        Book book = (Book) testData.documentMap.get(1L);
        Chapter deleteChapter = book.getChaptersMap().get(1L);
        dao.deleteChapter(deleteChapter, 1L);
        assertNotNull(Cache.get().getDocumentCache().getDocumentMap().get(1L));
        Book cachedBook = (Book) Cache.get().getDocumentCache().getDocumentMap().get(1L);
        assertFalse(cachedBook.getChaptersMap().isEmpty());
        assertFalse(cachedBook.getChaptersMap().containsKey(1L));
        assertFalse(Cache.get().getNoteCache().getNoteMap().containsKey(1L));
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#deleteDocument(notes.entity.Document)}.
     */
    @Test
    public void testDeleteDocument() {
        UnitTestData testData = new UnitTestData();
        Book deleteDocument = (Book) testData.documentMap.get(1L);
        dao.deleteDocument(deleteDocument);
        assertNotNull(Cache.get().getDocumentCache().getDocumentMap());
        assertNotNull(Cache.get().getDocumentCache().getDocumentTitleIdMap());
        assertFalse(Cache.get().getDocumentCache().getDocumentMap().isEmpty());
        assertFalse(Cache.get().getDocumentCache().getDocumentTitleIdMap().isEmpty());
        assertNull(Cache.get().getDocumentCache().getDocumentMap()
                .get(deleteDocument.getDocumentId()));
        assertFalse(Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .containsKey(deleteDocument.getDocumentTitle()));
        assertFalse(Cache.get().getNoteCache().getNoteMap().containsKey(1L));
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#deleteNote(notes.entity.Note)}.
     */
    @Test
    public void testDeleteNote() {
        UnitTestData testData = new UnitTestData();
        BookNote deleteNote = (BookNote) testData.noteMap.get(1L);
        dao.deleteNote(deleteNote);
        assertNotNull(Cache.get().getNoteCache().getNoteMap());
        assertFalse(Cache.get().getNoteCache().getNoteMap().isEmpty());
        assertNull(Cache.get().getNoteCache().getNoteMap().get(deleteNote.getNoteId()));
        Book book = (Book) Cache.get().getDocumentCache().getDocumentMap()
                .get(deleteNote.getDocumentId());
        assertFalse(book.getChaptersMap().get(deleteNote.getChapterId()).getNotesList()
                .contains(deleteNote.getNoteId()));
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#findAllNotesByChapters(java.lang.Long)}.
     */
    @Test
    public void testFindAllNotesByChapters() {
        UnitTestData testData = new UnitTestData();
        Map<Long, List<BookNote>> noteMap = dao.findAllNotesByChapters(1L);
        assertNotNull(noteMap);
        assertTrue(noteMap.size() == 2);
        assertTrue(noteMap.containsKey(1L));
        assertNotNull(noteMap.get(1L));
        assertTrue(noteMap.get(1L).size() == 1);
        assertTrue(noteMap.get(2L).isEmpty());
        assertEquals(testData.noteMap.get(noteMap.get(1L).get(0).getNoteId()),
                noteMap.get(1L).get(0));
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#findAllNotesByDocumentId(java.lang.Long)}.
     */
    @Test
    public void testFindAllNotesByDocumentId() {
        UnitTestData testData = new UnitTestData();
        List<Note> noteList = dao.findAllNotesByDocumentId(1L);
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));
    }

    /**
     * Test method for
     * {@link notes.dao.impl.BookNoteDAO#mergeChapter(notes.book.Chapter, java.lang.Long)}.
     */
    @Test
    public void testMergeChapter() {
        UnitTestData testData = new UnitTestData();
        Book testBook = (Book) testData.documentMap.get(1L);
        Chapter testChapter = testBook.getChaptersMap().get(1L);
        Chapter updateChapter = new Chapter();
        updateChapter.setChapterId(testChapter.getChapterId());
        updateChapter.setChapterTitle("Another chapter title");
        updateChapter.setNotesList(testChapter.getNotesList());
        Chapter mergedChapter = dao.mergeChapter(updateChapter, testBook.getDocumentId());

        assertNotNull(mergedChapter);
        Book cachedBook = (Book) Cache.get().getDocumentCache().getDocumentMap()
                .get(testBook.getDocumentId());
        assertEquals(mergedChapter, cachedBook.getChaptersMap().get(mergedChapter.getChapterId()));
        assertFalse(mergedChapter.equals(testChapter));
        assertEquals(mergedChapter.getChapterId(), updateChapter.getChapterId());
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#mergeDocument(notes.entity.Document)}.
     */
    @Test
    public void testMergeDocument() {
        UnitTestData testData = new UnitTestData();
        Book testBook = (Book) testData.documentMap.get(1L);
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
        Book updatedBook = (Book) dao.mergeDocument(newBook);

        assertNotNull(updatedBook);
        assertEquals(updatedBook,
                (Book) Cache.get().getDocumentCache().getDocumentMap().get(newBook.getDocumentId()));
        assertFalse(updatedBook.equals(testBook));
        assertEquals(updatedBook.getComment(), newBook.getComment());
        assertEquals(updatedBook.getChaptersMap(), newBook.getChaptersMap());
        assertEquals(testBook.getDocumentId(), updatedBook.getDocumentId());
        assertEquals(testBook.getCreatedTime(), updatedBook.getCreatedTime());
        assertTrue(testBook.getLastUpdatedTime().compareTo(updatedBook.getLastUpdatedTime()) < 0);
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#mergeNote(notes.entity.Note)}.
     */
    @Test
    public void testMergeNote() {
        UnitTestData testData = new UnitTestData();
        BookNote testBookNote = (BookNote) testData.noteMap.get(1L);
        BookNote newBookNote = new BookNote();
        newBookNote.setNoteId(testBookNote.getNoteId());
        newBookNote.setDocumentId(testBookNote.getDocumentId());
        newBookNote.setChapterId(2L);
        newBookNote.setTagIds(testBookNote.getTagIds());
        newBookNote.setNoteText(testBookNote.getNoteText());
        BookNote updatedBookNote = (BookNote) dao.mergeNote(newBookNote);

        assertNotNull(updatedBookNote);
        assertEquals(updatedBookNote,
                (BookNote) Cache.get().getNoteCache().getNoteMap().get(newBookNote.getNoteId()));
        assertFalse(updatedBookNote.equals(testBookNote));
        assertEquals(updatedBookNote.getChapterId(), newBookNote.getChapterId());
        assertFalse(updatedBookNote.getChapterId() == testBookNote.getChapterId());
        assertEquals(testBookNote.getNoteId(), updatedBookNote.getNoteId());
        assertEquals(testBookNote.getCreatedTime(), updatedBookNote.getCreatedTime());
        Cache.get().reload();
    }

    /**
     * Test method for
     * {@link notes.dao.impl.BookNoteDAO#saveChapter(notes.book.Chapter, java.lang.Long)}.
     */
    @Test
    public void testSaveChapter() {
        Chapter newChapter = new Chapter();
        newChapter.setChapterId(3L);
        newChapter.setChapterTitle("Chapter 3");
        Chapter savedChapter = dao.saveChapter(newChapter, 1L);
        Book book = (Book) Cache.get().getDocumentCache().getDocumentMap().get(1L);

        assertEquals(savedChapter, book.getChaptersMap().get(3L));
        assertNotNull(savedChapter.getNotesList());
        assertTrue(savedChapter.getNotesList().isEmpty());
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#saveDocument(notes.entity.Document)}.
     */
    @Test
    public void testSaveDocument() {
        Book newBook = new Book();
        newBook.setDocumentId(3L);
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

        assertEquals(savedBook,
                Cache.get().getDocumentCache().getDocumentMap().get(newBook.getDocumentId()));
        assertTrue(Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .containsKey(newBook.getDocumentTitle()));
        assertEquals(Cache.get().getDocumentCache().getMaxDocumentId(), newBook.getDocumentId());
        assertNotNull(savedBook.getCreatedTime());
        assertNotNull(savedBook.getLastUpdatedTime());
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.dao.impl.BookNoteDAO#saveNote(notes.entity.Note)}.
     */
    @Test
    public void testSaveNote() {
        BookNote newBookNote = new BookNote();
        newBookNote.setNoteId(3L);
        newBookNote.setDocumentId(1L);
        newBookNote.setChapterId(2L);
        newBookNote.setTagIds(new ArrayList<Long>(Arrays.asList(2L)));
        newBookNote.setNoteText("New note.");
        BookNote savedBookNote = (BookNote) dao.saveNote(newBookNote);

        assertEquals(savedBookNote,
                Cache.get().getNoteCache().getNoteMap().get(newBookNote.getNoteId()));
        assertEquals(Cache.get().getNoteCache().getMaxNoteId(), newBookNote.getNoteId());
        assertNotNull(savedBookNote.getCreatedTime());
        Book book = (Book) Cache.get().getDocumentCache().getDocumentMap()
                .get(savedBookNote.getDocumentId());
        Chapter chapter = book.getChaptersMap().get(savedBookNote.getChapterId());
        assertTrue(chapter.getNotesList().contains(savedBookNote.getNoteId()));
        Cache.get().reload();
    }
}

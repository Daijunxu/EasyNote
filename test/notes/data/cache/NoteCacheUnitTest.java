package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
import notes.businessobjects.article.ArticleNote;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.workset.WorksheetNote;
import notes.dao.DuplicateRecordException;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code NoteCache}.
 * <p/>
 * Author: Rui Du
 */
public class NoteCacheUnitTest extends EasyNoteUnitTestCase {

    private final NoteCache noteCache = NoteCache.get();

    @Test
    public void testInitiate() {
        noteCache.initialize();
        assertNotNull(noteCache);
        assertTrue(noteCache.findAll().isEmpty());
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element noteCacheElement = noteCache.toXMLElement();
        assertEquals(noteCacheElement.getName(), "Notes");
        assertNotNull(noteCacheElement.elements());
        assertEquals(noteCacheElement.elements().size(), testData.noteMap.size());
        for (Element element : noteCacheElement.elements()) {
            assertTrue(element.getName().equals("BookNote")
                    || element.getName().equals("ArticleNote")
                    || element.getName().equals("WorksheetNote"));
        }
    }

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element noteCacheElement = noteCache.toXMLElement();
        noteCache.buildFromXMLElement(noteCacheElement);

        List<Note> noteList = CACHE.getNoteCache().findAll();

        assertEquals(noteList.size(), testData.noteMap.size());

        for (Note note : noteList) {
            assertEquals(note, testData.noteMap.get(note.getNoteId()));
        }
    }

    @Test
    public void testInsertBookNote() throws DuplicateRecordException {
        BookNote note = new BookNote();
        note.setNoteText("New BookNote");

        Note cachedNote = noteCache.insert(note);

        assertNotNull(cachedNote);
        assertNotNull(cachedNote.getNoteId());
        assertEquals(note.getNoteText(), cachedNote.getNoteText());
    }

    @Test
    public void testInsertArticleNote() throws DuplicateRecordException {
        ArticleNote note = new ArticleNote();
        note.setNoteText("New ArticleNote");

        Note cachedNote = noteCache.insert(note);

        assertNotNull(cachedNote);
        assertNotNull(cachedNote.getNoteId());
        assertEquals(note.getNoteText(), cachedNote.getNoteText());
    }

    @Test
    public void testInsertWorksheetNote() throws DuplicateRecordException {
        WorksheetNote note = new WorksheetNote();
        note.setNoteText("New WorksheetNote");

        Note cachedNote = noteCache.insert(note);

        assertNotNull(cachedNote);
        assertNotNull(cachedNote.getNoteId());
        assertEquals(note.getNoteText(), cachedNote.getNoteText());
    }

    @Test
    public void testRemove() {
        UnitTestData testData = new UnitTestData();
        Note note = testData.getBookNote();

        noteCache.remove(note.getNoteId());

        assertNull(noteCache.find(note.getNoteId()));
    }

    @Test
    public void testUpdateBookNote() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        BookNote note = testData.getBookNote();
        note.setNoteText("Updated BookNote");

        Note cachedNote = noteCache.update(note);

        assertEquals(note.getDocumentId(), cachedNote.getDocumentId());
        assertEquals(note.getNoteText(), cachedNote.getNoteText());
    }

    @Test
    public void testUpdateArticleNote() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        ArticleNote note = testData.getArticleNote();
        note.setNoteText("Updated ArticleNote");

        Note cachedNote = noteCache.update(note);

        assertEquals(note.getDocumentId(), cachedNote.getDocumentId());
        assertEquals(note.getNoteText(), cachedNote.getNoteText());
    }

    @Test
    public void testUpdateWorksheetNote() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        WorksheetNote note = testData.getWorksheetNote();
        note.setNoteText("Updated WorksheetNote");

        Note cachedNote = noteCache.update(note);

        assertEquals(note.getDocumentId(), cachedNote.getDocumentId());
        assertEquals(note.getNoteText(), cachedNote.getNoteText());
    }

    @Test
    public void testFind() {
        UnitTestData testData = new UnitTestData();
        Note note = testData.getWorksheetNote();

        Note cachedNote = noteCache.find(note.getNoteId());

        assertEquals(note, cachedNote);
    }

    @Test
    public void testFindAll() {
        UnitTestData testData = new UnitTestData();

        List<Note> noteList = noteCache.findAll();

        assertEquals(testData.noteMap.size(), noteList.size());
        for (Note note : noteList) {
            assertNotNull(testData.noteMap.get(note.getNoteId()));
            assertEquals(note, testData.noteMap.get(note.getNoteId()));
        }
    }
}

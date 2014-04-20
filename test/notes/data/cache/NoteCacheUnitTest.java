package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
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
    public void testClear() {
        noteCache.clear();
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
    public void testInsertBookNote() {
        // TODO
    }

    @Test
    public void testInsertArticleNote() {
        // TODO
    }

    @Test
    public void testInsertWorksheetNote() {
        // TODO
    }

    @Test
    public void testRemove() {
        UnitTestData testData = new UnitTestData();
        Note note = testData.getBookNote();

        noteCache.remove(note.getNoteId());

        assertNull(noteCache.find(note.getNoteId()));
    }

    @Test
    public void testUpdateBookNote() {
        // TODO
    }

    @Test
    public void testUpdateArticleNote() {
        // TODO
    }

    @Test
    public void testUpdateWorksheetNote() {
        // TODO
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

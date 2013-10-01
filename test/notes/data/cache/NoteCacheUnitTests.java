/**
 *
 */
package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.entity.Note;
import org.dom4j.Element;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code NoteCache}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class NoteCacheUnitTests extends EasyNoteUnitTestCase {

    private final NoteCache noteCache = NoteCache.get();

    /**
     * Test method for {@link notes.data.cache.NoteCache#clear()}.
     */
    @Test
    public void testClear() {
        noteCache.clear();
        assertNotNull(noteCache);
        assertTrue(noteCache.getNoteMap().isEmpty());
        assertTrue(noteCache.getMaxNoteId() == Long.MIN_VALUE);
    }

    /**
     * Test method for {@link notes.data.cache.NoteCache#getMaxNoteId()}.
     */
    @Test
    public void testGetMaxNoteId() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(noteCache.getMaxNoteId());
        assertEquals(testData.maxNoteId, noteCache.getMaxNoteId());
    }

    /**
     * Test method for {@link notes.data.cache.NoteCache#getNoteMap()}.
     */
    @Test
    public void testGetNoteMap() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(noteCache.getNoteMap());
        Map<Long, Note> noteMap = noteCache.getNoteMap();
        assertFalse(noteMap.isEmpty());
        assertEquals(testData.noteMap.keySet(), noteMap.keySet());
        for (Note note : testData.noteMap.values()) {
            assertEquals(note, noteMap.get(note.getNoteId()));
        }
    }

    /**
     * Test method for {@link notes.data.cache.NoteCache#toXMLElement()}.
     */
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

    /**
     * Test method for {@link notes.data.cache.NoteCache#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element noteCacheElement = noteCache.toXMLElement();
        noteCache.buildFromXMLElement(noteCacheElement);

        assertEquals(noteCache.getNoteMap(), testData.noteMap);
        assertEquals(noteCache.getMaxNoteId(), testData.maxNoteId);
    }
}

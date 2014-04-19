package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code NoteCache}.
 * <p/>
 * Author: Rui Du
 */
public class NoteCacheUnitTest extends EasyNoteUnitTestCase {

    private final NoteCache noteCache = NoteCache.get();

    /**
     * Test method for {@link notes.data.cache.NoteCache#clear()}.
     */
    @Test
    public void testClear() {
        noteCache.clear();
        assertNotNull(noteCache);
        assertTrue(noteCache.findAll().isEmpty());
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

        List<Note> noteList = CACHE.getNoteCache().findAll();

        assertEquals(noteList.size(), testData.noteMap.size());

        for (Note note : noteList) {
            assertEquals(note, testData.noteMap.get(note.getNoteId()));
        }
    }
}

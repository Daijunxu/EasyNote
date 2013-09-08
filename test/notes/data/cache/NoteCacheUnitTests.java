/**
 *
 */
package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.entity.Note;
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

    /**
     * Test method for {@link notes.data.cache.NoteCache#clear()}.
     */
    @Test
    public void testClear() {
        Cache.get().getNoteCache().clear();
        assertNotNull(Cache.get().getNoteCache());
        assertTrue(Cache.get().getNoteCache().getNoteMap().isEmpty());
        assertTrue(Cache.get().getNoteCache().getMaxNoteId() == Long.MIN_VALUE);
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.data.cache.NoteCache#getMaxNoteId()}.
     */
    @Test
    public void testGetMaxNoteId() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(Cache.get().getNoteCache().getMaxNoteId());
        assertEquals(testData.maxNoteId, Cache.get().getNoteCache().getMaxNoteId());
    }

    /**
     * Test method for {@link notes.data.cache.NoteCache#getNoteMap()}.
     */
    @Test
    public void testGetNoteMap() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(Cache.get().getNoteCache().getNoteMap());
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
        assertFalse(noteMap.isEmpty());
        assertEquals(testData.noteMap.keySet(), noteMap.keySet());
        for (Note note : testData.noteMap.values()) {
            assertEquals(note, noteMap.get(note.getNoteId()));
        }
    }
}

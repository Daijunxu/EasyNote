package notes.entity.impl;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.entity.Note;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code AbstractNote}.
 *
 * @author Rui Du
 */
public class AbstractNoteUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.entity.impl.AbstractNote#compareTo(notes.entity.Note)}.
     */
    @Test
    public void testCompareTo() {
        final UnitTestData testData = new UnitTestData();
        Note testNote1 = testData.noteMap.get(1L);
        Note testNote2 = testData.noteMap.get(2L);
        Note cachedNote = Cache.get().getNoteCache().getNoteMap().get(1L);
        assertEquals(testNote1.compareTo(cachedNote), 0);
        assertTrue(testNote1.compareTo(testNote2) < 0);
        assertTrue(testNote2.compareTo(testNote1) > 0);
    }

}

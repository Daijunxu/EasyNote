/**
 *
 */
package notes.entity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.data.cache.Property;
import notes.entity.Note;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@code AbstractNote}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class AbstractNoteUnitTests {

    /**
     * Data required for unit tests. NOTE: A new instance should be created for each unit test.
     */
    public static class UnitTestData extends CacheUnitTests.UnitTestData {
    }

    /**
     * Load the cache if it has not been initialized.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void initializeCache() throws Exception {
        Property.get().setDataLocation("./test/reading_notes.data");
        Cache.get();
    }

    /**
     * Reload the cache.
     *
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void reloadCache() throws Exception {
        Cache.get().reload();
    }

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

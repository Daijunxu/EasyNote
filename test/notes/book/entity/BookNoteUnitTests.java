/**
 *
 */
package notes.book.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.data.cache.Property;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@code BookNote}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class BookNoteUnitTests {

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
        Property.get().setDataLocation("./resources/testData/reading_notes.data");
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
     * Test method for {@link notes.book.entity.BookNote#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        BookNote book = (BookNote) (testData.noteMap.get(1L));
        assertTrue(book.equals(Cache.get().getNoteCache().getNoteMap().get(1L)));
        assertFalse(book.equals(new BookNote()));
        assertFalse(book.equals(null));
        assertFalse(book.equals(new Object()));
    }

    /**
     * Test method for {@link notes.book.entity.BookNote#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.noteMap.get(1L).hashCode(), Cache.get().getNoteCache().getNoteMap()
                .get(1L).hashCode());
    }

    /**
     * Test method for {@link notes.book.entity.BookNote#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        BookNote testBookNote = (BookNote) (testData.noteMap.get(1L));
        BookNote cachedBookNote = (BookNote) (Cache.get().getNoteCache().getNoteMap().get(1L));
        assertEquals(StringUtils.substringAfter(testBookNote.toString(), "["),
                StringUtils.substringAfter(cachedBookNote.toString(), "["));
    }

}

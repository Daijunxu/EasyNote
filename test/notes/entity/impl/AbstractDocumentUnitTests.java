/**
 *
 */
package notes.entity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.data.cache.Property;
import notes.entity.Document;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@code AbstractDocument}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class AbstractDocumentUnitTests {

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
     * Test method for {@link notes.entity.impl.AbstractDocument#compareTo(notes.entity.Document)}.
     */
    @Test
    public void testCompareTo() {
        final UnitTestData testData = new UnitTestData();
        Document testDocument1 = testData.documentMap.get(1L);
        Document testDocument2 = testData.documentMap.get(2L);
        Document cachedDocument = Cache.get().getDocumentCache().getDocumentMap().get(1L);
        assertEquals(testDocument1.compareTo(cachedDocument), 0);
        assertTrue(testDocument1.compareTo(testDocument2) < 0);
        assertTrue(testDocument2.compareTo(testDocument1) > 0);
    }

}

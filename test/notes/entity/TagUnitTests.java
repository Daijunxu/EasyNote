/**
 *
 */
package notes.entity;

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
 * Unit tests for the {@code Tag}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class TagUnitTests {

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
     * Test method for {@link notes.entity.Tag#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Tag tag = (Tag) (testData.tagIdMap.get(1L));
        assertTrue(tag.equals(Cache.get().getTagCache().getTagIdMap().get(1L)));
        assertFalse(tag.equals(new Tag()));
        assertFalse(tag.equals(null));
        assertFalse(tag.equals(new Object()));
    }

    /**
     * Test method for {@link notes.entity.Tag#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.tagIdMap.get(1L).hashCode(), Cache.get().getTagCache().getTagIdMap()
                .get(1L).hashCode());
    }

    /**
     * Test method for {@link notes.entity.Tag#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Tag testTag = testData.tagIdMap.get(1L);
        Tag cachedTag = Cache.get().getTagCache().getTagIdMap().get(1L);
        assertEquals(StringUtils.substringAfter(testTag.toString(), "["),
                StringUtils.substringAfter(cachedTag.toString(), "["));
    }

}

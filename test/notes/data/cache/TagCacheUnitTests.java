/**
 *
 */
package notes.data.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.data.cache.Property;
import notes.entity.Tag;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@code TagCache}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class TagCacheUnitTests {

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
     * Test method for {@link notes.data.cache.TagCache#clear()}.
     */
    @Test
    public void testClear() {
        Cache.get().getTagCache().clear();
        assertNotNull(Cache.get().getTagCache());
        assertTrue(Cache.get().getTagCache().getTagIdMap().isEmpty());
        assertTrue(Cache.get().getTagCache().getTagTextMap().isEmpty());
        assertTrue(Cache.get().getTagCache().getMaxTagId() == Long.MIN_VALUE);
        Cache.get().reload();
    }

    /**
     * Test method for {@link notes.data.cache.TagCache#getMaxTagId()}.
     */
    @Test
    public void testGetMaxTagId() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(Cache.get().getTagCache().getMaxTagId());
        assertEquals(testData.maxTagId, Cache.get().getTagCache().getMaxTagId());
    }

    /**
     * Test method for {@link notes.data.cache.TagCache#getTagIdMap()}.
     */
    @Test
    public void testGetTagMap() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(Cache.get().getTagCache().getTagIdMap());
        Map<Long, Tag> tagMap = Cache.get().getTagCache().getTagIdMap();
        assertFalse(tagMap.isEmpty());
        assertEquals(testData.tagIdMap.keySet(), tagMap.keySet());
        for (Tag tag : testData.tagIdMap.values()) {
            assertEquals(tag, tagMap.get(tag.getTagId()));
        }
    }

    /**
     * Test method for {@link notes.data.cache.TagCache#getTagTextMap()}.
     */
    @Test
    public void testGetTagTextSet() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(Cache.get().getTagCache().getTagTextMap());
        Map<String, Tag> tagTextMap = Cache.get().getTagCache().getTagTextMap();
        assertFalse(tagTextMap.isEmpty());
        assertEquals(testData.tagTextMap, tagTextMap);
    }
}

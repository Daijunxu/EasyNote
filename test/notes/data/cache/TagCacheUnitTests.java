/**
 *
 */
package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.entity.Tag;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code TagCache}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class TagCacheUnitTests extends EasyNoteUnitTestCase {

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

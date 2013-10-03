package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.entity.Tag;
import org.dom4j.Element;
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
 */
public class TagCacheUnitTests extends EasyNoteUnitTestCase {

    private final TagCache tagCache = TagCache.get();

    /**
     * Test method for {@link notes.data.cache.TagCache#clear()}.
     */
    @Test
    public void testClear() {
        tagCache.clear();
        assertNotNull(tagCache);
        assertTrue(tagCache.getTagIdMap().isEmpty());
        assertTrue(tagCache.getTagTextMap().isEmpty());
        assertTrue(tagCache.getMaxTagId() == Long.MIN_VALUE);
    }

    /**
     * Test method for {@link notes.data.cache.TagCache#getMaxTagId()}.
     */
    @Test
    public void testGetMaxTagId() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(tagCache.getMaxTagId());
        assertEquals(testData.maxTagId, tagCache.getMaxTagId());
    }

    /**
     * Test method for {@link notes.data.cache.TagCache#getTagIdMap()}.
     */
    @Test
    public void testGetTagMap() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(tagCache.getTagIdMap());
        Map<Long, Tag> tagMap = tagCache.getTagIdMap();
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
        assertNotNull(tagCache.getTagTextMap());
        Map<String, Tag> tagTextMap = tagCache.getTagTextMap();
        assertFalse(tagTextMap.isEmpty());
        assertEquals(testData.tagTextMap, tagTextMap);
    }

    /**
     * Test method for {@link notes.data.cache.TagCache#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element tagCacheElement = tagCache.toXMLElement();
        assertEquals(tagCacheElement.getName(), "Tags");
        assertNotNull(tagCacheElement.elements());
        assertEquals(tagCacheElement.elements().size(), testData.tagIdMap.size());
        for (Element element : tagCacheElement.elements()) {
            assertEquals(element.getName(), "Tag");
        }
    }

    /**
     * Test method for {@link notes.data.cache.TagCache#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element tagCacheElement = tagCache.toXMLElement();
        tagCache.buildFromXMLElement(tagCacheElement);

        assertEquals(tagCache.getTagIdMap(), testData.tagIdMap);
        assertEquals(tagCache.getTagTextMap(), testData.tagTextMap);
        assertEquals(tagCache.getMaxTagId(), testData.maxTagId);
    }
}

package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Tag;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code TagCache}.
 * <p/>
 * Author: Rui Du
 */
public class TagCacheUnitTest extends EasyNoteUnitTestCase {

    private final TagCache tagCache = TagCache.get();

    @Test
    public void testInitialize() {
        tagCache.initialize();
        assertNotNull(tagCache);
        assertTrue(tagCache.findAll().isEmpty());
    }

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

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element tagCacheElement = tagCache.toXMLElement();
        tagCache.buildFromXMLElement(tagCacheElement);

        List<Tag> tagList = CACHE.getTagCache().findAll();

        assertEquals(tagList.size(), testData.tagTextMap.size());

        for (Tag tag : tagList) {
            assertEquals(tag, testData.tagIdMap.get(tag.getTagId()));
        }
    }

    @Test
    public void testInsert() {
        Tag tag = new Tag();
        tag.setTagText("New Tag");

        Tag cachedTag = tagCache.insert(tag);

        assertNotNull(cachedTag);
        assertNotNull(cachedTag.getTagId());
        assertEquals(tag.getTagText(), cachedTag.getTagText());
    }

    @Test
    public void testRemove() {
        UnitTestData testData = new UnitTestData();
        Tag tag = testData.getTag();

        tagCache.remove(tag.getTagId());

        assertNull(tagCache.find(tag.getTagId()));
        assertNull(tagCache.find(tag.getTagText()));
    }

    @Test
    public void testUpdate() {
        UnitTestData testData = new UnitTestData();
        Tag tag = testData.getTag();
        tag.setTagText("Updated Text");

        Tag cachedTag = tagCache.update(tag);

        assertEquals(tag.getTagId(), cachedTag.getTagId());
        assertEquals(tag.getTagText(), cachedTag.getTagText());
    }

    @Test
    public void testFindById() {
        UnitTestData testData = new UnitTestData();
        Tag tag = testData.getTag();

        Tag cachedTag = tagCache.find(tag.getTagId());

        assertEquals(tag, cachedTag);
    }

    @Test
    public void testFindByText() {
        UnitTestData testData = new UnitTestData();
        Tag tag = testData.getTag();

        Tag cachedTag = tagCache.find(tag.getTagText());

        assertEquals(tag, cachedTag);
    }

    @Test
    public void testFindAll() {
        UnitTestData testData = new UnitTestData();

        List<Tag> tagList = tagCache.findAll();

        assertEquals(testData.tagIdMap.size(), tagList.size());
        for (Tag tag : tagList) {
            assertNotNull(testData.tagIdMap.get(tag.getTagId()));
            assertEquals(tag, testData.tagIdMap.get(tag.getTagId()));
        }
    }
}

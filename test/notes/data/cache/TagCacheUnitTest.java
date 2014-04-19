package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Tag;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code TagCache}.
 * <p/>
 * Author: Rui Du
 */
public class TagCacheUnitTest extends EasyNoteUnitTestCase {

    private final TagCache tagCache = TagCache.get();

    /**
     * Test method for {@link notes.data.cache.TagCache#clear()}.
     */
    @Test
    public void testClear() {
        tagCache.clear();
        assertNotNull(tagCache);
        assertTrue(tagCache.findAll().isEmpty());
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

        List<Tag> tagList = CACHE.getTagCache().findAll();

        assertEquals(tagList.size(), testData.tagTextMap.size());

        for (Tag tag : tagList) {
            assertEquals(tag, testData.tagIdMap.get(tag.getTagId()));
        }
    }
}

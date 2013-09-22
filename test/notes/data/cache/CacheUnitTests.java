/**
 *
 */
package notes.data.cache;

import core.EasyNoteUnitTestCase;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@code Cache}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class CacheUnitTests extends EasyNoteUnitTestCase {

    private final Cache cache = Cache.get();

    /**
     * Test method for {@link notes.data.cache.Cache#getDocumentCache()}.
     */
    @Test
    public void testGetDocumentCache() {
        assertNotNull(cache.getDocumentCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#get()}.
     */
    @Test
    public void testGetInstance() {
        assertNotNull(cache);
    }

    /**
     * Test method for {@link notes.data.cache.Cache#getNoteCache()}.
     */
    @Test
    public void testGetNoteCache() {
        assertNotNull(cache.getNoteCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#getTagCache()}.
     */
    @Test
    public void testGetTagCache() {
        assertNotNull(cache.getTagCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        Element cacheElement = cache.toXMLElement();
        assertEquals(cacheElement.getName(), "Data");
        assertNotNull(cacheElement.elements());
        assertEquals(cacheElement.elements().size(), 3);
        assertNotNull(cacheElement.element("Documents"));
        assertNotNull(cacheElement.element("Tags"));
        assertNotNull(cacheElement.element("Notes"));
    }

    /**
     * Test method for {@link notes.data.cache.Cache#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element noteCacheElement = cache.toXMLElement();
        cache.buildFromXMLElement(noteCacheElement);

        assertEquals(cache.getDocumentCache().getDocumentMap(), testData.documentMap);
        assertEquals(cache.getDocumentCache().getMaxDocumentId(), testData.maxDocumentId);
        assertEquals(cache.getTagCache().getTagIdMap(), testData.tagIdMap);
        assertEquals(cache.getTagCache().getMaxTagId(), testData.maxTagId);
        assertEquals(cache.getNoteCache().getNoteMap(), testData.noteMap);
        assertEquals(cache.getNoteCache().getMaxNoteId(), testData.maxNoteId);
    }
}

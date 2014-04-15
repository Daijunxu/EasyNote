package notes.data.cache;

import core.EasyNoteUnitTestCase;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@code Cache}.
 *
 * Author: Rui Du
 */
public class CacheUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.data.cache.Cache#getDocumentCache()}.
     */
    @Test
    public void testGetDocumentCache() {
        assertNotNull(CACHE.getDocumentCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#get()}.
     */
    @Test
    public void testGetInstance() {
        assertNotNull(CACHE);
    }

    /**
     * Test method for {@link notes.data.cache.Cache#getNoteCache()}.
     */
    @Test
    public void testGetNoteCache() {
        assertNotNull(CACHE.getNoteCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#getTagCache()}.
     */
    @Test
    public void testGetTagCache() {
        assertNotNull(CACHE.getTagCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        Element cacheElement = CACHE.toXMLElement();
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
        Element noteCacheElement = CACHE.toXMLElement();
        CACHE.buildFromXMLElement(noteCacheElement);

        assertEquals(CACHE.getDocumentCache().getDocumentMap(), testData.documentMap);
        assertEquals(CACHE.getDocumentCache().getMaxDocumentId(), testData.maxDocumentId);
        assertEquals(CACHE.getTagCache().getTagIdMap(), testData.tagIdMap);
        assertEquals(CACHE.getTagCache().getMaxTagId(), testData.maxTagId);
        assertEquals(CACHE.getNoteCache().getNoteMap(), testData.noteMap);
        assertEquals(CACHE.getNoteCache().getMaxNoteId(), testData.maxNoteId);
    }
}

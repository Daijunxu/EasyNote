/**
 *
 */
package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.entity.Document;
import org.dom4j.Element;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code DocumentCache}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class DocumentCacheUnitTests extends EasyNoteUnitTestCase {

    private final DocumentCache documentCache = DocumentCache.get();

    /**
     * Test method for {@link notes.data.cache.DocumentCache#clear()}.
     */
    @Test
    public void testClear() {
        documentCache.clear();
        assertNotNull(documentCache);
        assertTrue(documentCache.getDocumentMap().isEmpty());
        assertTrue(documentCache.getDocumentTitleIdMap().isEmpty());
        assertTrue(documentCache.getMaxDocumentId() == Long.MIN_VALUE);
    }

    /**
     * Test method for {@link notes.data.cache.DocumentCache#getDocumentMap()}.
     */
    @Test
    public void testGetDocumentMap() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(documentCache.getDocumentMap());
        Map<Long, Document> documentMap = documentCache.getDocumentMap();
        assertFalse(documentMap.isEmpty());
        assertEquals(testData.documentMap.keySet(), documentMap.keySet());
        for (Document document : testData.documentMap.values()) {
            assertEquals(document, documentMap.get(document.getDocumentId()));
        }
    }

    /**
     * Test method for {@link notes.data.cache.DocumentCache#getDocumentTitleIdMap()}.
     */
    @Test
    public void testGetDocumentTitleIdMap() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(documentCache.getDocumentTitleIdMap());
        Map<String, Long> documentTitleIdMap = documentCache
                .getDocumentTitleIdMap();
        assertFalse(documentTitleIdMap.isEmpty());
        assertEquals(testData.documentTitleIdMap, documentTitleIdMap);
    }

    /**
     * Test method for {@link notes.data.cache.DocumentCache#getMaxDocumentId()}.
     */
    @Test
    public void testGetMaxDocumentId() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(documentCache.getMaxDocumentId());
        assertEquals(testData.maxDocumentId, documentCache.getMaxDocumentId());
    }

    /**
     * Test method for {@link notes.data.cache.DocumentCache#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element documentCacheElement = documentCache.toXMLElement();
        assertEquals(documentCacheElement.getName(), "Documents");
        assertNotNull(documentCacheElement.elements());
        assertEquals(documentCacheElement.elements().size(), testData.documentMap.size());
        for (Element element : documentCacheElement.elements()) {
            assertTrue(element.getName().equals("Book")
                    || element.getName().equals("Article")
                    || element.getName().equals("WORKSET"));
        }
    }

    /**
     * Test method for {@link notes.data.cache.DocumentCache#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element documentCacheElement = documentCache.toXMLElement();
        documentCache.buildFromXMLElement(documentCacheElement);

        assertEquals(documentCache.getDocumentMap(), testData.documentMap);
        assertEquals(documentCache.getDocumentTitleIdMap(), testData.documentTitleIdMap);
        assertEquals(documentCache.getMaxDocumentId(), testData.maxDocumentId);
    }
}

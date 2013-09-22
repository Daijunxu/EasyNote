/**
 *
 */
package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.entity.Document;
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

    /**
     * Test method for {@link notes.data.cache.DocumentCache#clear()}.
     */
    @Test
    public void testClear() {
        Cache.get().getDocumentCache().clear();
        assertNotNull(Cache.get().getDocumentCache());
        assertTrue(Cache.get().getDocumentCache().getDocumentMap().isEmpty());
        assertTrue(Cache.get().getDocumentCache().getDocumentTitleIdMap().isEmpty());
        assertTrue(Cache.get().getDocumentCache().getMaxDocumentId() == Long.MIN_VALUE);
    }

    /**
     * Test method for {@link notes.data.cache.DocumentCache#getDocumentMap()}.
     */
    @Test
    public void testGetDocumentMap() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(Cache.get().getDocumentCache().getDocumentMap());
        Map<Long, Document> documentMap = Cache.get().getDocumentCache().getDocumentMap();
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
    public void testGetDocumentTitleSet() {
        final UnitTestData testData = new UnitTestData();
        assertNotNull(Cache.get().getDocumentCache().getDocumentTitleIdMap());
        Map<String, Long> documentTitleIdMap = Cache.get().getDocumentCache()
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
        assertNotNull(Cache.get().getDocumentCache().getMaxDocumentId());
        assertEquals(testData.maxDocumentId, Cache.get().getDocumentCache().getMaxDocumentId());
    }

}

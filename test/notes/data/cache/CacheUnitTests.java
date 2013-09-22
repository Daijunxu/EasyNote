/**
 *
 */
package notes.data.cache;

import core.EasyNoteUnitTestCase;
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

    /**
     * Test method for {@link notes.data.cache.Cache#getDocumentCache()}.
     */
    @Test
    public void testGetDocumentCache() {
        assertNotNull(Cache.get().getDocumentCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#get()}.
     */
    @Test
    public void testGetInstance() {
        assertNotNull(Cache.get());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#getNoteCache()}.
     */
    @Test
    public void testGetNoteCache() {
        assertNotNull(Cache.get().getNoteCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#getTagCache()}.
     */
    @Test
    public void testGetTagCache() {
        assertNotNull(Cache.get().getTagCache());
    }

    /**
     * Test method for {@link notes.data.cache.Cache#loadAllCaches()}.
     */
    @Test
    public void testLoadAllCaches() {
        final UnitTestData testData = new UnitTestData();
        Cache.get().getDocumentCache().clear();
        Cache.get().loadAllCaches();
        assertNotNull(Cache.get());
        assertEquals(Cache.get().getDocumentCache().getDocumentMap(), testData.documentMap);
        assertEquals(Cache.get().getDocumentCache().getMaxDocumentId(), testData.maxDocumentId);
        assertEquals(Cache.get().getTagCache().getTagIdMap(), testData.tagIdMap);
        assertEquals(Cache.get().getTagCache().getMaxTagId(), testData.maxTagId);
        assertEquals(Cache.get().getNoteCache().getNoteMap(), testData.noteMap);
        assertEquals(Cache.get().getNoteCache().getMaxNoteId(), testData.maxNoteId);
    }
}

/**
 * 
 */
package notes.data.cache.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.data.cache.Property;
import notes.entity.Document;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@code DocumentCache}.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class DocumentCacheUnitTests {

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
		Property.get().setDataLocation("./resources/testData/reading_notes.data");
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
	 * Test method for {@link notes.data.cache.document.DocumentCache#clear()}.
	 */
	@Test
	public void testClear() {
		Cache.get().getDocumentCache().clear();
		assertNotNull(Cache.get().getDocumentCache());
		assertTrue(Cache.get().getDocumentCache().getDocumentMap().isEmpty());
		assertTrue(Cache.get().getDocumentCache().getDocumentTitleIdMap().isEmpty());
		assertTrue(Cache.get().getDocumentCache().getMaxDocumentId() == Long.MIN_VALUE);
		Cache.get().reload();
	}

	/**
	 * Test method for {@link notes.data.cache.document.DocumentCache#getDocumentMap()}.
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
	 * Test method for {@link notes.data.cache.document.DocumentCache#getDocumentTitleSet()}.
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
	 * Test method for {@link notes.data.cache.document.DocumentCache#getMaxDocumentId()}.
	 */
	@Test
	public void testGetMaxDocumentId() {
		final UnitTestData testData = new UnitTestData();
		assertNotNull(Cache.get().getDocumentCache().getMaxDocumentId());
		assertEquals(testData.maxDocumentId, Cache.get().getDocumentCache().getMaxDocumentId());
	}

}

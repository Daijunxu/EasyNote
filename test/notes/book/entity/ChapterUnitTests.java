/**
 * 
 */
package notes.book.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.data.cache.Property;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@code Chapter}.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class ChapterUnitTests {

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
	 * Test method for {@link notes.book.entity.Chapter#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		final UnitTestData testData = new UnitTestData();
		Book testBook = (Book) (testData.documentMap.get(1L));
		Chapter testChapter = testBook.getChaptersMap().get(1L);
		Book cachedBook = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(1L));
		Chapter cachedChapter = cachedBook.getChaptersMap().get(1L);
		assertTrue(testChapter.equals(cachedChapter));
		assertFalse(testChapter.equals(new Chapter()));
		assertFalse(testChapter.equals(null));
		assertFalse(testChapter.equals(new Object()));
	}

	/**
	 * Test method for {@link notes.book.entity.Chapter#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		final UnitTestData testData = new UnitTestData();
		Book testBook = (Book) (testData.documentMap.get(1L));
		Chapter testChapter = testBook.getChaptersMap().get(1L);
		Book cachedBook = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(1L));
		Chapter cachedChapter = cachedBook.getChaptersMap().get(1L);
		assertEquals(testChapter.hashCode(), cachedChapter.hashCode());
	}

	/**
	 * Test method for {@link notes.book.entity.Chapter#toString()}.
	 */
	@Test
	public void testToString() {
		final UnitTestData testData = new UnitTestData();
		Book testBook = (Book) (testData.documentMap.get(1L));
		Chapter testChapter = testBook.getChaptersMap().get(1L);
		Book cachedBook = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(1L));
		Chapter cachedChapter = cachedBook.getChaptersMap().get(1L);
		assertEquals(StringUtils.substringAfter(testChapter.toString(), "["),
				StringUtils.substringAfter(cachedChapter.toString(), "["));
	}

}

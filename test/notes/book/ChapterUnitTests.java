/**
 *
 */
package notes.book;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code Chapter}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ChapterUnitTests extends EasyNoteUnitTestCase {

    /**
     * Data required for unit tests. NOTE: A new instance should be created for each unit test.
     */
    public static class UnitTestData extends CacheUnitTests.UnitTestData {
    }

    /**
     * Test method for {@link notes.book.Chapter#equals(java.lang.Object)}.
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
     * Test method for {@link notes.book.Chapter#hashCode()}.
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
     * Test method for {@link notes.book.Chapter#toString()}.
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

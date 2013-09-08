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
 * Unit tests for the {@code Book}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class BookUnitTests extends EasyNoteUnitTestCase {

    /**
     * Data required for unit tests. NOTE: A new instance should be created for each unit test.
     */
    public static class UnitTestData extends CacheUnitTests.UnitTestData {
    }

    /**
     * Test method for {@link notes.book.Book#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Book book = (Book) (testData.documentMap.get(1L));
        assertTrue(book.equals(Cache.get().getDocumentCache().getDocumentMap().get(1L)));
        assertFalse(book.equals(new Book()));
        assertFalse(book.equals(null));
        assertFalse(book.equals(new Object()));
    }

    /**
     * Test method for {@link notes.book.Book#getNotesCount()}.
     */
    @Test
    public void testGetNotesCount() {
        assertEquals(1, Cache.get().getDocumentCache().getDocumentMap().get(1L).getNotesCount());
    }

    /**
     * Test method for {@link notes.book.Book#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.documentMap.get(1L).hashCode(), Cache.get().getDocumentCache()
                .getDocumentMap().get(1L).hashCode());
    }

    /**
     * Test method for {@link notes.book.Book#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = (Book) (testData.documentMap.get(1L));
        Book cachedBook = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(1L));
        assertEquals(StringUtils.substringBetween(testBook.toString(), "[", "chapterMap"),
                StringUtils.substringBetween(cachedBook.toString(), "[", "chapterMap"));
        assertEquals(StringUtils.substringAfter(testBook.toString(), "createdTime"),
                StringUtils.substringAfter(cachedBook.toString(), "createdTime"));
    }
}

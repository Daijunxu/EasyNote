/**
 *
 */
package notes.article;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code Article}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleUnitTests extends EasyNoteUnitTestCase {

    /**
     * Data required for unit tests. NOTE: A new instance should be created for each unit test.
     */
    public static class UnitTestData extends CacheUnitTests.UnitTestData {
    }

    /**
     * Test method for {@link notes.article.Article#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Article article = (Article) (testData.documentMap.get(2L));
        assertTrue(article.equals(Cache.get().getDocumentCache().getDocumentMap().get(2L)));
        assertFalse(article.equals(new Article()));
        assertFalse(article.equals(null));
        assertFalse(article.equals(new Object()));
    }

    /**
     * Test method for {@link notes.article.Article#getNotesCount()}.
     */
    @Test
    public void testGetNotesCount() {
        assertEquals(1, Cache.get().getDocumentCache().getDocumentMap().get(2L).getNotesCount());
    }

    /**
     * Test method for {@link notes.article.Article#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.documentMap.get(2L).hashCode(), Cache.get().getDocumentCache()
                .getDocumentMap().get(2L).hashCode());
    }

    /**
     * Test method for {@link notes.article.Article#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Article testArticle = (Article) (testData.documentMap.get(2L));
        Article cachedArticle = (Article) (Cache.get().getDocumentCache().getDocumentMap().get(2L));
        assertEquals(StringUtils.substringAfter(testArticle.toString(), "["),
                StringUtils.substringAfter(cachedArticle.toString(), "["));
    }

}

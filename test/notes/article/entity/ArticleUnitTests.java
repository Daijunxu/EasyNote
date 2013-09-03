/**
 *
 */
package notes.article.entity;

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
 * Unit tests for the {@code Article}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleUnitTests {

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
     * Test method for {@link notes.article.entity.Article#equals(java.lang.Object)}.
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
     * Test method for {@link notes.article.entity.Article#getNotesCount()}.
     */
    @Test
    public void testGetNotesCount() {
        assertEquals(1, Cache.get().getDocumentCache().getDocumentMap().get(2L).getNotesCount());
    }

    /**
     * Test method for {@link notes.article.entity.Article#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.documentMap.get(2L).hashCode(), Cache.get().getDocumentCache()
                .getDocumentMap().get(2L).hashCode());
    }

    /**
     * Test method for {@link notes.article.entity.Article#toString()}.
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

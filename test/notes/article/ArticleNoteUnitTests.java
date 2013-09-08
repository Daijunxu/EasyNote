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
 * Unit tests for the {@code ArticleNote}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleNoteUnitTests extends EasyNoteUnitTestCase {

    /**
     * Data required for unit tests. NOTE: A new instance should be created for each unit test.
     */
    public static class UnitTestData extends CacheUnitTests.UnitTestData {
    }

    /**
     * Test method for {@link notes.article.ArticleNote#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote articleNote = (ArticleNote) (testData.noteMap.get(2L));
        assertTrue(articleNote.equals(Cache.get().getNoteCache().getNoteMap().get(2L)));
        assertFalse(articleNote.equals(new ArticleNote()));
        assertFalse(articleNote.equals(null));
        assertFalse(articleNote.equals(new Object()));
    }

    /**
     * Test method for {@link notes.article.ArticleNote#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.noteMap.get(2L).hashCode(), Cache.get().getNoteCache().getNoteMap()
                .get(2L).hashCode());
    }

    /**
     * Test method for {@link notes.article.ArticleNote#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = (ArticleNote) (testData.noteMap.get(2L));
        ArticleNote cachedArticleNote = (ArticleNote) (Cache.get().getNoteCache().getNoteMap()
                .get(2L));
        assertEquals(StringUtils.substringAfter(testArticleNote.toString(), "["),
                StringUtils.substringAfter(cachedArticleNote.toString(), "["));
    }

}

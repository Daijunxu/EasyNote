/**
 *
 */
package notes.entity.article;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.utils.EntityHelper;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code Article}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.entity.article.Article#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Article article = (Article) (testData.documentMap.get(2L));
        assertTrue(article.equals(Cache.get().getDocumentCache().getDocumentMap().get(2L)));
        assertFalse(article.equals(new Article()));
        assertFalse(article.equals(new Object()));
    }

    /**
     * Test method for {@link notes.entity.article.Article#getNotesCount()}.
     */
    @Test
    public void testGetNotesCount() {
        assertEquals(1, Cache.get().getDocumentCache().getDocumentMap().get(2L).getNotesCount());
    }

    /**
     * Test method for {@link notes.entity.article.Article#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.documentMap.get(2L).hashCode(), Cache.get().getDocumentCache()
                .getDocumentMap().get(2L).hashCode());
    }

    /**
     * Test method for {@link notes.entity.article.Article#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Article testArticle = (Article) (testData.documentMap.get(2L));
        Article cachedArticle = (Article) (Cache.get().getDocumentCache().getDocumentMap().get(2L));
        assertEquals(StringUtils.substringAfter(testArticle.toString(), "["),
                StringUtils.substringAfter(cachedArticle.toString(), "["));
    }

    /**
     * Test method for {@link notes.entity.article.Article#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Article testArticle = (Article) (testData.documentMap.get(2L));
        Element articleElement = testArticle.toXMLElement();

        assertEquals(articleElement.getName(), "Article");
        assertNotNull(articleElement.attribute("DocumentId"));
        assertNotNull(articleElement.attribute("DocumentTitle"));
        assertNotNull(articleElement.attribute("AuthorsList"));
        assertNotNull(articleElement.attribute("Comment"));
        assertNotNull(articleElement.attribute("Source"));
        assertNotNull(articleElement.attribute("CreatedTime"));
        assertNotNull(articleElement.attribute("LastUpdatedTime"));
        assertEquals(Long.parseLong(articleElement.attributeValue("DocumentId")),
                testArticle.getDocumentId().longValue());
        assertEquals(articleElement.attributeValue("DocumentTitle"), testArticle.getDocumentTitle());
        assertEquals(EntityHelper.buildAuthorsStrList(articleElement.attributeValue("AuthorsList")),
                testArticle.getAuthorsList());
        assertEquals(articleElement.attributeValue("Comment"), testArticle.getComment());
        assertEquals(articleElement.attributeValue("Source"), testArticle.getSource());
        assertEquals(Long.parseLong(articleElement.attributeValue("CreatedTime")),
                testArticle.getCreatedTime().getTime());
        assertEquals(Long.parseLong(articleElement.attributeValue("LastUpdatedTime")),
                testArticle.getLastUpdatedTime().getTime());
    }

    /**
     * Test method for {@link notes.entity.article.Article#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Article testArticle = (Article) (testData.documentMap.get(2L));
        Element bookElement = testArticle.toXMLElement();
        Article newArticle = new Article().buildFromXMLElement(bookElement);

        assertEquals(testArticle, newArticle);
    }
}
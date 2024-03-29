package notes.businessobjects.article;

import core.EasyNoteUnitTestCase;
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
 * <p/>
 * Author: Rui Du
 */
public class ArticleUnitTest extends EasyNoteUnitTestCase {

    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Article article = testData.getArticle();
        assertTrue(article.equals(CACHE.getDocumentCache().find(2L)));
        assertFalse(article.equals(new Article()));
        assertFalse(article.equals(new Object()));
    }

    @Test
    public void testGetNotesCount() {
        assertEquals(1, CACHE.getDocumentCache().find(2L).getNotesCount());
    }

    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        Article article = testData.getArticle();
        assertEquals(article.hashCode(), CACHE.getDocumentCache().find(article.getDocumentId()).hashCode());
    }

    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Article testArticle = testData.getArticle();
        Article cachedArticle = (Article) (CACHE.getDocumentCache().find(2L));
        assertEquals(StringUtils.substringAfter(testArticle.toString(), "["),
                StringUtils.substringAfter(cachedArticle.toString(), "["));
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Article testArticle = testData.getArticle();
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

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Article testArticle = testData.getArticle();
        Element bookElement = testArticle.toXMLElement();
        Article newArticle = new Article().buildFromXMLElement(bookElement);

        assertEquals(testArticle, newArticle);
    }
}

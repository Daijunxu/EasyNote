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
 * Unit tests for the {@code ArticleNote}.
 *
 * Author: Rui Du
 */
public class ArticleNoteUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.article.ArticleNote#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote articleNote = (ArticleNote) (testData.noteMap.get(2L));
        assertTrue(articleNote.equals(CACHE.getNoteCache().getNoteMap().get(2L)));
        assertFalse(articleNote.equals(new ArticleNote()));
        assertFalse(articleNote.equals(new Object()));
    }

    /**
     * Test method for {@link notes.businessobjects.article.ArticleNote#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.noteMap.get(2L).hashCode(), CACHE.getNoteCache().getNoteMap()
                .get(2L).hashCode());
    }

    /**
     * Test method for {@link notes.businessobjects.article.ArticleNote#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = (ArticleNote) (testData.noteMap.get(2L));
        ArticleNote cachedArticleNote = (ArticleNote) (CACHE.getNoteCache().getNoteMap()
                .get(2L));
        assertEquals(StringUtils.substringAfter(testArticleNote.toString(), "["),
                StringUtils.substringAfter(cachedArticleNote.toString(), "["));
    }

    /**
     * Test method for {@link notes.businessobjects.article.ArticleNote#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = (ArticleNote) (testData.noteMap.get(2L));
        Element articleNoteElement = testArticleNote.toXMLElement();

        assertEquals(articleNoteElement.getName(), "ArticleNote");
        assertNotNull(articleNoteElement.attribute("NoteId"));
        assertNotNull(articleNoteElement.attribute("DocumentId"));
        assertNotNull(articleNoteElement.attribute("TagIds"));
        assertNotNull(articleNoteElement.attribute("CreatedTime"));
        assertNotNull(articleNoteElement.getText());
        assertEquals(Long.parseLong(articleNoteElement.attributeValue("NoteId")),
                        testArticleNote.getNoteId().longValue());
        assertEquals(Long.parseLong(articleNoteElement.attributeValue("DocumentId")),
                testArticleNote.getDocumentId().longValue());
        assertEquals(EntityHelper.buildIDsList(articleNoteElement.attributeValue("TagIds")),
                testArticleNote.getTagIds());
        assertEquals(Long.parseLong(articleNoteElement.attributeValue("CreatedTime")),
                testArticleNote.getCreatedTime().getTime());
        assertEquals(articleNoteElement.getText(), testArticleNote.getNoteText());
    }

    /**
     * Test method for {@link notes.businessobjects.article.ArticleNote#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = (ArticleNote) (testData.noteMap.get(2L));
        Element articleNoteElement = testArticleNote.toXMLElement();
        ArticleNote newArticleNote = new ArticleNote().buildFromXMLElement(articleNoteElement);

        assertEquals(testArticleNote, newArticleNote);
    }
}

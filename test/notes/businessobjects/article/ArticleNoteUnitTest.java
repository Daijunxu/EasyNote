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
 * <p/>
 * Author: Rui Du
 */
public class ArticleNoteUnitTest extends EasyNoteUnitTestCase {

    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote articleNote = testData.getArticleNote();
        assertTrue(articleNote.equals(CACHE.getNoteCache().find(2L)));
        assertFalse(articleNote.equals(new ArticleNote()));
        assertFalse(articleNote.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote articleNote = testData.getArticleNote();
        assertEquals(articleNote.hashCode(), CACHE.getNoteCache().find(articleNote.getNoteId()).hashCode());
    }

    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = testData.getArticleNote();
        ArticleNote cachedArticleNote = (ArticleNote) (CACHE.getNoteCache().find(2L));
        assertEquals(StringUtils.substringAfter(testArticleNote.toString(), "["),
                StringUtils.substringAfter(cachedArticleNote.toString(), "["));
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = testData.getArticleNote();
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

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = testData.getArticleNote();
        Element articleNoteElement = testArticleNote.toXMLElement();
        ArticleNote newArticleNote = new ArticleNote().buildFromXMLElement(articleNoteElement);

        assertEquals(testArticleNote, newArticleNote);
    }
}

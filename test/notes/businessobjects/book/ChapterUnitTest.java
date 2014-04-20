package notes.businessobjects.book;

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
 * Unit tests for the {@code Chapter}.
 * <p/>
 * Author: Rui Du
 */
public class ChapterUnitTest extends EasyNoteUnitTestCase {

    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Chapter testChapter = testData.getChapter();
        Book cachedBook = (Book) (CACHE.getDocumentCache().find(1L));
        Chapter cachedChapter = cachedBook.getChaptersMap().get(1L);
        assertTrue(testChapter.equals(cachedChapter));
        assertFalse(testChapter.equals(new Chapter()));
        assertFalse(testChapter.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        Chapter testChapter = testData.getChapter();
        Book cachedBook = (Book) (CACHE.getDocumentCache().find(1L));
        Chapter cachedChapter = cachedBook.getChaptersMap().get(1L);
        assertEquals(testChapter.hashCode(), cachedChapter.hashCode());
    }

    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Chapter testChapter = testData.getChapter();
        Book cachedBook = (Book) (CACHE.getDocumentCache().find(1L));
        Chapter cachedChapter = cachedBook.getChaptersMap().get(1L);
        assertEquals(StringUtils.substringAfter(testChapter.toString(), "["),
                StringUtils.substringAfter(cachedChapter.toString(), "["));
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Chapter testChapter = testData.getChapter();
        Element chapterElement = testChapter.toXMLElement();

        assertEquals(chapterElement.getName(), "Chapter");
        assertNotNull(chapterElement.attribute("ChapterId"));
        assertNotNull(chapterElement.getText());
        assertEquals(Long.parseLong(chapterElement.attributeValue("ChapterId")), testChapter.getChapterId().longValue());
        assertEquals(chapterElement.attributeValue("ChapterTitle"), testChapter.getChapterTitle());
        assertEquals(chapterElement.attributeValue("NotesList"), EntityHelper.buildEntityStrFromList(testChapter.getNotesList()));
    }

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Chapter testChapter = testData.getChapter();
        Element chapterElement = testChapter.toXMLElement();
        Chapter newChapter = new Chapter().buildFromXMLElement(chapterElement);

        assertEquals(testChapter, newChapter);
    }
}

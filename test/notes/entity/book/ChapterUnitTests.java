/**
 *
 */
package notes.entity.book;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code Chapter}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ChapterUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.entity.book.Chapter#equals(java.lang.Object)}.
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
        assertFalse(testChapter.equals(new Object()));
    }

    /**
     * Test method for {@link notes.entity.book.Chapter#hashCode()}.
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
     * Test method for {@link notes.entity.book.Chapter#toString()}.
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

    /**
     * Test method for {@link notes.entity.book.Chapter#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = (Book) (testData.documentMap.get(1L));
        Chapter testChapter = testBook.getChaptersMap().get(1L);
        Element chapterElement = testChapter.toXMLElement();

        assertEquals(chapterElement.getName(), "Chapter");
        assertNotNull(chapterElement.attribute("ChapterId"));
        assertNotNull(chapterElement.getText());
        assertEquals(Long.parseLong(chapterElement.attributeValue("ChapterId")), testChapter.getChapterId().longValue());
        assertEquals(chapterElement.attributeValue("ChapterTitle"), testChapter.getChapterTitle());
    }

    /**
     * Test method for {@link notes.entity.book.Chapter#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = (Book) (testData.documentMap.get(1L));
        Chapter testChapter = testBook.getChaptersMap().get(1L);
        Element chapterElement = testChapter.toXMLElement();
        Chapter newChapter = new Chapter().buildFromXMLElement(chapterElement);

        assertEquals(testChapter, newChapter);
    }
}

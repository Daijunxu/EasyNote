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
 * Unit tests for the {@code Book}.
 * <p/>
 * Author: Rui Du
 */
public class BookUnitTest extends EasyNoteUnitTestCase {

    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Book book = testData.getBook();
        assertTrue(book.equals(CACHE.getDocumentCache().find(1L)));
        assertFalse(book.equals(new Book()));
        assertFalse(book.equals(new Object()));
    }

    @Test
    public void testGetNotesCount() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(1, testData.getBook().getNotesCount());
    }

    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        Book book = testData.getBook();
        assertEquals(book.hashCode(), CACHE.getDocumentCache().find(book.getDocumentId()).hashCode());
    }

    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = testData.getBook();
        Book cachedBook = (Book) (CACHE.getDocumentCache().find(1L));
        assertEquals(StringUtils.substringBetween(testBook.toString(), "[", "chapterMap"),
                StringUtils.substringBetween(cachedBook.toString(), "[", "chapterMap"));
        assertEquals(StringUtils.substringAfter(testBook.toString(), "createdTime"),
                StringUtils.substringAfter(cachedBook.toString(), "createdTime"));
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = testData.getBook();
        Element bookElement = testBook.toXMLElement();

        assertEquals(bookElement.getName(), "Book");
        assertNotNull(bookElement.attribute("DocumentId"));
        assertNotNull(bookElement.attribute("DocumentTitle"));
        assertNotNull(bookElement.attribute("AuthorsList"));
        assertNotNull(bookElement.attribute("Comment"));
        assertNotNull(bookElement.attribute("Edition"));
        assertNotNull(bookElement.attribute("PublishedYear"));
        assertNotNull(bookElement.attribute("ISBN"));
        assertNotNull(bookElement.attribute("CreatedTime"));
        assertNotNull(bookElement.attribute("LastUpdatedTime"));
        assertNotNull(bookElement.elements().get(0));
        assertEquals(Long.parseLong(bookElement.attributeValue("DocumentId")),
                testBook.getDocumentId().longValue());
        assertEquals(bookElement.attributeValue("DocumentTitle"), testBook.getDocumentTitle());
        assertEquals(EntityHelper.buildAuthorsStrList(bookElement.attributeValue("AuthorsList")),
                testBook.getAuthorsList());
        assertEquals(bookElement.attributeValue("Comment"), testBook.getComment());
        assertEquals(Integer.parseInt(bookElement.attributeValue("Edition")), testBook.getEdition().intValue());
        assertEquals(Integer.parseInt(bookElement.attributeValue("PublishedYear")),
                testBook.getPublishedYear().intValue());
        assertEquals(bookElement.attributeValue("ISBN"), testBook.getIsbn());
        assertEquals(Long.parseLong(bookElement.attributeValue("CreatedTime")),
                testBook.getCreatedTime().getTime());
        assertEquals(Long.parseLong(bookElement.attributeValue("LastUpdatedTime")),
                testBook.getLastUpdatedTime().getTime());
        assertEquals(bookElement.elements().get(0).getName(), "Chapter");
    }

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = testData.getBook();
        Element bookElement = testBook.toXMLElement();
        Book newBook = new Book().buildFromXMLElement(bookElement);

        assertEquals(testBook, newBook);
    }
}

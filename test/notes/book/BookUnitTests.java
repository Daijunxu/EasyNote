/**
 *
 */
package notes.book;

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
 * Unit tests for the {@code Book}.
 *
 * @author Rui Du
 * @version 1.0
 */
public class BookUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.book.Book#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Book book = (Book) (testData.documentMap.get(1L));
        assertTrue(book.equals(Cache.get().getDocumentCache().getDocumentMap().get(1L)));
        assertFalse(book.equals(new Book()));
        assertFalse(book.equals(new Object()));
    }

    /**
     * Test method for {@link notes.book.Book#getNotesCount()}.
     */
    @Test
    public void testGetNotesCount() {
        assertEquals(1, Cache.get().getDocumentCache().getDocumentMap().get(1L).getNotesCount());
    }

    /**
     * Test method for {@link notes.book.Book#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.documentMap.get(1L).hashCode(), Cache.get().getDocumentCache()
                .getDocumentMap().get(1L).hashCode());
    }

    /**
     * Test method for {@link notes.book.Book#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = (Book) (testData.documentMap.get(1L));
        Book cachedBook = (Book) (Cache.get().getDocumentCache().getDocumentMap().get(1L));
        assertEquals(StringUtils.substringBetween(testBook.toString(), "[", "chapterMap"),
                StringUtils.substringBetween(cachedBook.toString(), "[", "chapterMap"));
        assertEquals(StringUtils.substringAfter(testBook.toString(), "createdTime"),
                StringUtils.substringAfter(cachedBook.toString(), "createdTime"));
    }

    /**
     * Test method for {@link notes.book.Book#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = (Book) (testData.documentMap.get(1L));
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

    /**
     * Test method for {@link notes.book.Book#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Book testBook = (Book) (testData.documentMap.get(1L));
        Element bookElement = testBook.toXMLElement();
        Book newBook = new Book().buildFromXMLElement(bookElement);

        assertEquals(testBook, newBook);
    }
}

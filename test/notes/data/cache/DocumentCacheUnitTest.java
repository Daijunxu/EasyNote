package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Document;
import notes.businessobjects.article.Article;
import notes.businessobjects.book.Book;
import notes.businessobjects.workset.Workset;
import notes.dao.DuplicateRecordException;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code DocumentCache}.
 * <p/>
 * Author: Rui Du
 */
public class DocumentCacheUnitTest extends EasyNoteUnitTestCase {

    private final DocumentCache documentCache = DocumentCache.get();

    @Test
    public void testInitiate() {
        documentCache.initialize();
        assertNotNull(documentCache);
        assertTrue(documentCache.findAll().isEmpty());
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element documentCacheElement = documentCache.toXMLElement();
        assertEquals(documentCacheElement.getName(), "Documents");
        assertNotNull(documentCacheElement.elements());
        assertEquals(documentCacheElement.elements().size(), testData.documentMap.size());
        for (Element element : documentCacheElement.elements()) {
            assertTrue(element.getName().equals("Book")
                    || element.getName().equals("Article")
                    || element.getName().equals("Workset"));
        }
    }

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element documentCacheElement = documentCache.toXMLElement();
        documentCache.buildFromXMLElement(documentCacheElement);

        List<Document> documentList = CACHE.getDocumentCache().findAll();

        assertEquals(documentList.size(), testData.documentMap.size());

        for (Document document : documentList) {
            assertEquals(document, testData.documentMap.get(document.getDocumentId()));
        }
    }

    @Test
    public void testInsertBook() throws DuplicateRecordException {
        Book book = new Book();
        book.setDocumentTitle("New Book");

        Document cachedDocument = documentCache.insert(book);

        assertNotNull(cachedDocument);
        assertNotNull(cachedDocument.getDocumentId());
        assertEquals(book.getDocumentTitle(), cachedDocument.getDocumentTitle());
    }

    @Test
    public void testInsertArticle() throws DuplicateRecordException {
        Article article = new Article();
        article.setDocumentTitle("New Article");

        Document cachedDocument = documentCache.insert(article);

        assertNotNull(cachedDocument);
        assertNotNull(cachedDocument.getDocumentId());
        assertEquals(article.getDocumentTitle(), cachedDocument.getDocumentTitle());
    }

    @Test
    public void testInsertWorkset() throws DuplicateRecordException {
        Workset workset = new Workset();
        workset.setDocumentTitle("New Workset");

        Document cachedDocument = documentCache.insert(workset);

        assertNotNull(cachedDocument);
        assertNotNull(cachedDocument.getDocumentId());
        assertEquals(workset.getDocumentTitle(), cachedDocument.getDocumentTitle());
    }

    @Test
    public void testRemove() {
        UnitTestData testData = new UnitTestData();
        Document document = testData.getBook();

        documentCache.remove(document.getDocumentId());

        assertNull(documentCache.find(document.getDocumentId()));
    }

    @Test
    public void testUpdateBook() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        Book book = testData.getBook();
        book.setDocumentTitle("Updated Book");

        Document cachedDocument = documentCache.update(book);

        assertEquals(book.getDocumentId(), cachedDocument.getDocumentId());
        assertEquals(book.getDocumentTitle(), cachedDocument.getDocumentTitle());
    }

    @Test
    public void testUpdateArticle() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        Article article = testData.getArticle();
        article.setDocumentTitle("Updated Article");

        Document cachedDocument = documentCache.update(article);

        assertEquals(article.getDocumentId(), cachedDocument.getDocumentId());
        assertEquals(article.getDocumentTitle(), cachedDocument.getDocumentTitle());
    }

    @Test
    public void testUpdateWorkset() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        Workset workset = testData.getWorkset();
        workset.setDocumentTitle("Updated Workset");

        Document cachedDocument = documentCache.update(workset);

        assertEquals(workset.getDocumentId(), cachedDocument.getDocumentId());
        assertEquals(workset.getDocumentTitle(), cachedDocument.getDocumentTitle());
    }

    @Test
    public void testFind() {
        UnitTestData testData = new UnitTestData();
        Document document = testData.getWorkset();

        Document cachedDocument = documentCache.find(document.getDocumentId());

        assertEquals(document, cachedDocument);
    }

    @Test
    public void testFindAll() {
        UnitTestData testData = new UnitTestData();

        List<Document> documentList = documentCache.findAll();

        assertEquals(testData.documentMap.size(), documentList.size());
        for (Document document : documentList) {
            assertNotNull(testData.documentMap.get(document.getDocumentId()));
            assertEquals(document, testData.documentMap.get(document.getDocumentId()));
        }
    }
}

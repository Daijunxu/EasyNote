package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Document;
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
    public void testClear() {
        documentCache.clear();
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
    public void testInsertBook() {
        // TODO
    }

    @Test
    public void testInsertArticle() {
        // TODO
    }

    @Test
    public void testInsertWorkset() {
        // TODO
    }

    @Test
    public void testRemove() {
        UnitTestData testData = new UnitTestData();
        Document document = testData.getBook();

        documentCache.remove(document.getDocumentId());

        assertNull(documentCache.find(document.getDocumentId()));
    }

    @Test
    public void testUpdateBook() {
        // TODO
    }

    @Test
    public void testUpdateArticle() {
        // TODO
    }

    @Test
    public void testUpdateWorkset() {
        // TODO
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

package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Document;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code DocumentCache}.
 * <p/>
 * Author: Rui Du
 */
public class DocumentCacheUnitTest extends EasyNoteUnitTestCase {

    private final DocumentCache documentCache = DocumentCache.get();

    /**
     * Test method for {@link notes.data.cache.DocumentCache#clear()}.
     */
    @Test
    public void testClear() {
        documentCache.clear();
        assertNotNull(documentCache);
        assertTrue(documentCache.findAll().isEmpty());
    }

    /**
     * Test method for {@link notes.data.cache.DocumentCache#toXMLElement()}.
     */
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

    /**
     * Test method for {@link notes.data.cache.DocumentCache#buildFromXMLElement(org.dom4j.Element)}.
     */
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
}

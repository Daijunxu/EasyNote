package notes.businessobjects.impl;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code AbstractDocument}.
 * <p/>
 * Author: Rui Du
 */
public class AbstractDocumentUnitTest extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.impl.AbstractDocument#compareTo(notes.businessobjects.Document)}.
     */
    @Test
    public void testCompareTo() {
        final UnitTestData testData = new UnitTestData();
        Document testDocument1 = testData.getBook();
        Document testDocument2 = testData.getArticle();
        Document cachedDocument = CACHE.getDocumentCache().find(testDocument1.getDocumentId());
        assertEquals(testDocument1.compareTo(cachedDocument), 0);
        assertTrue(testDocument1.compareTo(testDocument2) < 0);
        assertTrue(testDocument2.compareTo(testDocument1) > 0);
    }

}

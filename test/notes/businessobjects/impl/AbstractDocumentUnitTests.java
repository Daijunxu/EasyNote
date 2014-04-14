package notes.businessobjects.impl;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.businessobjects.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code AbstractDocument}.
 *
 * Author: Rui Du
 */
public class AbstractDocumentUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.impl.AbstractDocument#compareTo(notes.businessobjects.Document)}.
     */
    @Test
    public void testCompareTo() {
        final UnitTestData testData = new UnitTestData();
        Document testDocument1 = testData.documentMap.get(1L);
        Document testDocument2 = testData.documentMap.get(2L);
        Document cachedDocument = Cache.get().getDocumentCache().getDocumentMap().get(1L);
        assertEquals(testDocument1.compareTo(cachedDocument), 0);
        assertTrue(testDocument1.compareTo(testDocument2) < 0);
        assertTrue(testDocument2.compareTo(testDocument1) > 0);
    }

}

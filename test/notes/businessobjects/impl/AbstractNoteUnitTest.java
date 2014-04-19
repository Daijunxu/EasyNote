package notes.businessobjects.impl;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code AbstractNote}.
 * <p/>
 * Author: Rui Du
 */
public class AbstractNoteUnitTest extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.impl.AbstractNote#compareTo(notes.businessobjects.Note)}.
     */
    @Test
    public void testCompareTo() {
        final UnitTestData testData = new UnitTestData();
        Note testNote1 = testData.getBookNote();
        Note testNote2 = testData.getArticleNote();
        Note cachedNote = CACHE.getNoteCache().find(testNote1.getNoteId());
        assertEquals(testNote1.compareTo(cachedNote), 0);
        assertTrue(testNote1.compareTo(testNote2) < 0);
        assertTrue(testNote2.compareTo(testNote1) > 0);
    }

}

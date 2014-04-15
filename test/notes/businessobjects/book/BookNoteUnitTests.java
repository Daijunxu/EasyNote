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
 * Unit tests for the {@code BookNote}.
 *
 * Author: Rui Du
 */
public class BookNoteUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.book.BookNote#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        BookNote bookNote = (BookNote) (testData.noteMap.get(1L));
        assertTrue(bookNote.equals(CACHE.getNoteCache().getNoteMap().get(1L)));
        assertFalse(bookNote.equals(new BookNote()));
        assertFalse(bookNote.equals(new Object()));
    }

    /**
     * Test method for {@link notes.businessobjects.book.BookNote#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.noteMap.get(1L).hashCode(), CACHE.getNoteCache().getNoteMap()
                .get(1L).hashCode());
    }

    /**
     * Test method for {@link notes.businessobjects.book.BookNote#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        BookNote testBookNote = (BookNote) (testData.noteMap.get(1L));
        BookNote cachedBookNote = (BookNote) (CACHE.getNoteCache().getNoteMap().get(1L));
        assertEquals(StringUtils.substringAfter(testBookNote.toString(), "["),
                StringUtils.substringAfter(cachedBookNote.toString(), "["));
    }

    /**
     * Test method for {@link notes.businessobjects.book.BookNote#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        BookNote testBookNote = (BookNote) (testData.noteMap.get(1L));
        Element bookNoteElement = testBookNote.toXMLElement();

        assertEquals(bookNoteElement.getName(), "BookNote");
        assertNotNull(bookNoteElement.attribute("NoteId"));
        assertNotNull(bookNoteElement.attribute("DocumentId"));
        assertNotNull(bookNoteElement.attribute("ChapterId"));
        assertNotNull(bookNoteElement.attribute("TagIds"));
        assertNotNull(bookNoteElement.attribute("CreatedTime"));
        assertNotNull(bookNoteElement.getText());
        assertEquals(Long.parseLong(bookNoteElement.attributeValue("NoteId")),
                testBookNote.getNoteId().longValue());
        assertEquals(Long.parseLong(bookNoteElement.attributeValue("DocumentId")),
                testBookNote.getDocumentId().longValue());
        assertEquals(Long.parseLong(bookNoteElement.attributeValue("ChapterId")),
                testBookNote.getChapterId().longValue());
        assertEquals(EntityHelper.buildIDsList(bookNoteElement.attributeValue("TagIds")),
                testBookNote.getTagIds());
        assertEquals(Long.parseLong(bookNoteElement.attributeValue("CreatedTime")),
                testBookNote.getCreatedTime().getTime());
        assertEquals(bookNoteElement.getText(), testBookNote.getNoteText());
    }

    /**
     * Test method for {@link notes.businessobjects.book.BookNote#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        BookNote testBookNote = (BookNote) (testData.noteMap.get(1L));
        Element articleNoteElement = testBookNote.toXMLElement();
        BookNote newBookNote = new BookNote().buildFromXMLElement(articleNoteElement);

        assertEquals(testBookNote, newBookNote);
    }
}

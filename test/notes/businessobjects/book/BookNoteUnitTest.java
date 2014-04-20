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
 * <p/>
 * Author: Rui Du
 */
public class BookNoteUnitTest extends EasyNoteUnitTestCase {

    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        BookNote bookNote = testData.getBookNote();
        assertTrue(bookNote.equals(CACHE.getNoteCache().find(1L)));
        assertFalse(bookNote.equals(new BookNote()));
        assertFalse(bookNote.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        BookNote bookNote = testData.getBookNote();
        assertEquals(bookNote.hashCode(), CACHE.getNoteCache().find(bookNote.getNoteId()).hashCode());
    }

    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        BookNote testBookNote = testData.getBookNote();
        BookNote cachedBookNote = (BookNote) (CACHE.getNoteCache().find(1L));
        assertEquals(StringUtils.substringAfter(testBookNote.toString(), "["),
                StringUtils.substringAfter(cachedBookNote.toString(), "["));
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        BookNote testBookNote = testData.getBookNote();
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

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        BookNote testBookNote = testData.getBookNote();
        Element articleNoteElement = testBookNote.toXMLElement();
        BookNote newBookNote = new BookNote().buildFromXMLElement(articleNoteElement);

        assertEquals(testBookNote, newBookNote);
    }
}

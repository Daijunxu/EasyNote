package notes.businessobjects.workset;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.NoteStatus;
import notes.utils.EntityHelper;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code WorksheetNote}.
 * <p/>
 * Author: Rui Du
 * Date: 9/30/13
 * Time: 10:17 PM
 */
public class WorksheetNoteUnitTest extends EasyNoteUnitTestCase {

    @Test
    public void testEquals() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote worksheetNote = testData.getWorksheetNote();
        assertTrue(worksheetNote.equals(CACHE.getNoteCache().find(3L)));
        assertFalse(worksheetNote.equals(new WorksheetNote()));
        assertFalse(worksheetNote.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote worksheetNote = testData.getWorksheetNote();
        assertEquals(worksheetNote.hashCode(), CACHE.getNoteCache().find(worksheetNote.getNoteId()).hashCode());
    }

    @Test
    public void testToString() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = testData.getWorksheetNote();
        WorksheetNote cachedWorksheetNote = (WorksheetNote) (CACHE.getNoteCache().find(3L));
        assertEquals(StringUtils.substringAfter(testWorksheetNote.toString(), "["),
                StringUtils.substringAfter(cachedWorksheetNote.toString(), "["));
    }

    @Test
    public void testToXMLElement() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = testData.getWorksheetNote();
        Element worksheetNoteElement = testWorksheetNote.toXMLElement();

        assertEquals(worksheetNoteElement.getName(), "WorksheetNote");
        assertNotNull(worksheetNoteElement.attribute("NoteId"));
        assertNotNull(worksheetNoteElement.attribute("DocumentId"));
        assertNotNull(worksheetNoteElement.attribute("WorksheetId"));
        assertNotNull(worksheetNoteElement.attribute("TagIds"));
        assertNotNull(worksheetNoteElement.attribute("CreatedTime"));
        assertNotNull(worksheetNoteElement.getText());
        assertEquals(Long.parseLong(worksheetNoteElement.attributeValue("NoteId")),
                testWorksheetNote.getNoteId().longValue());
        assertEquals(Long.parseLong(worksheetNoteElement.attributeValue("DocumentId")),
                testWorksheetNote.getDocumentId().longValue());
        assertEquals(Long.parseLong(worksheetNoteElement.attributeValue("WorksheetId")),
                testWorksheetNote.getWorksheetId().longValue());
        assertEquals(EntityHelper.buildIDsList(worksheetNoteElement.attributeValue("TagIds")),
                testWorksheetNote.getTagIds());
        assertEquals(NoteStatus.values()[Integer.parseInt(worksheetNoteElement.attributeValue("NoteStatus"))],
                testWorksheetNote.getNoteStatus());
        assertEquals(Long.parseLong(worksheetNoteElement.attributeValue("CreatedTime")),
                testWorksheetNote.getCreatedTime().getTime());
        assertEquals(worksheetNoteElement.getText(), testWorksheetNote.getNoteText());
    }

    @Test
    public void testBuildFromXMLElement() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = testData.getWorksheetNote();
        Element articleNoteElement = testWorksheetNote.toXMLElement();
        WorksheetNote newWorksheetNote = new WorksheetNote().buildFromXMLElement(articleNoteElement);

        assertEquals(testWorksheetNote, newWorksheetNote);
    }
}

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
public class WorksheetNoteUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.workset.WorksheetNote#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote worksheetNote = (WorksheetNote) (testData.noteMap.get(3L));
        assertTrue(worksheetNote.equals(CACHE.getNoteCache().getNoteMap().get(3L)));
        assertFalse(worksheetNote.equals(new WorksheetNote()));
        assertFalse(worksheetNote.equals(new Object()));
    }

    /**
     * Test method for {@link notes.businessobjects.workset.WorksheetNote#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        assertEquals(testData.noteMap.get(3L).hashCode(), CACHE.getNoteCache().getNoteMap()
                .get(3L).hashCode());
    }

    /**
     * Test method for {@link notes.businessobjects.workset.WorksheetNote#toString()}.
     */
    @Test
    public void testToString() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = (WorksheetNote) (testData.noteMap.get(3L));
        WorksheetNote cachedWorksheetNote = (WorksheetNote) (CACHE.getNoteCache().getNoteMap().get(3L));
        assertEquals(StringUtils.substringAfter(testWorksheetNote.toString(), "["),
                StringUtils.substringAfter(cachedWorksheetNote.toString(), "["));
    }

    /**
     * Test method for {@link notes.businessobjects.workset.WorksheetNote#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = (WorksheetNote) (testData.noteMap.get(3L));
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

    /**
     * Test method for {@link notes.businessobjects.workset.WorksheetNote#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = (WorksheetNote) (testData.noteMap.get(3L));
        Element articleNoteElement = testWorksheetNote.toXMLElement();
        WorksheetNote newWorksheetNote = new WorksheetNote().buildFromXMLElement(articleNoteElement);

        assertEquals(testWorksheetNote, newWorksheetNote);
    }
}

package notes.businessobjects.workset;

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
 * Unit tests for the {@code Worksheet}.
 * <p/>
 * Author: Rui Du
 * Date: 9/30/13
 * Time: 10:17 PM
 */
public class WorksheetUnitTest extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.workset.Worksheet#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Worksheet testWorksheet = testData.getWorksheet();
        Workset cachedWorkset = (Workset) (CACHE.getDocumentCache().find(3L));
        Worksheet cachedWorksheet = cachedWorkset.getWorksheetsMap().get(1L);
        assertTrue(testWorksheet.equals(cachedWorksheet));
        assertFalse(testWorksheet.equals(new Worksheet()));
        assertFalse(testWorksheet.equals(new Object()));
    }

    /**
     * Test method for {@link notes.businessobjects.workset.Worksheet#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        Worksheet testWorksheet = testData.getWorksheet();
        Workset cachedWorkset = (Workset) (CACHE.getDocumentCache().find(3L));
        Worksheet cachedWorksheet = cachedWorkset.getWorksheetsMap().get(1L);
        assertEquals(testWorksheet.hashCode(), cachedWorksheet.hashCode());
    }

    /**
     * Test method for {@link notes.businessobjects.workset.Worksheet#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Worksheet testWorksheet = testData.getWorksheet();
        Workset cachedWorkset = (Workset) (CACHE.getDocumentCache().find(3L));
        Worksheet cachedWorksheet = cachedWorkset.getWorksheetsMap().get(1L);
        assertEquals(StringUtils.substringAfter(testWorksheet.toString(), "["),
                StringUtils.substringAfter(cachedWorksheet.toString(), "["));
    }

    /**
     * Test method for {@link notes.businessobjects.workset.Worksheet#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Worksheet testWorksheet = testData.getWorksheet();
        Element worksheetElement = testWorksheet.toXMLElement();

        assertEquals(worksheetElement.getName(), "Worksheet");
        assertNotNull(worksheetElement.attribute("WorksheetId"));
        assertNotNull(worksheetElement.getText());
        assertEquals(Long.parseLong(worksheetElement.attributeValue("WorksheetId")), testWorksheet.getWorksheetId().longValue());
        assertEquals(worksheetElement.attributeValue("WorksheetTitle"), testWorksheet.getWorksheetTitle());
        assertEquals(worksheetElement.attributeValue("NotesList"), EntityHelper.buildEntityStrFromList(testWorksheet.getNotesList()));
    }

    /**
     * Test method for {@link notes.businessobjects.workset.Worksheet#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Worksheet testWorksheet = testData.getWorksheet();
        Element worksheetElement = testWorksheet.toXMLElement();
        Worksheet newWorksheet = new Worksheet().buildFromXMLElement(worksheetElement);

        assertEquals(testWorksheet, newWorksheet);
    }
}

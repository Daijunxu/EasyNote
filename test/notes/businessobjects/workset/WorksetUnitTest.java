package notes.businessobjects.workset;

import core.EasyNoteUnitTestCase;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code WORKSET}.
 * <p/>
 * Author: Rui Du
 * Date: 9/30/13
 * Time: 10:16 PM
 */
public class WorksetUnitTest extends EasyNoteUnitTestCase {

    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Workset workset = testData.getWorkset();
        assertTrue(workset.equals(CACHE.getDocumentCache().find(workset.getDocumentId())));
        assertFalse(workset.equals(new Workset()));
        assertFalse(workset.equals(new Object()));
    }

    @Test
    public void testGetNotesCount() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(1, testData.getWorkset().getNotesCount());
    }

    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        Workset workset = testData.getWorkset();
        assertEquals(workset.hashCode(), CACHE.getDocumentCache().find(workset.getDocumentId()).hashCode());
    }

    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Workset testWorkset = testData.getWorkset();
        Element workSetElement = testWorkset.toXMLElement();

        assertEquals(workSetElement.getName(), "Workset");
        assertNotNull(workSetElement.attribute("DocumentId"));
        assertNotNull(workSetElement.attribute("DocumentTitle"));
        assertNotNull(workSetElement.attribute("AuthorsList"));
        assertNotNull(workSetElement.attribute("Comment"));
        assertNotNull(workSetElement.attribute("CreatedTime"));
        assertNotNull(workSetElement.attribute("LastUpdatedTime"));
        assertNotNull(workSetElement.elements().get(0));
        assertEquals(Long.parseLong(workSetElement.attributeValue("DocumentId")),
                testWorkset.getDocumentId().longValue());
        assertEquals(workSetElement.attributeValue("DocumentTitle"), testWorkset.getDocumentTitle());
        assertEquals(EntityHelper.buildAuthorsStrList(workSetElement.attributeValue("AuthorsList")),
                testWorkset.getAuthorsList());
        assertEquals(workSetElement.attributeValue("Comment"), testWorkset.getComment());
        assertEquals(Long.parseLong(workSetElement.attributeValue("CreatedTime")),
                testWorkset.getCreatedTime().getTime());
        assertEquals(Long.parseLong(workSetElement.attributeValue("LastUpdatedTime")),
                testWorkset.getLastUpdatedTime().getTime());
        assertEquals(workSetElement.elements().get(0).getName(), "Worksheet");
    }

    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Workset testWorkset = testData.getWorkset();
        Element workSetElement = testWorkset.toXMLElement();
        Workset newWorkset = new Workset().buildFromXMLElement(workSetElement);

        assertEquals(testWorkset, newWorkset);
    }

}

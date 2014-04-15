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
public class WorksetUnitTests extends EasyNoteUnitTestCase {
    
    /**
     * Test method for {@link Workset#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Workset workset = (Workset) (testData.documentMap.get(3L));
        assertTrue(workset.equals(CACHE.getDocumentCache().getDocumentMap().get(3L)));
        assertFalse(workset.equals(new Workset()));
        assertFalse(workset.equals(new Object()));
    }

    /**
     * Test method for {@link Workset#getNotesCount()}.
     */
    @Test
    public void testGetNotesCount() {
        assertEquals(1, CACHE.getDocumentCache().getDocumentMap().get(3L).getNotesCount());
    }

    /**
     * Test method for {@link Workset#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.documentMap.get(3L).hashCode(), CACHE.getDocumentCache()
                .getDocumentMap().get(3L).hashCode());
    }

    /**
     * Test method for {@link Workset#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Workset testWorkset = (Workset) (testData.documentMap.get(3L));
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

    /**
     * Test method for {@link Workset#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Workset testWorkset = (Workset) (testData.documentMap.get(3L));
        Element workSetElement = testWorkset.toXMLElement();
        Workset newWorkset = new Workset().buildFromXMLElement(workSetElement);

        assertEquals(testWorkset, newWorkset);
    }

}

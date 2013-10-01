package notes.entity.workset;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code WorkSet}.
 * <p/>
 * User: rui
 * Date: 9/30/13
 * Time: 10:16 PM
 */
public class WorkSetUnitTests extends EasyNoteUnitTestCase {
    
    /**
     * Test method for {@link notes.entity.workset.WorkSet#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        WorkSet workSet = (WorkSet) (testData.documentMap.get(3L));
        assertTrue(workSet.equals(Cache.get().getDocumentCache().getDocumentMap().get(3L)));
        assertFalse(workSet.equals(new WorkSet()));
        assertFalse(workSet.equals(new Object()));
    }

    /**
     * Test method for {@link notes.entity.workset.WorkSet#getNotesCount()}.
     */
    @Test
    public void testGetNotesCount() {
        assertEquals(1, Cache.get().getDocumentCache().getDocumentMap().get(3L).getNotesCount());
    }

    /**
     * Test method for {@link notes.entity.workset.WorkSet#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.documentMap.get(3L).hashCode(), Cache.get().getDocumentCache()
                .getDocumentMap().get(3L).hashCode());
    }

    /**
     * Test method for {@link notes.entity.workset.WorkSet#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        WorkSet testWorkSet = (WorkSet) (testData.documentMap.get(3L));
        Element workSetElement = testWorkSet.toXMLElement();

        assertEquals(workSetElement.getName(), "WorkSet");
        assertNotNull(workSetElement.attribute("DocumentId"));
        assertNotNull(workSetElement.attribute("DocumentTitle"));
        assertNotNull(workSetElement.attribute("AuthorsList"));
        assertNotNull(workSetElement.attribute("Comment"));
        assertNotNull(workSetElement.attribute("CreatedTime"));
        assertNotNull(workSetElement.attribute("LastUpdatedTime"));
        assertNotNull(workSetElement.elements().get(0));
        assertEquals(Long.parseLong(workSetElement.attributeValue("DocumentId")),
                testWorkSet.getDocumentId().longValue());
        assertEquals(workSetElement.attributeValue("DocumentTitle"), testWorkSet.getDocumentTitle());
        assertEquals(EntityHelper.buildAuthorsStrList(workSetElement.attributeValue("AuthorsList")),
                testWorkSet.getAuthorsList());
        assertEquals(workSetElement.attributeValue("Comment"), testWorkSet.getComment());
        assertEquals(Long.parseLong(workSetElement.attributeValue("CreatedTime")),
                testWorkSet.getCreatedTime().getTime());
        assertEquals(Long.parseLong(workSetElement.attributeValue("LastUpdatedTime")),
                testWorkSet.getLastUpdatedTime().getTime());
        assertEquals(workSetElement.elements().get(0).getName(), "Worksheet");
    }

    /**
     * Test method for {@link notes.entity.workset.WorkSet#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        WorkSet testWorkSet = (WorkSet) (testData.documentMap.get(3L));
        Element workSetElement = testWorkSet.toXMLElement();
        WorkSet newWorkSet = new WorkSet().buildFromXMLElement(workSetElement);

        assertEquals(testWorkSet, newWorkSet);
    }

}

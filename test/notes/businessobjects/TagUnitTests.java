package notes.businessobjects;

import core.EasyNoteUnitTestCase;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code Tag}.
 *
 * Author: Rui Du
 */
public class TagUnitTests extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link notes.businessobjects.Tag#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        final UnitTestData testData = new UnitTestData();
        Tag tag = testData.tagIdMap.get(1L);
        assertTrue(tag.equals(CACHE.getTagCache().getTagIdMap().get(1L)));
        assertFalse(tag.equals(new Tag()));
        assertFalse(tag.equals(new Object()));
    }

    /**
     * Test method for {@link notes.businessobjects.Tag#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final UnitTestData testData = new UnitTestData();
        assertEquals(testData.tagIdMap.get(1L).hashCode(), CACHE.getTagCache().getTagIdMap()
                .get(1L).hashCode());
    }

    /**
     * Test method for {@link notes.businessobjects.Tag#toString()}.
     */
    @Test
    public void testToString() {
        final UnitTestData testData = new UnitTestData();
        Tag testTag = testData.tagIdMap.get(1L);
        Tag cachedTag = CACHE.getTagCache().getTagIdMap().get(1L);
        assertEquals(StringUtils.substringAfter(testTag.toString(), "["),
                StringUtils.substringAfter(cachedTag.toString(), "["));
    }

    /**
     * Test method for {@link notes.businessobjects.Tag#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Tag testTag = testData.tagIdMap.get(1L);
        Element tagElement = testTag.toXMLElement();

        assertEquals(tagElement.getName(), "Tag");
        assertNotNull(tagElement.attribute("TagId"));
        assertNotNull(tagElement.getText());
        assertEquals(Long.parseLong(tagElement.attributeValue("TagId")), testTag.getTagId().longValue());
        assertEquals(tagElement.getText(), testTag.getTagText());
    }

    /**
     * Test method for {@link notes.businessobjects.Tag#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Tag testTag = testData.tagIdMap.get(1L);
        Element tagElement = testTag.toXMLElement();
        Tag newTag = new Tag().buildFromXMLElement(tagElement);

        assertEquals(testTag, newTag);
    }
}

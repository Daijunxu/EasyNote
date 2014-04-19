package notes.data.cache;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.Tag;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@code CacheDelegate}.
 * <p/>
 * Author: Rui Du
 */
public class CacheUnitTest extends EasyNoteUnitTestCase {

    /**
     * Test method for {@link CacheDelegate#getDocumentCache()}.
     */
    @Test
    public void testGetDocumentCache() {
        assertNotNull(CACHE.getDocumentCache());
    }

    /**
     * Test method for {@link CacheDelegate#get()}.
     */
    @Test
    public void testGetInstance() {
        assertNotNull(CACHE);
    }

    /**
     * Test method for {@link CacheDelegate#getNoteCache()}.
     */
    @Test
    public void testGetNoteCache() {
        assertNotNull(CACHE.getNoteCache());
    }

    /**
     * Test method for {@link CacheDelegate#getTagCache()}.
     */
    @Test
    public void testGetTagCache() {
        assertNotNull(CACHE.getTagCache());
    }

    /**
     * Test method for {@link CacheDelegate#toXMLElement()}.
     */
    @Test
    public void testToXMLElement() {
        Element cacheElement = CACHE.toXMLElement();
        assertEquals(cacheElement.getName(), "Data");
        assertNotNull(cacheElement.elements());
        assertEquals(cacheElement.elements().size(), 3);
        assertNotNull(cacheElement.element("Documents"));
        assertNotNull(cacheElement.element("Tags"));
        assertNotNull(cacheElement.element("Notes"));
    }

    /**
     * Test method for {@link CacheDelegate#buildFromXMLElement(org.dom4j.Element)}.
     */
    @Test
    public void testBuildFromXMLElement() {
        final UnitTestData testData = new UnitTestData();
        Element noteCacheElement = CACHE.toXMLElement();
        CACHE.buildFromXMLElement(noteCacheElement);

        List<Document> documentList = CACHE.getDocumentCache().findAll();
        List<Note> noteList = CACHE.getNoteCache().findAll();
        List<Tag> tagList = CACHE.getTagCache().findAll();

        assertEquals(documentList.size(), testData.documentMap.size());
        assertEquals(noteList.size(), testData.noteMap.size());
        assertEquals(tagList.size(), testData.tagIdMap.size());

        for (Document document : documentList) {
            assertEquals(document, testData.documentMap.get(document.getDocumentId()));
        }

        for (Note note : noteList) {
            assertEquals(note, testData.noteMap.get(note.getNoteId()));
        }

        for (Tag tag : tagList) {
            assertEquals(tag, testData.tagIdMap.get(tag.getTagId()));
        }
    }
}

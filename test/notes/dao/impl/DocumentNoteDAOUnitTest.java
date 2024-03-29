package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.Tag;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code DocumentNoteDAO}.
 * <p/>
 * Author: Rui Du
 */
public class DocumentNoteDAOUnitTest extends EasyNoteUnitTestCase {

    private DocumentNoteDAO dao = DocumentNoteDAO.get();

    @Test
    public void testDeleteTag() {
        Tag deletedTag = new Tag(1L, "Algorithm");
        dao.deleteTag(deletedTag);
        assertNotNull(CACHE.getTagCache().findAll());
        assertNull(CACHE.getTagCache().find(deletedTag.getTagId()));
        assertNull(CACHE.getTagCache().find(deletedTag.getTagId()));
        assertNull(CACHE.getTagCache().find(deletedTag.getTagText()));
        assertFalse(CACHE.getNoteCache().find(2L).getTagIds().contains(deletedTag.getTagId()));
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testFindAllDocuments() {
        UnitTestData testData = new UnitTestData();
        List<Document> documentList = dao.findAllDocuments();
        assertNotNull(documentList);
        assertFalse(documentList.isEmpty());
        assertTrue(documentList.size() == testData.documentMap.size());
        for (Document document : documentList) {
            assertEquals(testData.documentMap.get(document.getDocumentId()), document);
        }
    }

    @Test
    public void testFindAllNotes() {
        UnitTestData testData = new UnitTestData();
        List<Note> noteList = dao.findAllNotes();
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == testData.noteMap.size());
        for (Note note : noteList) {
            assertEquals(testData.noteMap.get(note.getNoteId()), note);
        }
    }

    @Test
    public void testFindAllNotesByTagId() {
        UnitTestData testData = new UnitTestData();
        List<Note> noteList = dao.findAllNotesByTagId(1L);
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));
    }

    @Test
    public void testFindAllNotesContainingTextInAllDocuments() {
        UnitTestData testData = new UnitTestData();

        // Test not case sensitive, not exact search.
        List<Note> noteList = dao.findAllNotesContainingText(null, "fmm collaborative filtering", false, false);
        assertNotNull(noteList);
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));

        // Test not case sensitive, exact search.
        List<Note> noteList2 = dao.findAllNotesContainingText(null, "fmm collaborative filtering", false, true);
        assertNotNull(noteList2);
        assertTrue(noteList2.isEmpty());
        List<Note> noteList3 = dao.findAllNotesContainingText(null, "fmm extends existing", false, true);
        assertNotNull(noteList3);
        assertTrue(noteList3.size() == 1);
        assertEquals(testData.noteMap.get(noteList3.get(0).getNoteId()), noteList3.get(0));

        // Test case sensitive, not exact search.
        List<Note> noteList4 = dao.findAllNotesContainingText(null, "collaborative fitering fmm", true, false);
        assertNotNull(noteList4);
        assertTrue(noteList4.isEmpty());
        List<Note> noteList5 = dao.findAllNotesContainingText(null, "collaborative filtering FMM", true, false);
        assertNotNull(noteList5);
        assertTrue(noteList5.size() == 1);
        assertEquals(testData.noteMap.get(noteList5.get(0).getNoteId()), noteList5.get(0));

        // Test case sensitive, exact search.
        List<Note> noteList6 = dao.findAllNotesContainingText(null, "FMM extends existing", true, true);
        assertNotNull(noteList6);
        assertTrue(noteList6.size() == 1);
        assertEquals(testData.noteMap.get(noteList6.get(0).getNoteId()), noteList6.get(0));
    }

    @Test
    public void testFindAllNotesContainingTextWithCandidateDocuments() {
        UnitTestData testData = new UnitTestData();
        Set<Long> candidates = new HashSet<Long>();
        candidates.add(2L);

        // Test not case sensitive, not exact search.
        List<Note> noteList = dao.findAllNotesContainingText(candidates, "fmm collaborative filtering", false, false);
        assertNotNull(noteList);
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));

        // Test not case sensitive, exact search.
        List<Note> noteList2 = dao.findAllNotesContainingText(candidates, "fmm collaborative filtering", false, true);
        assertNotNull(noteList2);
        assertTrue(noteList2.isEmpty());
        List<Note> noteList3 = dao.findAllNotesContainingText(candidates, "fmm extends existing", false, true);
        assertNotNull(noteList3);
        assertTrue(noteList3.size() == 1);
        assertEquals(testData.noteMap.get(noteList3.get(0).getNoteId()), noteList3.get(0));

        // Test case sensitive, not exact search.
        List<Note> noteList4 = dao.findAllNotesContainingText(candidates, "collaborative fitering fmm", true, false);
        assertNotNull(noteList4);
        assertTrue(noteList4.isEmpty());
        List<Note> noteList5 = dao.findAllNotesContainingText(candidates, "collaborative filtering FMM", true, false);
        assertNotNull(noteList5);
        assertTrue(noteList5.size() == 1);
        assertEquals(testData.noteMap.get(noteList5.get(0).getNoteId()), noteList5.get(0));

        // Test case sensitive, exact search.
        List<Note> noteList6 = dao.findAllNotesContainingText(candidates, "FMM extends existing", true, true);
        assertNotNull(noteList6);
        assertTrue(noteList6.size() == 1);
        assertEquals(testData.noteMap.get(noteList6.get(0).getNoteId()), noteList6.get(0));

        // Test no result.
        candidates.clear();
        candidates.add(1L);
        List<Note> noteList7 = dao.findAllNotesContainingText(candidates, "fmm", false, false);
        assertNotNull(noteList7);
        assertTrue(noteList7.isEmpty());
    }

    @Test
    public void testFindAllTags() {
        UnitTestData testData = new UnitTestData();
        List<Tag> tagList = dao.findAllTags();
        assertNotNull(tagList);
        assertFalse(tagList.isEmpty());
        assertTrue(tagList.size() == 2);
        for (Tag tag : tagList) {
            assertEquals(testData.tagIdMap.get(tag.getTagId()), tag);
        }
    }

    @Test
    public void testFindDocumentById() {
        UnitTestData testData = new UnitTestData();
        Document document = dao.findDocumentById(1L);
        assertNotNull(document);
        assertEquals(testData.documentMap.get(1L), document);
    }

    @Test
    public void testFindNoteById() {
        UnitTestData testData = new UnitTestData();
        Note note = dao.findNoteById(1L);
        assertNotNull(note);
        assertEquals(testData.noteMap.get(1L), note);
    }

    @Test
    public void testFindTagById() {
        UnitTestData testData = new UnitTestData();
        Tag tag = dao.findTagById(1L);
        assertNotNull(tag);
        assertEquals(testData.tagIdMap.get(1L), tag);
    }

    @Test
    public void testFindTagByText() {
        UnitTestData testData = new UnitTestData();
        Tag tag = dao.findTagByText("Algorithm");
        assertNotNull(tag);
        assertEquals(testData.tagTextMap.get("Algorithm"), tag);
    }

    @Test
    public void testUpdateTag() {
        UnitTestData testData = new UnitTestData();
        Tag newTag = new Tag(1L, "Method");
        Tag testTag = testData.tagIdMap.get(newTag.getTagId());
        Tag updatedTag = dao.updateTag(newTag);

        assertNotNull(updatedTag);
        assertEquals(updatedTag, CACHE.getTagCache().find(newTag.getTagId()));
        assertFalse(updatedTag.equals(testTag));
        assertEquals(updatedTag.getTagText(), newTag.getTagText());
        assertNull(CACHE.getTagCache().find(testTag.getTagText()));
        assertNotNull(CACHE.getTagCache().find(updatedTag.getTagText()));
        assertTrue(CACHE.isCacheChanged());
    }

    @Test
    public void testSaveTag() {
        Tag newTag = new Tag(3L, "Performance");
        Tag savedTag = dao.saveTag(newTag);
        assertEquals(savedTag, CACHE.getTagCache().find(newTag.getTagId()));
        assertNotNull(CACHE.getTagCache().find(newTag.getTagText()));
        assertTrue(CACHE.isCacheChanged());
    }

}

package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.dao.NoteDAO;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code AbstractNoteDAO}.
 *
 * @author Rui Du
 */
public class AbstractNoteDAOUnitTests extends EasyNoteUnitTestCase {

    /**
     * The data access object for the {@code NoteDAO}.
     */
    private NoteDAO<Note, Document> dao = new BookNoteDAO();

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#deleteTag(notes.entity.Tag)}.
     */
    @Test
    public void testDeleteTag() {
        Tag deletedTag = new Tag(1L, "Algorithm");
        dao.deleteTag(deletedTag);
        assertNotNull(Cache.get().getTagCache().getTagIdMap());
        assertNotNull(Cache.get().getTagCache().getTagTextMap());
        assertFalse(Cache.get().getTagCache().getTagIdMap().isEmpty());
        assertFalse(Cache.get().getTagCache().getTagTextMap().isEmpty());
        assertFalse(Cache.get().getTagCache().getTagIdMap().containsKey(deletedTag.getTagId()));
        assertNull(Cache.get().getTagCache().getTagIdMap().get(deletedTag.getTagId()));
        assertFalse(Cache.get().getTagCache().getTagTextMap().containsKey(deletedTag.getTagText()));
        assertFalse(Cache.get().getNoteCache().getNoteMap().get(2L).getTagIds()
                .contains(deletedTag.getTagId()));
    }

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findAllDocuments()}.
     */
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

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findAllNotes()}.
     */
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

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findAllNotesByTagId(java.lang.Long)}.
     */
    @Test
    public void testFindAllNotesByTagId() {
        UnitTestData testData = new UnitTestData();
        List<Note> noteList = dao.findAllNotesByTagId(1L);
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));
    }

    /**
     * Test method for
     * {@link notes.dao.impl.AbstractNoteDAO#findAllNotesContainingText(java.lang.String, boolean, boolean)}
     * .
     */
    @Test
    public void testFindAllNotesContainingText() {
        UnitTestData testData = new UnitTestData();

        // Test not case sensitive, not exact search.
        List<Note> noteList = dao.findAllNotesContainingText("fmm collaborative filtering", false,
                false);
        assertNotNull(noteList);
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));

        // Test not case sensitive, exact search.
        List<Note> noteList2 = dao.findAllNotesContainingText("fmm collaborative filtering", false,
                true);
        assertNotNull(noteList2);
        assertTrue(noteList2.isEmpty());
        List<Note> noteList3 = dao.findAllNotesContainingText("fmm extends existing", false, true);
        assertNotNull(noteList3);
        assertTrue(noteList3.size() == 1);
        assertEquals(testData.noteMap.get(noteList3.get(0).getNoteId()), noteList3.get(0));

        // Test case sensitive, not exact search.
        List<Note> noteList4 = dao.findAllNotesContainingText("collaborative fitering fmm", true,
                false);
        assertNotNull(noteList4);
        assertTrue(noteList4.isEmpty());
        List<Note> noteList5 = dao.findAllNotesContainingText("collaborative filtering FMM", true,
                false);
        assertNotNull(noteList5);
        assertTrue(noteList5.size() == 1);
        assertEquals(testData.noteMap.get(noteList5.get(0).getNoteId()), noteList5.get(0));

        // Test case sensitive, exact search.
        List<Note> noteList6 = dao.findAllNotesContainingText("FMM extends existing", true, true);
        assertNotNull(noteList6);
        assertTrue(noteList6.size() == 1);
        assertEquals(testData.noteMap.get(noteList6.get(0).getNoteId()), noteList6.get(0));
    }

    /**
     * Test method for
     * {@link notes.dao.impl.AbstractNoteDAO#findAllNotesContainingText(java.lang.Long, java.lang.String, boolean, boolean)}
     * .
     */
    @Test
    public void testFindAllNotesContainingText2() {
        UnitTestData testData = new UnitTestData();

        // Test not case sensitive, not exact search.
        List<Note> noteList = dao.findAllNotesContainingText(2L, "fmm collaborative filtering",
                false, false);
        assertNotNull(noteList);
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));

        // Test not case sensitive, exact search.
        List<Note> noteList2 = dao.findAllNotesContainingText(2L, "fmm collaborative filtering",
                false, true);
        assertNotNull(noteList2);
        assertTrue(noteList2.isEmpty());
        List<Note> noteList3 = dao.findAllNotesContainingText(2L, "fmm extends existing", false,
                true);
        assertNotNull(noteList3);
        assertTrue(noteList3.size() == 1);
        assertEquals(testData.noteMap.get(noteList3.get(0).getNoteId()), noteList3.get(0));

        // Test case sensitive, not exact search.
        List<Note> noteList4 = dao.findAllNotesContainingText(2L, "collaborative fitering fmm",
                true, false);
        assertNotNull(noteList4);
        assertTrue(noteList4.isEmpty());
        List<Note> noteList5 = dao.findAllNotesContainingText(2L, "collaborative filtering FMM",
                true, false);
        assertNotNull(noteList5);
        assertTrue(noteList5.size() == 1);
        assertEquals(testData.noteMap.get(noteList5.get(0).getNoteId()), noteList5.get(0));

        // Test case sensitive, exact search.
        List<Note> noteList6 = dao.findAllNotesContainingText(2L, "FMM extends existing", true,
                true);
        assertNotNull(noteList6);
        assertTrue(noteList6.size() == 1);
        assertEquals(testData.noteMap.get(noteList6.get(0).getNoteId()), noteList6.get(0));

        // Test no result.
        List<Note> noteList7 = dao.findAllNotesContainingText(1L, "fmm", false, false);
        assertNotNull(noteList7);
        assertTrue(noteList7.isEmpty());
    }

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findAllTags()}.
     */
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

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findDocumentById(java.lang.Long)}.
     */
    @Test
    public void testFindDocumentById() {
        UnitTestData testData = new UnitTestData();
        Document document = dao.findDocumentById(1L);
        assertNotNull(document);
        assertEquals(testData.documentMap.get(1L), document);
    }

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findNoteById(java.lang.Long)}.
     */
    @Test
    public void testFindNoteById() {
        UnitTestData testData = new UnitTestData();
        Note note = dao.findNoteById(1L);
        assertNotNull(note);
        assertEquals(testData.noteMap.get(1L), note);
    }

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findTagById(java.lang.Long)}.
     */
    @Test
    public void testFindTagById() {
        UnitTestData testData = new UnitTestData();
        Tag tag = dao.findTagById(1L);
        assertNotNull(tag);
        assertEquals(testData.tagIdMap.get(1L), tag);
    }

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#findTagByText(java.lang.String)}.
     */
    @Test
    public void testFindTagByText() {
        UnitTestData testData = new UnitTestData();
        Tag tag = dao.findTagByText("Algorithm");
        assertNotNull(tag);
        assertEquals(testData.tagTextMap.get("Algorithm"), tag);
    }

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#updateTag(notes.entity.Tag)}.
     */
    @Test
    public void testUpdateTag() {
        UnitTestData testData = new UnitTestData();
        Tag newTag = new Tag(1L, "Method");
        Tag testTag = testData.tagIdMap.get(newTag.getTagId());
        Tag updatedTag = dao.updateTag(newTag);

        assertNotNull(updatedTag);
        assertEquals(updatedTag, Cache.get().getTagCache().getTagIdMap().get(newTag.getTagId()));
        assertFalse(updatedTag.equals(testTag));
        assertEquals(updatedTag.getTagText(), newTag.getTagText());
        assertFalse(Cache.get().getTagCache().getTagTextMap().containsKey(testTag.getTagText()));
        assertTrue(Cache.get().getTagCache().getTagTextMap().containsKey(updatedTag.getTagText()));
    }

    /**
     * Test method for {@link notes.dao.impl.AbstractNoteDAO#saveTag(notes.entity.Tag)}.
     */
    @Test
    public void testSaveTag() {
        Tag newTag = new Tag(3L, "Performance");
        Tag savedTag = dao.saveTag(newTag);
        assertEquals(savedTag, Cache.get().getTagCache().getTagIdMap().get(newTag.getTagId()));
        assertTrue(Cache.get().getTagCache().getTagTextMap().containsKey(newTag.getTagText()));
        assertEquals(Cache.get().getTagCache().getMaxTagId(), newTag.getTagId());
    }

}

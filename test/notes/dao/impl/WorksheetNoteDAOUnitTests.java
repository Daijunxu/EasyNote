package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.entity.Note;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code WorksheetNoteDAO}.
 * <p/>
 * User: rui
 * Date: 10/1/13
 * Time: 12:36 AM
 */
public class WorksheetNoteDAOUnitTests extends EasyNoteUnitTestCase {

    /**
     * The data access object for the {@code WorksheetNoteDAO}.
     */
    private WorksheetNoteDAO dao = new WorksheetNoteDAO();

    /**
     * Test method for
     * {@link notes.dao.impl.WorksheetNoteDAO#deleteWorksheet(notes.entity.workset.Worksheet, Long)}
     */
    @Test
    public void testDeleteWorksheet() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset workset = (Workset) testData.documentMap.get(3L);
        Worksheet deleteWorksheet = workset.getWorksheetsMap().get(1L);
        dao.deleteWorksheet(deleteWorksheet, 3L);
        assertNotNull(Cache.get().getDocumentCache().getDocumentMap().get(3L));
        Workset cachedWorkset = (Workset) Cache.get().getDocumentCache().getDocumentMap().get(3L);
        assertFalse(cachedWorkset.getWorksheetsMap().isEmpty());
        assertFalse(cachedWorkset.getWorksheetsMap().containsKey(1L));
        assertFalse(Cache.get().getNoteCache().getNoteMap().containsKey(3L));
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#deleteDocument(notes.entity.Document)}.
     */
    @Test
    public void testDeleteDocument() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset deleteDocument = (Workset) testData.documentMap.get(3L);
        dao.deleteDocument(deleteDocument);
        assertNotNull(Cache.get().getDocumentCache().getDocumentMap());
        assertNotNull(Cache.get().getDocumentCache().getDocumentTitleIdMap());
        assertFalse(Cache.get().getDocumentCache().getDocumentMap().isEmpty());
        assertFalse(Cache.get().getDocumentCache().getDocumentTitleIdMap().isEmpty());
        assertNull(Cache.get().getDocumentCache().getDocumentMap()
                .get(deleteDocument.getDocumentId()));
        assertFalse(Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .containsKey(deleteDocument.getDocumentTitle()));
        assertFalse(Cache.get().getNoteCache().getNoteMap().containsKey(3L));
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#deleteNote(notes.entity.Note)}.
     */
    @Test
    public void testDeleteNote() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote deleteNote = (WorksheetNote) testData.noteMap.get(3L);
        dao.deleteNote(deleteNote);
        assertNotNull(Cache.get().getNoteCache().getNoteMap());
        assertFalse(Cache.get().getNoteCache().getNoteMap().isEmpty());
        assertNull(Cache.get().getNoteCache().getNoteMap().get(deleteNote.getNoteId()));
        Workset workset = (Workset) Cache.get().getDocumentCache().getDocumentMap()
                .get(deleteNote.getDocumentId());
        assertFalse(workset.getWorksheetsMap().get(deleteNote.getWorksheetId()).getNotesList()
                .contains(deleteNote.getNoteId()));
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#findAllNotesByWorksheets(java.lang.Long)}.
     */
    @Test
    public void testFindAllNotesByWorksheets() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Map<Long, List<WorksheetNote>> noteMap = dao.findAllNotesByWorksheets(3L);
        assertNotNull(noteMap);
        assertTrue(noteMap.size() == 2);
        assertTrue(noteMap.containsKey(1L));
        assertNotNull(noteMap.get(1L));
        assertTrue(noteMap.get(1L).size() == 1);
        assertTrue(noteMap.get(2L).isEmpty());
        assertEquals(testData.noteMap.get(noteMap.get(1L).get(0).getNoteId()),
                noteMap.get(1L).get(0));
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#findAllNotesByDocumentId(java.lang.Long)}.
     */
    @Test
    public void testFindAllNotesByDocumentId() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        List<Note> noteList = dao.findAllNotesByDocumentId(3L);
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));
    }

    /**
     * Test method for
     * {@link notes.dao.impl.WorksheetNoteDAO#mergeWorksheet(notes.entity.workset.Worksheet, Long, Long)}.
     */
    @Test
    public void testMergeWorksheet() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset testWorkset = (Workset) testData.documentMap.get(3L);
        Worksheet testWorksheet = testWorkset.getWorksheetsMap().get(1L);
        Worksheet updateWorksheet = new Worksheet();
        updateWorksheet.setWorksheetId(999L);
        updateWorksheet.setWorksheetTitle("Another worksheet title");
        updateWorksheet.setNotesList(testWorksheet.getNotesList());
        Worksheet mergedWorksheet = dao.mergeWorksheet(updateWorksheet, testWorkset.getDocumentId(),
                testWorksheet.getWorksheetId());

        assertNotNull(mergedWorksheet);
        Workset cachedWorkset = (Workset) Cache.get().getDocumentCache().getDocumentMap()
                .get(testWorkset.getDocumentId());
        assertEquals(mergedWorksheet, cachedWorkset.getWorksheetsMap().get(mergedWorksheet.getWorksheetId()));
        assertFalse(mergedWorksheet.equals(testWorksheet));
        assertEquals(mergedWorksheet.getWorksheetId(), updateWorksheet.getWorksheetId());
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#mergeDocument(notes.entity.Document)}.
     */
    @Test
    public void testMergeDocument() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset testWorkset = (Workset) testData.documentMap.get(3L);
        Workset newWorkset = new Workset();
        newWorkset.setDocumentId(testWorkset.getDocumentId());
        newWorkset.setDocumentTitle(testWorkset.getDocumentTitle());
        newWorkset.setAuthorsList(testWorkset.getAuthorsList());
        newWorkset.setComment("This workset is not worth reading!");
        newWorkset.setWorksheetsMap(testWorkset.getWorksheetsMap());
        newWorkset.getWorksheetsMap().put(2L, new Worksheet(2L, "Second Worksheet", new ArrayList<Long>()));
        Workset updatedWorkset = (Workset) dao.mergeDocument(newWorkset);

        assertNotNull(updatedWorkset);
        assertEquals(updatedWorkset, Cache.get().getDocumentCache().getDocumentMap().get(newWorkset.getDocumentId()));
        assertFalse(updatedWorkset.equals(testWorkset));
        assertEquals(updatedWorkset.getComment(), newWorkset.getComment());
        assertEquals(updatedWorkset.getWorksheetsMap(), newWorkset.getWorksheetsMap());
        assertEquals(testWorkset.getDocumentId(), updatedWorkset.getDocumentId());
        assertEquals(testWorkset.getCreatedTime(), updatedWorkset.getCreatedTime());
        assertTrue(testWorkset.getLastUpdatedTime().compareTo(updatedWorkset.getLastUpdatedTime()) < 0);
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#mergeNote(notes.entity.Note)}.
     */
    @Test
    public void testMergeNote() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = (WorksheetNote) testData.noteMap.get(3L);
        WorksheetNote newWorksheetNote = new WorksheetNote();
        newWorksheetNote.setNoteId(testWorksheetNote.getNoteId());
        newWorksheetNote.setDocumentId(testWorksheetNote.getDocumentId());
        newWorksheetNote.setWorksheetId(2L);
        newWorksheetNote.setTagIds(testWorksheetNote.getTagIds());
        newWorksheetNote.setNoteText(testWorksheetNote.getNoteText());
        newWorksheetNote.setNoteStatus(testWorksheetNote.getNoteStatus());
        WorksheetNote updatedWorksheetNote = (WorksheetNote) dao.mergeNote(newWorksheetNote);

        assertNotNull(updatedWorksheetNote);
        assertEquals(updatedWorksheetNote, Cache.get().getNoteCache().getNoteMap().get(newWorksheetNote.getNoteId()));
        assertFalse(updatedWorksheetNote.equals(testWorksheetNote));
        assertEquals(updatedWorksheetNote.getWorksheetId(), newWorksheetNote.getWorksheetId());
        assertFalse(updatedWorksheetNote.getWorksheetId().equals(testWorksheetNote.getWorksheetId()));
        assertEquals(testWorksheetNote.getNoteId(), updatedWorksheetNote.getNoteId());
        assertEquals(testWorksheetNote.getCreatedTime(), updatedWorksheetNote.getCreatedTime());
    }

    /**
     * Test method for
     * {@link notes.dao.impl.WorksheetNoteDAO#saveWorksheet(notes.entity.workset.Worksheet, java.lang.Long)}.
     */
    @Test
    public void testSaveWorksheet() {
        Worksheet newWorksheet = new Worksheet();
        newWorksheet.setWorksheetId(3L);
        newWorksheet.setWorksheetTitle("Worksheet 3");
        Worksheet savedWorksheet = dao.saveWorksheet(newWorksheet, 3L);
        Workset workset = (Workset) Cache.get().getDocumentCache().getDocumentMap().get(3L);

        assertEquals(savedWorksheet, workset.getWorksheetsMap().get(3L));
        assertNotNull(savedWorksheet.getNotesList());
        assertTrue(savedWorksheet.getNotesList().isEmpty());
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#saveDocument(notes.entity.Document)}.
     */
    @Test
    public void testSaveDocument() {
        Workset newWorkset = new Workset();
        newWorkset.setDocumentId(4L);
        newWorkset.setDocumentTitle("Data Mining");
        newWorkset.setAuthorsList(new ArrayList<String>(Arrays.asList("Author")));
        newWorkset.setComment("Good workset.");
        TreeMap<Long, Worksheet> worksheetsMap = new TreeMap<Long, Worksheet>();
        Worksheet worksheet1 = new Worksheet(1L, "Worksheet 1", new ArrayList<Long>());
        worksheet1.getNotesList().add(1L);
        worksheetsMap.put(1L, worksheet1);
        newWorkset.setWorksheetsMap(worksheetsMap);
        Workset savedWorkset = (Workset) dao.saveDocument(newWorkset);

        assertEquals(savedWorkset,
                Cache.get().getDocumentCache().getDocumentMap().get(newWorkset.getDocumentId()));
        assertTrue(Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .containsKey(newWorkset.getDocumentTitle()));
        assertEquals(Cache.get().getDocumentCache().getMaxDocumentId(), newWorkset.getDocumentId());
        assertNotNull(savedWorkset.getCreatedTime());
        assertNotNull(savedWorkset.getLastUpdatedTime());
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#saveNote(notes.entity.Note)}.
     */
    @Test
    public void testSaveNote() {
        WorksheetNote newWorksheetNote = new WorksheetNote();
        newWorksheetNote.setNoteId(4L);
        newWorksheetNote.setDocumentId(3L);
        newWorksheetNote.setWorksheetId(2L);
        newWorksheetNote.setTagIds(new ArrayList<Long>(Arrays.asList(2L)));
        newWorksheetNote.setNoteText("New note.");
        WorksheetNote savedWorksheetNote = (WorksheetNote) dao.saveNote(newWorksheetNote);

        assertEquals(savedWorksheetNote,
                Cache.get().getNoteCache().getNoteMap().get(newWorksheetNote.getNoteId()));
        assertEquals(Cache.get().getNoteCache().getMaxNoteId(), newWorksheetNote.getNoteId());
        assertNotNull(savedWorksheetNote.getCreatedTime());
        Workset workset = (Workset) Cache.get().getDocumentCache().getDocumentMap()
                .get(savedWorksheetNote.getDocumentId());
        Worksheet worksheet = workset.getWorksheetsMap().get(savedWorksheetNote.getWorksheetId());
        assertTrue(worksheet.getNotesList().contains(savedWorksheetNote.getNoteId()));
    }
}

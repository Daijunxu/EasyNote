package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
import notes.businessobjects.workset.Workset;
import notes.businessobjects.workset.Worksheet;
import notes.businessobjects.workset.WorksheetNote;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code WorksheetNoteDAO}.
 * <p/>
 * Author: Rui Du
 * Date: 10/1/13
 * Time: 12:36 AM
 */
public class WorksheetNoteDAOUnitTest extends EasyNoteUnitTestCase {

    /**
     * The data access object for the {@code WorksheetNoteDAO}.
     */
    private WorksheetNoteDAO dao = WorksheetNoteDAO.get();

    /**
     * Test method for
     * {@link notes.dao.impl.WorksheetNoteDAO#deleteWorksheet(notes.businessobjects.workset.Worksheet, Long)}
     */
    @Test
    public void testDeleteWorksheet() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset workset = testData.getWorkset();
        Long documentId = workset.getDocumentId();
        Worksheet deleteWorksheet = workset.getWorksheetsMap().get(1L);

        dao.deleteWorksheet(deleteWorksheet, documentId);
        assertNotNull(CACHE.getDocumentCache().find(documentId));
        Workset cachedWorkset = (Workset) CACHE.getDocumentCache().find(documentId);
        assertFalse(cachedWorkset.getWorksheetsMap().isEmpty());
        assertFalse(cachedWorkset.getWorksheetsMap().containsKey(1L));
        assertNull(CACHE.getNoteCache().find(3L));
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#deleteDocument(notes.businessobjects.Document)}.
     */
    @Test
    public void testDeleteDocument() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset deleteDocument = testData.getWorkset();
        dao.deleteDocument(deleteDocument);
        assertFalse(CACHE.getDocumentCache().findAll().isEmpty());
        assertNull(CACHE.getDocumentCache().find(deleteDocument.getDocumentId()));
        assertNull(CACHE.getNoteCache().find(3L));
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#deleteNote(notes.businessobjects.Note)}.
     */
    @Test
    public void testDeleteNote() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote deleteNote = testData.getWorksheetNote();
        Workset workset = (Workset) (CACHE.getDocumentCache().find(deleteNote.getDocumentId()));
        Worksheet worksheet = workset.getWorksheetsMap().get(deleteNote.getWorksheetId());
        Date updateTimeBeforeDelete = worksheet.getLastUpdatedTime();
        dao.deleteNote(deleteNote);
        Date updateTimeAfterDelete = worksheet.getLastUpdatedTime();

        assertFalse(CACHE.getNoteCache().findAll().isEmpty());
        assertNull(CACHE.getNoteCache().find(deleteNote.getNoteId()));
        assertTrue(updateTimeAfterDelete.after(updateTimeBeforeDelete));
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
     * {@link notes.dao.impl.WorksheetNoteDAO#updateWorksheet(notes.businessobjects.workset.Worksheet, Long, Long)}.
     */
    @Test
    public void testUpdateWorksheet() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset testWorkset = testData.getWorkset();
        Worksheet testWorksheet = testWorkset.getWorksheetsMap().get(1L);
        Worksheet updateWorksheet = new Worksheet();
        updateWorksheet.setWorksheetId(999L);
        updateWorksheet.setWorksheetTitle("Another worksheet title");
        updateWorksheet.setNotesList(testWorksheet.getNotesList());

        Worksheet updatedWorksheet = dao.updateWorksheet(updateWorksheet, testWorkset.getDocumentId(),
                testWorksheet.getWorksheetId());

        assertNotNull(updatedWorksheet);
        Workset cachedWorkset = (Workset) CACHE.getDocumentCache().find(testWorkset.getDocumentId());
        assertEquals(updatedWorksheet, cachedWorkset.getWorksheetsMap().get(updatedWorksheet.getWorksheetId()));
        assertFalse(updatedWorksheet.equals(testWorksheet));
        assertEquals(updatedWorksheet.getWorksheetId(), updateWorksheet.getWorksheetId());
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#updateDocument(notes.businessobjects.Document)}.
     */
    @Test
    public void testUpdateDocument() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        Workset testWorkset = testData.getWorkset();
        Workset newWorkset = new Workset();
        newWorkset.setDocumentId(testWorkset.getDocumentId());
        newWorkset.setDocumentTitle(testWorkset.getDocumentTitle());
        newWorkset.setAuthorsList(testWorkset.getAuthorsList());
        newWorkset.setComment("This workset is not worth reading!");
        newWorkset.setWorksheetsMap(testWorkset.getWorksheetsMap());
        newWorkset.getWorksheetsMap().put(2L, new Worksheet(2L, "Second Worksheet", "New comments", new ArrayList<Long>(),
                new Date(1341429512312L), new Date(1341429512312L)));
        Workset updatedWorkset = (Workset) dao.updateDocument(newWorkset);

        assertNotNull(updatedWorkset);
        assertEquals(updatedWorkset, CACHE.getDocumentCache().find(newWorkset.getDocumentId()));
        assertFalse(updatedWorkset.equals(testWorkset));
        assertEquals(updatedWorkset.getComment(), newWorkset.getComment());
        assertEquals(updatedWorkset.getWorksheetsMap(), newWorkset.getWorksheetsMap());
        assertEquals(testWorkset.getDocumentId(), updatedWorkset.getDocumentId());
        assertEquals(testWorkset.getCreatedTime(), updatedWorkset.getCreatedTime());
        assertTrue(testWorkset.getLastUpdatedTime().compareTo(updatedWorkset.getLastUpdatedTime()) < 0);
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#updateNote(notes.businessobjects.Note)}.
     */
    @Test
    public void testUpdateNote() {
        EasyNoteUnitTestCase.UnitTestData testData = new EasyNoteUnitTestCase.UnitTestData();
        WorksheetNote testWorksheetNote = testData.getWorksheetNote();
        WorksheetNote newWorksheetNote = new WorksheetNote();
        newWorksheetNote.setNoteId(testWorksheetNote.getNoteId());
        newWorksheetNote.setDocumentId(testWorksheetNote.getDocumentId());
        newWorksheetNote.setWorksheetId(2L);
        newWorksheetNote.setTagIds(testWorksheetNote.getTagIds());
        newWorksheetNote.setNoteText(testWorksheetNote.getNoteText());
        newWorksheetNote.setNoteStatus(testWorksheetNote.getNoteStatus());
        WorksheetNote updatedWorksheetNote = (WorksheetNote) dao.updateNote(newWorksheetNote);

        assertNotNull(updatedWorksheetNote);
        assertEquals(updatedWorksheetNote, CACHE.getNoteCache().find(newWorksheetNote.getNoteId()));
        assertFalse(updatedWorksheetNote.equals(testWorksheetNote));
        assertEquals(updatedWorksheetNote.getWorksheetId(), newWorksheetNote.getWorksheetId());
        assertFalse(updatedWorksheetNote.getWorksheetId().equals(testWorksheetNote.getWorksheetId()));
        assertEquals(testWorksheetNote.getNoteId(), updatedWorksheetNote.getNoteId());
        assertEquals(testWorksheetNote.getCreatedTime(), updatedWorksheetNote.getCreatedTime());
    }

    /**
     * Test method for
     * {@link notes.dao.impl.WorksheetNoteDAO#saveWorksheet(notes.businessobjects.workset.Worksheet, java.lang.Long)}.
     */
    @Test
    public void testSaveWorksheet() {
        Worksheet newWorksheet = new Worksheet();
        newWorksheet.setWorksheetId(3L);
        newWorksheet.setWorksheetTitle("Worksheet 3");
        Worksheet savedWorksheet = dao.saveWorksheet(newWorksheet, 3L);
        Workset workset = (Workset) CACHE.getDocumentCache().find(3L);

        assertEquals(savedWorksheet, workset.getWorksheetsMap().get(3L));
        assertNotNull(savedWorksheet.getNotesList());
        assertTrue(savedWorksheet.getNotesList().isEmpty());
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#saveDocument(notes.businessobjects.Document)}.
     */
    @Test
    public void testSaveDocument() {
        Workset newWorkset = new Workset();
        newWorkset.setDocumentId(4L);
        newWorkset.setDocumentTitle("Data Mining");
        newWorkset.setAuthorsList(new ArrayList<String>(Arrays.asList("Author")));
        newWorkset.setComment("Good workset.");
        Worksheet worksheet1 = new Worksheet(1L, "Worksheet 1", "Some comments", new ArrayList<Long>(), new Date(1341429512312L),
                new Date(1341429512312L));
        worksheet1.getNotesList().add(1L);
        List<Long> worksheetIdsList = new ArrayList<Long>();
        HashMap<Long, Worksheet> worksheetsMap = new HashMap<Long, Worksheet>();
        worksheetIdsList.add(1L);
        worksheetsMap.put(1L, worksheet1);
        newWorkset.setWorksheetIdsList(worksheetIdsList);
        newWorkset.setWorksheetsMap(worksheetsMap);
        Workset savedWorkset = (Workset) dao.saveDocument(newWorkset);

        assertEquals(savedWorkset, CACHE.getDocumentCache().find(newWorkset.getDocumentId()));
        assertNotNull(savedWorkset.getCreatedTime());
        assertNotNull(savedWorkset.getLastUpdatedTime());
    }

    /**
     * Test method for {@link notes.dao.impl.WorksheetNoteDAO#saveNote(notes.businessobjects.Note)}.
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

        assertEquals(savedWorksheetNote, CACHE.getNoteCache().find(newWorksheetNote.getNoteId()));
        assertNotNull(savedWorksheetNote.getCreatedTime());
        Workset workset = (Workset) CACHE.getDocumentCache().find(savedWorksheetNote.getDocumentId());
        Worksheet worksheet = workset.getWorksheetsMap().get(savedWorksheetNote.getWorksheetId());
        assertTrue(worksheet.getNotesList().contains(savedWorksheetNote.getNoteId()));
    }
}

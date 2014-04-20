package notes.dao.impl;

import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.workset.Workset;
import notes.businessobjects.workset.Worksheet;
import notes.businessobjects.workset.WorksheetNote;
import notes.dao.DuplicateRecordException;
import notes.data.cache.CacheDelegate;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data access object for workset notes.
 * <p/>
 * Author: Rui Du
 * Date: 10/1/13
 * Time: 12:22 AM
 */
public class WorksheetNoteDAO extends DocumentNoteDAO {

    private static final WorksheetNoteDAO INSTANCE = new WorksheetNoteDAO();
    private static final CacheDelegate CACHE = CacheDelegate.get();

    private WorksheetNoteDAO() {
    }

    public static WorksheetNoteDAO get() {
        return INSTANCE;
    }

    /**
     * Deletes a worksheet and all its notes.
     *
     * @param worksheet  The worksheet to delete.
     * @param documentId The document ID of the document that the worksheet belongs to.
     */
    public void deleteWorksheet(Worksheet worksheet, Long documentId) {
        Workset cachedWorkset = (Workset) CACHE.getDocumentCache().find(documentId);
        Worksheet cachedWorksheet = cachedWorkset.getWorksheetsMap().get(worksheet.getWorksheetId());

        // Remove all notes in the worksheet.
        for (Long noteId : cachedWorksheet.getNotesList()) {
            CACHE.getNoteCache().remove(noteId);
        }

        // Remove the worksheet.
        cachedWorkset.getWorksheetsMap().remove(worksheet.getWorksheetId());
        cachedWorkset.getWorksheetIdsList().remove(worksheet.getWorksheetId());

        // Update workset's last updated time.
        cachedWorkset.setLastUpdatedTime(new Date());
    }

    @Override
    public void deleteDocument(Document document) {
        Workset cachedWorkset = (Workset) CACHE.getDocumentCache().find(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Worksheet> worksheetsMap = cachedWorkset.getWorksheetsMap();
        for (Worksheet worksheet : worksheetsMap.values()) {
            for (Long noteId : worksheet.getNotesList()) {
                CACHE.getNoteCache().remove(noteId);
            }
        }

        // Remove the document in the document cache.
        CACHE.getDocumentCache().remove(cachedWorkset.getDocumentId());
    }

    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Workset workset = (Workset) CACHE.getDocumentCache().find(note.getDocumentId());
        Worksheet worksheet = workset.getWorksheetsMap().get(((WorksheetNote) note).getWorksheetId());
        worksheet.getNotesList().remove(noteId);

        // Remove the note in the note cache.
        CACHE.getNoteCache().remove(noteId);

        // Update worksheet's last updated time.
        worksheet.setLastUpdatedTime(new Date());

        // Update workset's last updated time.
        workset.setLastUpdatedTime(new Date());
    }

    /**
     * Finds all worksets.
     *
     * @return {@code Set<Long>} The set of document IDs.
     */
    public Set<Long> findAllWorksets() {
        Set<Long> resultSet = new HashSet<Long>();
        for (Document document : CACHE.getDocumentCache().findAll()) {
            if (document instanceof Workset) {
                resultSet.add(document.getDocumentId());
            }
        }
        return resultSet;
    }

    /**
     * Finds all notes in the document, grouped by worksheet IDs.
     *
     * @param documentId The document ID.
     * @return {@code Map} All notes in the document grouped by worksheet IDs.
     */
    public Map<Long, List<WorksheetNote>> findAllNotesByWorksheets(Long documentId) {
        Workset workset = (Workset) (CACHE.getDocumentCache().find(documentId));
        Map<Long, List<WorksheetNote>> noteMapByWorksheets = new HashMap<Long, List<WorksheetNote>>();

        for (Worksheet worksheet : workset.getWorksheetsMap().values()) {
            List<WorksheetNote> notesList = new ArrayList<WorksheetNote>();
            for (Long noteId : worksheet.getNotesList()) {
                notesList.add((WorksheetNote) CACHE.getNoteCache().find(noteId));
            }
            Collections.sort(notesList);
            noteMapByWorksheets.put(worksheet.getWorksheetId(), notesList);
        }
        return noteMapByWorksheets;
    }

    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        Workset workset = (Workset) (CACHE.getDocumentCache().find(documentId));
        List<Note> noteList = new ArrayList<Note>();

        for (Worksheet worksheet : workset.getWorksheetsMap().values()) {
            for (Long noteId : worksheet.getNotesList()) {
                noteList.add(CACHE.getNoteCache().find(noteId));
            }
        }
        Collections.sort(noteList);
        return noteList;
    }

    /**
     * Updates a worksheet.
     *
     * @param worksheet  The worksheet to update.
     * @param documentId The document ID of the workset that the worksheet belongs to.
     * @return {@code Worksheet} The updated worksheet object.
     */
    public Worksheet updateWorksheet(Worksheet worksheet, Long documentId, Long oldWorksheetId) {
        try {
            Workset cachedWorkset = (Workset) (CACHE.getDocumentCache().find(documentId));
            Map<Long, Worksheet> worksheetsMap = cachedWorkset.getWorksheetsMap();
            Worksheet cachedWorksheet = cachedWorkset.getWorksheetsMap().get(oldWorksheetId);
            Long updatedWorksheetId = worksheet.getWorksheetId();

            cachedWorksheet.setWorksheetTitle(worksheet.getWorksheetTitle());
            cachedWorksheet.setComment(worksheet.getComment());
            cachedWorksheet.setNotesList(worksheet.getNotesList());
            if (cachedWorksheet.getLastUpdatedTime() == null) {
                cachedWorksheet.setLastUpdatedTime(new Date());
            } else {
                cachedWorksheet.setLastUpdatedTime(worksheet.getLastUpdatedTime());
            }
            if (!oldWorksheetId.equals(updatedWorksheetId)) {
                // Worksheet id is changed.
                if (worksheetsMap.containsKey(updatedWorksheetId)) {
                    throw new InvalidKeyException("The updated worksheet id is already taken by another worksheet.");
                }
                cachedWorksheet.setWorksheetId(updatedWorksheetId);
                worksheetsMap.remove(oldWorksheetId);
                worksheetsMap.put(updatedWorksheetId, cachedWorksheet);
            }

            // Update workset's last updated time.
            if (worksheet.getLastUpdatedTime() != null) {
                cachedWorkset.setLastUpdatedTime(worksheet.getLastUpdatedTime());
            } else {
                cachedWorkset.setLastUpdatedTime(new Date());
            }

            return cachedWorksheet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Note updateNote(Note note) {
        WorksheetNote cachedNote = (WorksheetNote) CACHE.getNoteCache().find(note.getNoteId());
        Long oldWorksheetId = cachedNote.getWorksheetId();
        Workset workset = (Workset) (CACHE.getDocumentCache().find(note.getDocumentId()));

        cachedNote = (WorksheetNote) CACHE.getNoteCache().update(note);

        // Update worksheets' notes list.
        if (!oldWorksheetId.equals(((WorksheetNote) note).getWorksheetId())) {
            // The note is moved to another worksheet.
            Worksheet oldWorksheet = workset.getWorksheetsMap().get(oldWorksheetId);
            oldWorksheet.getNotesList().remove(note.getNoteId());
            Worksheet newWorksheet = workset.getWorksheetsMap().get(cachedNote.getWorksheetId());
            newWorksheet.getNotesList().add(note.getNoteId());

            // Update worksheet's last updated time.
            oldWorksheet.setLastUpdatedTime(new Date());
            newWorksheet.setLastUpdatedTime(new Date());
        } else {
            // Update worksheet's last updated time.
            Worksheet worksheet = workset.getWorksheetsMap().get(oldWorksheetId);
            worksheet.setLastUpdatedTime(new Date());
        }

        // Update workset's last updated time.
        workset.setLastUpdatedTime(new Date());

        return cachedNote;
    }

    /**
     * Saves a worksheet.
     *
     * @param worksheet  The worksheet to save.
     * @param documentId The document ID of the workset that the new worksheet belongs to.
     * @return {@code Worksheet} The saved worksheet, NULL if exception occurred.
     */
    public Worksheet saveWorksheet(Worksheet worksheet, Long documentId) {
        try {
            Workset cachedWorkset = (Workset) CACHE.getDocumentCache().find(documentId);
            Map<Long, Worksheet> worksheetMap = cachedWorkset.getWorksheetsMap();
            if (worksheetMap.containsKey(worksheet.getWorksheetId())) {
                throw new DuplicateRecordException("Duplicate worksheet ID when saving a new worksheet!");
            }
            if (worksheet.getNotesList() == null) {
                worksheet.setNotesList(new ArrayList<Long>());
            }
            worksheetMap.put(worksheet.getWorksheetId(), worksheet);

            // Add the new worksheet to the bottom of the worksheet list.
            cachedWorkset.getWorksheetIdsList().add(worksheet.getWorksheetId());

            // Update workset's created time and last updated time.
            if (worksheet.getCreatedTime() != null) {
                cachedWorkset.setCreatedTime(worksheet.getCreatedTime());
            } else {
                cachedWorkset.setCreatedTime(new Date());
            }
            if (worksheet.getLastUpdatedTime() != null) {
                cachedWorkset.setLastUpdatedTime(worksheet.getLastUpdatedTime());
            } else {
                cachedWorkset.setLastUpdatedTime(new Date());
            }

            return worksheet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Note saveNote(Note note) {
        if (note instanceof WorksheetNote) {
            try {
                WorksheetNote newNote = (WorksheetNote) CACHE.getNoteCache().insert(note);

                // Add the note ID to corresponding notes list in the worksheet.
                Workset workset = (Workset) (CACHE.getDocumentCache().find(newNote.getDocumentId()));
                Worksheet worksheet = workset.getWorksheetsMap().get(newNote.getWorksheetId());
                worksheet.getNotesList().add(newNote.getNoteId());

                // Update worksheet's last updated time.
                worksheet.setLastUpdatedTime(new Date());

                // Update workset's last updated time.
                workset.setLastUpdatedTime(new Date());

                return newNote;
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

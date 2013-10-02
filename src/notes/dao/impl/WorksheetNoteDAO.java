package notes.dao.impl;

import notes.dao.DuplicateRecordException;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data access object for workset notes.
 * <p/>
 * User: rui
 * Date: 10/1/13
 * Time: 12:22 AM
 */
public class WorksheetNoteDAO extends AbstractNoteDAO {

    /**
     * Deletes a worksheet and all its notes.
     *
     * @param worksheet  The worksheet to delete.
     * @param documentId The document ID of the document that the worksheet belongs to.
     */
    public void deleteWorksheet(Worksheet worksheet, Long documentId) {
        Workset cachedWorkset = (Workset) Cache.get().getDocumentCache().getDocumentMap().get(documentId);
        Worksheet cachedWorksheet = cachedWorkset.getWorksheetsMap().get(worksheet.getWorksheetId());
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();

        // Remove all notes in the worksheet.
        for (Long noteId : cachedWorksheet.getNotesList()) {
            noteMap.remove(noteId);
        }

        // Remove the worksheet.
        cachedWorkset.getWorksheetsMap().remove(worksheet.getWorksheetId());

        // Update workset's last updated time.
        cachedWorkset.setLastUpdatedTime(new Date());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocument(Document document) {
        Workset cachedWorkset = (Workset) Cache.get().getDocumentCache().getDocumentMap()
                .get(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
        Map<Long, Worksheet> worksheetsMap = cachedWorkset.getWorksheetsMap();
        for (Worksheet worksheet : worksheetsMap.values()) {
            for (Long noteId : worksheet.getNotesList()) {
                noteMap.remove(noteId);
            }
        }

        // Remove the document in the document cache.
        Cache.get().getDocumentCache().getDocumentMap().remove(cachedWorkset.getDocumentId());
        Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .remove(cachedWorkset.getDocumentTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Workset workset = (Workset) Cache.get().getDocumentCache().getDocumentMap()
                .get(note.getDocumentId());
        Worksheet worksheet = workset.getWorksheetsMap().get(((WorksheetNote) note).getWorksheetId());
        worksheet.getNotesList().remove(noteId);

        // Remove the note in the note cache.
        Cache.get().getNoteCache().getNoteMap().remove(noteId);

        // Update workset's last updated time.
        workset.setLastUpdatedTime(new Date());
    }

    /**
     * Finds all notes in the document, grouped by worksheet IDs.
     *
     * @param documentId The document ID.
     * @return {@code Map} All notes in the document grouped by worksheet IDs.
     */
    public Map<Long, List<WorksheetNote>> findAllNotesByWorksheets(Long documentId) {
        Workset workset = (Workset) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
        Map<Long, List<WorksheetNote>> noteMapByWorksheets = new HashMap<Long, List<WorksheetNote>>();

        for (Worksheet worksheet : workset.getWorksheetsMap().values()) {
            List<WorksheetNote> notesList = new ArrayList<WorksheetNote>();
            for (Long noteId : worksheet.getNotesList()) {
                notesList.add((WorksheetNote) (noteMap.get(noteId)));
            }
            Collections.sort(notesList);
            noteMapByWorksheets.put(worksheet.getWorksheetId(), notesList);
        }
        return noteMapByWorksheets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        Workset workset = (Workset) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
        List<Note> noteList = new ArrayList<Note>();

        for (Worksheet worksheet : workset.getWorksheetsMap().values()) {
            for (Long noteId : worksheet.getNotesList()) {
                noteList.add(noteMap.get(noteId));
            }
        }
        Collections.sort(noteList);
        return noteList;
    }

    /**
     * Updates the worksheetIds list of a workset.
     *
     * @param documentId       The document ID of the workset.
     * @param worksheetIdsList The updated list of worksheetIds.
     * @return {@code Workset} The updated workset object.
     */
    public Workset updateWorksheetsOrder(Long documentId, List<Long> worksheetIdsList) {
        Workset cachedWorkset = (Workset) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
        cachedWorkset.setWorksheetIdsList(worksheetIdsList);

        return cachedWorkset;
    }

    /**
     * Merges a worksheet.
     *
     * @param worksheet  The worksheet to merge.
     * @param documentId The document ID of the workset that the worksheet belongs to.
     * @return {@code Worksheet} The merged worksheet object.
     */
    public Worksheet mergeWorksheet(Worksheet worksheet, Long documentId, Long oldWorksheetId) {
        try {
            Workset cachedWorkset = (Workset) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
            Map<Long, Worksheet> worksheetsMap = cachedWorkset.getWorksheetsMap();
            Worksheet cachedWorksheet = cachedWorkset.getWorksheetsMap().get(oldWorksheetId);
            Long updatedWorksheetId = worksheet.getWorksheetId();

            System.out.println("old id: " + oldWorksheetId);
            System.out.println("new id: " + updatedWorksheetId);

            if (worksheetsMap.containsKey(updatedWorksheetId)) {
                throw new InvalidKeyException("The updated worksheet id is already taken by another worksheet.");
            }

            cachedWorksheet.setWorksheetTitle(worksheet.getWorksheetTitle());
            cachedWorksheet.setNotesList(worksheet.getNotesList());

            if (!oldWorksheetId.equals(updatedWorksheetId)) {
                cachedWorksheet.setWorksheetId(updatedWorksheetId);
                worksheetsMap.remove(oldWorksheetId);
                worksheetsMap.put(updatedWorksheetId, cachedWorksheet);
            }

            // Update workset's last updated time.
            cachedWorkset.setLastUpdatedTime(new Date());

            return cachedWorksheet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document mergeDocument(Document document) {
        Workset updateWorkset = (Workset) (Cache.get().getDocumentCache().getDocumentMap().get(document
                .getDocumentId()));
        if (updateWorkset != null) {
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
                    .remove(updateWorkset.getDocumentTitle());
            updateWorkset.setDocumentTitle(document.getDocumentTitle());
            updateWorkset.setAuthorsList(document.getAuthorsList());
            updateWorkset.setComment(document.getComment());
            updateWorkset.setWorksheetsMap(((Workset) document).getWorksheetsMap());
            updateWorkset.setWorksheetIdsList(((Workset) document).getWorksheetIdsList());
            if (document.getLastUpdatedTime() == null) {
                updateWorkset.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                updateWorkset.setLastUpdatedTime(document.getLastUpdatedTime());
            }
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
                    .put(updateWorkset.getDocumentTitle(), updateWorkset.getDocumentId());
            return updateWorkset;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note mergeNote(Note note) {
        Workset workset = (Workset) (Cache.get().getDocumentCache().getDocumentMap().get(note
                .getDocumentId()));
        WorksheetNote cachedNote = (WorksheetNote) (Cache.get().getNoteCache().getNoteMap().get(note
                .getNoteId()));
        if (cachedNote != null) {
            cachedNote.setDocumentId(note.getDocumentId());
            Long oldWorksheetId = cachedNote.getWorksheetId();
            cachedNote.setWorksheetId(((WorksheetNote) note).getWorksheetId());
            cachedNote.setTagIds(note.getTagIds());
            cachedNote.setNoteText(note.getNoteText());
            cachedNote.setNoteStatus(((WorksheetNote) note).getNoteStatus());

            // Update worksheets' notes list.
            if (!oldWorksheetId.equals(((WorksheetNote) note).getWorksheetId())) {
                Worksheet oldWorksheet = workset.getWorksheetsMap().get(oldWorksheetId);
                oldWorksheet.getNotesList().remove(note.getNoteId());
                Worksheet newWorksheet = workset.getWorksheetsMap().get(cachedNote.getWorksheetId());
                newWorksheet.getNotesList().add(note.getNoteId());
            }

            // Update workset's last updated time.
            workset.setLastUpdatedTime(new Date());

            return cachedNote;
        }
        return null;
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
            Workset cachedWorkset = (Workset) Cache.get().getDocumentCache().getDocumentMap()
                    .get(documentId);
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

            // Update workset's last updated time.
            cachedWorkset.setLastUpdatedTime(new Date());

            return worksheet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document saveDocument(Document document) {
        if (document instanceof Workset) {
            Workset newWorkset = new Workset();
            if (document.getDocumentId() == null) {
                newWorkset.setDocumentId(Cache.get().getDocumentCache().getMaxDocumentId() + 1L);
            } else {
                newWorkset.setDocumentId(document.getDocumentId());
            }
            newWorkset.setDocumentTitle(document.getDocumentTitle());
            newWorkset.setAuthorsList(document.getAuthorsList());
            newWorkset.setComment(document.getComment());
            newWorkset.setWorksheetIdsList(((Workset) document).getWorksheetIdsList());
            newWorkset.setWorksheetsMap(((Workset) document).getWorksheetsMap());
            if (document.getCreatedTime() == null) {
                newWorkset.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newWorkset.setCreatedTime(document.getCreatedTime());
            }
            if (document.getLastUpdatedTime() == null) {
                newWorkset.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                newWorkset.setLastUpdatedTime(document.getLastUpdatedTime());
            }

            // Add the document to document cache.
            try {
                if (Cache.get().getDocumentCache().getDocumentMap()
                        .containsKey(newWorkset.getDocumentId())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document ID!");
                }
                if (Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .containsKey(newWorkset.getDocumentTitle())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document title!");
                }

                Cache.get().getDocumentCache().getDocumentMap()
                        .put(newWorkset.getDocumentId(), newWorkset);
                Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .put(newWorkset.getDocumentTitle(), newWorkset.getDocumentId());

                // Update the max document ID in document cache.
                if (Cache.get().getDocumentCache().getMaxDocumentId() < newWorkset.getDocumentId()) {
                    Cache.get().getDocumentCache().setMaxDocumentId(newWorkset.getDocumentId());
                }

                return newWorkset;
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note saveNote(Note note) {
        if (note instanceof WorksheetNote) {
            WorksheetNote newNote = new WorksheetNote();
            if (note.getNoteId() == null) {
                newNote.setNoteId(Cache.get().getNoteCache().getMaxNoteId() + 1L);
            } else {
                newNote.setNoteId(note.getNoteId());
            }
            newNote.setDocumentId(note.getDocumentId());
            newNote.setWorksheetId(((WorksheetNote) note).getWorksheetId());
            newNote.setTagIds(note.getTagIds());
            newNote.setNoteText(note.getNoteText());
            newNote.setNoteStatus(((WorksheetNote) note).getNoteStatus());
            if (note.getCreatedTime() == null) {
                newNote.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newNote.setCreatedTime(note.getCreatedTime());
            }

            // Add the note to note cache.
            try {
                if (Cache.get().getNoteCache().getNoteMap().containsKey(newNote.getNoteId())) {
                    throw new DuplicateRecordException("Duplicate note exception: same note ID!");
                }
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
            Cache.get().getNoteCache().getNoteMap().put(newNote.getNoteId(), newNote);

            // Update the max note ID in note cache.
            if (Cache.get().getNoteCache().getMaxNoteId() < newNote.getNoteId()) {
                Cache.get().getNoteCache().setMaxNoteId(newNote.getNoteId());
            }

            // Add the note ID to corresponding notes list.
            Workset workset = (Workset) (Cache.get().getDocumentCache().getDocumentMap().get(newNote
                    .getDocumentId()));
            Worksheet worksheet = workset.getWorksheetsMap().get(newNote.getWorksheetId());
            worksheet.getNotesList().add(newNote.getNoteId());

            // Update workset's last updated time.
            workset.setLastUpdatedTime(new Date());

            return newNote;
        }
        return null;
    }
}

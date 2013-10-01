package notes.dao.impl;

import notes.dao.DuplicateRecordException;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.workset.WorkSet;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        WorkSet cachedWorkSet = (WorkSet) Cache.get().getDocumentCache().getDocumentMap().get(documentId);
        Worksheet cachedWorksheet = cachedWorkSet.getWorksheetsMap().get(worksheet.getWorksheetId());
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();

        // Remove all notes in the worksheet.
        for (Long noteId : cachedWorksheet.getNotesList()) {
            noteMap.remove(noteId);
        }

        // Remove the worksheet.
        cachedWorkSet.getWorksheetsMap().remove(worksheet.getWorksheetId());

        // Update workset's last updated time.
        cachedWorkSet.setLastUpdatedTime(new Date());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocument(Document document) {
        WorkSet cachedWorkSet = (WorkSet) Cache.get().getDocumentCache().getDocumentMap()
                .get(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
        Map<Long, Worksheet> worksheetsMap = cachedWorkSet.getWorksheetsMap();
        for (Worksheet worksheet : worksheetsMap.values()) {
            for (Long noteId : worksheet.getNotesList()) {
                noteMap.remove(noteId);
            }
        }

        // Remove the document in the document cache.
        Cache.get().getDocumentCache().getDocumentMap().remove(cachedWorkSet.getDocumentId());
        Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .remove(cachedWorkSet.getDocumentTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        WorkSet workset = (WorkSet) Cache.get().getDocumentCache().getDocumentMap()
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
        WorkSet workset = (WorkSet) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
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
        WorkSet workset = (WorkSet) (Cache.get().getDocumentCache().getDocumentMap().get(documentId));
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
     * Merges a worksheet.
     *
     * @param worksheet  The worksheet to merge.
     * @param documentId The document ID of the workset that the worksheet belongs to.
     * @return {@code Worksheet} The merged worksheet object.
     */
    public Worksheet mergeWorksheet(Worksheet worksheet, Long documentId) {
        try {
            WorkSet updateWorkSet = (WorkSet) (Cache.get().getDocumentCache().getDocumentMap()
                    .get(documentId));
            Worksheet updateWorksheet = updateWorkSet.getWorksheetsMap().get(worksheet.getWorksheetId());
            updateWorksheet.setWorksheetTitle(worksheet.getWorksheetTitle());
            updateWorksheet.setNotesList(worksheet.getNotesList());

            // Update workset's last updated time.
            updateWorkSet.setLastUpdatedTime(new Date());

            return updateWorksheet;
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
        WorkSet updateWorkSet = (WorkSet) (Cache.get().getDocumentCache().getDocumentMap().get(document
                .getDocumentId()));
        if (updateWorkSet != null) {
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
                    .remove(updateWorkSet.getDocumentTitle());
            updateWorkSet.setDocumentTitle(document.getDocumentTitle());
            updateWorkSet.setAuthorsList(document.getAuthorsList());
            updateWorkSet.setComment(document.getComment());
            updateWorkSet.setWorksheetsMap(((WorkSet) document).getWorksheetsMap());
            if (document.getLastUpdatedTime() == null) {
                updateWorkSet.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                updateWorkSet.setLastUpdatedTime(document.getLastUpdatedTime());
            }
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
                    .put(updateWorkSet.getDocumentTitle(), updateWorkSet.getDocumentId());
            return updateWorkSet;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note mergeNote(Note note) {
        WorkSet workset = (WorkSet) (Cache.get().getDocumentCache().getDocumentMap().get(note
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
            WorkSet cachedWorkSet = (WorkSet) Cache.get().getDocumentCache().getDocumentMap()
                    .get(documentId);
            TreeMap<Long, Worksheet> worksheetMap = cachedWorkSet.getWorksheetsMap();
            if (worksheetMap.containsKey(worksheet.getWorksheetId())) {
                throw new DuplicateRecordException("Duplicate worksheet ID when saving a new worksheet!");
            }
            if (worksheet.getNotesList() == null) {
                worksheet.setNotesList(new ArrayList<Long>());
            }
            worksheetMap.put(worksheet.getWorksheetId(), worksheet);

            // Update workset's last updated time.
            cachedWorkSet.setLastUpdatedTime(new Date());

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
        if (document instanceof WorkSet) {
            WorkSet newWorkSet = new WorkSet();
            if (document.getDocumentId() == null) {
                newWorkSet.setDocumentId(Cache.get().getDocumentCache().getMaxDocumentId() + 1L);
            } else {
                newWorkSet.setDocumentId(document.getDocumentId());
            }
            newWorkSet.setDocumentTitle(document.getDocumentTitle());
            newWorkSet.setAuthorsList(document.getAuthorsList());
            newWorkSet.setComment(document.getComment());
            newWorkSet.setWorksheetsMap(((WorkSet) document).getWorksheetsMap());
            if (document.getCreatedTime() == null) {
                newWorkSet.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newWorkSet.setCreatedTime(document.getCreatedTime());
            }
            if (document.getLastUpdatedTime() == null) {
                newWorkSet.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                newWorkSet.setLastUpdatedTime(document.getLastUpdatedTime());
            }

            // Add the document to document cache.
            try {
                if (Cache.get().getDocumentCache().getDocumentMap()
                        .containsKey(newWorkSet.getDocumentId())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document ID!");
                }
                if (Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .containsKey(newWorkSet.getDocumentTitle())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document title!");
                }

                Cache.get().getDocumentCache().getDocumentMap()
                        .put(newWorkSet.getDocumentId(), newWorkSet);
                Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .put(newWorkSet.getDocumentTitle(), newWorkSet.getDocumentId());

                // Update the max document ID in document cache.
                if (Cache.get().getDocumentCache().getMaxDocumentId() < newWorkSet.getDocumentId()) {
                    Cache.get().getDocumentCache().setMaxDocumentId(newWorkSet.getDocumentId());
                }

                return newWorkSet;
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
            WorkSet workset = (WorkSet) (Cache.get().getDocumentCache().getDocumentMap().get(newNote
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

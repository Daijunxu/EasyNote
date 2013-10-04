package notes.bean;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.WorksheetNoteDAO;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 * <p/>
 * User: rui
 * Date: 10/1/13
 * Time: 12:59 AM
 */
public class WorksetHome implements Serializable {

    /**
     * The single instance of WorksetHome.
     */
    private static final WorksetHome instance = new WorksetHome();
    /**
     * The workset note DAO.
     */
    @Getter
    private WorksheetNoteDAO worksheetNoteDAO;
    /**
     * The list of documents.
     */
    @Getter
    @Setter
    private List<Document> documentList;
    /**
     * The current selected workset.
     */
    @Getter
    @Setter
    private Workset currentWorkset;
    /**
     * The current selected worksheet.
     */
    @Getter
    @Setter
    private Worksheet currentWorksheet;
    /**
     * The current selected workset note.
     */
    @Getter
    @Setter
    private WorksheetNote currentWorksheetNote;

    /**
     * Constructs an instance of {@code WorksetHome}.
     */
    private WorksetHome() {
        worksheetNoteDAO = new WorksheetNoteDAO();
        documentList = worksheetNoteDAO.findAllDocuments();
    }

    /**
     * Gets the instance of {@code WorksetHome}.
     *
     * @return {@code WorksetHome} The instance of {@code WorksetHome}.
     */
    public static WorksetHome get() {
        return instance;
    }

    /**
     * Clears all temporary data stored in WorksetHome.
     */
    public void clearAllTemporaryData() {
        documentList.clear();
        currentWorkset = null;
        currentWorksheet = null;
        currentWorksheetNote = null;
    }

    /**
     * Clears the corresponding temporary data when current worksheet is changed.
     */
    public void clearTemporaryDataWhenWorksheetChanged() {
        currentWorksheet = null;
        currentWorksheetNote = null;
    }

    /**
     * Updates the data members in WorksetHome, which are acquired from Cache.
     *
     * @param documentId  The current selected document ID.
     * @param worksheetId The current selected worksheet ID.
     * @param noteId      The current selected note ID.
     */
    public void updateTemporaryData(Long documentId, Long worksheetId, Long noteId) {
        // Clear temporary data.
        clearAllTemporaryData();

        // Update documentList.
        documentList = worksheetNoteDAO.findAllDocuments();

        if (documentId != null) {
            // Update currentWorkset.
            currentWorkset = ((Workset) worksheetNoteDAO.findDocumentById(documentId));
        }

        if (worksheetId != null) {
            // Update currentWorksheet.
            currentWorksheet = currentWorkset.getWorksheetsMap().get(worksheetId);
        }

        if (noteId != null) {
            // Update currentWorksheetNote.
            currentWorksheetNote = (WorksheetNote) (worksheetNoteDAO.findNoteById(noteId));
        }
    }

    /**
     * Gets the list of notes for the current worksheet.
     *
     * @return {@code List} The list of notes for the current worksheet.
     */
    public List<WorksheetNote> getAllNotesForCurrentWorksheet() {
        Cache cache = Cache.get();
        List<WorksheetNote> worksheetNoteList = new ArrayList<WorksheetNote>();
        for (Long worksheetNoteId : currentWorksheet.getNotesList()) {
            worksheetNoteList.add((WorksheetNote) cache.getNoteCache().getNoteMap().get(worksheetNoteId));
        }
        return worksheetNoteList;
    }
}

package notes.bean;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.WorksheetNoteDAO;
import notes.entity.Document;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorkSet;
import notes.entity.workset.WorksheetNote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 * <p/>
 * User: rui
 * Date: 10/1/13
 * Time: 12:59 AM
 */
public class WorkSetHome implements Serializable {

    /**
     * The single instance of WorkSetHome.
     */
    private static final WorkSetHome instance = new WorkSetHome();
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
    private WorkSet currentWorkSet;
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
     * The list of worksheets in current selected workset.
     */
    @Getter
    @Setter
    private List<Worksheet> currentWorksheetList;
    /**
     * The map from worksheet IDs to notes in current selected workset.
     */
    @Getter
    @Setter
    private Map<Long, List<WorksheetNote>> currentWorksheetNotesMap;
    /**
     * The list of workset notes in current selected worksheet.
     */
    @Getter
    @Setter
    private List<WorksheetNote> currentWorksheetNotesList;

    /**
     * Constructs an instance of {@code WorkSetHome}.
     */
    private WorkSetHome() {
        worksheetNoteDAO = new WorksheetNoteDAO();
        documentList = worksheetNoteDAO.findAllDocuments();
        currentWorksheetList = new ArrayList<Worksheet>();
        currentWorksheetNotesMap = new HashMap<Long, List<WorksheetNote>>();
        currentWorksheetNotesList = new ArrayList<WorksheetNote>();
    }

    /**
     * Gets the instance of {@code WorkSetHome}.
     *
     * @return {@code WorkSetHome} The instance of {@code WorkSetHome}.
     */
    public static WorkSetHome get() {
        return instance;
    }

    /**
     * Clears all temporary data stored in WorkSetHome.
     */
    public void clearAllTemporaryData() {
        documentList.clear();
        currentWorkSet = null;
        currentWorksheet = null;
        currentWorksheetNote = null;
        currentWorksheetList.clear();
        currentWorksheetNotesMap.clear();
        currentWorksheetNotesList.clear();
    }

    /**
     * Clears the corresponding temporary data when current worksheet is changed.
     */
    public void clearTemporaryDataWhenWorksheetChanged() {
        currentWorksheet = null;
        currentWorksheetNote = null;
    }

    /**
     * Updates the data members in WorkSetHome, which are acquired from Cache.
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
            // Update currentWorkSet.
            currentWorkSet = ((WorkSet) worksheetNoteDAO.findDocumentById(documentId));

            // Update worksheetsData.
            for (Map.Entry<Long, Worksheet> entry : currentWorkSet.getWorksheetsMap().entrySet()) {
                currentWorksheetList.add(entry.getValue());
            }

            // Update notesMap.
            currentWorksheetNotesMap = WorkSetHome.get().getWorksheetNoteDAO()
                    .findAllNotesByWorksheets(documentId);
        }

        if (worksheetId != null) {
            // Update currentWorksheet.
            currentWorksheet = currentWorkSet.getWorksheetsMap().get(worksheetId);

            // Update currentNotesList.
            currentWorksheetNotesList = currentWorksheetNotesMap.get(worksheetId);
        }

        if (noteId != null) {
            // Update currentWorksheetNote.
            currentWorksheetNote = (WorksheetNote) (worksheetNoteDAO.findNoteById(noteId));
        }

    }
}

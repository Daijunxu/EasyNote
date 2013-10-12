package notes.bean;

import lombok.Getter;
import lombok.Setter;
import notes.dao.impl.WorksheetNoteDAO;
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
    public List<WorksheetNote> getNotesListForCurrentWorksheet() {
        List<WorksheetNote> worksheetNotesList = new ArrayList<WorksheetNote>();
        for (Long worksheetNoteId : currentWorksheet.getNotesList()) {
            worksheetNotesList.add((WorksheetNote) worksheetNoteDAO.findNoteById(worksheetNoteId));
        }
        return worksheetNotesList;
    }

    /**
     * Gets the index of the given worksheet in the current workset.
     *
     * @param worksheetIdToFind The ID of the worksheet ID to find.
     * @return int The index of the worksheet.
     */
    public int getIndexForWorksheet(Long worksheetIdToFind) {
        int index = 0;
        for (Long worksheetId : currentWorkset.getWorksheetIdsList()) {
            if (!worksheetId.equals(worksheetIdToFind)) {
                index++;
            } else {
                break;
            }
        }
        return index;
    }
}

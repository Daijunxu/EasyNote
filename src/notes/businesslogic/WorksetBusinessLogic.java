package notes.businesslogic;

import lombok.Getter;
import lombok.Setter;
import notes.businessobjects.workset.Workset;
import notes.businessobjects.workset.Worksheet;
import notes.businessobjects.workset.WorksheetNote;
import notes.dao.impl.WorksheetNoteDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The object that stores temporary data for the front end and provides access to DAO component.
 * <p/>
 * Author: Rui Du
 * Date: 10/1/13
 * Time: 12:59 AM
 */
public class WorksetBusinessLogic extends AbstractDocumentBusinessLogic {

    /**
     * The single instance of WorksetBusinessLogic.
     */
    private static final WorksetBusinessLogic instance = new WorksetBusinessLogic();
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
    private WorksheetNote currentNote;

    /**
     * Constructs an instance of {@code WorksetBusinessLogic}.
     */
    private WorksetBusinessLogic() {
        worksheetNoteDAO = WorksheetNoteDAO.get();
    }

    /**
     * Gets the instance of {@code WorksetBusinessLogic}.
     *
     * @return {@code WorksetBusinessLogic} The instance of {@code WorksetBusinessLogic}.
     */
    public static WorksetBusinessLogic get() {
        return instance;
    }

    /**
     * Clears all temporary data stored in WorksetBusinessLogic.
     */
    public void clearAllTemporaryData() {
        currentWorkset = null;
        currentWorksheet = null;
        currentNote = null;
    }

    /**
     * Clears the corresponding temporary data when current worksheet is changed.
     */
    public void clearTemporaryDataWhenWorksheetChanged() {
        currentWorksheet = null;
        currentNote = null;
    }

    /**
     * Updates the data members in WorksetBusinessLogic, which are acquired from CacheDelegate.
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
            // Update currentNote.
            currentNote = (WorksheetNote) (worksheetNoteDAO.findNoteById(noteId));
        }
    }

    /**
     * Gets the list of notes for the current worksheet.
     *
     * @return {@code List} The list of notes for the current worksheet.
     */
    public List<WorksheetNote> getNotesListForCurrentWorksheet() {
        List<WorksheetNote> worksheetNotesList = new ArrayList<WorksheetNote>();

        // To keep notes in the order of the current worksheet.
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

    @Override
    public int getIndexForNote(Long noteIdToFind) {
        return currentWorksheet.getNotesList().indexOf(noteIdToFind);
    }

    public boolean canMoveCurrentWorksheetUp() {
        Long worksheetId = currentWorksheet.getWorksheetId();
        int index = getIndexForWorksheet(worksheetId);
        return (index > 0);
    }

    public boolean canMoveCurrentWorksheetDown() {
        Long currentWorksheetId = currentWorksheet.getWorksheetId();
        int index = getIndexForWorksheet(currentWorksheetId);
        return (index < getNumberOfWorksheets() - 1);
    }

    public void moveCurrentWorksheetUp() {
        Long currentWorksheetId = currentWorksheet.getWorksheetId();
        int index = getIndexForWorksheet(currentWorksheetId);
        Collections.swap(currentWorkset.getWorksheetIdsList(), index, index - 1);
    }

    public void moveCurrentWorksheetDown() {
        Long currentWorksheetId = currentWorksheet.getWorksheetId();
        int index = getIndexForWorksheet(currentWorksheetId);
        Collections.swap(currentWorkset.getWorksheetIdsList(), index, index + 1);
    }

    public void moveCurrentWorksheetToTop() {
        Long currentWorksheetId = currentWorksheet.getWorksheetId();
        int index = getIndexForWorksheet(currentWorksheetId);
        List<Long> worksheetIdsList = currentWorkset.getWorksheetIdsList();

        for (int i = index; i > 0; i--) {
            Collections.swap(worksheetIdsList, i, i - 1);
        }
    }

    public void moveCurrentWorksheetToBottom() {
        Long currentWorksheetId = currentWorksheet.getWorksheetId();
        int index = getIndexForWorksheet(currentWorksheetId);
        List<Long> worksheetIdsList = currentWorkset.getWorksheetIdsList();

        for (int i = index; i < worksheetIdsList.size() - 1; i++) {
            Collections.swap(worksheetIdsList, i, i + 1);
        }
    }

    private int getNumberOfWorksheets() {
        return currentWorkset.getWorksheetIdsList().size();
    }

    @Override
    protected List<Long> getCurrentNoteList() {
        return currentWorksheet.getNotesList();
    }

    public Worksheet getWorksheetByIndex(int index) {
        Long worksheetId = currentWorkset.getWorksheetIdsList().get(index);
        return currentWorkset.getWorksheetsMap().get(worksheetId);
    }
}

package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.workset.Workset;
import notes.businessobjects.workset.Worksheet;
import notes.businessobjects.workset.WorksheetStatus;
import notes.dao.impl.WorksheetNoteDAO;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the event when the status of a worksheet is changed.
 * <p/>
 * Author: Rui Du
 * Date: 4/24/14
 * Time: 1:20 AM
 */
public class WorksheetStatusChangedActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        WorksetBusinessLogic logic = WorksetBusinessLogic.get();
        Workset workset = logic.getCurrentWorkset();
        Worksheet worksheet = logic.getCurrentWorksheet();

        if (WorksheetStatus.ACTIVE.equals(worksheet.getStatus())) {
            worksheet.setStatus(WorksheetStatus.COMPLETED);
        } else if (WorksheetStatus.COMPLETED.equals(worksheet.getStatus())) {
            worksheet.setStatus(WorksheetStatus.ACTIVE);
        }

        // Save the updated worksheet.
        WorksheetNoteDAO.get().updateWorksheet(worksheet, workset.getDocumentId(), worksheet.getWorksheetId());

        MainPanel.get().updateWorksetPanel(workset.getDocumentId(), logic.getCurrentWorksheet().getWorksheetId(), null);
        SoundFactory.playUpdate();
    }
}

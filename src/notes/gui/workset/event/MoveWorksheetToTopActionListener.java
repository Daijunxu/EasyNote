package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener for moving worksheet to the top in the relative locations of all worksheets in the current workset.
 * <p/>
 * Author: Rui Du
 * Date: 4/14/14
 * Time: 11:51 PM
 */
public class MoveWorksheetToTopActionListener implements ActionListener {

    private static final WorksetBusinessLogic logic = WorksetBusinessLogic.get();

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (logic.getCurrentWorkset() == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                WorksetBusinessLogic.get().moveCurrentWorksheetToTop();
                MainPanel.get().updateWorksetPanel(logic.getCurrentWorkset().getDocumentId(),
                        logic.getCurrentWorksheet().getWorksheetId(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

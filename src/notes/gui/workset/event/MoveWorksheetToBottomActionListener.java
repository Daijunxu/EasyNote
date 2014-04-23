package notes.gui.workset.event;

import notes.businesslogic.WorksetBusinessLogic;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener for moving worksheet to the bottom in the relative locations of all worksheets in the current workset.
 * <p/>
 * Author: Rui Du
 * Date: 4/14/14
 * Time: 11:51 PM
 */
public class MoveWorksheetToBottomActionListener implements ActionListener {

    private static final WorksetBusinessLogic logic = WorksetBusinessLogic.get();

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        logic.moveCurrentWorksheetToBottom();
        MainPanel.get().updateWorksetPanel(logic.getCurrentWorkset().getDocumentId(),
                logic.getCurrentWorksheet().getWorksheetId(), null);
        SoundFactory.playNavigation();
    }
}

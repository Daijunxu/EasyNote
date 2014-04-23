package notes.gui.main.event.document;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businesslogic.BookBusinessLogic;
import notes.businesslogic.WorksetBusinessLogic;
import notes.data.persistence.Property;
import notes.businessobjects.SystemMode;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for closing the opened document.
 * <p/>
 * Author: Rui Du
 * Date: 10/12/13
 * Time: 3:15 AM
 */
public class CloseDocumentEventListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        SoundFactory.playUpdate();
        MainPanel mainPanel = MainPanel.get();

        // Clear temporary data in logic objects.
        SystemMode currentMode = mainPanel.getCurrentMode();
        if (currentMode.equals(SystemMode.WORKSET)) {
            WorksetBusinessLogic.get().clearAllTemporaryData();
        } else if (currentMode.equals(SystemMode.BOOK)) {
            BookBusinessLogic.get().clearAllTemporaryData();
        } else if (currentMode.equals(SystemMode.ARTICLE)) {
            ArticleBusinessLogic.get().clearAllTemporaryData();
        }

        // Set the last opened document to null.
        Property.get().setLastOpenedDocumentId(null);

        // Show the default panel.
        mainPanel.setDefaultPanel();
    }
}

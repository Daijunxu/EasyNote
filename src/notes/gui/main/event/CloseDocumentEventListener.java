package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.entity.SystemMode;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for closing the opened document.
 * <p/>
 * User: rui
 * Date: 10/12/13
 * Time: 3:15 AM
 */
public class CloseDocumentEventListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
            SoundFactory.playUpdate();
        }
        MainPanel mainPanel = MainPanel.get();

        // Clear temporary data in home objects.
        SystemMode currentMode = mainPanel.getCurrentMode();
        if (currentMode.equals(SystemMode.WORKSET)) {
            WorksetHome.get().clearAllTemporaryData();
        } else if (currentMode.equals(SystemMode.BOOK)) {
            BookHome.get().clearAllTemporaryData();
        } else if (currentMode.equals(SystemMode.ARTICLE)) {
            ArticleHome.get().clearAllTemporaryData();
        }

        // Set the last opened document to null.
        Property.get().setLastOpenedDocumentId(null);

        // Show the default panel.
        mainPanel.setDefaultPanel();
    }
}

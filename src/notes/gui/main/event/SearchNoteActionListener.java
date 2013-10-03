package notes.gui.main.event;

import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.SearchNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for note searching.
 *
 * @author Rui Du
 */
public class SearchNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        SearchNoteDialog.get().setLocationRelativeTo(MainPanel.get());
        if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
            SoundFactory.playPopup();
        }
        MainPanel.get().setSearchMode(true);
        SearchNoteDialog.get().setVisible(true);
    }
}

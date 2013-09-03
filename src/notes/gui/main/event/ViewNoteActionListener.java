/**
 *
 */
package notes.gui.main.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import notes.data.cache.Property;
import notes.gui.main.component.SearchNoteDialog;
import notes.gui.main.component.ViewNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Defines event listener of viewing a note. Notes can belong to different types.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewNoteActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }
            new ViewNoteDialog(SearchNoteDialog.get().getSelectedResultNote());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package notes.gui.workset.event;

import notes.data.cache.Property;
import notes.gui.workset.component.NewWorksetDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new workset.
 *
 * @author Rui Du
 */
public class NewWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }
            new NewWorksetDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

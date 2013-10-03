package notes.gui.main.event;

import notes.data.cache.Property;
import notes.gui.main.component.AboutDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for showing system's information.
 *
 * @author Rui Du
 */
public class AboutActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
            SoundFactory.playPopup();
        }
        new AboutDialog();
    }
}

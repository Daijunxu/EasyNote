/**
 *
 */
package notes.gui.main.event;

import notes.data.cache.Cache;
import notes.data.cache.Property;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for saving all changes.
 *
 * @author Rui Du
 * @version 1.0
 */
public class SaveAllActoinListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
            SoundFactory.playUpdate();
        }
        Cache.get().saveAllCachesToXML();
        Property.get().saveProperty();
    }
}

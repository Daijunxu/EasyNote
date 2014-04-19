package notes.gui.main.event;

import notes.data.cache.CacheDelegate;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for saving all changes.
 *
 * Author: Rui Du
 */
public class SaveAllActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        SoundFactory.playUpdate();
        CacheDelegate.get().saveAllCachesToXML();
    }
}

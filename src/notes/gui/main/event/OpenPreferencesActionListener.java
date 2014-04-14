package notes.gui.main.event;

import notes.gui.main.component.MainPanel;
import notes.gui.main.component.PreferencesDialog;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for Preferences menu item.
 *
 * Author: Rui Du
 */
public class OpenPreferencesActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        PreferencesDialog.get().setLocationRelativeTo(MainPanel.get());
        SoundFactory.playPopup();
        PreferencesDialog.get().setVisible(true);
    }
}

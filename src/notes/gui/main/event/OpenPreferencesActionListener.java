/**
 * 
 */
package notes.gui.main.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.PreferencesDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * The event listener for Preferences menu item.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class OpenPreferencesActionListener implements ActionListener {

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		PreferencesDialog.get().setLocationRelativeTo(MainPanel.get());
		if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
			SoundFactory.playPopup();
		}
		PreferencesDialog.get().setVisible(true);
	}
}

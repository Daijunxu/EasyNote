/**
 * 
 */
package notes.gui.main.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import notes.data.cache.Property;
import notes.gui.main.component.OpenDocumentDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * The event listener for opening a document.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class OpenDocumentActionListener implements ActionListener {

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
			SoundFactory.playPopup();
		}
		new OpenDocumentDialog();
	}
}
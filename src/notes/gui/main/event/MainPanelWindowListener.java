/**
 * 
 */
package notes.gui.main.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import notes.data.cache.Cache;
import notes.data.cache.Property;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Window listener of the main panel.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class MainPanelWindowListener implements WindowListener {

	/**
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
			SoundFactory.playOff();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Cache.get().saveAllCaches();
		Property.get().saveProperty();
	}

	/**
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {
	}

}

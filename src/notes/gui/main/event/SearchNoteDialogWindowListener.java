package notes.gui.main.event;

import notes.gui.main.component.MainPanel;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Author: Rui Du
 * Date: 9/15/13
 * Time: 4:22 PM
 */
public class SearchNoteDialogWindowListener implements WindowListener {

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
        MainPanel.get().setSearchMode(false);
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

package notes.gui.main.event;

import notes.entity.SystemMode;
import notes.gui.main.component.MainPanel;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * User: rui
 * Date: 9/29/13
 * Time: 7:04 PM
 */
public class MainPanelComponentListener implements ComponentListener {
    /**
     * Invoked when the component's size changes.
     */
    public void componentResized(ComponentEvent e) {
        MainPanel mainPanel = MainPanel.get();
        SystemMode currentMode = mainPanel.getCurrentMode();

        if (currentMode == null) {
            return;
        }

        if (currentMode.equals(SystemMode.BOOK)) {
            mainPanel.updateBookNotePanel(null);
        } else if (currentMode.equals(SystemMode.ARTICLE)) {
            mainPanel.updateArticleNotePanel();
        }
    }

    /**
     * Invoked when the component's position changes.
     */
    public void componentMoved(ComponentEvent e) {
    }

    /**
     * Invoked when the component has been made visible.
     */
    public void componentShown(ComponentEvent e) {
    }

    /**
     * Invoked when the component has been made invisible.
     */
    public void componentHidden(ComponentEvent e) {
    }
}

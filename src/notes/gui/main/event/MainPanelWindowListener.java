package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.data.cache.Cache;
import notes.data.persistence.Property;
import notes.entity.SystemMode;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Window listener of the main panel.
 *
 * @author Rui Du
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
        SoundFactory.playOff();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Cache.get().saveAllCachesToXML();

        // Save the current opened document id.
        SystemMode currentMode = MainPanel.get().getCurrentMode();
        if (currentMode != null) {
            if (currentMode.equals(SystemMode.WORKSET)
                    && WorksetHome.get().getCurrentWorkset() != null) {
                Property.get().setLastOpenedDocumentId(WorksetHome.get().getCurrentWorkset().getDocumentId());
            } else if (currentMode.equals(SystemMode.ARTICLE)
                    && ArticleHome.get().getCurrentArticle() != null) {
                Property.get().setLastOpenedDocumentId(ArticleHome.get().getCurrentArticle().getDocumentId());
            } else if (currentMode.equals(SystemMode.BOOK)
                    && BookHome.get().getCurrentBook() != null) {
                Property.get().setLastOpenedDocumentId(BookHome.get().getCurrentBook().getDocumentId());
            }
        }

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

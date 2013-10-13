package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
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

        if (currentMode.equals(SystemMode.WORKSET)) {
            WorksetHome worksetHome = WorksetHome.get();
            if (worksetHome.getCurrentWorksheetNote() != null) {
                mainPanel.updateWorksheetNotePanel(worksetHome.getCurrentWorksheet(),
                        worksetHome.getCurrentWorksheetNote().getNoteId());
            } else {
                mainPanel.updateWorksheetNotePanel(worksetHome.getCurrentWorksheet(), null);
            }
        } else if (currentMode.equals(SystemMode.BOOK)) {
            BookHome bookHome = BookHome.get();
            if (bookHome.getCurrentBookNote() != null) {
                mainPanel.updateBookNotePanel(bookHome.getCurrentChapter(),
                        bookHome.getCurrentBookNote().getNoteId());
            } else {
                mainPanel.updateBookNotePanel(bookHome.getCurrentChapter(), null);
            }
        } else if (currentMode.equals(SystemMode.ARTICLE)) {
            ArticleHome articleHome = ArticleHome.get();
            if (articleHome.getCurrentArticleNote() != null) {
                mainPanel.updateArticleNotePanel(articleHome.getCurrentArticleNote().getNoteId());
            } else {
                mainPanel.updateArticleNotePanel(null);
            }
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

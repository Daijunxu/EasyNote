package notes.gui.main.event;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businesslogic.BookBusinessLogic;
import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.SystemMode;
import notes.gui.main.component.MainPanel;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Author: Rui Du
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
            WorksetBusinessLogic worksetBusinessLogic = WorksetBusinessLogic.get();
            if (worksetBusinessLogic.getCurrentWorksheetNote() != null) {
                mainPanel.updateWorksheetNotePanel(worksetBusinessLogic.getCurrentWorksheet(),
                        worksetBusinessLogic.getCurrentWorksheetNote().getNoteId());
            } else {
                mainPanel.updateWorksheetNotePanel(worksetBusinessLogic.getCurrentWorksheet(), null);
            }
        } else if (currentMode.equals(SystemMode.BOOK)) {
            BookBusinessLogic bookBusinessLogic = BookBusinessLogic.get();
            if (bookBusinessLogic.getCurrentBookNote() != null) {
                mainPanel.updateBookNotePanel(bookBusinessLogic.getCurrentChapter(),
                        bookBusinessLogic.getCurrentBookNote().getNoteId());
            } else {
                mainPanel.updateBookNotePanel(bookBusinessLogic.getCurrentChapter(), null);
            }
        } else if (currentMode.equals(SystemMode.ARTICLE)) {
            ArticleBusinessLogic articleBusinessLogic = ArticleBusinessLogic.get();
            if (articleBusinessLogic.getCurrentArticleNote() != null) {
                mainPanel.updateArticleNotePanel(articleBusinessLogic.getCurrentArticleNote().getNoteId());
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

package notes.gui.main.event.note;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businesslogic.BookBusinessLogic;
import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.SystemMode;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener to move the selected note down in the relative locations of all notes in the collection.
 * <p/>
 * Author: Rui Du
 * Date: 4/22/14
 * Time: 9:51 PM
 */
public class MoveNoteDownActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        SystemMode currentMode = MainPanel.get().getCurrentMode();

        if (currentMode.equals(SystemMode.WORKSET)) {
            WorksetBusinessLogic logic = WorksetBusinessLogic.get();
            logic.moveCurrentNoteDown();
            MainPanel.get().updateWorksheetNotePanel(logic.getCurrentWorksheet(), logic.getCurrentNote().getNoteId());
        } else if (currentMode.equals(SystemMode.BOOK)) {
            BookBusinessLogic logic = BookBusinessLogic.get();
            logic.moveCurrentNoteDown();
            MainPanel.get().updateBookNotePanel(logic.getCurrentChapter(), logic.getCurrentNote().getNoteId());
        } else if (currentMode.equals(SystemMode.ARTICLE)) {
            ArticleBusinessLogic logic = ArticleBusinessLogic.get();
            logic.moveCurrentNoteDown();
            MainPanel.get().updateArticleNotePanel(logic.getCurrentNote().getNoteId());
        }
        SoundFactory.playNavigation();
    }

}

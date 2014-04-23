package notes.gui.main.event.note;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businesslogic.BookBusinessLogic;
import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.SystemMode;
import notes.businessobjects.article.ArticleNote;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The event listener for book note list. Triggers when selected item in the list changes.
 *
 * Author: Rui Du
 */
public class NoteListSelectionListener implements ListSelectionListener {

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {
        // When the user release the mouse button and completes the selection,
        // getValueIsAdjusting() becomes false.
        if (!event.getValueIsAdjusting()) {
            JList list = (JList) event.getSource();
            int selected = list.getSelectedIndex();
            MainPanel frame = MainPanel.get();
            if (frame.getCurrentMode().equals(SystemMode.WORKSET)) {
                Long worksheetNoteId = WorksetBusinessLogic.get().getCurrentWorksheet().getNotesList().get(selected);
                WorksheetNote worksheetNote = (WorksheetNote) WorksetBusinessLogic.get().getWorksheetNoteDAO()
                        .findNoteById(worksheetNoteId);
                WorksetBusinessLogic.get().setCurrentNote(worksheetNote);
            } else if (frame.getCurrentMode().equals(SystemMode.ARTICLE)) {
                Long articleNoteId = ArticleBusinessLogic.get().getCurrentArticle().getNotesList().get(selected);
                ArticleNote articleNote = (ArticleNote) ArticleBusinessLogic.get().getArticleNoteDAO().
                        findNoteById(articleNoteId);
                ArticleBusinessLogic.get().setCurrentNote(articleNote);
            } else if (frame.getCurrentMode().equals(SystemMode.BOOK)) {
                Long bookNoteId = BookBusinessLogic.get().getCurrentChapter().getNotesList().get(selected);
                BookNote bookNote = (BookNote) BookBusinessLogic.get().getBookNoteDAO().findNoteById(bookNoteId);
                BookBusinessLogic.get().setCurrentNote(bookNote);
            }
            SoundFactory.playNavigation();
        }
    }

}

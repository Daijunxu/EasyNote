package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.entity.SystemMode;
import notes.entity.article.ArticleNote;
import notes.entity.book.BookNote;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The event listener for book note list. Triggers when selected item in the list changes.
 *
 * @author Rui Du
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
                Long worksheetNoteId = WorksetHome.get().getCurrentWorksheet().getNotesList().get(selected);
                WorksheetNote worksheetNote = (WorksheetNote) WorksetHome.get().getWorksheetNoteDAO()
                        .findNoteById(worksheetNoteId);
                WorksetHome.get().setCurrentWorksheetNote(worksheetNote);
            } else if (frame.getCurrentMode().equals(SystemMode.ARTICLE)) {
                Long articleNoteId = ArticleHome.get().getCurrentArticle().getNotesList().get(selected);
                ArticleNote articleNote = (ArticleNote) ArticleHome.get().getArticleNoteDAO().
                        findNoteById(articleNoteId);
                ArticleHome.get().setCurrentArticleNote(articleNote);
            } else if (frame.getCurrentMode().equals(SystemMode.BOOK)) {
                Long bookNoteId = BookHome.get().getCurrentChapter().getNotesList().get(selected);
                BookNote bookNote = (BookNote) BookHome.get().getBookNoteDAO().findNoteById(bookNoteId);
                BookHome.get().setCurrentBookNote(bookNote);
            }
            SoundFactory.playNavigation();
        }
    }

}

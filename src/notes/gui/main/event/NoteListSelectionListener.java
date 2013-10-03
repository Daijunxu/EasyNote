package notes.gui.main.event;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.entity.SystemMode;
import notes.entity.article.ArticleNote;
import notes.entity.book.BookNote;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

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
                WorksheetNote worksheetNote = WorksetHome.get().getCurrentWorksheetNotesList().get(selected);
                WorksetHome.get().setCurrentWorksheetNote(worksheetNote);
            } else if (frame.getCurrentMode().equals(SystemMode.ARTICLE)) {
                ArticleNote articleNote = ArticleHome.get().getCurrentArticleNotesList().get(selected);
                ArticleHome.get().setCurrentArticleNote(articleNote);
            } else if (frame.getCurrentMode().equals(SystemMode.BOOK)) {
                BookNote bookNote = BookHome.get().getCurrentBookNotesList().get(selected);
                BookHome.get().setCurrentBookNote(bookNote);
            }
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
        }
    }

}

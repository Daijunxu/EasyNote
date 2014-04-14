package notes.gui.book.event;

import notes.businesslogic.BookBusinessLogic;
import notes.businessobjects.book.Chapter;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The event listener for chapter list. Triggers when selected item in the list changes.
 *
 * Author: Rui Du
 */
public class ChapterListSelectionListener implements ListSelectionListener {

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {
        // When the user release the mouse button and completes the selection,
        // getValueIsAdjusting() becomes false.
        if (!event.getValueIsAdjusting()) {
            MainPanel frame = MainPanel.get();
            frame.remove(frame.getNotesPanel());

            // Clears the temporary data.
            BookBusinessLogic.get().clearTemporaryDataWhenChapterChanged();

            JList list = (JList) event.getSource();
            int selected = list.getSelectedIndex();

            // Get the chapter's note data.
            Chapter chapter = BookBusinessLogic.get().getChaptersListForCurrentBook().get(selected);
            frame.updateBookNotePanel(chapter, null);

            SoundFactory.playNavigation();
        }
    }
}

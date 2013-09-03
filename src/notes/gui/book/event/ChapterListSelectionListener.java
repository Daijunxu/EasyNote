/**
 *
 */
package notes.gui.book.event;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import notes.bean.BookHome;
import notes.book.Chapter;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * The event listener for chapter list. Triggers when selected item in the list changes.
 *
 * @author Rui Du
 * @version 1.0
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
            frame.remove(frame.getNotesPane());

            JList list = (JList) event.getSource();
            int selected = list.getSelectedIndex();

            // Get the chapter's note data.
            Chapter chapter = BookHome.get().getCurrentChapterList().get(selected);
            frame.updateBookNotePanel(chapter);

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
        }
    }
}

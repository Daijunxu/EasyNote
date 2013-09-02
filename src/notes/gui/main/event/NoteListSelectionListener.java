/**
 * 
 */
package notes.gui.main.event;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import notes.article.entity.ArticleNote;
import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.book.entity.BookNote;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * The event listener for book note list. Triggers when selected item in the list changes.
 * 
 * @author Rui Du
 * @version 1.0
 * 
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
			if (frame.getCurrentMode().equals("Article")) {
				ArticleNote articleNote = ArticleHome.get().getCurrentArticleNotesList()
						.get(selected);
				ArticleHome.get().setCurrentArticleNote(articleNote);
			} else if (frame.getCurrentMode().equals("Book")) {
				BookNote bookNote = BookHome.get().getCurrentBookNotesList().get(selected);
				BookHome.get().setCurrentBookNote(bookNote);
			}
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playNavigation();
			}
		}
	}

}

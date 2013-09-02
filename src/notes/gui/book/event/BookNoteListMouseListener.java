/**
 * 
 */
package notes.gui.book.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.SwingUtilities;

import notes.bean.BookHome;
import notes.data.cache.Property;
import notes.gui.book.component.BookNotePopupMenu;
import notes.gui.book.component.ViewBookNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Mouse event listener for notes' JList in book panel.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class BookNoteListMouseListener extends MouseAdapter {

	private void doPop(MouseEvent event) {
		BookNotePopupMenu menu = new BookNotePopupMenu();
		menu.show(event.getComponent(), event.getX(), event.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() == 2 && BookHome.get().getCurrentBook() != null
				&& BookHome.get().getCurrentChapter() != null
				&& BookHome.get().getCurrentBookNote() != null) {
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playPopup();
			}
			new ViewBookNoteDialog();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		if (SwingUtilities.isRightMouseButton(event)) {
			JList noteList = (JList) event.getSource();
			int row = noteList.locationToIndex(event.getPoint());
			noteList.setSelectedIndex(row);
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playNavigation();
			}
			doPop(event);
		}
	}

}
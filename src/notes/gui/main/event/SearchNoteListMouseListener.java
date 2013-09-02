/**
 * 
 */
package notes.gui.main.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.SwingUtilities;

import notes.data.cache.Property;
import notes.entity.Note;
import notes.gui.main.component.SearchNotePopupMenu;
import notes.gui.main.component.ViewNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Mouse event listener for notes' JList in search note panel.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class SearchNoteListMouseListener extends MouseAdapter {

	private void doPop(MouseEvent event, Note selectedNote) {
		SearchNotePopupMenu menu = new SearchNotePopupMenu(selectedNote);
		menu.show(event.getComponent(), event.getX(), event.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		JList noteList = (JList) event.getSource();
		Note selectedNote = (Note) noteList.getSelectedValue();
		if (event.getClickCount() == 2) {
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playPopup();
			}
			new ViewNoteDialog(selectedNote);
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
			Note selectedNote = (Note) noteList.getSelectedValue();
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playNavigation();
			}
			doPop(event, selectedNote);
		}
	}
}

/**
 * 
 */
package notes.gui.article.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.SwingUtilities;

import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.article.component.ArticleNotePopupMenu;
import notes.gui.article.component.ViewArticleNoteDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Mouse event listener for notes' JList in article panel.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class ArticleNoteListMouseListener extends MouseAdapter {

	private void doPop(MouseEvent event) {
		ArticleNotePopupMenu menu = new ArticleNotePopupMenu();
		menu.show(event.getComponent(), event.getX(), event.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() == 2 && ArticleHome.get().getCurrentArticle() != null
				&& ArticleHome.get().getCurrentArticleNote() != null) {
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playPopup();
			}
			new ViewArticleNoteDialog();
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
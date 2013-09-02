/**
 * 
 */
package notes.gui.article.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import notes.article.entity.ArticleNote;
import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Defines event listener of deleting an article note.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class DeleteArticleNoteActionListener implements ActionListener {

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		ArticleHome home = ArticleHome.get();
		MainPanel frame = MainPanel.get();
		try {
			ArticleNote articleNote = home.getCurrentArticleNote();
			if (articleNote == null) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playNotify();
			}
			int result = JOptionPane.showConfirmDialog(null, "Delete this note?", "Confirm Dialog",
					JOptionPane.YES_NO_OPTION);
			// 0 for yes and 1 for no.
			if (result == 0) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playDelete();
				}
				// Delete the selected article note.
				home.getArticleNoteDAO().deleteNote(articleNote);
				// Update temporary data in the ArticleHome.
				home.updateTemporaryData(home.getCurrentArticle().getDocumentId(), null);
				// Update the note panel.
				frame.updateArticleNotePanel();
			} else {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playNavigation();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

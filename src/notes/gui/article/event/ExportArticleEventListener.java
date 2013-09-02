/**
 * 
 */
package notes.gui.article.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.article.component.ExportArticleDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * The event listener for exporting current article.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class ExportArticleEventListener implements ActionListener {

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (ArticleHome.get().getCurrentArticle() != null) {
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playPopup();
			}
			new ExportArticleDialog();
		}
	}
}

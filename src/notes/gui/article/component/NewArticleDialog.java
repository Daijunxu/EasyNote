/**
 * 
 */
package notes.gui.article.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import notes.article.dao.ArticleNoteDAO;
import notes.article.entity.Article;
import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.AuthorsStrListBuilder;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Defines the dialog and event listener of creating a new article.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class NewArticleDialog extends JDialog {
	private static final long serialVersionUID = 6759636401637649597L;

	private JButton okButton = new JButton(new AbstractAction("OK") {
		private static final long serialVersionUID = 5215148273923029678L;

		public void actionPerformed(ActionEvent e) {

			// Input validation.
			if (documentTitleField.getText() == null
					|| documentTitleField.getText().trim().equals("")) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "Document title cannot be empty!",
						"Input error", JOptionPane.ERROR_MESSAGE);
				documentTitleField.requestFocus();
				return;
			} else if (documentTitleField.getText().trim().split("\n").length > 1) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "Document title can only have one line!",
						"Input error", JOptionPane.ERROR_MESSAGE);
				documentTitleField.requestFocus();
				return;
			} else if (authorField.getText() != null
					&& authorField.getText().trim().equals("") == false
					&& authorField.getText().trim().split("\n").length > 1) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "Author list can only have one line!",
						"Input error", JOptionPane.ERROR_MESSAGE);
				authorField.requestFocus();
				return;
			} else if (commentField.getText().trim().split("\n").length > 1) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "Comment can only have one line!",
						"Input error", JOptionPane.ERROR_MESSAGE);
				commentField.requestFocus();
				return;
			} else if (sourceField.getText().trim().split("\n").length > 1) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "Source can only have one line!",
						"Input error", JOptionPane.ERROR_MESSAGE);
				sourceField.requestFocus();
				return;
			}

			MainPanel frame = MainPanel.get();
			ArticleHome home = ArticleHome.get();
			ArticleNoteDAO dao = home.getArticleNoteDAO();

			// Create instance of the new article.
			Article newArticle = new Article();
			newArticle.setDocumentTitle(WordUtils.capitalize(documentTitleField.getText().trim()));
			newArticle.setAuthorsList(AuthorsStrListBuilder.buildAuthorsStrList(authorField
					.getText()));
			if (commentField.getText() != null && commentField.getText().trim().equals("") == false) {
				newArticle.setComment(commentField.getText().trim());
			}
			if (sourceField.getText() != null && sourceField.getText().trim().equals("") == false) {
				newArticle.setSource(sourceField.getText().trim());
			}
			newArticle.setCreatedTime(new Date(System.currentTimeMillis()));
			newArticle.setLastUpdatedTime(new Date(System.currentTimeMillis()));
			newArticle.setNotesList(new ArrayList<Long>());

			// Save the updated article.
			Article savedArticle = (Article) dao.saveDocument(newArticle);

			if (savedArticle == null) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "Document already exists!", "Input error",
						JOptionPane.ERROR_MESSAGE);
				documentTitleField.requestFocus();
			} else {
				// Update temporary data in the ArticleHome.
				home.updateTemporaryData(savedArticle.getDocumentId(), null);

				// Update the note panel.
				frame.setArticlePanel(home.getCurrentArticle());

				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playUpdate();
				}

				setVisible(false);
			}
		}
	});

	private JButton cancelButton = new JButton(new AbstractAction("Cancel") {
		private static final long serialVersionUID = -8851788447035738145L;

		public void actionPerformed(ActionEvent e) {
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playNavigation();
			}
			setVisible(false);
		}
	});

	private JTextArea documentTitleField = new JTextArea(2, 50);
	private JTextArea authorField = new JTextArea(2, 50);
	private JTextArea commentField = new JTextArea(10, 50);
	private JTextArea sourceField = new JTextArea(2, 50);

	/**
	 * Creates an instance of {@code NewArticleDialog}.
	 */
	public NewArticleDialog() {
		super(MainPanel.get(), "New Article", true);
		setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
		MainPanel frame = MainPanel.get();

		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
		getContentPane().add(dialogPanel);

		JPanel articlePanel = new JPanel();
		articlePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		articlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
		articlePanel.add(new JLabel("Article Title:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		documentTitleField.setLineWrap(true);
		articlePanel.add(new JScrollPane(documentTitleField), c);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		articlePanel.add(new JLabel("Authors:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 5);
		authorField.setLineWrap(true);
		articlePanel.add(new JScrollPane(authorField), c);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 5, 5, 5);
		JLabel authorSuggestionLabel = new JLabel("Separate authors by \",\".");
		authorSuggestionLabel.setForeground(Color.GRAY);
		articlePanel.add(authorSuggestionLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		articlePanel.add(new JLabel("Comment:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		commentField.setLineWrap(true);
		articlePanel.add(new JScrollPane(commentField), c);

		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		articlePanel.add(new JLabel("Source:"), c);

		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		sourceField.setLineWrap(true);
		articlePanel.add(new JScrollPane(sourceField), c);

		dialogPanel.add(articlePanel);

		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(okButton);
		buttons.add(cancelButton);

		dialogPanel.add(buttons);

		pack();
		setLocationRelativeTo(frame);
		setSize(getWidth() + 15, getHeight());
		setResizable(false);
		setVisible(true);
	}
}
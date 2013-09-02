package notes.gui.article.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JTextField;

import notes.article.dao.ArticleNoteDAO;
import notes.article.entity.ArticleNote;
import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.entity.Tag;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;
import notes.utils.TagsStrListBuilder;

/**
 * Defines the dialog and event listener for editing an article note.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class EditArticleNoteDialog extends JDialog {
	private static final long serialVersionUID = -4771583013693557849L;

	private JButton okButton = new JButton(new AbstractAction("OK") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {

			// Input validation.
			List<String> tagsStrList = TagsStrListBuilder.buildTagsStrList(tagsField.getText());
			if (tagsStrList.size() > 5) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "A note can have at most 5 tags!",
						"Input error", JOptionPane.ERROR_MESSAGE);
				tagsField.requestFocus();
				return;
			}
			for (String tagStr : tagsStrList) {
				if (tagStr.length() > 20) {
					if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
						SoundFactory.playError();
					}
					JOptionPane.showMessageDialog(null, "A tag can have at most 20 characters!",
							"Input error", JOptionPane.ERROR_MESSAGE);
					tagsField.requestFocus();
					return;
				}
			}
			if (noteTextField.getText() == null || noteTextField.getText().trim().equals("")) {
				if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
					SoundFactory.playError();
				}
				JOptionPane.showMessageDialog(null, "Note text cannot be empty!", "Input error",
						JOptionPane.ERROR_MESSAGE);
				noteTextField.requestFocus();
				return;
			}

			MainPanel frame = MainPanel.get();
			ArticleHome home = ArticleHome.get();
			ArticleNoteDAO dao = home.getArticleNoteDAO();

			// Create instance of the updated article note.
			ArticleNote updatedArticleNote = new ArticleNote();
			updatedArticleNote.setNoteId(home.getCurrentArticleNote().getNoteId());
			updatedArticleNote.setDocumentId(home.getCurrentArticleNote().getDocumentId());

			List<Long> updatedTagsList = new ArrayList<Long>();
			for (String tagStr : tagsStrList) {
				// Set the new tag IDs, save tags if they are new.
				Tag cachedTag = dao.findTagByText(tagStr);
				if (cachedTag != null) {
					updatedTagsList.add(cachedTag.getTagId());
				} else {
					Tag newTag = new Tag();
					newTag.setTagText(tagStr);
					Tag savedTag = dao.saveTag(newTag);
					updatedTagsList.add(savedTag.getTagId());
				}
			}
			updatedArticleNote.setTagIds(updatedTagsList);
			updatedArticleNote.setNoteText(noteTextField.getText());

			// Save the updated article note.
			ArticleNote newArticleNote = (ArticleNote) (dao.mergeNote(updatedArticleNote));

			// Update temporary data in the ArticleHome.
			home.updateTemporaryData(home.getCurrentArticle().getDocumentId(),
					newArticleNote.getNoteId());

			// Update the note panel.
			frame.updateArticleNotePanel();

			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playUpdate();
			}

			setVisible(false);
		}
	});

	private JButton cancelButton = new JButton(new AbstractAction("Cancel") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playNavigation();
			}
			setVisible(false);
		}
	});

	private JTextArea documentField = new JTextArea(2, 50);
	private JTextField tagsField = new JTextField();
	private JTextArea noteTextField = new JTextArea(10, 50);

	/**
	 * Creates an instance of {@code EditArticleNoteDialog}.
	 */
	public EditArticleNoteDialog() {
		super(MainPanel.get(), "Edit Article Note", true);
		setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
		MainPanel frame = MainPanel.get();
		ArticleHome home = ArticleHome.get();

		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
		getContentPane().add(dialogPanel);

		JPanel notePanel = new JPanel();
		notePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		notePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
		notePanel.add(new JLabel("Document:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		documentField.setLineWrap(true);
		documentField.setText(home.getCurrentArticle().getDocumentTitle());
		documentField.setEditable(false);
		notePanel.add(new JScrollPane(documentField), c);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		notePanel.add(new JLabel("Tags:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 5);
		StringBuilder tagStrBuilder = new StringBuilder();
		if (home.getCurrentArticleNote().getTagIds().isEmpty() == false) {
			for (Long tagId : home.getCurrentArticleNote().getTagIds()) {
				tagStrBuilder.append(ArticleHome.get().getArticleNoteDAO().findTagById(tagId)
						.getTagText()
						+ ",");
			}
			tagStrBuilder.deleteCharAt(tagStrBuilder.length() - 1);
		}
		tagsField.setText(tagStrBuilder.toString());
		notePanel.add(tagsField, c);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 5, 5, 5);
		JLabel suggestionLabel = new JLabel(
				"Use captialized words and separate tags by \",\". E.g. \"Design Pattern,Algrithm\"");
		suggestionLabel.setForeground(Color.GRAY);
		notePanel.add(suggestionLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		notePanel.add(new JLabel("Note Text:"), c);

		noteTextField.setLineWrap(true);
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		noteTextField.setText(home.getCurrentArticleNote().getNoteText());
		notePanel.add(new JScrollPane(noteTextField), c);

		dialogPanel.add(notePanel);

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
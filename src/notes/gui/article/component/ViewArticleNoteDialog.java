/**
 * 
 */
package notes.gui.article.component;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Defines the dialog and event listener for viewing an article note.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class ViewArticleNoteDialog extends JDialog {

	private static final long serialVersionUID = -8380650370707991224L;

	private JButton okButton = new JButton(new AbstractAction("OK") {
		private static final long serialVersionUID = -5237737680519770896L;

		public void actionPerformed(ActionEvent e) {
			if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
				SoundFactory.playNavigation();
			}
			setVisible(false);
		}
	});

	private JLabel noteIdField = new JLabel();
	private JTextArea documentField = new JTextArea(2, 50);
	private JTextArea tagsField = new JTextArea(2, 50);
	private JTextArea noteTextField = new JTextArea(10, 50);
	private JLabel createdTimeField = new JLabel();

	/**
	 * Creates an instance of {@code ViewArticleNoteDialog}.
	 */
	public ViewArticleNoteDialog() {
		super(MainPanel.get(), "View Article Note", true);
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
		notePanel.add(new JLabel("Note ID:"), c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		noteIdField.setText(home.getCurrentArticleNote().getNoteId().toString());
		notePanel.add(noteIdField, c);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		notePanel.add(new JLabel("Document:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		documentField.setLineWrap(true);
		documentField.setText(home.getCurrentArticle().getDocumentTitle());
		documentField.setEditable(false);
		notePanel.add(new JScrollPane(documentField), c);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		notePanel.add(new JLabel("Tags:"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		StringBuilder tagStrBuilder = new StringBuilder();
		if (home.getCurrentArticleNote().getTagIds().isEmpty() == false) {
			for (Long tagId : home.getCurrentArticleNote().getTagIds()) {
				tagStrBuilder.append(ArticleHome.get().getArticleNoteDAO().findTagById(tagId)
						.getTagText()
						+ ",");
			}
			tagStrBuilder.deleteCharAt(tagStrBuilder.length() - 1);
		}
		tagsField.setLineWrap(true);
		tagsField.setText(tagStrBuilder.toString());
		tagsField.setEditable(false);
		notePanel.add(new JScrollPane(tagsField), c);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		notePanel.add(new JLabel("Note Text:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		noteTextField.setLineWrap(true);
		noteTextField.setText(home.getCurrentArticleNote().getNoteText());
		noteTextField.setEditable(false);
		notePanel.add(new JScrollPane(noteTextField), c);

		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		notePanel.add(new JLabel("Created Time:"), c);

		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		createdTimeField.setText(home.getCurrentArticleNote().getCreatedTime().toString());
		notePanel.add(createdTimeField, c);

		dialogPanel.add(notePanel);

		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(okButton);

		dialogPanel.add(buttons);

		pack();
		setLocationRelativeTo(frame);
		setSize(getWidth() + 15, getHeight());
		setResizable(false);
		setVisible(true);
	}
}

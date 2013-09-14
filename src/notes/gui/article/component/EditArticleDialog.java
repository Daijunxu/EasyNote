/**
 *
 */
package notes.gui.article.component;

import notes.article.Article;
import notes.bean.ArticleHome;
import notes.dao.impl.ArticleNoteDAO;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.EntityStrListBuilder;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

/**
 * Defines the dialog and event listener for editing an article.
 *
 * @author Rui Du
 * @version 1.0
 */
public class EditArticleDialog extends JDialog {

    private static final long serialVersionUID = 5523266061079957193L;
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
                    && !authorField.getText().trim().equals("")
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

            // Create instance of the updated article.
            Article updatedArticle = new Article();
            updatedArticle.setDocumentId(home.getCurrentArticle().getDocumentId());
            updatedArticle.setDocumentTitle(WordUtils.capitalize(documentTitleField.getText()
                    .trim()));
            updatedArticle.setAuthorsList(EntityStrListBuilder.buildAuthorsStrList(authorField
                    .getText()));
            if (commentField.getText() != null && !commentField.getText().trim().equals("")) {
                updatedArticle.setComment(commentField.getText().trim());
            }
            if (sourceField.getText() != null && !sourceField.getText().trim().equals("")) {
                updatedArticle.setSource(sourceField.getText().trim());
            }
            updatedArticle.setCreatedTime(home.getCurrentArticle().getCreatedTime());
            updatedArticle.setLastUpdatedTime(new Date(System.currentTimeMillis()));

            // Save the updated article.
            dao.mergeDocument(updatedArticle);

            // Update temporary data in the ArticleHome.
            home.updateTemporaryData(home.getCurrentArticle().getDocumentId(), null);

            // Update the note panel.
            frame.setArticlePanel(home.getCurrentArticle());

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playUpdate();
            }

            setVisible(false);
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
     * Creates an instance of {@code EditArticleDialog}.
     */
    public EditArticleDialog() {
        super(MainPanel.get(), "Edit Article Information", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();
        ArticleHome home = ArticleHome.get();

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
        documentTitleField.setText(home.getCurrentArticle().getDocumentTitle());
        articlePanel.add(new JScrollPane(documentTitleField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Authors:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 0, 5);
        authorField.setLineWrap(true);
        StringBuilder sb = new StringBuilder();
        List<String> authorsList = home.getCurrentArticle().getAuthorsList();
        if (!authorsList.isEmpty()) {
            for (String author : home.getCurrentArticle().getAuthorsList()) {
                sb.append(author);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        authorField.setText(sb.toString());
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
        commentField.setText(home.getCurrentArticle().getComment());
        articlePanel.add(new JScrollPane(commentField), c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Source:"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        sourceField.setLineWrap(true);
        sourceField.setText(home.getCurrentArticle().getSource());
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

package notes.gui.article.component;

import notes.businesslogic.ArticleBusinessLogic;
import notes.dao.impl.ArticleNoteDAO;
import notes.businessobjects.article.Article;
import notes.gui.main.component.MainPanel;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;

/**
 * Defines the dialog and event listener of creating a new article.
 *
 * Author: Rui Du
 */
public class NewArticleDialog extends JDialog {
    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (documentTitleField.getText() == null
                    || documentTitleField.getText().trim().equals("")) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Document title cannot be empty!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
                return;
            } else if (documentTitleField.getText().trim().split("\n").length > 1) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Document title can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
                return;
            } else if (authorField.getText() != null
                    && !authorField.getText().trim().equals("")
                    && authorField.getText().trim().split("\n").length > 1) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Author list can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                authorField.requestFocus();
                return;
            } else if (commentField.getText().trim().split("\n").length > 1) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Comment can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                commentField.requestFocus();
                return;
            } else if (sourceField.getText().trim().split("\n").length > 1) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Source can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                sourceField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            ArticleBusinessLogic logic = ArticleBusinessLogic.get();
            ArticleNoteDAO dao = logic.getArticleNoteDAO();

            // Create instance of the new article.
            Article newArticle = new Article();
            newArticle.setDocumentTitle(WordUtils.capitalize(documentTitleField.getText().trim()));
            newArticle.setAuthorsList(EntityHelper.buildAuthorsStrList(authorField
                    .getText()));
            if (commentField.getText() != null && !commentField.getText().trim().equals("")) {
                newArticle.setComment(commentField.getText().trim());
            }
            if (sourceField.getText() != null && !sourceField.getText().trim().equals("")) {
                newArticle.setSource(sourceField.getText().trim());
            }
            newArticle.setCreatedTime(new Date(System.currentTimeMillis()));
            newArticle.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            newArticle.setNotesList(new ArrayList<Long>());

            // Save the updated article.
            Article savedArticle = (Article) dao.saveDocument(newArticle);

            if (savedArticle == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Document already exists!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
            } else {
                // Update the note panel.
                frame.setArticlePanel(savedArticle, null);

                SoundFactory.playUpdate();

                setVisible(false);
            }
        }
    });
    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        public void actionPerformed(ActionEvent e) {
            SoundFactory.playNavigation();
            setVisible(false);
        }
    });
    private final JTextArea documentTitleField = new JTextArea(2, 50);
    private final JTextArea authorField = new JTextArea(2, 50);
    private final JTextArea commentField = new JTextArea(10, 50);
    private final JTextArea sourceField = new JTextArea(2, 50);

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
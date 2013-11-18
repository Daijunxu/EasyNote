package notes.gui.article.component;

import notes.bean.ArticleHome;
import notes.dao.impl.ArticleNoteDAO;
import notes.entity.Tag;
import notes.entity.article.ArticleNote;
import notes.gui.main.component.MainPanel;
import notes.gui.main.validation.NoteTextInputValidator;
import notes.gui.main.validation.TagsInputValidator;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import notes.utils.TextHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the dialog and event listener for creating a article note.
 *
 * @author Rui Du
 */
public class NewArticleNoteDialog extends JDialog {
    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            String errorMessage;

            List<String> tagsStrList = EntityHelper.buildTagsStrList(tagsField.getText());
            errorMessage = TagsInputValidator.hasError(tagsStrList);
            if (errorMessage != null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, errorMessage, "Input error", JOptionPane.ERROR_MESSAGE);
                tagsField.requestFocus();
                return;
            }
            errorMessage = NoteTextInputValidator.hasError(noteTextField.getText());
            if (errorMessage != null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, errorMessage, "Input error", JOptionPane.ERROR_MESSAGE);
                noteTextField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            ArticleHome home = ArticleHome.get();
            ArticleNoteDAO dao = home.getArticleNoteDAO();

            // Create instance of the created article note.
            ArticleNote createdArticleNote = new ArticleNote();
            createdArticleNote.setDocumentId(home.getCurrentArticle().getDocumentId());
            List<Long> tagsList = new ArrayList<Long>();
            for (String tagStr : tagsStrList) {
                // Set the new tag IDs, save tags if they are new.
                Tag cachedTag = dao.findTagByText(tagStr);
                if (cachedTag != null) {
                    tagsList.add(cachedTag.getTagId());
                } else {
                    Tag newTag = new Tag();
                    newTag.setTagText(tagStr);
                    Tag savedTag = dao.saveTag(newTag);
                    tagsList.add(savedTag.getTagId());
                }
            }
            createdArticleNote.setTagIds(tagsList);
            createdArticleNote.setNoteText(TextHelper.processInputText(noteTextField.getText()));

            // Save the created article note.
            ArticleNote cachedArticleNote = (ArticleNote) (dao.saveNote(createdArticleNote));

            // Update temporary data in the ArticleHome.
            home.updateTemporaryData(home.getCurrentArticle().getDocumentId(),
                    cachedArticleNote.getNoteId());

            // Update the note panel.
            frame.updateArticleNotePanel(cachedArticleNote.getNoteId());

            SoundFactory.playUpdate();

            setVisible(false);
        }
    });
    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        public void actionPerformed(ActionEvent e) {
            SoundFactory.playNavigation();
            setVisible(false);
        }
    });
    private final JTextArea documentField = new JTextArea(2, 50);
    private final JTextArea noteTextField = new JTextArea(25, 80);
    private final JTextField tagsField = new JTextField();

    /**
     * Creates an instance of {@code NewArticleNoteDialog}.
     */
    public NewArticleNoteDialog() {
        super(MainPanel.get(), "Create Article Note", true);
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
        notePanel.add(new JLabel("Note Text:"), c);

        noteTextField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 0, 5);
        notePanel.add(tagsField, c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel suggestionLabel = new JLabel("Words separated by \",\".");
        suggestionLabel.setForeground(Color.GRAY);
        notePanel.add(suggestionLabel, c);

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

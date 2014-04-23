package notes.gui.article.component;

import notes.businesslogic.ArticleBusinessLogic;
import notes.dao.impl.ArticleNoteDAO;
import notes.businessobjects.Note;
import notes.businessobjects.Tag;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.SearchNoteDialog;
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
 * Defines the dialog and event listener for editing an article note.
 *
 * Author: Rui Du
 */
public class EditArticleNoteDialog extends JDialog {
    private final ArticleNote selectedNote;
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
            ArticleBusinessLogic logic = ArticleBusinessLogic.get();
            ArticleNoteDAO dao = logic.getArticleNoteDAO();

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
            selectedNote.setTagIds(updatedTagsList);
            selectedNote.setNoteText(TextHelper.processInputText(noteTextField.getText()));

            // Save the updated article note.
            Note updatedNote = dao.updateNote(selectedNote);

            // Update the note panel.
            if (frame.isSearchMode()) {
                SearchNoteDialog.get().updateResultPanel();
            } else {
                frame.updateArticleNotePanel(updatedNote.getNoteId());
            }

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
    private final JTextArea noteTextField = new JTextArea(25, 80);
    private final JTextField tagsField = new JTextField();
    private final JLabel createdTimeField = new JLabel();

    /**
     * Creates an instance of {@code EditArticleNoteDialog}.
     */
    public EditArticleNoteDialog(Article selectedArticle, ArticleNote selectedNote) {
        super(MainPanel.get(), selectedArticle.getDocumentTitle(), true);

        this.selectedNote = selectedNote;

        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();
        ArticleBusinessLogic logic = ArticleBusinessLogic.get();

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
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text:"), c);

        noteTextField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        noteTextField.setText(selectedNote.getNoteText());
        noteTextField.select(0, 0);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 0, 5);
        StringBuilder tagStrBuilder = new StringBuilder();
        if (!selectedNote.getTagIds().isEmpty()) {
            for (Long tagId : selectedNote.getTagIds()) {
                tagStrBuilder.append(logic.getArticleNoteDAO().findTagById(tagId).getTagText()).append(", ");
            }
            tagStrBuilder.delete(tagStrBuilder.length() - 2, tagStrBuilder.length());
        }
        tagsField.setText(tagStrBuilder.toString());
        notePanel.add(tagsField, c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel suggestionLabel = new JLabel(
                "Use capitalized words and separate tags by \",\". E.g. \"Design Pattern, Algorithm\"");
        suggestionLabel.setForeground(Color.GRAY);
        notePanel.add(suggestionLabel, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(selectedNote.getCreatedTime().toString());
        notePanel.add(createdTimeField, c);

        dialogPanel.add(notePanel);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(okButton);
        buttons.add(cancelButton);

        dialogPanel.add(buttons);

        pack();
        setLocationRelativeTo(frame);
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
        noteTextField.requestFocus();
        setVisible(true);
    }

}
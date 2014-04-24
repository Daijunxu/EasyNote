package notes.gui.workset.component;

import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.workset.WorksheetNoteStatus;
import notes.dao.impl.WorksheetNoteDAO;
import notes.businessobjects.Tag;
import notes.businessobjects.workset.Workset;
import notes.businessobjects.workset.Worksheet;
import notes.businessobjects.workset.WorksheetNote;
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
 * Defines the dialog and event listener for editing a worksheet note.
 *
 * Author: Rui Du
 */
public class EditWorksheetNoteDialog extends JDialog {
    private final WorksheetNote selectedNote;
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
            WorksetBusinessLogic logic = WorksetBusinessLogic.get();
            WorksheetNoteDAO dao = logic.getWorksheetNoteDAO();
            WorksheetNote newNote = new WorksheetNote();
            newNote.setNoteId(selectedNote.getNoteId());
            newNote.setDocumentId(selectedNote.getDocumentId());

            Long selectedWorksheetId = logic.getCurrentWorkset().getWorksheetIdsList()
                    .get(worksheetField.getSelectedIndex());
            newNote.setWorksheetId(selectedWorksheetId);

            newNote.setNoteText(TextHelper.processInputText(noteTextField.getText()));

            newNote.setNoteStatus(WorksheetNoteStatus.values()[noteStatusField.getSelectedIndex()]);

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
            newNote.setTagIds(updatedTagsList);

            // Save the updated workset note.
            WorksheetNote updatedNote = (WorksheetNote) dao.updateNote(newNote);

            // Update current note.
            logic.setCurrentNote(updatedNote);

            // Update the note panel.
            if (frame.isSearchMode()) {
                SearchNoteDialog.get().updateResultPanel();
            } else {
                frame.updateWorksheetNotePanel(logic.getCurrentWorksheet(), updatedNote.getNoteId());
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
    private final JComboBox worksheetField = new JComboBox();
    private final JTextArea noteTextField = new JTextArea(25, 80);
    private final JComboBox noteStatusField = new JComboBox();
    private final JTextField tagsField = new JTextField();
    private final JLabel createdTimeField = new JLabel();

    /**
     * Creates an instance of {@code EditWorksheetNoteDialog}.
     */
    public EditWorksheetNoteDialog(Workset selectedWorkset, Worksheet selectedWorksheet, WorksheetNote selectedNote) {
        super(MainPanel.get(), selectedWorkset.getDocumentTitle(), true);

        this.selectedNote = selectedNote;

        setIconImage(new ImageIcon("./resources/images/workset.gif").getImage());
        MainPanel frame = MainPanel.get();
        WorksetBusinessLogic logic = WorksetBusinessLogic.get();

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
        notePanel.add(new JLabel("Worksheet:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        for (Long worksheetId : selectedWorkset.getWorksheetIdsList()) {
            worksheetField.addItem(selectedWorkset.getWorksheetsMap().get(worksheetId).getWorksheetTitle());
        }
        Long currentWorksheetId = selectedWorksheet.getWorksheetId();
        worksheetField.setSelectedIndex(selectedWorkset.getWorksheetIdsList().indexOf(currentWorksheetId));
        notePanel.add(worksheetField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text:"), c);

        noteTextField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        noteTextField.setText(selectedNote.getNoteText());
        noteTextField.select(0, 0);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Status:"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        for (WorksheetNoteStatus noteStatus : WorksheetNoteStatus.values()) {
            // Note: the order should be the same as defined in WorksheetNoteStatus.
            noteStatusField.addItem(noteStatus.getDescription());
        }
        noteStatusField.setSelectedIndex(selectedNote.getNoteStatus().ordinal());
        notePanel.add(noteStatusField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 0, 5);
        StringBuilder tagStrBuilder = new StringBuilder();
        if (!selectedNote.getTagIds().isEmpty()) {
            for (Long tagId : selectedNote.getTagIds()) {
                tagStrBuilder.append(logic.getWorksheetNoteDAO().findTagById(tagId).getTagText()).append(", ");
            }
            tagStrBuilder.delete(tagStrBuilder.length() - 2, tagStrBuilder.length());
        }
        tagsField.setText(tagStrBuilder.toString());
        tagsField.select(0, 0);
        notePanel.add(tagsField, c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel suggestionLabel = new JLabel(
                "Use capitalized words and separate tags by \",\". E.g. \"Design Pattern, Algorithm\"");
        suggestionLabel.setForeground(Color.GRAY);
        notePanel.add(suggestionLabel, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 5;
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
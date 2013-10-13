package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.dao.impl.WorksheetNoteDAO;
import notes.data.cache.Property;
import notes.entity.NoteStatus;
import notes.entity.Tag;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.SearchNoteDialog;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the dialog and event listener for editing a worksheet note.
 *
 * @author Rui Du
 */
public class EditWorksheetNoteDialog extends JDialog {
    private final WorksheetNote selectedNote;
    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (tagsField.getText() != null && !tagsField.getText().trim().equals("")
                    && tagsField.getText().trim().split("\n").length > 1) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Tag list can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                tagsField.requestFocus();
                return;
            }
            List<String> tagsStrList = EntityHelper.buildTagsStrList(tagsField.getText());
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
                if (tagStr.length() > 30) {
                    if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                        SoundFactory.playError();
                    }
                    JOptionPane.showMessageDialog(null, "A tag can have at most 30 characters!",
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
            WorksetHome home = WorksetHome.get();
            WorksheetNoteDAO dao = home.getWorksheetNoteDAO();
            WorksheetNote newNote = new WorksheetNote();
            newNote.setNoteId(selectedNote.getNoteId());
            newNote.setDocumentId(selectedNote.getDocumentId());

            Long selectedWorksheetId = home.getCurrentWorkset().getWorksheetIdsList()
                    .get(worksheetField.getSelectedIndex());
            newNote.setWorksheetId(selectedWorksheetId);

            newNote.setNoteText(noteTextField.getText());

            newNote.setNoteStatus(NoteStatus.values()[noteStatusField.getSelectedIndex()]);

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
            home.setCurrentWorksheetNote(updatedNote);

            // Update the note panel.
            if (frame.isSearchMode()) {
                SearchNoteDialog.get().updateResultPanel();
            } else {
                frame.updateWorksheetNotePanel(home.getCurrentWorksheet(), updatedNote.getNoteId());
            }

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playUpdate();
            }

            setVisible(false);
        }
    });
    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });
    private final JLabel noteIdField = new JLabel();
    private final JComboBox worksheetField = new JComboBox();
    private final JTextArea noteTextField = new JTextArea(20, 50);
    private final JComboBox noteStatusField = new JComboBox();
    private final JTextArea tagsField = new JTextArea(2, 50);
    private final JLabel createdTimeField = new JLabel();

    /**
     * Creates an instance of {@code EditWorksheetNoteDialog}.
     */
    public EditWorksheetNoteDialog(Workset selectedWorkset, Worksheet selectedWorksheet, WorksheetNote selectedNote) {
        super(MainPanel.get(), selectedWorkset.getDocumentTitle(), true);

        this.selectedNote = selectedNote;

        setIconImage(new ImageIcon("./resources/images/workset.gif").getImage());
        MainPanel frame = MainPanel.get();
        WorksetHome home = WorksetHome.get();

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

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        noteIdField.setText(selectedNote.getNoteId().toString());
        notePanel.add(noteIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Worksheet:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        for (Long worksheetId : selectedWorkset.getWorksheetIdsList()) {
            worksheetField.addItem(selectedWorkset.getWorksheetsMap().get(worksheetId).getWorksheetTitle());
        }
        Long currentWorksheetId = selectedWorksheet.getWorksheetId();
        worksheetField.setSelectedIndex(selectedWorkset.getWorksheetIdsList().indexOf(currentWorksheetId));
        notePanel.add(worksheetField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text:"), c);

        noteTextField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        noteTextField.setText(selectedNote.getNoteText());
        noteTextField.select(0, 0);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Status:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        for (NoteStatus noteStatus : NoteStatus.values()) {
            // Note: the order should be the same as defined in NoteStatus.
            noteStatusField.addItem(noteStatus.getDescription());
        }
        noteStatusField.setSelectedIndex(selectedNote.getNoteStatus().ordinal());
        notePanel.add(noteStatusField, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 0, 5);
        StringBuilder tagStrBuilder = new StringBuilder();
        if (!selectedNote.getTagIds().isEmpty()) {
            for (Long tagId : selectedNote.getTagIds()) {
                tagStrBuilder.append(home.getWorksheetNoteDAO().findTagById(tagId).getTagText()).append(", ");
            }
            tagStrBuilder.delete(tagStrBuilder.length() - 2, tagStrBuilder.length());
        }
        tagsField.setLineWrap(true);
        tagsField.setText(tagStrBuilder.toString());
        tagsField.select(0, 0);
        notePanel.add(new JScrollPane(tagsField), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel suggestionLabel = new JLabel(
                "Use capitalized words and separate tags by \",\". E.g. \"Design Pattern, Algorithm\"");
        suggestionLabel.setForeground(Color.GRAY);
        notePanel.add(suggestionLabel, c);

        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 6;
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
        setVisible(true);
    }

}
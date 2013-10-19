package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.dao.impl.WorksheetNoteDAO;
import notes.entity.NoteStatus;
import notes.entity.Tag;
import notes.entity.workset.WorksheetNote;
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
 * Defines the dialog and event listener for creating a workset note.
 *
 * @author Rui Du
 */
public class NewWorksheetNoteDialog extends JDialog {

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
            WorksetHome home = WorksetHome.get();
            WorksheetNoteDAO dao = home.getWorksheetNoteDAO();

            // Create instance of the created workset note.
            WorksheetNote createdWorksheetNote = new WorksheetNote();
            createdWorksheetNote.setDocumentId(home.getCurrentWorkset().getDocumentId());
            Long selectedWorksheetId = home.getCurrentWorkset().getWorksheetIdsList()
                    .get(worksheetField.getSelectedIndex());
            createdWorksheetNote.setWorksheetId(selectedWorksheetId);
            createdWorksheetNote.setNoteText(TextHelper.processInputText(noteTextField.getText()));

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

            createdWorksheetNote.setNoteStatus(NoteStatus.values()[noteStatusField.getSelectedIndex()]);
            createdWorksheetNote.setTagIds(tagsList);

            // Save the created workset note.
            WorksheetNote cachedWorksheetNote = (WorksheetNote) (dao.saveNote(createdWorksheetNote));

            // Update the note panel.
            frame.updateWorksheetNotePanel(home.getCurrentWorksheet(), cachedWorksheetNote.getNoteId());

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
    private final JComboBox worksheetField = new JComboBox();
    private final JTextArea noteTextField = new JTextArea(20, 50);
    private final JComboBox noteStatusField = new JComboBox();
    private final JTextField tagsField = new JTextField();

    /**
     * Creates an instance of {@code NewWorksheetNoteDialog}.
     */
    public NewWorksheetNoteDialog() {
        super(MainPanel.get(), "Create Workset Note", true);
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
        notePanel.add(new JLabel("Document:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(home.getCurrentWorkset().getDocumentTitle());
        documentField.setEditable(false);
        notePanel.add(new JScrollPane(documentField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Worksheet:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        for (Long worksheetId : home.getCurrentWorkset().getWorksheetIdsList()) {
            worksheetField.addItem(home.getCurrentWorkset().getWorksheetsMap().get(worksheetId).getWorksheetTitle());
        }
        Long currentWorksheetId = home.getCurrentWorksheet().getWorksheetId();
        worksheetField.setSelectedIndex(home.getCurrentWorkset().getWorksheetIdsList().indexOf(currentWorksheetId));
        notePanel.add(worksheetField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text:"), c);

        noteTextField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Status:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        for (NoteStatus noteStatus : NoteStatus.values()) {
            noteStatusField.addItem(noteStatus.getDescription());
        }
        noteStatusField.setSelectedIndex(0); // The default note status is "No Action".
        notePanel.add(noteStatusField, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 0, 5);
        notePanel.add(tagsField, c);

        c.gridx = 1;
        c.gridy = 5;
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

package notes.gui.workset.component;

import notes.businesslogic.WorksetBusinessLogic;
import notes.dao.DuplicateRecordException;
import notes.dao.impl.WorksheetNoteDAO;
import notes.businessobjects.workset.Workset;
import notes.gui.main.component.MainPanel;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

/**
 * Defines the dialog and event listener for editing a workset.
 * <p/>
 * Author: Rui Du
 */
public class EditWorksetDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (documentTitleField.getText() == null || documentTitleField.getText().trim().equals("")) {
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
            }

            MainPanel frame = MainPanel.get();
            WorksetBusinessLogic logic = WorksetBusinessLogic.get();
            WorksheetNoteDAO dao = logic.getWorksheetNoteDAO();

            // Create instance of the updated workset.
            Workset updatedWorkset = new Workset();
            updatedWorkset.setDocumentId(logic.getCurrentWorkset().getDocumentId());
            updatedWorkset.setDocumentTitle(WordUtils.capitalize(documentTitleField.getText().trim()));
            updatedWorkset.setAuthorsList(EntityHelper.buildAuthorsStrList(authorField
                    .getText()));
            if (commentField.getText() != null && !commentField.getText().trim().equals("")) {
                updatedWorkset.setComment(commentField.getText().trim());
            }
            updatedWorkset.setWorksheetIdsList(logic.getCurrentWorkset().getWorksheetIdsList());
            updatedWorkset.setWorksheetsMap(logic.getCurrentWorkset().getWorksheetsMap());
            updatedWorkset.setCreatedTime(logic.getCurrentWorkset().getCreatedTime());
            updatedWorkset.setLastUpdatedTime(new Date(System.currentTimeMillis()));

            // Save the updated Workset.
            try {
                dao.updateDocument(updatedWorkset);

                // Reset the Workset panel.
                frame.setWorksetPanel(logic.getCurrentWorkset(), null);

                SoundFactory.playUpdate();
                setVisible(false);
            } catch (DuplicateRecordException exception) {
                exception.printStackTrace();
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Document with the same title already exists!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
            }
        }
    });
    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        public void actionPerformed(ActionEvent e) {
            SoundFactory.playNavigation();
            setVisible(false);
        }
    });
    private final JLabel documentIdField = new JLabel();
    private final JTextArea documentTitleField = new JTextArea(2, 50);
    private final JTextArea authorField = new JTextArea(2, 50);
    private final JTextArea commentField = new JTextArea(10, 50);
    private final JLabel createdTimeField = new JLabel();
    private final JLabel lastUpdatedTimeField = new JLabel();

    /**
     * Creates an instance of {@code EditWorksetDialog}.
     */
    public EditWorksetDialog() {
        super(MainPanel.get(), "Workset Information", true);
        setIconImage(new ImageIcon("./resources/images/Workset.gif").getImage());
        MainPanel frame = MainPanel.get();
        WorksetBusinessLogic logic = WorksetBusinessLogic.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel worksetPanel = new JPanel();
        worksetPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        worksetPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        worksetPanel.add(new JLabel("Document ID:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentIdField.setText(logic.getCurrentWorkset().getDocumentId().toString());
        worksetPanel.add(documentIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        worksetPanel.add(new JLabel("Workset Title:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentTitleField.setLineWrap(true);
        documentTitleField.setText(logic.getCurrentWorkset().getDocumentTitle());
        documentTitleField.select(0, 0);
        worksetPanel.add(new JScrollPane(documentTitleField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Authors:"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 0, 5);
        authorField.setLineWrap(true);
        StringBuilder sb = new StringBuilder();
        List<String> authorsList = logic.getCurrentWorkset().getAuthorsList();
        if (!authorsList.isEmpty()) {
            for (String author : authorsList) {
                sb.append(author);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        authorField.setText(sb.toString());
        authorField.select(0, 0);
        worksetPanel.add(new JScrollPane(authorField), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel authorSuggestionLabel = new JLabel("Separate authors by \",\".");
        authorSuggestionLabel.setForeground(Color.GRAY);
        worksetPanel.add(authorSuggestionLabel, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Comment:"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        commentField.setLineWrap(true);
        commentField.setText(logic.getCurrentWorkset().getComment());
        commentField.select(0, 0);
        worksetPanel.add(new JScrollPane(commentField), c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(logic.getCurrentWorkset().getCreatedTime().toString());
        worksetPanel.add(createdTimeField, c);

        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Last Updated Time:"), c);

        c.gridx = 1;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        lastUpdatedTimeField.setText(logic.getCurrentWorkset().getLastUpdatedTime().toString());
        worksetPanel.add(lastUpdatedTimeField, c);

        dialogPanel.add(worksetPanel);

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

package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.dao.impl.WorksheetNoteDAO;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Defines the dialog and event listener for creating a new workset.
 *
 * @author Rui Du
 */
public class NewWorksetDialog extends JDialog {

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
            }

            MainPanel frame = MainPanel.get();
            WorksetHome home = WorksetHome.get();
            WorksheetNoteDAO dao = home.getWorksheetNoteDAO();

            // Create instance of the new workset.
            Workset updatedWorkset = new Workset();
            updatedWorkset.setDocumentTitle(WordUtils.capitalize(documentTitleField.getText().trim()));
            updatedWorkset.setAuthorsList(EntityHelper.buildAuthorsStrList(authorField
                    .getText()));
            if (commentField.getText() != null && !commentField.getText().trim().equals("")) {
                updatedWorkset.setComment(commentField.getText().trim());
            }
            updatedWorkset.setWorksheetIdsList(new ArrayList<Long>());
            updatedWorkset.setWorksheetsMap(new HashMap<Long, Worksheet>());
            updatedWorkset.setCreatedTime(new Date(System.currentTimeMillis()));
            updatedWorkset.setLastUpdatedTime(new Date(System.currentTimeMillis()));

            // Save the new workset.
            Workset savedWorkset = (Workset) dao.saveDocument(updatedWorkset);

            if (savedWorkset == null) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Document already exists!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
            } else {
                // Reset the workset panel.
                frame.setWorksetPanel(home.getCurrentWorkset(), null);

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

    /**
     * Creates an instance of {@code NewWorksetDialog}.
     */
    public NewWorksetDialog() {
        super(MainPanel.get(), "New Workset", true);
        setIconImage(new ImageIcon("./resources/images/workset.gif").getImage());
        MainPanel frame = MainPanel.get();

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
        worksetPanel.add(new JLabel("Workset Title:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentTitleField.setLineWrap(true);
        worksetPanel.add(new JScrollPane(documentTitleField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Authors:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 0, 5);
        authorField.setLineWrap(true);
        worksetPanel.add(new JScrollPane(authorField), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel authorSuggestionLabel = new JLabel("Separate authors by \",\".");
        authorSuggestionLabel.setForeground(Color.GRAY);
        worksetPanel.add(authorSuggestionLabel, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Comment:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        commentField.setLineWrap(true);
        worksetPanel.add(new JScrollPane(commentField), c);

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

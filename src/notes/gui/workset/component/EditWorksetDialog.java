package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.dao.impl.WorksheetNoteDAO;
import notes.data.cache.Property;
import notes.entity.workset.Workset;
import notes.gui.main.component.MainPanel;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

/**
 * Defines the dialog and event listener for editing a workset.
 *
 * @author Rui Du
 */
public class EditWorksetDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
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
            }

            MainPanel frame = MainPanel.get();
            WorksetHome home = WorksetHome.get();
            WorksheetNoteDAO dao = home.getWorksheetNoteDAO();

            // Create instance of the updated workset.
            Workset updatedWorkset = new Workset();
            updatedWorkset.setDocumentId(home.getCurrentWorkset().getDocumentId());
            updatedWorkset.setDocumentTitle(WordUtils.capitalize(documentTitleField.getText().trim()));
            updatedWorkset.setAuthorsList(EntityHelper.buildAuthorsStrList(authorField
                    .getText()));
            if (commentField.getText() != null && !commentField.getText().trim().equals("")) {
                updatedWorkset.setComment(commentField.getText().trim());
            }
            updatedWorkset.setWorksheetsMap(home.getCurrentWorkset().getWorksheetsMap());
            updatedWorkset.setCreatedTime(home.getCurrentWorkset().getCreatedTime());
            updatedWorkset.setLastUpdatedTime(new Date(System.currentTimeMillis()));

            // Save the updated Workset.
            dao.mergeDocument(updatedWorkset);

            // Update temporary data in the WorksetHome.
            home.updateTemporaryData(home.getCurrentWorkset().getDocumentId(), null, null);

            // Reset the Workset panel.
            frame.setWorksetPanel(home.getCurrentWorkset());

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
    private final JTextArea documentTitleField = new JTextArea(2, 50);
    private final JTextArea authorField = new JTextArea(2, 50);
    private final JTextArea commentField = new JTextArea(10, 50);

    /**
     * Creates an instance of {@code EditWorksetDialog}.
     */
    public EditWorksetDialog() {
        super(MainPanel.get(), "Edit Workset Information", true);
        setIconImage(new ImageIcon("./resources/images/Workset.gif").getImage());
        MainPanel frame = MainPanel.get();
        WorksetHome home = WorksetHome.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel WorksetPanel = new JPanel();
        WorksetPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        WorksetPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        WorksetPanel.add(new JLabel("Workset Title *"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentTitleField.setLineWrap(true);
        documentTitleField.setText(home.getCurrentWorkset().getDocumentTitle());
        WorksetPanel.add(new JScrollPane(documentTitleField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        WorksetPanel.add(new JLabel("Authors"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 0, 5);
        authorField.setLineWrap(true);
        StringBuilder sb = new StringBuilder();
        List<String> authorsList = home.getCurrentWorkset().getAuthorsList();
        if (!authorsList.isEmpty()) {
            for (String author : authorsList) {
                sb.append(author);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        authorField.setText(sb.toString());
        WorksetPanel.add(new JScrollPane(authorField), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel authorSuggestionLabel = new JLabel("Separate authors by \",\".");
        authorSuggestionLabel.setForeground(Color.GRAY);
        WorksetPanel.add(authorSuggestionLabel, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        WorksetPanel.add(new JLabel("Comment"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        commentField.setLineWrap(true);
        commentField.setText(home.getCurrentWorkset().getComment());
        WorksetPanel.add(new JScrollPane(commentField), c);

        dialogPanel.add(WorksetPanel);

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

package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.dao.impl.WorksheetNoteDAO;
import notes.data.cache.Property;
import notes.entity.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.gui.main.verifier.IdInputVerifier;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Defines the dialog and event listener for editing a worksheet in a workset.
 *
 * @author Rui Du
 */
public class EditWorksheetDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (!worksheetIdField.getInputVerifier().verify(worksheetIdField)) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid worksheet ID!\n(A non-negative integer)",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                worksheetIdField.requestFocus();
                return;
            } else if (worksheetField.getText() == null || worksheetField.getText().trim().equals("")) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Worksheet title cannot be empty!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                worksheetField.requestFocus();
                return;
            } else if (worksheetField.getText().trim().split("\n").length > 1) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Worksheet title can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                worksheetField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            WorksetHome home = WorksetHome.get();
            WorksheetNoteDAO dao = home.getWorksheetNoteDAO();

            // Create instance of the updated worksheet.
            Worksheet updateWorksheet = new Worksheet();
            updateWorksheet.setWorksheetId(Long.parseLong(worksheetIdField.getText()));
            updateWorksheet.setWorksheetTitle(WordUtils.capitalize(worksheetField.getText().trim()));
            updateWorksheet.setNotesList(home.getCurrentWorksheet().getNotesList());

            // Save the updated worksheet.
            Worksheet cachedWorksheet = dao.mergeWorksheet(updateWorksheet, home.getCurrentWorkset().getDocumentId(),
                    home.getCurrentWorksheet().getWorksheetId());

            if (cachedWorksheet != null) {
                // Update temporary data in the WorksetHome.
                home.updateTemporaryData(home.getCurrentWorkset().getDocumentId(), null, null);

                // Update the worksheet and note panel.
                frame.updateIndexPanel();

                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playUpdate();
                }
                setVisible(false);
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null,
                        "Duplicate worksheet ID!",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }
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
    private final JTextArea documentField = new JTextArea(2, 50);
    private final JTextField worksheetIdField = new JTextField();
    private final JTextArea worksheetField = new JTextArea(2, 50);

    /**
     * Creates an instance of {@code EditWorksheetDialog}.
     */
    public EditWorksheetDialog() {
        super(MainPanel.get(), "Edit Worksheet", true);
        setIconImage(new ImageIcon("./resources/images/workset.gif").getImage());
        MainPanel frame = MainPanel.get();
        WorksetHome home = WorksetHome.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel worksheetPanel = new JPanel();
        worksheetPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        worksheetPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        worksheetPanel.add(new JLabel("Document *"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(home.getCurrentWorkset().getDocumentTitle());
        documentField.setEditable(false);
        worksheetPanel.add(new JScrollPane(documentField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        worksheetPanel.add(new JLabel("Worksheet ID *"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        worksheetIdField.setInputVerifier(new IdInputVerifier());
        worksheetIdField.setText(home.getCurrentWorksheet().getWorksheetId().toString());
        worksheetPanel.add(worksheetIdField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        worksheetPanel.add(new JLabel("Worksheet *"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        worksheetField.setLineWrap(true);
        worksheetField.setText(home.getCurrentWorksheet().getWorksheetTitle());
        worksheetPanel.add(new JScrollPane(worksheetField), c);

        dialogPanel.add(worksheetPanel);

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

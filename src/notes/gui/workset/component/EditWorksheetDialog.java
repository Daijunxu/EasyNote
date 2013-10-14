package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.dao.impl.WorksheetNoteDAO;
import notes.entity.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

/**
 * Defines the dialog and event listener for editing a worksheet in a workset.
 *
 * @author Rui Du
 */
public class EditWorksheetDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (worksheetTitleField.getText() == null || worksheetTitleField.getText().trim().equals("")) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Worksheet title cannot be empty!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                worksheetTitleField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            WorksetHome home = WorksetHome.get();
            WorksheetNoteDAO dao = home.getWorksheetNoteDAO();

            // Create instance of the updated worksheet.
            Worksheet updateWorksheet = new Worksheet();
            updateWorksheet.setWorksheetId(home.getCurrentWorksheet().getWorksheetId());
            updateWorksheet.setWorksheetTitle(WordUtils.capitalize(worksheetTitleField.getText().trim()));
            updateWorksheet.setNotesList(home.getCurrentWorksheet().getNotesList());
            updateWorksheet.setLastUpdatedTime(new Date());

            // Save the updated worksheet.
            Worksheet cachedWorksheet = dao.updateWorksheet(updateWorksheet, home.getCurrentWorkset().getDocumentId(),
                    home.getCurrentWorksheet().getWorksheetId());

            if (cachedWorksheet != null) {
                // Update the worksheet and note panel.
                if (home.getCurrentWorksheetNote() != null) {
                    frame.updateWorksetPanel(home.getCurrentWorkset().getDocumentId(), cachedWorksheet.getWorksheetId(),
                            home.getCurrentWorksheetNote().getNoteId());
                } else {
                    frame.updateWorksetPanel(home.getCurrentWorkset().getDocumentId(), cachedWorksheet.getWorksheetId(),
                            null);
                }

                SoundFactory.playUpdate();
                setVisible(false);
            } else {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Duplicate worksheet ID!",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        public void actionPerformed(ActionEvent e) {
            SoundFactory.playNavigation();
            setVisible(false);
        }
    });
    private final JTextField worksheetTitleField = new JTextField(50);

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

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        worksheetPanel.add(new JLabel("Title: "), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        worksheetTitleField.setText(home.getCurrentWorksheet().getWorksheetTitle());
        worksheetPanel.add(worksheetTitleField, c);

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

package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.dao.impl.WorksheetNoteDAO;
import notes.data.cache.Property;
import notes.entity.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;

/**
 * Defines the dialog and event listener for creating a worksheet.
 *
 * @author Rui Du
 */
public class NewWorksheetDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (worksheetField.getText() == null || worksheetField.getText().trim().equals("")) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Worksheet title cannot be empty!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                worksheetField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            WorksetHome home = WorksetHome.get();
            WorksheetNoteDAO dao = home.getWorksheetNoteDAO();

            // Create instance of the created worksheet.
            Worksheet createdWorksheet = new Worksheet();
            createdWorksheet.setWorksheetId(home.getCurrentWorkset().generateNewWorksheetId());
            createdWorksheet.setWorksheetTitle(WordUtils.capitalize(worksheetField.getText().trim()));
            createdWorksheet.setNotesList(new ArrayList<Long>());
            createdWorksheet.setCreatedTime(new Date());
            createdWorksheet.setLastUpdatedTime(new Date());

            // Save the created worksheet.
            Worksheet cachedWorksheet = dao.saveWorksheet(createdWorksheet, home.getCurrentWorkset()
                    .getDocumentId());
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
                JOptionPane.showMessageDialog(null, "Duplicate worksheet ID!",
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
    private final JTextField worksheetField = new JTextField(30);

    /**
     * Creates an instance of {@code NewWorksheetDialog}.
     */
    public NewWorksheetDialog() {
        super(MainPanel.get(), "Create Worksheet", true);
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
        worksheetPanel.add(new JLabel("Title:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        worksheetPanel.add(worksheetField, c);

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

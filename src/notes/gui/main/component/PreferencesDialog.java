/**
 *
 */
package notes.gui.main.component;

import notes.data.cache.Cache;
import notes.data.cache.Property;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines the dialog and event listener for editing system preferences.
 *
 * @author Rui Du
 * @version 1.0
 */
public class PreferencesDialog extends JDialog {

    private static final long serialVersionUID = 5443009634695952662L;

    private static PreferencesDialog instance;

    /**
     * Gets the instance of {@code PreferencesDialog}.
     *
     * @return {@code PreferencesDialog} The instance of {@code PreferencesDialog}.
     */
    public static PreferencesDialog get() {
        if (instance == null) {
            instance = new PreferencesDialog();
        }
        return instance;
    }

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        private static final long serialVersionUID = -1077277029101012739L;

        public void actionPerformed(ActionEvent e) {
            MainPanel frame = MainPanel.get();

            // Check setting for data location.
            if (!dataLocationField.getText().equals(Property.get().getDataLocation())) {
                // Save current cache data.
                Cache.get().saveAllCaches();
                // Change default data location.
                Property.get().setDataLocation(dataLocationField.getText());
                // Reload the cache data.
                Cache.get().reload();
                // Clear all temporary data.
                frame.clearAllTemporaryData();
                // Set up the default panel.
                frame.setDefaultPanel();
            }

            // Check setting for sound theme.
            Property.get().setSoundTheme(soundThemeField.getSelectedItem().toString());

            // Check setting for option of whether to open the last document when program starts.
            Property.get().setShowLastDocumentOnOpening(showLastDocumentOnOpeningCheckBox.isSelected());

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playUpdate();
            }

            setVisible(false);
        }
    });

    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        private static final long serialVersionUID = 4172730058717036113L;

        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });

    private final JTextField dataLocationField = new JTextField(40);
    private final JButton chooseDataLocationButton = new JButton("Choose");
    private final JComboBox soundThemeField = new JComboBox();
    private final JCheckBox showLastDocumentOnOpeningCheckBox = new JCheckBox("Open last viewed document when program starts");

    private PreferencesDialog() {
        super(MainPanel.get(), "Edit Preferences", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel preferencesPanel = new JPanel();
        preferencesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        preferencesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        preferencesPanel.add(new JLabel("Data Location:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        dataLocationField.setText(Property.get().getDataLocation());
        dataLocationField.setEditable(false);
        preferencesPanel.add(dataLocationField, c);

        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridwidth = 2;
        chooseDataLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playNavigation();
                }
                new ChooseDataLocationDialog();
            }
        });
        preferencesPanel.add(chooseDataLocationButton, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        preferencesPanel.add(new JLabel("Sound Theme:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        for (SoundTheme theme : SoundTheme.values()) {
            soundThemeField.addItem(theme.getDescription());
        }
        soundThemeField.setSelectedItem(Property.get().getSoundTheme());
        preferencesPanel.add(soundThemeField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 2;
        c.insets = new Insets(5, 5, 5, 5);
        showLastDocumentOnOpeningCheckBox.setSelected(Property.get().showLastDocumentOnOpening());
        preferencesPanel.add(showLastDocumentOnOpeningCheckBox, c);

        dialogPanel.add(preferencesPanel);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(okButton);
        buttons.add(cancelButton);

        dialogPanel.add(buttons);

        pack();
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
    }

    /**
     * Gets the data location field.
     *
     * @return {@code JTextField} The data location field.
     */
    public JTextField getDataLocationField() {
        return dataLocationField;
    }
}

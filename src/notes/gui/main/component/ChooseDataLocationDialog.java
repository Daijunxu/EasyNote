/**
 *
 */
package notes.gui.main.component;

import notes.data.cache.Property;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Defines the dialog and event listener for choosing data file location.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ChooseDataLocationDialog extends JDialog {

    private JFileChooser fileChooserField = new JFileChooser();

    /**
     * Constructs an instance of {@code ChooseDataLocationDialog}.
     */
    public ChooseDataLocationDialog() {
        super(PreferencesDialog.get(), "Choose Data File Location", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());

        final JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel fileChooserPanel = new JPanel();
        fileChooserPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        fileChooserPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        fileChooserField.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooserField.setCurrentDirectory(new File(Property.get().getDataLocation()));
        fileChooserField.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -4290129625901289936L;

            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = (JFileChooser) event.getSource();
                if (JFileChooser.APPROVE_SELECTION.equals(event.getActionCommand())) {
                    // Open was clicked.
                    File selectedFile = chooser.getSelectedFile();
                    if (selectedFile.isFile()) {
                        // Selected another data file.
                        PreferencesDialog.get().getDataLocationField()
                                .setText(selectedFile.getAbsolutePath());
                        if (!Property.get().getSoundTheme()
                                .equals(SoundTheme.NONE.getDescription())) {
                            SoundFactory.playNavigation();
                        }
                        setVisible(false);
                    } else {
                        // Selected a directory.
                        if (!Property.get().getSoundTheme()
                                .equals(SoundTheme.NONE.getDescription())) {
                            SoundFactory.playNotify();
                        }
                        int result = JOptionPane.showConfirmDialog(null,
                                "Create a data file under this folder?", "Confirm Dialog",
                                JOptionPane.YES_NO_OPTION);
                        // 0 for yes and 1 for no.
                        if (result == 0) {
                            try {
                                String path = selectedFile.getAbsolutePath()
                                        + "/reading_notes.data";
                                BufferedWriter output = new BufferedWriter(new FileWriter(path));
                                output.append("#DOCUMENTS\n#END_DOCUMENTS\n#TAGS\n#END_TAGS\n#NOTES\n#END_NOTES");
                                output.close();
                                PreferencesDialog.get().getDataLocationField().setText(path);
                                if (!Property.get().getSoundTheme()
                                        .equals(SoundTheme.NONE.getDescription())) {
                                    SoundFactory.playNavigation();
                                }
                                setVisible(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!Property.get().getSoundTheme()
                                    .equals(SoundTheme.NONE.getDescription())) {
                                SoundFactory.playNavigation();
                            }
                        }
                    }
                } else if (JFileChooser.CANCEL_SELECTION.equals(event.getActionCommand())) {
                    // Cancel was clicked.
                    if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                        SoundFactory.playNavigation();
                    }
                    setVisible(false);
                }
            }
        });
        fileChooserPanel.add(fileChooserField, c);

        dialogPanel.add(fileChooserPanel);

        pack();
        setLocationRelativeTo(PreferencesDialog.get());
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
        setVisible(true);
    }
}

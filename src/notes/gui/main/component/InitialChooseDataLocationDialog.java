package notes.gui.main.component;

import notes.data.cache.CacheDelegate;
import notes.data.persistence.Property;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Defines the dialog and event listener for choosing data file location.
 *
 * Author: Rui Du
 */
public class InitialChooseDataLocationDialog extends JDialog {

    private final JFileChooser fileChooserField = new JFileChooser();

    /**
     * Constructs an instance of {@code InitialChooseDataLocationDialog}.
     */
    public InitialChooseDataLocationDialog() {
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
        fileChooserField.setCurrentDirectory(new File(Property.get().getXmlDataLocation()));
        fileChooserField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = (JFileChooser) event.getSource();
                if (JFileChooser.APPROVE_SELECTION.equals(event.getActionCommand())) {
                    // Open was clicked.
                    File selectedFile = chooser.getSelectedFile();
                    if (selectedFile.isFile()) {
                        setVisible(false);
                        // Change default data location.
                        Property.get().setXmlDataLocation(selectedFile.getAbsolutePath());
                        CacheDelegate.hasProblem = false;
                        CacheDelegate.get().loadAllCachesFromXML();
                        if (CacheDelegate.hasProblem) {
                            SoundFactory.playNotify();
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Missing data file or having invalid data file. Choose a new data file?",
                                    "Confirm Dialog", JOptionPane.YES_NO_OPTION);
                            // 0 for yes and 1 for no.
                            if (result == 0) {
                                SoundFactory.playPopup();
                                new InitialChooseDataLocationDialog();
                            } else {
                                System.exit(0);
                            }
                        } else {
                            SoundFactory.playOn();
                            MainPanel.get().setVisible(true);
                            MainPanel.get().setLocation(MainPanel.get().getLocationOnScreen());
                        }
                    } else {
                        // Selected a directory.
                        SoundFactory.playNotify();
                        int result = JOptionPane.showConfirmDialog(null,
                                "Create a data file under this folder?", "Confirm Dialog",
                                JOptionPane.YES_NO_OPTION);
                        // 0 for yes and 1 for no.
                        if (result == 0) {
                            try {
                                String path = selectedFile.getAbsolutePath()
                                        + "/reading_notes.xml";
                                Writer output = new BufferedWriter(new FileWriter(path));
                                output.append(CacheDelegate.getDefaultContentToWrite());
                                output.close();

                                Property.get().setXmlDataLocation(path);
                                CacheDelegate.hasProblem = false;
                                CacheDelegate.get().loadAllCachesFromXML();

                                SoundFactory.playOn();

                                MainPanel.get().setVisible(true);
                                MainPanel.get().setLocation(MainPanel.get().getLocationOnScreen());

                                setVisible(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (JFileChooser.CANCEL_SELECTION.equals(event.getActionCommand())) {
                    // Cancel was clicked.
                    setVisible(false);
                    System.exit(0);
                }
            }
        });
        fileChooserPanel.add(fileChooserField, c);

        dialogPanel.add(fileChooserPanel);

        pack();
        setLocationRelativeTo(null);
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
        setModal(true);
        setVisible(true);
    }
}

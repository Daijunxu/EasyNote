package notes.gui.book.component;

import notes.businesslogic.BookBusinessLogic;
import notes.data.persistence.Property;
import notes.businessobjects.book.Book;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.PreferencesDialog;
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
 * Defines the dialog and event listener for exporting current book.
 *
 * Author: Rui Du
 */
public class ExportBookDialog extends JDialog {

    /**
     * Creates an instance of {@code ExportBookDialog}.
     */
    public ExportBookDialog() {
        super(MainPanel.get(), "Choose Location to Export Current Book", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());

        final Book book = BookBusinessLogic.get().getCurrentBook();

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

        JFileChooser fileChooserField = new JFileChooser();
        fileChooserField.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooserField.setCurrentDirectory(new File(Property.get().getXmlDataLocation()));
        fileChooserField.setSelectedFile(new File(Property.get().getXmlDataLocation() + "/[Notes] "
                + book.getDocumentTitle() + ".html"));
        fileChooserField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = (JFileChooser) event.getSource();
                if (JFileChooser.APPROVE_SELECTION.equals(event.getActionCommand())) {
                    // Open was clicked.
                    File selectedFile = chooser.getSelectedFile();
                    if (selectedFile.isDirectory()) {
                        // Selected file is a directory.
                        int result = JOptionPane.showConfirmDialog(null,
                                "Export the current document under this folder?", "Confirm Dialog",
                                JOptionPane.YES_NO_OPTION);
                        // 0 for yes and 1 for no.
                        if (result == 0) {
                            try {
                                File newFile = new File(selectedFile.getAbsolutePath() + "/[Notes]"
                                        + book.getDocumentTitle() + ".html");
                                if (newFile.exists()) {
                                    SoundFactory.playError();
                                    JOptionPane.showMessageDialog(null, "File already exists!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                Writer output = new BufferedWriter(new FileWriter(newFile));
                                book.export(output);
                                output.close();
                                SoundFactory.playExport();
                                setVisible(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // Selected file is a file.
                        if (selectedFile.exists()) {
                            SoundFactory.playError();
                            JOptionPane.showMessageDialog(null, "File already exists!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        try {
                            Writer output = new BufferedWriter(new FileWriter(selectedFile));
                            book.export(output);
                            output.close();
                            SoundFactory.playExport();
                            setVisible(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (JFileChooser.CANCEL_SELECTION.equals(event.getActionCommand())) {
                    // Cancel was clicked.
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

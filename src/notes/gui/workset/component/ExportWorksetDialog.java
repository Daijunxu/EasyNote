package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.entity.workset.Workset;
import notes.entity.workset.WorksheetNote;
import notes.entity.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.PreferencesDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Defines the dialog and event listener for exporting current workset.
 *
 * @author Rui Du
 */
public class ExportWorksetDialog extends JDialog {

    /**
     * Creates an instance of {@code ExportWorksetDialog}.
     */
    public ExportWorksetDialog() {
        super(MainPanel.get(), "Choose Location to Export Current Workset", true);
        setIconImage(new ImageIcon("./resources/images/workset.gif").getImage());

        final Workset workset = WorksetHome.get().getCurrentWorkset();

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
                + workset.getDocumentTitle() + ".html"));
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
                                        + workset.getDocumentTitle() + ".html");
                                if (newFile.exists()) {
                                    if (!Property.get().getSoundTheme()
                                            .equals(SoundTheme.NONE.getDescription())) {
                                        SoundFactory.playError();
                                    }
                                    JOptionPane.showMessageDialog(null, "File already exists!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                BufferedWriter output = new BufferedWriter(new FileWriter(newFile));
                                exportWorkset(workset, output);
                                output.close();
                                if (!Property.get().getSoundTheme()
                                        .equals(SoundTheme.NONE.getDescription())) {
                                    SoundFactory.playExport();
                                }
                                setVisible(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // Selected file is a file.
                        if (selectedFile.exists()) {
                            if (!Property.get().getSoundTheme()
                                    .equals(SoundTheme.NONE.getDescription())) {
                                SoundFactory.playError();
                            }
                            JOptionPane.showMessageDialog(null, "File already exists!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        try {
                            BufferedWriter output = new BufferedWriter(new FileWriter(selectedFile));
                            exportWorkset(workset, output);
                            output.close();
                            if (!Property.get().getSoundTheme()
                                    .equals(SoundTheme.NONE.getDescription())) {
                                SoundFactory.playExport();
                            }
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

    private void exportWorkset(Workset workset, BufferedWriter output) {
        WorksetHome home = WorksetHome.get();
        try {
            output.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF8'><title>");
            output.append(workset.getDocumentTitle());
            output.append("</title></head>");
            output.append("<body><a name='title'/><h2>").append(workset.getDocumentTitle()).append("</h2>");

            // Output document ID.
            output.append("<b>Document ID: </b>").append(workset.getDocumentId().toString()).append("<br>");

            // Output authors.
            if (!workset.getAuthorsList().isEmpty()) {
                output.append("<b>Author(s): </b>");
                StringBuilder authorsBuilder = new StringBuilder();
                for (String author : workset.getAuthorsList()) {
                    authorsBuilder.append(author).append(", ");
                }
                authorsBuilder.delete(authorsBuilder.length() - 2, authorsBuilder.length());
                output.append(authorsBuilder);
                output.append("<br>");
            }

            // Output created time.
            output.append("<b>Created Time: </b>").append(workset.getCreatedTime().toString()).append("<br>");

            // Output last updated time.
            output.append("<b>Last Updated Time: </b>").append(workset.getLastUpdatedTime().toString()).append("<br>");

            // Output number of notes.
            output.append("<b>Number of Notes: </b>").append(String.valueOf(workset.getNotesCount())).append("<br>");

            // Output comment.
            output.append("<b>Comment: </b><br>");
            output.append(workset.getComment());
            output.append("<br>");

            output.append("<br>");

            // Output anchors for worksheets.
            for (Long worksheetId : workset.getWorksheetsMap().keySet()) {
                output.append("<a href='#worksheet").append(String.valueOf(worksheetId)).append("'><b>Worksheet ")
                        .append(String.valueOf(worksheetId)).append(": ");
                output.append(workset.getWorksheetsMap().get(worksheetId).getWorksheetTitle());
                output.append("</b></a><br>");
            }

            output.append("<br><hr>");

            // Output notes for each worksheet.
            for (Long worksheetId : workset.getWorksheetsMap().keySet()) {
                // Output worksheet title.
                Worksheet worksheet = workset.getWorksheetsMap().get(worksheetId);
                output.append("<a name='worksheet").append(String.valueOf(worksheetId)).append("'/><h3>Worksheet ")
                        .append(String.valueOf(worksheetId)).append(": ");
                output.append(worksheet.getWorksheetTitle());
                output.append("</h3>");

                // Output anchor to title.
                output.append("<a href='#title'>Back to Top</a>");

                // Output all notes in the worksheet.
                List<WorksheetNote> noteList = home.getAllNotesForCurrentWorksheet();
                for (WorksheetNote note : noteList) {
                    output.append("<p>");

                    // Output note text.
                    String noteText = note.getNoteText();
                    noteText = noteText.replaceAll("&", "&amp;");
                    noteText = noteText.replaceAll("<", "&lt;");
                    noteText = noteText.replaceAll("\n", "<br>");
                    noteText = noteText.replaceAll("  ", "&emsp;");
                    noteText = noteText.replaceAll("\t", "&emsp;&emsp;");
                    output.append(noteText);
                    output.append("<br><i>");

                    // Output tags.
                    if (!note.getTagIds().isEmpty()) {
                        for (Long tagId : note.getTagIds()) {
                            output.append("[").append(home.getWorksheetNoteDAO().findTagById(tagId).getTagText())
                                    .append("] ");
                        }
                    }

                    // Output note ID.
                    output.append("ID: ").append(note.getNoteId().toString());

                    output.append("</i><br></p>");
                }
                output.append("<hr>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

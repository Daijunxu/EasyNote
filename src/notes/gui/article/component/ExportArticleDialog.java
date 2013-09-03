/**
 *
 */
package notes.gui.article.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import notes.article.entity.Article;
import notes.article.entity.ArticleNote;
import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.PreferencesDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Defines the dialog and event listener for exporting current article.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ExportArticleDialog extends JDialog {

    private static final long serialVersionUID = 3138411222044812952L;

    private JFileChooser fileChooserField = new JFileChooser();

    /**
     * Creates an instance of {@code ExportArticleDialog}.
     */
    public ExportArticleDialog() {
        super(MainPanel.get(), "Choose Location to Export Current Article", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());

        final Article article = ArticleHome.get().getCurrentArticle();

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
        fileChooserField.setSelectedFile(new File(Property.get().getDataLocation() + "/[Notes] "
                + article.getDocumentTitle() + ".html"));
        fileChooserField.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -3689467804538872221L;

            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = (JFileChooser) event.getSource();
                if (JFileChooser.APPROVE_SELECTION.equals(event.getActionCommand())) {
                    // Open was clicked.
                    File selectedFile = chooser.getSelectedFile();
                    if (selectedFile.isDirectory()) {
                        // Selected file is a directory.
                        if (!Property.get().getSoundTheme()
                                .equals(SoundTheme.NONE.getDescription())) {
                            SoundFactory.playNotify();
                        }
                        int result = JOptionPane.showConfirmDialog(null,
                                "Export the current document under this folder?", "Confirm Dialog",
                                JOptionPane.YES_NO_OPTION);
                        // 0 for yes and 1 for no.
                        if (result == 0) {
                            try {
                                File newFile = new File(selectedFile.getAbsolutePath() + "/[Notes]"
                                        + article.getDocumentTitle() + ".html");
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
                                exportArticle(article, output);
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
                            exportArticle(article, output);
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

    private void exportArticle(Article article, BufferedWriter output) {
        ArticleHome home = ArticleHome.get();
        try {
            output.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF8'><title>");
            output.append(article.getDocumentTitle());
            output.append("</title></head>");
            output.append("<body><a name='title'/><h2>" + article.getDocumentTitle() + "</h2>");

            // Output document ID.
            output.append("<b>Document ID: </b>" + article.getDocumentId() + "<br>");

            // Output authors.
            if (article.getAuthorsList().isEmpty() == false) {
                output.append("<b>Author(s): </b>");
                StringBuilder authorsBuilder = new StringBuilder();
                for (String author : article.getAuthorsList()) {
                    authorsBuilder.append(author + ", ");
                }
                authorsBuilder.delete(authorsBuilder.length() - 2, authorsBuilder.length());
                output.append(authorsBuilder);
                output.append("<br>");
            }

            // Output source.
            if (article.getSource() != null && article.getSource().equals("") == false) {
                output.append("<b>Source: </b>");
                output.append(article.getSource());
                output.append("<br>");
            }

            // Output created time.
            output.append("<b>Created Time: </b>" + article.getCreatedTime() + "<br>");

            // Output last updated time.
            output.append("<b>Last Updated Time: </b>" + article.getLastUpdatedTime() + "<br>");

            // Output number of notes.
            output.append("<b>Number of Notes: </b>" + article.getNotesCount() + "<br>");

            // Output comment.
            output.append("<b>Comment: </b><br>");
            output.append(article.getComment());
            output.append("<br>");

            output.append("<br><hr>");

            // Output all notes in the chapter.
            for (ArticleNote note : home.getCurrentArticleNotesList()) {
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
                if (note.getTagIds().isEmpty() == false) {
                    for (Long tagId : note.getTagIds()) {
                        output.append("["
                                + home.getArticleNoteDAO().findTagById(tagId).getTagText() + "] ");
                    }
                }

                // Output note ID.
                output.append("ID: " + note.getNoteId());

                output.append("</i><br></p>");
            }
            output.append("<hr>");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

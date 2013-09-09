/**
 *
 */
package notes.gui.book.component;

import notes.bean.BookHome;
import notes.book.Book;
import notes.book.BookNote;
import notes.book.Chapter;
import notes.data.cache.Property;
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
 * Defines the dialog and event listener for exporting current book.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ExportBookDialog extends JDialog {

    private static final long serialVersionUID = -232028710742317165L;

    /**
     * Creates an instance of {@code ExportBookDialog}.
     */
    public ExportBookDialog() {
        super(MainPanel.get(), "Choose Location to Export Current Book", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());

        final Book book = BookHome.get().getCurrentBook();

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
        fileChooserField.setCurrentDirectory(new File(Property.get().getDataLocation()));
        fileChooserField.setSelectedFile(new File(Property.get().getDataLocation() + "/[Notes] "
                + book.getDocumentTitle() + ".html"));
        fileChooserField.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 2458473741002905434L;

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
                                    if (!Property.get().getSoundTheme()
                                            .equals(SoundTheme.NONE.getDescription())) {
                                        SoundFactory.playError();
                                    }
                                    JOptionPane.showMessageDialog(null, "File already exists!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                BufferedWriter output = new BufferedWriter(new FileWriter(newFile));
                                exportBook(book, output);
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
                            exportBook(book, output);
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

    private void exportBook(Book book, BufferedWriter output) {
        BookHome home = BookHome.get();
        try {
            output.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF8'><title>");
            output.append(book.getDocumentTitle());
            output.append("</title></head>");
            output.append("<body><a name='title'/><h2>").append(book.getDocumentTitle()).append("</h2>");

            // Output document ID.
            output.append("<b>Document ID: </b>").append(book.getDocumentId().toString()).append("<br>");

            // Output authors.
            if (!book.getAuthorsList().isEmpty()) {
                output.append("<b>Author(s): </b>");
                StringBuilder authorsBuilder = new StringBuilder();
                for (String author : book.getAuthorsList()) {
                    authorsBuilder.append(author).append(", ");
                }
                authorsBuilder.delete(authorsBuilder.length() - 2, authorsBuilder.length());
                output.append(authorsBuilder);
                output.append("<br>");
            }

            // Output edition.
            if (book.getEdition() != null) {
                output.append("<b>Edition: </b>").append(book.getEdition().toString()).append("<br>");
            }

            // Output published year.
            if (book.getPublishedYear() != null) {
                output.append("<b>Published Year: </b>").append(book.getPublishedYear().toString()).append("<br>");
            }

            // Output ISBN.
            if (book.getIsbn() != null && !book.getIsbn().equals("")) {
                output.append("<b>ISBN: </b>").append(book.getIsbn()).append("<br>");
            }

            // Output created time.
            output.append("<b>Created Time: </b>").append(book.getCreatedTime().toString()).append("<br>");

            // Output last updated time.
            output.append("<b>Last Updated Time: </b>").append(book.getLastUpdatedTime().toString()).append("<br>");

            // Output number of notes.
            output.append("<b>Number of Notes: </b>").append(String.valueOf(book.getNotesCount())).append("<br>");

            // Output comment.
            output.append("<b>Comment: </b><br>");
            output.append(book.getComment());
            output.append("<br>");

            output.append("<br>");

            // Output anchors for chapters.
            for (Long chapterId : book.getChaptersMap().keySet()) {
                output.append("<a href='#chapter").append(String.valueOf(chapterId)).append("'><b>Chapter ")
                        .append(String.valueOf(chapterId)).append(": ");
                output.append(book.getChaptersMap().get(chapterId).getChapterTitle());
                output.append("</b></a><br>");
            }

            output.append("<br><hr>");

            // Output notes for each chapter.
            for (Long chapterId : book.getChaptersMap().keySet()) {
                // Output chapter title.
                Chapter chapter = book.getChaptersMap().get(chapterId);
                output.append("<a name='chapter").append(String.valueOf(chapterId)).append("'/><h3>Chapter ")
                        .append(String.valueOf(chapterId)).append(": ");
                output.append(chapter.getChapterTitle());
                output.append("</h3>");

                // Output anchor to title.
                output.append("<a href='#title'>Back to Top</a>");

                // Output all notes in the chapter.
                List<BookNote> noteList = home.getCurrentChapterNotesMap().get(chapterId);
                for (BookNote note : noteList) {
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
                            output.append("[").append(home.getBookNoteDAO().findTagById(tagId).getTagText())
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

package notes.gui.book.component;

import notes.bean.BookHome;
import notes.dao.impl.BookNoteDAO;
import notes.data.cache.Property;
import notes.entity.book.Book;
import notes.gui.book.verifier.EditionInputVerifier;
import notes.gui.book.verifier.ISBNInputVerifier;
import notes.gui.book.verifier.PublishedYearInputVerifier;
import notes.gui.main.component.MainPanel;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

/**
 * Defines the dialog and event listener for editing a book.
 *
 * @author Rui Du
 */
public class EditBookDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (documentTitleField.getText() == null
                    || documentTitleField.getText().trim().equals("")) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Document title cannot be empty!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
                return;
            } else if (documentTitleField.getText().trim().split("\n").length > 1) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Document title can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
                return;
            } else if (authorField.getText() != null
                    && !authorField.getText().trim().equals("")
                    && authorField.getText().trim().split("\n").length > 1) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Author list can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                authorField.requestFocus();
                return;
            } else if (!editionField.getInputVerifier().verify(editionField)) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid edition number!\n(A non-negative integer)",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                editionField.requestFocus();
                return;
            } else if (!publishedYearField.getInputVerifier().verify(publishedYearField)) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid published year!\n(1800 - 2100)", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                publishedYearField.requestFocus();
                return;
            } else if (!ISBNField.getInputVerifier().verify(ISBNField)) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Please enter a valid ISBN!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                ISBNField.requestFocus();
                return;
            } else if (commentField.getText().trim().split("\n").length > 1) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Comment can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                commentField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            BookHome home = BookHome.get();
            BookNoteDAO dao = home.getBookNoteDAO();

            // Create instance of the updated book.
            Book updatedBook = new Book();
            updatedBook.setDocumentId(home.getCurrentBook().getDocumentId());
            updatedBook.setDocumentTitle(WordUtils.capitalize(documentTitleField.getText().trim()));
            updatedBook.setAuthorsList(EntityHelper.buildAuthorsStrList(authorField
                    .getText()));
            if (commentField.getText() != null && !commentField.getText().trim().equals("")) {
                updatedBook.setComment(commentField.getText().trim());
            }
            if (editionField.getText() != null && !editionField.getText().equals("")) {
                updatedBook.setEdition(Integer.parseInt(editionField.getText()));
            }
            if (publishedYearField.getText() != null
                    && !publishedYearField.getText().equals("")) {
                updatedBook.setPublishedYear(Integer.parseInt(publishedYearField.getText()));
            }
            if (ISBNField.getText() != null && !ISBNField.getText().equals("")) {
                updatedBook.setIsbn(ISBNField.getText());
            }
            updatedBook.setChaptersMap(home.getCurrentBook().getChaptersMap());
            updatedBook.setCreatedTime(home.getCurrentBook().getCreatedTime());
            updatedBook.setLastUpdatedTime(new Date(System.currentTimeMillis()));

            // Save the updated book.
            dao.updateDocument(updatedBook);

            // Update temporary data in the BookHome.
            home.updateTemporaryData(home.getCurrentBook().getDocumentId(), null, null);

            // Reset the book panel.
            frame.setBookPanel(home.getCurrentBook());

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playUpdate();
            }

            setVisible(false);
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
    private final JLabel documentIdField = new JLabel();
    private final JTextArea documentTitleField = new JTextArea(2, 50);
    private final JTextArea authorField = new JTextArea(2, 50);
    private final JTextField editionField = new JTextField();
    private final JTextField publishedYearField = new JTextField();
    private final JTextField ISBNField = new JTextField();
    private final JTextArea commentField = new JTextArea(10, 50);
    private final JLabel createdTimeField = new JLabel();
    private final JLabel lastUpdatedTimeField = new JLabel();

    /**
     * Creates an instance of {@code EditBookDialog}.
     */
    public EditBookDialog() {
        super(MainPanel.get(), "Book Information", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();
        BookHome home = BookHome.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        bookPanel.add(new JLabel("Document ID:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentIdField.setText(home.getCurrentBook().getDocumentId().toString());
        bookPanel.add(documentIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        bookPanel.add(new JLabel("Title:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentTitleField.setLineWrap(true);
        documentTitleField.setText(home.getCurrentBook().getDocumentTitle());
        bookPanel.add(new JScrollPane(documentTitleField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Authors:"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 0, 5);
        authorField.setLineWrap(true);
        StringBuilder sb = new StringBuilder();
        List<String> authorsList = home.getCurrentBook().getAuthorsList();
        if (!authorsList.isEmpty()) {
            for (String author : authorsList) {
                sb.append(author);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        authorField.setText(sb.toString());
        bookPanel.add(new JScrollPane(authorField), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel authorSuggestionLabel = new JLabel("Separate authors by \",\".");
        authorSuggestionLabel.setForeground(Color.GRAY);
        bookPanel.add(authorSuggestionLabel, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Edition:"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        editionField.setInputVerifier(new EditionInputVerifier());
        if (home.getCurrentBook().getEdition() != null) {
            editionField.setText(home.getCurrentBook().getEdition().toString());
        }
        bookPanel.add(editionField, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Published Year:"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        publishedYearField.setInputVerifier(new PublishedYearInputVerifier());
        if (home.getCurrentBook().getPublishedYear() != null) {
            publishedYearField.setText(home.getCurrentBook().getPublishedYear().toString());
        }
        bookPanel.add(publishedYearField, c);

        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("ISBN:"), c);

        c.gridx = 1;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 0, 5);
        ISBNField.setInputVerifier(new ISBNInputVerifier());
        ISBNField.setText(home.getCurrentBook().getIsbn());
        bookPanel.add(ISBNField, c);

        c.gridx = 1;
        c.gridy = 7;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel ISBNSuggestionLabel = new JLabel("Either 10 digits or 13 digits.");
        ISBNSuggestionLabel.setForeground(Color.GRAY);
        bookPanel.add(ISBNSuggestionLabel, c);

        c.gridx = 0;
        c.gridy = 8;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Comment:"), c);

        c.gridx = 1;
        c.gridy = 8;
        c.insets = new Insets(5, 5, 5, 5);
        commentField.setLineWrap(true);
        commentField.setText(home.getCurrentBook().getComment());
        bookPanel.add(new JScrollPane(commentField), c);

        c.gridx = 0;
        c.gridy = 9;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 9;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(home.getCurrentBook().getCreatedTime().toString());
        bookPanel.add(createdTimeField, c);

        c.gridx = 0;
        c.gridy = 10;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Last Updated Time:"), c);

        c.gridx = 1;
        c.gridy = 10;
        c.insets = new Insets(5, 5, 5, 5);
        lastUpdatedTimeField.setText(home.getCurrentBook().getLastUpdatedTime().toString());
        bookPanel.add(lastUpdatedTimeField, c);

        dialogPanel.add(bookPanel);

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

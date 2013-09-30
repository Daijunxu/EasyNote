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
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Defines the dialog and event listener for viewing a book note.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewBookNoteDialog extends JDialog {

    private final Book selectedBook;
    private final Chapter selectedChapter;
    private final BookNote selectedNote;

    private final JButton editButton = new JButton(new AbstractAction("Edit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            // Make the current view book note dialog disappear.
            setVisible(false);

            // Show edit book note dialog.
            new EditBookNoteDialog(selectedBook, selectedChapter, selectedNote);
        }
    });
    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });
    private final JLabel noteIdField = new JLabel();
    private final JTextArea documentField = new JTextArea(2, 50);
    private final JTextArea chapterField = new JTextArea(2, 50);
    private final JTextArea tagsField = new JTextArea(2, 50);
    private final JTextArea noteTextField = new JTextArea(10, 50);
    private final JLabel createdTimeField = new JLabel();

    /**
     * Creates an instance of {@code ViewBookNoteDialog}.
     */
    public ViewBookNoteDialog(Book selectedBook, Chapter selectedChapter, BookNote selectedNote) {
        super(MainPanel.get(), "View Book Note", true);

        this.selectedBook = selectedBook;
        this.selectedChapter = selectedChapter;
        this.selectedNote = selectedNote;

        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();
        BookHome home = BookHome.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel notePanel = new JPanel();
        notePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        notePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        notePanel.add(new JLabel("Note ID"), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        noteIdField.setText(selectedNote.getNoteId().toString());
        notePanel.add(noteIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Document"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(selectedBook.getDocumentTitle());
        documentField.setEditable(false);
        notePanel.add(new JScrollPane(documentField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Chapter"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        chapterField.setLineWrap(true);
        chapterField.setText(selectedChapter.getChapterId() + ". "
                + selectedChapter.getChapterTitle());
        chapterField.setEditable(false);
        notePanel.add(new JScrollPane(chapterField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        StringBuilder tagStrBuilder = new StringBuilder();
        if (!selectedNote.getTagIds().isEmpty()) {
            for (Long tagId : selectedNote.getTagIds()) {
                tagStrBuilder.append(home.getBookNoteDAO().findTagById(tagId).getTagText()).append(", ");
            }
            tagStrBuilder.delete(tagStrBuilder.length() - 2, tagStrBuilder.length());
        }
        tagsField.setLineWrap(true);
        tagsField.setText(tagStrBuilder.toString());
        tagsField.setEditable(false);
        notePanel.add(new JScrollPane(tagsField), c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        noteTextField.setLineWrap(true);
        noteTextField.setText(selectedNote.getNoteText());
        noteTextField.setEditable(false);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(selectedNote.getCreatedTime().toString());
        notePanel.add(createdTimeField, c);

        dialogPanel.add(notePanel);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(editButton);
        buttons.add(okButton);

        dialogPanel.add(buttons);

        pack();
        setLocationRelativeTo(frame);
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
        setVisible(true);
    }
}

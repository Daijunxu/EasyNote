package notes.gui.book.component;

import notes.bean.BookHome;
import notes.dao.impl.BookNoteDAO;
import notes.data.cache.Property;
import notes.entity.Tag;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;
import notes.gui.main.component.MainPanel;
import notes.gui.main.component.SearchNoteDialog;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the dialog and event listener for editing a book note.
 *
 * @author Rui Du
 */
public class EditBookNoteDialog extends JDialog {
    private final BookNote selectedNote;
    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (tagsField.getText() != null && !tagsField.getText().trim().equals("")
                    && tagsField.getText().trim().split("\n").length > 1) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Tag list can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                tagsField.requestFocus();
                return;
            }
            List<String> tagsStrList = EntityHelper.buildTagsStrList(tagsField.getText());
            if (tagsStrList.size() > 5) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "A note can have at most 5 tags!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                tagsField.requestFocus();
                return;
            }
            for (String tagStr : tagsStrList) {
                if (tagStr.length() > 30) {
                    if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                        SoundFactory.playError();
                    }
                    JOptionPane.showMessageDialog(null, "A tag can have at most 30 characters!",
                            "Input error", JOptionPane.ERROR_MESSAGE);
                    tagsField.requestFocus();
                    return;
                }
            }
            if (noteTextField.getText() == null || noteTextField.getText().trim().equals("")) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Note text cannot be empty!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                noteTextField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            BookHome home = BookHome.get();
            BookNoteDAO dao = home.getBookNoteDAO();
            BookNote newNote = new BookNote();
            newNote.setNoteId(selectedNote.getNoteId());
            newNote.setDocumentId(selectedNote.getDocumentId());

            String chapterStr = (String) chapterField.getSelectedItem();
            Long selectedChapterId = Long.parseLong(chapterStr.substring(0, chapterStr.indexOf(".")));
            newNote.setChapterId(selectedChapterId);

            List<Long> updatedTagsList = new ArrayList<Long>();
            for (String tagStr : tagsStrList) {
                // Set the new tag IDs, save tags if they are new.
                Tag cachedTag = dao.findTagByText(tagStr);
                if (cachedTag != null) {
                    updatedTagsList.add(cachedTag.getTagId());
                } else {
                    Tag newTag = new Tag();
                    newTag.setTagText(tagStr);
                    Tag savedTag = dao.saveTag(newTag);
                    updatedTagsList.add(savedTag.getTagId());
                }
            }
            newNote.setTagIds(updatedTagsList);
            newNote.setNoteText(noteTextField.getText());

            // Save the updated book note.
            BookNote updatedNote = (BookNote) dao.updateNote(newNote);

            // Update current note.
            home.setCurrentBookNote(updatedNote);

            // Update the note panel.
            if (frame.isSearchMode()) {
                SearchNoteDialog.get().updateResultPanel();
            } else {
                frame.updateBookNotePanel(home.getCurrentChapter(), updatedNote.getNoteId());
            }

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
    private final JLabel noteIdField = new JLabel();
    private final JComboBox chapterField = new JComboBox();
    private final JTextArea tagsField = new JTextArea(2, 50);
    private final JTextArea noteTextField = new JTextArea(20, 50);
    private final JLabel createdTimeField = new JLabel();

    /**
     * Creates an instance of {@code EditBookNoteDialog}.
     */
    public EditBookNoteDialog(Book selectedBook, Chapter selectedChapter, BookNote selectedNote) {
        super(MainPanel.get(), selectedBook.getDocumentTitle(), true);

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
        notePanel.add(new JLabel("Note ID:"), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        noteIdField.setText(selectedNote.getNoteId().toString());
        notePanel.add(noteIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Chapter:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        int selected = -1;
        int counter = -1;
        for (Long chapterId : selectedBook.getChaptersMap().keySet()) {
            counter++;
            if (selectedChapter.getChapterId().equals(chapterId)) {
                selected = counter;
            }
            Chapter chapter = selectedBook.getChaptersMap().get(chapterId);
            chapterField.addItem(chapterId + ". " + chapter.getChapterTitle());
        }
        chapterField.setSelectedIndex(selected);
        notePanel.add(chapterField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text:"), c);

        noteTextField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        noteTextField.setText(selectedNote.getNoteText());
        noteTextField.select(0, 0);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 0, 5);
        StringBuilder tagStrBuilder = new StringBuilder();
        if (!selectedNote.getTagIds().isEmpty()) {
            for (Long tagId : selectedNote.getTagIds()) {
                tagStrBuilder.append(home.getBookNoteDAO().findTagById(tagId).getTagText()).append(", ");
            }
            tagStrBuilder.delete(tagStrBuilder.length() - 2, tagStrBuilder.length());
        }
        tagsField.setLineWrap(true);
        tagsField.setText(tagStrBuilder.toString());
        tagsField.select(0, 0);
        notePanel.add(new JScrollPane(tagsField), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel suggestionLabel = new JLabel(
                "Use capitalized words and separate tags by \",\". E.g. \"Design Pattern, Algorithm\"");
        suggestionLabel.setForeground(Color.GRAY);
        notePanel.add(suggestionLabel, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(selectedNote.getCreatedTime().toString());
        notePanel.add(createdTimeField, c);

        dialogPanel.add(notePanel);

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
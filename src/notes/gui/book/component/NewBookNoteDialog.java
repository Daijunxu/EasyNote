package notes.gui.book.component;

import notes.bean.BookHome;
import notes.dao.impl.BookNoteDAO;
import notes.entity.Tag;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;
import notes.gui.main.component.MainPanel;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import notes.utils.TextHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the dialog and event listener for creating a book note.
 *
 * @author Rui Du
 */
public class NewBookNoteDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (tagsField.getText() != null && !tagsField.getText().trim().equals("")
                    && tagsField.getText().trim().split("\n").length > 1) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Tag list can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                tagsField.requestFocus();
                return;
            }
            List<String> tagsStrList = EntityHelper.buildTagsStrList(tagsField.getText());
            if (tagsStrList.size() > 5) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "A note can have at most 5 tags!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                tagsField.requestFocus();
                return;
            }
            for (String tagStr : tagsStrList) {
                if (tagStr.length() > 30) {
                    SoundFactory.playError();
                    JOptionPane.showMessageDialog(null, "A tag can have at most 30 characters!",
                            "Input error", JOptionPane.ERROR_MESSAGE);
                    tagsField.requestFocus();
                    return;
                }
            }
            if (noteTextField.getText() == null || noteTextField.getText().trim().equals("")) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Note text cannot be empty!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                noteTextField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            BookHome home = BookHome.get();
            BookNoteDAO dao = home.getBookNoteDAO();

            // Create instance of the created book note.
            BookNote createdBookNote = new BookNote();
            createdBookNote.setDocumentId(home.getCurrentBook().getDocumentId());
            String chapterStr = (String) chapterField.getSelectedItem();
            Long selectedChapterId = Long.parseLong(chapterStr.substring(0, chapterStr.indexOf(".")));
            createdBookNote.setChapterId(selectedChapterId);
            List<Long> tagsList = new ArrayList<Long>();
            for (String tagStr : tagsStrList) {
                // Set the new tag IDs, save tags if they are new.
                Tag cachedTag = dao.findTagByText(tagStr);
                if (cachedTag != null) {
                    tagsList.add(cachedTag.getTagId());
                } else {
                    Tag newTag = new Tag();
                    newTag.setTagText(tagStr);
                    Tag savedTag = dao.saveTag(newTag);
                    tagsList.add(savedTag.getTagId());
                }
            }
            createdBookNote.setTagIds(tagsList);
            createdBookNote.setNoteText(TextHelper.processInputText(noteTextField.getText()));

            // Save the created book note.
            BookNote cachedBookNote = (BookNote) (dao.saveNote(createdBookNote));

            // Update the note panel.
            frame.updateBookNotePanel(home.getCurrentChapter(), cachedBookNote.getNoteId());

            SoundFactory.playUpdate();

            setVisible(false);
        }
    });
    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        public void actionPerformed(ActionEvent e) {
            SoundFactory.playNavigation();
            setVisible(false);
        }
    });
    private final JTextArea documentField = new JTextArea(2, 50);
    private final JComboBox chapterField = new JComboBox();
    private final JTextArea tagsField = new JTextArea(2, 50);
    private final JTextArea noteTextField = new JTextArea(20, 50);

    /**
     * Creates an instance of {@code NewBookNoteDialog}.
     */
    public NewBookNoteDialog() {
        super(MainPanel.get(), "Create Book Note", true);
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
        notePanel.add(new JLabel("Document:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(home.getCurrentBook().getDocumentTitle());
        documentField.setEditable(false);
        notePanel.add(new JScrollPane(documentField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Chapter:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        int selected = -1;
        int counter = -1;
        for (Long chapterId : home.getCurrentBook().getChaptersMap().keySet()) {
            counter++;
            if (home.getCurrentChapter().getChapterId().equals(chapterId)) {
                selected = counter;
            }
            Chapter chapter = home.getCurrentBook().getChaptersMap().get(chapterId);
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
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 0, 5);
        tagsField.setLineWrap(true);
        notePanel.add(new JScrollPane(tagsField), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel suggestionLabel = new JLabel("Words separated by \",\".");
        suggestionLabel.setForeground(Color.GRAY);
        notePanel.add(suggestionLabel, c);

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

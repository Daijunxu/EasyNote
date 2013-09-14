/**
 *
 */
package notes.gui.book.component;

import notes.bean.BookHome;
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

    private static final long serialVersionUID = -6695210036171096675L;
    private JButton okButton = new JButton(new AbstractAction("OK") {
        private static final long serialVersionUID = -3137484288088623070L;

        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });
    private JLabel noteIdField = new JLabel();
    private JTextArea documentField = new JTextArea(2, 50);
    private JTextArea chapterField = new JTextArea(2, 50);
    private JTextArea tagsField = new JTextArea(2, 50);
    private JTextArea noteTextField = new JTextArea(10, 50);
    private JLabel createdTimeField = new JLabel();

    /**
     * Creates an instance of {@code ViewBookNoteDialog}.
     */
    public ViewBookNoteDialog() {
        super(MainPanel.get(), "View Book Note", true);
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
        noteIdField.setText(home.getCurrentBookNote().getNoteId().toString());
        notePanel.add(noteIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Document:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(home.getCurrentBook().getDocumentTitle());
        documentField.setEditable(false);
        notePanel.add(new JScrollPane(documentField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Chapter:"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        chapterField.setLineWrap(true);
        chapterField.setText(home.getCurrentChapter().getChapterId() + ". "
                + home.getCurrentChapter().getChapterTitle());
        chapterField.setEditable(false);
        notePanel.add(new JScrollPane(chapterField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        StringBuilder tagStrBuilder = new StringBuilder();
        if (!home.getCurrentBookNote().getTagIds().isEmpty()) {
            for (Long tagId : home.getCurrentBookNote().getTagIds()) {
                tagStrBuilder.append(BookHome.get().getBookNoteDAO().findTagById(tagId)
                        .getTagText()
                        + ",");
            }
            tagStrBuilder.deleteCharAt(tagStrBuilder.length() - 1);
        }
        tagsField.setLineWrap(true);
        tagsField.setText(tagStrBuilder.toString());
        tagsField.setEditable(false);
        notePanel.add(new JScrollPane(tagsField), c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text:"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        noteTextField.setLineWrap(true);
        noteTextField.setText(home.getCurrentBookNote().getNoteText());
        noteTextField.setEditable(false);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(home.getCurrentBookNote().getCreatedTime().toString());
        notePanel.add(createdTimeField, c);

        dialogPanel.add(notePanel);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(okButton);

        dialogPanel.add(buttons);

        pack();
        setLocationRelativeTo(frame);
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
        setVisible(true);
    }
}

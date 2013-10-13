package notes.gui.book.component;

import notes.bean.BookHome;
import notes.dao.impl.BookNoteDAO;
import notes.data.cache.Property;
import notes.entity.book.Chapter;
import notes.gui.main.component.MainPanel;
import notes.gui.main.verifier.IdInputVerifier;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Defines the dialog and event listener for creating a chapter.
 *
 * @author Rui Du
 */
public class NewChapterDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (!chapterIdField.getInputVerifier().verify(chapterIdField)) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid chapter ID!\n(A non-negative integer)",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                chapterIdField.requestFocus();
                return;
            } else if (chapterField.getText() == null || chapterField.getText().trim().equals("")) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Chapter title cannot be empty!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                chapterField.requestFocus();
                return;
            } else if (chapterField.getText().trim().split("\n").length > 1) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "Chapter title can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                chapterField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            BookHome home = BookHome.get();
            BookNoteDAO dao = home.getBookNoteDAO();

            // Create instance of the created chapter.
            Chapter createdChapter = new Chapter();
            String chapterIdStr = chapterIdField.getText();
            createdChapter.setChapterId(Long.parseLong(chapterIdStr));
            createdChapter.setChapterTitle(WordUtils.capitalize(chapterField.getText().trim()));
            createdChapter.setNotesList(new ArrayList<Long>());

            // Save the created chapter.
            Chapter cachedChapter = dao.saveChapter(createdChapter, home.getCurrentBook()
                    .getDocumentId());
            if (cachedChapter != null) {
                // Update the chapter and note panel.
                frame.updateIndexPanel(home.getCurrentBook().getDocumentId(), cachedChapter.getChapterId(), null);

                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playUpdate();
                }

                setVisible(false);
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null,
                        "Duplicate chapter ID!",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }

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
    private final JTextArea documentField = new JTextArea(2, 50);
    private final JTextField chapterIdField = new JTextField();
    private final JTextArea chapterField = new JTextArea(2, 50);

    /**
     * Creates an instance of {@code NewChapterDialog}.
     */
    public NewChapterDialog() {
        super(MainPanel.get(), "Create Chapter", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();
        BookHome home = BookHome.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel chapterPanel = new JPanel();
        chapterPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        chapterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        chapterPanel.add(new JLabel("Document *"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(home.getCurrentBook().getDocumentTitle());
        documentField.setEditable(false);
        chapterPanel.add(new JScrollPane(documentField), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        chapterPanel.add(new JLabel("Chapter ID *"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        chapterIdField.setInputVerifier(new IdInputVerifier());
        chapterPanel.add(chapterIdField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        chapterPanel.add(new JLabel("Chapter Title *"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        chapterField.setLineWrap(true);
        chapterPanel.add(new JScrollPane(chapterField), c);

        dialogPanel.add(chapterPanel);

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

package notes.gui.book.component;

import notes.businesslogic.BookBusinessLogic;
import notes.dao.impl.BookNoteDAO;
import notes.businessobjects.book.Chapter;
import notes.gui.main.component.MainPanel;
import notes.gui.main.validation.IdInputVerifier;
import notes.utils.SoundFactory;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Defines the dialog and event listener for editing a chapter in a book.
 *
 * Author: Rui Du
 */
public class EditChapterDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (!chapterIdField.getInputVerifier().verify(chapterIdField)) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid chapter ID!\n(A non-negative integer)",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                chapterIdField.requestFocus();
                return;
            } else if (chapterField.getText() == null || chapterField.getText().trim().equals("")) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Chapter title cannot be empty!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                chapterField.requestFocus();
                return;
            } else if (chapterField.getText().trim().split("\n").length > 1) {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null, "Chapter title can only have one line!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                chapterField.requestFocus();
                return;
            }

            MainPanel frame = MainPanel.get();
            BookBusinessLogic logic = BookBusinessLogic.get();
            BookNoteDAO dao = logic.getBookNoteDAO();

            // Create instance of the updated chapter.
            Chapter updateChapter = new Chapter();
            updateChapter.setChapterId(Long.parseLong(chapterIdField.getText()));
            updateChapter.setChapterTitle(WordUtils.capitalize(chapterField.getText().trim()));
            updateChapter.setNotesList(logic.getCurrentChapter().getNotesList());

            // Save the updated chapter.
            Chapter cachedChapter = dao.updateChapter(updateChapter, logic.getCurrentBook().getDocumentId());

            if (cachedChapter != null) {
                // Update the chapter and note panel.
                if (logic.getCurrentNote() != null) {
                    frame.updateBookPanel(logic.getCurrentBook().getDocumentId(), cachedChapter.getChapterId(),
                            logic.getCurrentNote().getNoteId());
                } else {
                    frame.updateBookPanel(logic.getCurrentBook().getDocumentId(), cachedChapter.getChapterId(), null);
                }

                SoundFactory.playUpdate();
                setVisible(false);
            } else {
                SoundFactory.playError();
                JOptionPane.showMessageDialog(null,
                        "Duplicate chapter ID!",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    private final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        public void actionPerformed(ActionEvent e) {
            SoundFactory.playNavigation();
            setVisible(false);
        }
    });
    private final JTextField chapterIdField = new JTextField();
    private final JTextArea chapterField = new JTextArea(2, 30);

    /**
     * Creates an instance of {@code EditChapterDialog}.
     */
    public EditChapterDialog() {
        super(MainPanel.get(), "Edit Chapter", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();
        BookBusinessLogic logic = BookBusinessLogic.get();

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
        c.insets = new Insets(5, 5, 5, 5);
        chapterPanel.add(new JLabel("Chapter ID:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        chapterIdField.setInputVerifier(new IdInputVerifier());
        chapterIdField.setText(logic.getCurrentChapter().getChapterId().toString());
        chapterPanel.add(chapterIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        chapterPanel.add(new JLabel("Chapter:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        chapterField.setLineWrap(true);
        chapterField.setText(logic.getCurrentChapter().getChapterTitle());
        chapterField.select(0, 0);
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

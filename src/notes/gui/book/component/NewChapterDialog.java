/**
 *
 */
package notes.gui.book.component;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import notes.bean.BookHome;
import notes.dao.impl.BookNoteDAO;
import notes.book.Chapter;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.gui.main.verifier.IdInputVerifier;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Defines the dialog and event listener for creating a chapter.
 *
 * @author Rui Du
 * @version 1.0
 */
public class NewChapterDialog extends JDialog {

    private static final long serialVersionUID = -2657534900412465114L;

    private JButton okButton = new JButton(new AbstractAction("OK") {
        private static final long serialVersionUID = -3654597593178177421L;

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
                // Update temporary data in the BookHome.
                home.updateTemporaryData(home.getCurrentBook().getDocumentId(), null, null);

                // Update the chapter and note panel.
                frame.updateChapterPanel();

                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playUpdate();
                }

                setVisible(false);
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null,
                        "Error occurred when creating new chapter: Duplicate chapter ID!",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }

        }
    });

    private JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        private static final long serialVersionUID = 789206680230884263L;

        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });

    private JTextArea documentField = new JTextArea(2, 50);
    private JTextField chapterIdField = new JTextField();
    private JTextArea chapterField = new JTextArea(2, 50);

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
        chapterPanel.add(new JLabel("Document:"), c);

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
        chapterPanel.add(new JLabel("Chapter ID:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        chapterIdField.setInputVerifier(new IdInputVerifier());
        chapterPanel.add(chapterIdField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        chapterPanel.add(new JLabel("Chapter Title:"), c);

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

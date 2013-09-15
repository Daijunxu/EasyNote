/**
 *
 */
package notes.gui.main.component;

import notes.article.Article;
import notes.article.ArticleNote;
import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.book.Book;
import notes.book.BookNote;
import notes.book.Chapter;
import notes.data.cache.Property;
import notes.entity.Note;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Defines the dialog and event listener for viewing a note in search note panel. The notes can
 * belong to different types.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewNoteDialog extends JDialog {

    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });

    /**
     * Creates an instance of {@code ViewNoteDialog}.
     */
    public ViewNoteDialog(Note note) {
        super(MainPanel.get(), "View Note", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());

        if (note instanceof ArticleNote) {
            createViewSearchNoteDialog((ArticleNote) note);
        } else if (note instanceof BookNote) {
            createViewSearchNoteDialog((BookNote) note);
        }

        pack();
        setLocationRelativeTo(MainPanel.get());
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
        setVisible(true);
    }

    private void createViewSearchNoteDialog(ArticleNote note) {
        JLabel noteIdField = new JLabel();
        JTextArea documentField = new JTextArea(2, 50);
        JTextArea tagsField = new JTextArea(2, 50);
        JTextArea noteTextField = new JTextArea(10, 50);
        JLabel createdTimeField = new JLabel();

        Article article = (Article) ArticleHome.get().getArticleNoteDAO()
                .findDocumentById(note.getDocumentId());

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
        noteIdField.setText(note.getNoteId().toString());
        notePanel.add(noteIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Document:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(article.getDocumentTitle());
        documentField.setEditable(false);
        notePanel.add(new JScrollPane(documentField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Tags:"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        StringBuilder tagStrBuilder = new StringBuilder();
        if (!note.getTagIds().isEmpty()) {
            for (Long tagId : note.getTagIds()) {
                tagStrBuilder.append(ArticleHome.get().getArticleNoteDAO().findTagById(tagId).getTagText()).append(",");
            }
            tagStrBuilder.deleteCharAt(tagStrBuilder.length() - 1);
        }
        tagsField.setLineWrap(true);
        tagsField.setText(tagStrBuilder.toString());
        tagsField.setEditable(false);
        notePanel.add(new JScrollPane(tagsField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Note Text:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        noteTextField.setLineWrap(true);
        noteTextField.setText(note.getNoteText());
        noteTextField.setEditable(false);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(note.getCreatedTime().toString());
        notePanel.add(createdTimeField, c);

        dialogPanel.add(notePanel);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(okButton);

        dialogPanel.add(buttons);

        dialogPanel.add(buttons);
    }

    private void createViewSearchNoteDialog(BookNote note) {
        JLabel noteIdField = new JLabel();
        JTextArea documentField = new JTextArea(2, 50);
        JTextArea chapterField = new JTextArea(2, 50);
        JTextArea tagsField = new JTextArea(2, 50);
        JTextArea noteTextField = new JTextArea(10, 50);
        JLabel createdTimeField = new JLabel();

        Book book = (Book) BookHome.get().getBookNoteDAO().findDocumentById(note.getDocumentId());
        Chapter chapter = book.getChaptersMap().get(note.getChapterId());

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

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        noteIdField.setText(note.getNoteId().toString());
        notePanel.add(noteIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Document:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentField.setLineWrap(true);
        documentField.setText(book.getDocumentTitle());
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
        chapterField.setText(chapter.getChapterId() + ". " + chapter.getChapterTitle());
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
        if (!note.getTagIds().isEmpty()) {
            for (Long tagId : note.getTagIds()) {
                tagStrBuilder.append(BookHome.get().getBookNoteDAO().findTagById(tagId).getTagText()).append(",");
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
        noteTextField.setText(note.getNoteText());
        noteTextField.setEditable(false);
        notePanel.add(new JScrollPane(noteTextField), c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        notePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(note.getCreatedTime().toString());
        notePanel.add(createdTimeField, c);

        dialogPanel.add(notePanel);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(okButton);

        dialogPanel.add(buttons);
    }

}

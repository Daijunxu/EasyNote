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
import java.util.List;

/**
 * Defines the dialog and event listener for viewing a book.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewBookDialog extends JDialog {

    private static final long serialVersionUID = 4306956396552952456L;
    private JButton okButton = new JButton(new AbstractAction("OK") {
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });
    private JTextField documentIdField = new JTextField();
    private JTextArea documentTitleField = new JTextArea(2, 50);
    private JTextArea authorField = new JTextArea(2, 50);
    private JTextField editionField = new JTextField();
    private JTextField publishedYearField = new JTextField();
    private JTextField ISBNField = new JTextField();
    private JTextArea commentField = new JTextArea(10, 50);
    private JTextField createdTimeField = new JTextField();
    private JTextField lastUpdatedTimeField = new JTextField();

    /**
     * Creates an instance of {@code ViewBookDialog}.
     */
    public ViewBookDialog() {
        super(MainPanel.get(), "View Book Information", true);
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
        bookPanel.add(new JLabel("Book ID:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentIdField.setText(home.getCurrentBook().getDocumentId().toString());
        documentIdField.setEditable(false);
        bookPanel.add(documentIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Book Title:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentTitleField.setLineWrap(true);
        documentTitleField.setText(home.getCurrentBook().getDocumentTitle());
        documentTitleField.setEditable(false);
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
        authorField.setEditable(false);
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
        if (home.getCurrentBook().getEdition() != null) {
            editionField.setText(home.getCurrentBook().getEdition().toString());
        }
        editionField.setEditable(false);
        bookPanel.add(editionField, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Published Year:"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        if (home.getCurrentBook().getPublishedYear() != null) {
            publishedYearField.setText(home.getCurrentBook().getPublishedYear().toString());
        }
        publishedYearField.setEditable(false);
        bookPanel.add(publishedYearField, c);

        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("ISBN:"), c);

        c.gridx = 1;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 0, 5);
        ISBNField.setText(home.getCurrentBook().getIsbn());
        ISBNField.setEditable(false);
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

        commentField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 8;
        c.insets = new Insets(5, 5, 5, 5);
        commentField.setText(home.getCurrentBook().getComment());
        commentField.setEditable(false);
        bookPanel.add(new JScrollPane(commentField), c);

        c.gridx = 0;
        c.gridy = 9;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 9;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(home.getCurrentBook().getCreatedTime().toString());
        createdTimeField.setEditable(false);
        bookPanel.add(createdTimeField, c);

        c.gridx = 0;
        c.gridy = 10;
        c.insets = new Insets(5, 5, 5, 5);
        bookPanel.add(new JLabel("Last Updated Time:"), c);

        c.gridx = 1;
        c.gridy = 10;
        c.insets = new Insets(5, 5, 5, 5);
        lastUpdatedTimeField.setText(home.getCurrentBook().getLastUpdatedTime().toString());
        lastUpdatedTimeField.setEditable(false);
        bookPanel.add(lastUpdatedTimeField, c);

        dialogPanel.add(bookPanel);

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

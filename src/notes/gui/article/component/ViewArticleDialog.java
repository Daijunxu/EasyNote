/**
 *
 */
package notes.gui.article.component;

import notes.bean.ArticleHome;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Defines the dialog and event listener for viewing an article.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewArticleDialog extends JDialog {

    private static final long serialVersionUID = -8430094254439667310L;
    private JButton okButton = new JButton(new AbstractAction("OK") {
        private static final long serialVersionUID = -1239276335577405220L;

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
    private JTextArea sourceField = new JTextArea(2, 50);
    private JTextArea commentField = new JTextArea(10, 50);
    private JTextField createdTimeField = new JTextField();
    private JTextField lastUpdatedTimeField = new JTextField();

    /**
     * Creates an instance of {@code ViewArticleDialog}.
     */
    public ViewArticleDialog() {
        super(MainPanel.get(), "View Article Information", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();
        ArticleHome home = ArticleHome.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel articlePanel = new JPanel();
        articlePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        articlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        articlePanel.add(new JLabel("Article ID:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentIdField.setText(home.getCurrentArticle().getDocumentId().toString());
        documentIdField.setEditable(false);
        articlePanel.add(documentIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Article Title:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentTitleField.setLineWrap(true);
        documentTitleField.setText(home.getCurrentArticle().getDocumentTitle());
        documentTitleField.setEditable(false);
        articlePanel.add(new JScrollPane(documentTitleField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Authors:"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 0, 5);
        authorField.setLineWrap(true);
        StringBuilder sb = new StringBuilder();
        List<String> authorsList = home.getCurrentArticle().getAuthorsList();
        if (!authorsList.isEmpty()) {
            for (String author : authorsList) {
                sb.append(author);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        authorField.setText(sb.toString());
        authorField.setEditable(false);
        articlePanel.add(new JScrollPane(authorField), c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Source:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);
        sourceField.setLineWrap(true);
        sourceField.setText(home.getCurrentArticle().getSource());
        sourceField.setEditable(false);
        articlePanel.add(new JScrollPane(sourceField), c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Comment:"), c);

        commentField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        commentField.setText(home.getCurrentArticle().getComment());
        commentField.setEditable(false);
        articlePanel.add(new JScrollPane(commentField), c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Created Time:"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(home.getCurrentArticle().getCreatedTime().toString());
        createdTimeField.setEditable(false);
        articlePanel.add(createdTimeField, c);

        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        articlePanel.add(new JLabel("Last Updated Time:"), c);

        c.gridx = 1;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        lastUpdatedTimeField.setText(home.getCurrentArticle().getLastUpdatedTime().toString());
        lastUpdatedTimeField.setEditable(false);
        articlePanel.add(lastUpdatedTimeField, c);

        dialogPanel.add(articlePanel);

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

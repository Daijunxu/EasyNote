/**
 *
 */
package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Defines the dialog and event listener for viewing a workset.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ViewWorksetDialog extends JDialog {

    private final JButton editButton = new JButton(new AbstractAction("Edit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            // Make the current view workset note dialog disappear.
            setVisible(false);

            // Show edit workset dialog.
            new EditWorksetDialog();
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
    private final JTextField documentIdField = new JTextField();
    private final JTextArea documentTitleField = new JTextArea(2, 50);
    private final JTextArea authorField = new JTextArea(2, 50);
    private final JTextArea commentField = new JTextArea(10, 50);
    private final JTextField createdTimeField = new JTextField();
    private final JTextField lastUpdatedTimeField = new JTextField();

    /**
     * Creates an instance of {@code ViewWorksetDialog}.
     */
    public ViewWorksetDialog() {
        super(MainPanel.get(), "View Workset Information", true);
        setIconImage(new ImageIcon("./resources/images/workset.gif").getImage());
        MainPanel frame = MainPanel.get();
        WorksetHome home = WorksetHome.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel worksetPanel = new JPanel();
        worksetPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        worksetPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        worksetPanel.add(new JLabel("Workset ID"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentIdField.setText(home.getCurrentWorkset().getDocumentId().toString());
        documentIdField.setEditable(false);
        worksetPanel.add(documentIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Workset Title"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentTitleField.setLineWrap(true);
        documentTitleField.setText(home.getCurrentWorkset().getDocumentTitle());
        documentTitleField.setEditable(false);
        worksetPanel.add(new JScrollPane(documentTitleField), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Authors"), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 0, 5);
        authorField.setLineWrap(true);
        StringBuilder sb = new StringBuilder();
        List<String> authorsList = home.getCurrentWorkset().getAuthorsList();
        if (!authorsList.isEmpty()) {
            for (String author : authorsList) {
                sb.append(author);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        authorField.setText(sb.toString());
        authorField.setEditable(false);
        worksetPanel.add(new JScrollPane(authorField), c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 5, 5, 5);
        JLabel authorSuggestionLabel = new JLabel("Separate authors by \",\".");
        authorSuggestionLabel.setForeground(Color.GRAY);
        worksetPanel.add(authorSuggestionLabel, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Comment"), c);

        commentField.setLineWrap(true);
        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 5, 5, 5);
        commentField.setText(home.getCurrentWorkset().getComment());
        commentField.setEditable(false);
        worksetPanel.add(new JScrollPane(commentField), c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Created Time"), c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        createdTimeField.setText(home.getCurrentWorkset().getCreatedTime().toString());
        createdTimeField.setEditable(false);
        worksetPanel.add(createdTimeField, c);

        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        worksetPanel.add(new JLabel("Last Updated Time"), c);

        c.gridx = 1;
        c.gridy = 6;
        c.insets = new Insets(5, 5, 5, 5);
        lastUpdatedTimeField.setText(home.getCurrentWorkset().getLastUpdatedTime().toString());
        lastUpdatedTimeField.setEditable(false);
        worksetPanel.add(lastUpdatedTimeField, c);

        dialogPanel.add(worksetPanel);

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

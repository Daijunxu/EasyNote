/**
 *
 */
package notes.gui.main.component;

import notes.article.Article;
import notes.bean.BookHome;
import notes.book.Book;
import notes.data.cache.Property;
import notes.entity.Document;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the dialog and event listener for opening a document.
 *
 * @author Rui Du
 * @version 1.0
 */
public class OpenDocumentDialog extends JDialog {
    private static final long serialVersionUID = 3402344999808907159L;
    private JButton okButton = new JButton(new AbstractAction("OK") {
        private static final long serialVersionUID = -4225868807457964012L;

        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (documentTitleField.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(null, "Please select a document!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
                return;
            }

            String selectedDocumentTitle = documentTitleField.getSelectedValue().toString();
            Document document = BookHome.get().getBookNoteDAO()
                    .findDocumentByTitle(selectedDocumentTitle);
            if (document instanceof Book) {
                MainPanel.get().setBookPanel((Book) document);
            } else if (document instanceof Article) {
                MainPanel.get().setArticlePanel((Article) document);
            }

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playUpdate();
            }

            setVisible(false);
        }
    });
    private JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        private static final long serialVersionUID = -8921821739904585632L;

        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });
    private JComboBox documentTypeField = new JComboBox(Property.get().getDocumentTypes().toArray());
    private JList documentTitleField;

    /**
     * Constructs an instance of {@code OpenDocumentDialog}.
     */
    public OpenDocumentDialog() {
        super(MainPanel.get(), "Open Document", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        final MainPanel frame = MainPanel.get();

        final JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        final JPanel documentPanel = new JPanel();
        documentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        documentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5); // Top, left, bottom, right.
        documentPanel.add(new JLabel("Type:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        documentTypeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                updateDocumentTitleField();
            }
        });
        documentPanel.add(documentTypeField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        documentPanel.add(new JLabel("Title:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 5);
        DefaultListModel listModel = new DefaultListModel();
        documentTitleField = new JList(listModel);
        int documentListWidth = 500;
        documentTitleField.setCellRenderer(new DocumentListCellRenderer(documentListWidth));
        documentTitleField.setFixedCellWidth(documentListWidth);
        documentTitleField.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateDocumentTitleField();
        JScrollPane documentTitleScrollPane = new JScrollPane(documentTitleField);
        documentPanel.add(documentTitleScrollPane, c);

        dialogPanel.add(documentPanel);

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

    private void updateDocumentTitleField() {
        List<String> documentsTitleList = new ArrayList<String>();
        try {
            Class<?> documentClass;
            String documentType = documentTypeField.getSelectedItem().toString();
            if (documentType.equals("Article")) {
                documentClass = Article.class;
            } else if (documentType.equals("Book")) {
                documentClass = Book.class;
            } else {
                throw new Exception(
                        "Class not found exception: no class is matched with current document type.");
            }
            for (Document document : BookHome.get().getBookNoteDAO().findAllDocuments()) {
                if (document.getClass() == documentClass) {
                    documentsTitleList.add(document.getDocumentTitle());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((DefaultListModel) documentTitleField.getModel()).removeAllElements();
        for (String documentTitle : documentsTitleList) {
            ((DefaultListModel) documentTitleField.getModel()).addElement(documentTitle);
        }
        documentsTitleList.clear();
    }
}

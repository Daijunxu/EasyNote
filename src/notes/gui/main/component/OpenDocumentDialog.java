package notes.gui.main.component;

import notes.businessobjects.Document;
import notes.businessobjects.article.Article;
import notes.businessobjects.book.Book;
import notes.businessobjects.workset.Workset;
import notes.dao.impl.DocumentNoteDAO;
import notes.data.persistence.Property;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines the dialog and event listener for opening a document.
 * <p/>
 * Author: Rui Du
 */
public class OpenDocumentDialog extends JDialog {
    private final JButton okButton = new JButton(new AbstractAction("OK") {
        public void actionPerformed(ActionEvent e) {

            // Input validation.
            if (documentTitleField.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(null, "Please select a document!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                documentTitleField.requestFocus();
                return;
            }

            String selectedDocumentTitle = documentTitleField.getSelectedValue().toString();
            Long documentId = documentTitleToIdMap.get(selectedDocumentTitle);
            Document document = DocumentNoteDAO.get().findDocumentById(documentId);
            if (document instanceof Workset) {
                MainPanel.get().setWorksetPanel((Workset) document, null);
            } else if (document instanceof Book) {
                MainPanel.get().setBookPanel((Book) document, null);
            } else if (document instanceof Article) {
                MainPanel.get().setArticlePanel((Article) document, null);
            }

            documentTitleToIdMap.clear();

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
    private final JComboBox documentTypeField = new JComboBox(Property.get().getDocumentTypes().toArray());
    private final JList documentTitleField;

    private final Map<String, Long> documentTitleToIdMap;

    /**
     * Constructs an instance of {@code OpenDocumentDialog}.
     */
    public OpenDocumentDialog() {
        super(MainPanel.get(), "Open Document", true);

        documentTitleToIdMap = new HashMap<String, Long>();

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
            if (documentType.equals("Workset")) {
                documentClass = Workset.class;
            } else if (documentType.equals("Book")) {
                documentClass = Book.class;
            } else if (documentType.equals("Article")) {
                documentClass = Article.class;
            } else {
                throw new Exception(
                        "Class not found exception: no class is matched with current document type.");
            }
            for (Document document : DocumentNoteDAO.get().findAllDocuments()) {
                if (document.getClass() == documentClass) {
                    documentsTitleList.add(document.getDocumentTitle());
                    documentTitleToIdMap.put(document.getDocumentTitle(), document.getDocumentId());
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

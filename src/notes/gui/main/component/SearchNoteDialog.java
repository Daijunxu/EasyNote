package notes.gui.main.component;

import notes.businesslogic.ArticleBusinessLogic;
import notes.businesslogic.BookBusinessLogic;
import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.dao.impl.DocumentNoteDAO;
import notes.gui.main.event.SearchNoteDialogWindowListener;
import notes.gui.main.event.SearchNoteListMouseListener;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines the dialog and event listener for searching notes.
 * <p/>
 * Author: Rui Du
 */
public class SearchNoteDialog extends JDialog {

    private static final int RESULT_LIST_WIDTH = 800;
    private static final int RESULT_LIST_SCROLL_WIDTH = 835;
    private static final int RESULT_LIST_HEIGHT = 500;
    private static SearchNoteDialog instance;
    private final JButton searchButton = new JButton(new AbstractAction("Search") {
        public void actionPerformed(ActionEvent e) {
            String selectedValue = searchScopeField.getSelectedItem().toString();
            if (selectedValue.equals("All Documents")) {
                searchNotes(null);
            } else if (selectedValue.equals("All Articles")) {
                searchNotes(ArticleBusinessLogic.get().getArticleNoteDAO().findAllArticles());
            } else if (selectedValue.equals("All Books")) {
                searchNotes(BookBusinessLogic.get().getBookNoteDAO().findAllBooks());
            } else if (selectedValue.equals("All Worksets")) {
                searchNotes(WorksetBusinessLogic.get().getWorksheetNoteDAO().findAllWorksets());
            } else {
                // Search notes in a particular document.
                Long documentId = documentTitleToIdMap.get(selectedValue);
                Set<Long> candidateDocuments = new HashSet<Long>();
                candidateDocuments.add(documentId);
                searchNotes(candidateDocuments);
            }

            // Filter the note list by tags.
            filterNotesByTagsAND();

            // Update the result list in the result panel.
            updateResultPanel();

            SoundFactory.playNavigation();

        }
    });
    private final JButton clearButton = new JButton(new AbstractAction(" Clear ") {
        public void actionPerformed(ActionEvent e) {
            searchScopeField.setSelectedIndex(0);
            noteTextField.setText(null);
            tagsField.setText(null);
            SoundFactory.playNavigation();
        }
    });
    private final JPanel dialogPanel = new JPanel();
    private final JPanel searchPanel = new JPanel();
    private final JComboBox searchScopeField = new JComboBox();
    private final JTextField noteTextField = new JTextField(50);
    private final JCheckBox caseSensitiveField = new JCheckBox();
    private final JCheckBox exactSearchField = new JCheckBox();
    private final JTextField tagsField = new JTextField();
    private final JLabel resultSummaryField = new JLabel();
    private JScrollPane resultScrollPane = new JScrollPane();
    private JList resultList;
    private List<Note> resultNoteList;
    private Map<String, Long> documentTitleToIdMap;

    private SearchNoteDialog() {
        super(MainPanel.get(), "Search Notes", true);

        documentTitleToIdMap = new HashMap<String, Long>();

        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        addWindowListener(new SearchNoteDialogWindowListener());

        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 0); // Top, left, bottom, right.
        searchPanel.add(caseSensitiveField, c);

        c.gridx = 2;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 5, 5);
        JLabel caseSensitiveLabel = new JLabel("Case Sensitive");
        caseSensitiveLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (caseSensitiveField.isSelected()) {
                    caseSensitiveField.setSelected(false);
                } else {
                    caseSensitiveField.setSelected(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        searchPanel.add(caseSensitiveLabel, c);

        c.gridx = 3;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 0);
        searchPanel.add(exactSearchField, c);

        c.gridx = 4;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 5, 5);
        JLabel exactSearchLabel = new JLabel("Exact Words");
        exactSearchLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (exactSearchField.isSelected()) {
                    exactSearchField.setSelected(false);
                } else {
                    exactSearchField.setSelected(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });
        searchPanel.add(exactSearchLabel, c);

        c.gridx = 5;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 5, 5);
        JPanel searchButtonPanel = new JPanel();
        searchButtonPanel.add(searchButton);
        searchPanel.add(searchButtonPanel, c);

        c.gridx = 5;
        c.gridy = 3;
        c.insets = new Insets(0, 5, 10, 5);
        JPanel clearButtonPanel = new JPanel();
        clearButtonPanel.add(clearButton);
        searchPanel.add(clearButtonPanel, c);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 5, 5);
        searchPanel.add(new JLabel("Search In:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 4;
        c.insets = new Insets(5, 5, 5, 5);
        searchScopeField.addItem("All Documents");
        searchScopeField.addItem("All Articles");
        searchScopeField.addItem("All Books");
        searchScopeField.addItem("All Worksets");
        List<Document> documentsList = DocumentNoteDAO.get().findAllDocuments();
        List<String> documentTitleList = new ArrayList<String>();
        for (Document document : documentsList) {
            documentTitleList.add(document.getDocumentTitle());
            documentTitleToIdMap.put(document.getDocumentTitle(), document.getDocumentId());
        }
        Collections.sort(documentTitleList);
        for (String documentTitle : documentTitleList) {
            searchScopeField.addItem(documentTitle);
        }
        documentTitleList.clear();
        searchPanel.add(searchScopeField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 0, 5);
        searchPanel.add(new JLabel("Contain Text:"), c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 4;
        c.insets = new Insets(5, 5, 0, 5);
        searchPanel.add(noteTextField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 10, 5);
        searchPanel.add(new JLabel("Have Tags:"), c);

        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 4;
        c.insets = new Insets(5, 5, 5, 5);
        searchPanel.add(tagsField, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 0, 5);
        resultSummaryField.setText("Search Result: ");
        searchPanel.add(resultSummaryField, c);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 6;
        c.insets = new Insets(5, 5, 5, 5);
        resultList = new JList();
        resultList.setFixedCellWidth(RESULT_LIST_WIDTH);
        resultList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultScrollPane = new JScrollPane(resultList);
        resultScrollPane.setPreferredSize(new Dimension(RESULT_LIST_SCROLL_WIDTH, RESULT_LIST_HEIGHT));
        resultScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
        searchPanel.add(resultScrollPane, c);

        dialogPanel.add(searchPanel);

        pack();
        setSize(getWidth() + 15, getHeight());
        setResizable(false);
    }

    /**
     * Gets the instance of {@code SearchNoteDialog}.
     *
     * @return {@code SearchNoteDialog} The instance of {@code SearchNoteDialog}.
     */
    public static SearchNoteDialog get() {
        if (instance == null) {
            instance = new SearchNoteDialog();
        }
        return instance;
    }

    private void filterNotesByTagsAND() {
        List<String> tagsList = EntityHelper.buildTagsStrList(tagsField.getText());
        if (tagsList.isEmpty()) {
            return;
        }
        List<Note> newNoteList = new ArrayList<Note>();
        boolean isResult;
        for (Note note : resultNoteList) {
            Set<String> noteTagsSet = new HashSet<String>();
            for (Long tagId : note.getTagIds()) {
                noteTagsSet.add(DocumentNoteDAO.get().findTagById(tagId).getTagText());
            }
            isResult = true;
            for (String tag : tagsList) {
                if (!noteTagsSet.contains(tag)) {
                    isResult = false;
                    break;
                }
            }
            if (isResult) {
                newNoteList.add(note);
            }
        }
        resultNoteList.clear();
        resultNoteList = newNoteList;
    }

    /**
     * Gets the selected note in the result note panel.
     *
     * @return {@code Note} The selected note in the result note panel.
     */
    public Note getSelectedResultNote() {
        if (resultList == null) {
            return null;
        }
        return (Note) resultList.getSelectedValue();
    }

    private void searchNotes(Set<Long> candidateDocuments) {
        boolean caseSensitive = (caseSensitiveField.isSelected());
        boolean exactSearch = (exactSearchField.isSelected());
        resultNoteList = DocumentNoteDAO.get().findAllNotesContainingText(candidateDocuments,
                noteTextField.getText().trim(), caseSensitive, exactSearch);
    }

    public void updateResultPanel() {
        searchPanel.remove(resultScrollPane);

        resultSummaryField.setText("Found " + resultNoteList.size() + " note(s).");

        Note[] noteArray = resultNoteList.toArray(new Note[resultNoteList.size()]);
        resultList = new JList(noteArray);
        resultList.setCellRenderer(new SearchResultNoteListCellRenderer(RESULT_LIST_WIDTH));
        resultList.setFixedCellWidth(RESULT_LIST_WIDTH);
        resultList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultList.addMouseListener(new SearchNoteListMouseListener());
        resultScrollPane = new JScrollPane(resultList);
        resultScrollPane.setPreferredSize(new Dimension(RESULT_LIST_SCROLL_WIDTH, RESULT_LIST_HEIGHT));
        resultScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 6;
        c.insets = new Insets(5, 5, 5, 5);
        searchPanel.add(resultScrollPane, c);

        searchPanel.validate();
        searchPanel.repaint();
    }
}

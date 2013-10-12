package notes.gui.main.component;

import notes.bean.BookHome;
import notes.dao.impl.BookNoteDAO;
import notes.data.cache.Property;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.article.Article;
import notes.entity.book.Book;
import notes.entity.workset.Workset;
import notes.gui.main.event.SearchNoteDialogWindowListener;
import notes.gui.main.event.SearchNoteListMouseListener;
import notes.utils.EntityHelper;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines the dialog and event listener for searching notes.
 *
 * @author Rui Du
 */
public class SearchNoteDialog extends JDialog {

    private static SearchNoteDialog instance;
    private final JButton searchButton = new JButton(new AbstractAction("Search") {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = searchScopeField.getSelectedIndex();
            if (selectedIndex <= 3) {
                // Search notes in all documents.
                searchNotesInAllDocuments();
                if (selectedIndex == 1 || selectedIndex == 2 || selectedIndex == 3) {
                    // Filter notes in current scope.
                    filterNotesByScope(searchScopeField.getSelectedItem().toString());
                }
            } else {
                // Search notes in a particular document.
                String documentTitle = searchScopeField.getSelectedItem().toString();
                Long documentId = BookHome.get().getBookNoteDAO()
                        .findDocumentByTitle(documentTitle).getDocumentId();
                searchNotesInOneDocument(documentId);
            }

            // Filter the note list by tags.
            filterNotesByTagsAND();

            // Update the result list in the result panel.
            updateResultPanel();

            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }

        }
    });
    private final JButton clearButton = new JButton(new AbstractAction(" Clear ") {
        public void actionPerformed(ActionEvent e) {
            searchScopeField.setSelectedIndex(0);
            noteTextField.setText(null);
            tagsField.setText(null);
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
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
    private List<Note> noteList;

    private SearchNoteDialog() {
        super(MainPanel.get(), "Search Notes", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        addWindowListener(new SearchNoteDialogWindowListener());
        BookHome home = BookHome.get();

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
        List<Document> documentsList = BookHome.get().getBookNoteDAO().findAllDocuments();
        List<String> documentTitleList = new ArrayList<String>();
        for (Document document : documentsList) {
            documentTitleList.add(document.getDocumentTitle());
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
        int resultListWidth = 800;
        resultList.setFixedCellWidth(resultListWidth);
        resultList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultScrollPane = new JScrollPane(resultList);
        resultScrollPane.setPreferredSize(new Dimension(resultListWidth + 14, 500));
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

    private void filterNotesByScope(String scope) {
        BookNoteDAO dao = BookHome.get().getBookNoteDAO();
        List<Note> newNoteList = new ArrayList<Note>();
        if (scope.equals("All Articles")) {
            for (Note note : noteList) {
                if (dao.findDocumentById(note.getDocumentId()) instanceof Article) {
                    newNoteList.add(note);
                }
            }
        } else if (scope.equals("All Books")) {
            for (Note note : noteList) {
                if (dao.findDocumentById(note.getDocumentId()) instanceof Book) {
                    newNoteList.add(note);
                }
            }
        } else if (scope.equals("All Worksets")) {
            for (Note note : noteList) {
                if (dao.findDocumentById(note.getDocumentId()) instanceof Workset) {
                    newNoteList.add(note);
                }
            }
        }
        noteList.clear();
        noteList = newNoteList;
    }

    private void filterNotesByTagsAND() {
        BookNoteDAO dao = BookHome.get().getBookNoteDAO();
        List<String> tagsList = EntityHelper.buildTagsStrList(tagsField.getText());
        if (tagsList.isEmpty()) {
            return;
        }
        List<Note> newNoteList = new ArrayList<Note>();
        boolean isResult;
        for (Note note : noteList) {
            Set<String> noteTagsSet = new HashSet<String>();
            for (Long tagId : note.getTagIds()) {
                noteTagsSet.add(dao.findTagById(tagId).getTagText());
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
        noteList.clear();
        noteList = newNoteList;
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

    private void searchNotesInAllDocuments() {
        BookNoteDAO dao = BookHome.get().getBookNoteDAO();
        boolean caseSensitive = (caseSensitiveField.isSelected());
        boolean exactSearch = (exactSearchField.isSelected());

        if (!caseSensitive) {
            if (!exactSearch) {
                // Not case sensitive, not exact search.
                noteList = dao.findAllNotesContainingText(noteTextField.getText().trim(), false,
                        false);
            } else {
                // Not case sensitive, exact search.
                noteList = dao.findAllNotesContainingText(noteTextField.getText().trim(), false,
                        true);
            }
        } else {
            if (!exactSearch) {
                // Case sensitive, not exact search.
                noteList = dao.findAllNotesContainingText(noteTextField.getText().trim(), true,
                        false);
            } else {
                // Case sensitive, exact search.
                noteList = dao.findAllNotesContainingText(noteTextField.getText().trim(), true,
                        true);
            }
        }
    }

    private void searchNotesInOneDocument(Long documentId) {
        BookNoteDAO dao = BookHome.get().getBookNoteDAO();
        boolean caseSensitive = (caseSensitiveField.isSelected());
        boolean exactSearch = (exactSearchField.isSelected());

        if (!caseSensitive) {
            if (!exactSearch) {
                // Not case sensitive, not exact search.
                noteList = dao.findAllNotesContainingText(documentId, noteTextField.getText().trim(), false, false);
            } else {
                // Not case sensitive, exact search.
                noteList = dao.findAllNotesContainingText(documentId, noteTextField.getText().trim(), false, true);
            }
        } else {
            if (!exactSearch) {
                // Case sensitive, not exact search.
                noteList = dao.findAllNotesContainingText(documentId, noteTextField.getText().trim(), true, false);
            } else {
                // Case sensitive, exact search.
                noteList = dao.findAllNotesContainingText(documentId, noteTextField.getText().trim(), true, true);
            }
        }
    }

    public void updateResultPanel() {
        searchPanel.remove(resultScrollPane);

        resultSummaryField.setText("Found " + noteList.size() + " note(s).");

        Note[] noteArray = noteList.toArray(new Note[noteList.size()]);
        resultList = new JList(noteArray);
        int resultListWidth = 800;
        resultList.setCellRenderer(new SearchResultNoteListCellRenderer(resultListWidth));
        resultList.setFixedCellWidth(resultListWidth);
        resultList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultList.addMouseListener(new SearchNoteListMouseListener());
        resultScrollPane = new JScrollPane(resultList);
        resultScrollPane.setPreferredSize(new Dimension(resultListWidth + 14, 500));
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

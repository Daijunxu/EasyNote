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
import notes.data.cache.Cache;
import notes.data.cache.Property;
import notes.entity.Document;
import notes.entity.SystemMode;
import notes.gui.article.event.ArticleNoteListMouseListener;
import notes.gui.article.event.DeleteArticleActionListener;
import notes.gui.article.event.DeleteArticleNoteActionListener;
import notes.gui.article.event.EditArticleActionListener;
import notes.gui.article.event.EditArticleNoteActionListener;
import notes.gui.article.event.ExportArticleEventListener;
import notes.gui.article.event.NewArticleActionListener;
import notes.gui.article.event.NewArticleNoteActionListener;
import notes.gui.article.event.ViewArticleActionListener;
import notes.gui.article.event.ViewArticleNoteActionListener;
import notes.gui.book.component.ChapterListCellRenderer;
import notes.gui.book.event.BookNoteListMouseListener;
import notes.gui.book.event.ChapterListMouseListener;
import notes.gui.book.event.ChapterListSelectionListener;
import notes.gui.book.event.DeleteBookActionListener;
import notes.gui.book.event.DeleteBookNoteActionListener;
import notes.gui.book.event.DeleteChapterActionListener;
import notes.gui.book.event.EditBookActionListener;
import notes.gui.book.event.EditBookNoteActionListener;
import notes.gui.book.event.EditChapterActionListener;
import notes.gui.book.event.ExportBookActionListener;
import notes.gui.book.event.NewBookActionListener;
import notes.gui.book.event.NewBookNoteActionListener;
import notes.gui.book.event.NewChapterActionListener;
import notes.gui.book.event.ViewBookActionListener;
import notes.gui.book.event.ViewBookNoteActionListener;
import notes.gui.main.event.AboutActionListener;
import notes.gui.main.event.MainPanelWindowListener;
import notes.gui.main.event.NoteListSelectionListener;
import notes.gui.main.event.OpenDocumentActionListener;
import notes.gui.main.event.OpenPreferencesActionListener;
import notes.gui.main.event.SaveAllActoinListener;
import notes.gui.main.event.SearchNoteActionListener;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

/**
 * The main panel.
 *
 * @author Rui Du
 * @version 1.0
 */
public class MainPanel extends JFrame {

    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = -6486524899782603246L;

    /**
     * The single instance of {@code MainPanel}.
     */
    public static final MainPanel instance = new MainPanel();

    /**
     * Gets the instance of {@code MainPanel}.
     *
     * @return {@code MainPanel} The instance of {@code MainPanel}.
     */
    public static MainPanel get() {
        return instance;
    }

    /**
     * Create a main panel.
     *
     * @param args The array of arguments when starting the program.
     */
    public static void main(String[] args) {
        Property property = Property.get();

        if (Cache.get() == null || Cache.hasProblem) {
            System.out.println("Cache is having problem!");
            if (!property.getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNotify();
            }
            int result = JOptionPane.showConfirmDialog(null,
                    "Missing data file or having invalid data file. Choose a new data file?",
                    "Confirm Dialog", JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                if (!property.getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playPopup();
                }
                new InitialChooseDataLocationDialog();
            } else {
                System.exit(0);
            }
        } else {
            // Data has loaded successfully.

            if (!property.getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playOn();
            }

            if (property.showLastDocumentOnOpening() && property.getLastOpenedDocumentId() != null) {
                MainPanel.get().openLastDocument(property.getLastOpenedDocumentId());
            }

            MainPanel.get().setVisible(true);
            MainPanel.get().setLocation(MainPanel.get().getLocationOnScreen());
        }

    }

    private void openLastDocument(Long documentId) {
        Document document = BookHome.get().getBookNoteDAO().findDocumentById(documentId);
        if (document instanceof Book) {
            MainPanel.get().setBookPanel((Book) document);
        } else if (document instanceof Article) {
            MainPanel.get().setArticlePanel((Article) document);
        }
    }

    /**
     * The current mode indicating the type of the current opened document.
     */
    private SystemMode currentMode;

    /**
     * The menu bar on the main panel that contains different operations for this application.
     */
    private JMenuBar menuBar;

    /**
     * The chapter panel that contains the information of chapters in a book.
     */
    private JPanel chaptersPanel;

    /**
     * The note panel that contains the information of notes in current document/chapter.
     */
    private JPanel notesPanel;

    /**
     * Constructs an instance of {@code MainPanel}.
     */
    private MainPanel() {

        setDefaultPanel();

        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        addWindowListener(new MainPanelWindowListener());

        // Fix the form to locate in the middle of screen.
        int width = 1280;
        int height = 800;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setResizable(false);
    }

    /**
     * Clears all temporary data in home bean.
     */
    public void clearAllTemporaryData() {
        ArticleHome.get().clearAllTemporaryData();
        BookHome.get().clearAllTemporaryData();
    }

    /**
     * Creates the menu bar when a article is opened.
     */
    private void createArticleMenuBar() {
        menuBar = new JMenuBar();
        JMenu articleMenu = new JMenu("Article");
        JMenu noteMenu = new JMenu("Note");
        JMenu searchMenu = new JMenu("Search");
        JMenu helpMenu = new JMenu("Help");

        articleMenu.setMnemonic(KeyEvent.VK_A);
        JMenu newDocumentMenu = new JMenu("New Document");
        newDocumentMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newArticleItem = new JMenuItem("Article", KeyEvent.VK_A);
        newArticleItem.addActionListener(new NewArticleActionListener());
        newDocumentMenu.add(newArticleItem);
        JMenuItem newBookItem = new JMenuItem("Book", KeyEvent.VK_B);
        newBookItem.addActionListener(new NewBookActionListener());
        newDocumentMenu.add(newBookItem);
        JMenuItem openDocumentItem = new JMenuItem("Open Document", KeyEvent.VK_O);
        openDocumentItem.addActionListener(new OpenDocumentActionListener());
        JMenuItem viewDocumentItem = new JMenuItem("View Article Info", KeyEvent.VK_V);
        viewDocumentItem.addActionListener(new ViewArticleActionListener());
        JMenuItem editDocumentItem = new JMenuItem("Edit Article Info", KeyEvent.VK_E);
        editDocumentItem.addActionListener(new EditArticleActionListener());
        JMenuItem deleteDocumentItem = new JMenuItem("Delete This Article", KeyEvent.VK_D);
        deleteDocumentItem.addActionListener(new DeleteArticleActionListener());
        JMenuItem exportDocumentItem = new JMenuItem("Export", KeyEvent.VK_P);
        exportDocumentItem.addActionListener(new ExportArticleEventListener());
        JMenuItem saveAllItem = new JMenuItem("Save All", KeyEvent.VK_S);
        saveAllItem.addActionListener(new SaveAllActoinListener());
        articleMenu.add(newDocumentMenu);
        articleMenu.add(openDocumentItem);
        articleMenu.add(viewDocumentItem);
        articleMenu.add(editDocumentItem);
        articleMenu.add(deleteDocumentItem);
        articleMenu.add(exportDocumentItem);
        articleMenu.add(saveAllItem);

        noteMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newNoteItem = new JMenuItem("New Note", KeyEvent.VK_N);
        newNoteItem.addActionListener(new NewArticleNoteActionListener());
        JMenuItem viewNoteItem = new JMenuItem("View This Note", KeyEvent.VK_V);
        viewNoteItem.addActionListener(new ViewArticleNoteActionListener());
        JMenuItem editNoteItem = new JMenuItem("Edit This Note", KeyEvent.VK_E);
        editNoteItem.addActionListener(new EditArticleNoteActionListener());
        JMenuItem deleteNoteItem = new JMenuItem("Delete This Note", KeyEvent.VK_D);
        deleteNoteItem.addActionListener(new DeleteArticleNoteActionListener());
        noteMenu.add(newNoteItem);
        noteMenu.add(viewNoteItem);
        noteMenu.add(editNoteItem);
        noteMenu.add(deleteNoteItem);

        searchMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchNoteItem = new JMenuItem("Search Notes", KeyEvent.VK_N);
        searchNoteItem.addActionListener(new SearchNoteActionListener());
        searchMenu.add(searchNoteItem);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem aboutItem = new JMenuItem("About EasyNote", KeyEvent.VK_A);
        aboutItem.addActionListener(new AboutActionListener());
        JMenuItem preferencesItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesItem.addActionListener(new OpenPreferencesActionListener());
        helpMenu.add(aboutItem);
        helpMenu.add(preferencesItem);

        menuBar.add(articleMenu);
        menuBar.add(noteMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Creates the menu bar when a book is opened.
     */
    private void createBookMenuBar() {
        menuBar = new JMenuBar();
        JMenu bookMenu = new JMenu("Book");
        JMenu chapterMenu = new JMenu("Chapter");
        JMenu noteMenu = new JMenu("Note");
        JMenu searchMenu = new JMenu("Search");
        JMenu helpMenu = new JMenu("Help");

        bookMenu.setMnemonic(KeyEvent.VK_B);
        JMenu newDocumentMenu = new JMenu("New Document");
        newDocumentMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newArticleItem = new JMenuItem("Article", KeyEvent.VK_A);
        newArticleItem.addActionListener(new NewArticleActionListener());
        newDocumentMenu.add(newArticleItem);
        JMenuItem newBookItem = new JMenuItem("Book", KeyEvent.VK_B);
        newBookItem.addActionListener(new NewBookActionListener());
        newDocumentMenu.add(newBookItem);
        JMenuItem openDocumentItem = new JMenuItem("Open Document", KeyEvent.VK_O);
        openDocumentItem.addActionListener(new OpenDocumentActionListener());
        JMenuItem viewDocumentItem = new JMenuItem("View Book Info", KeyEvent.VK_V);
        viewDocumentItem.addActionListener(new ViewBookActionListener());
        JMenuItem editDocumentItem = new JMenuItem("Edit Book Info", KeyEvent.VK_E);
        editDocumentItem.addActionListener(new EditBookActionListener());
        JMenuItem deleteDocumentItem = new JMenuItem("Delete This Book", KeyEvent.VK_D);
        deleteDocumentItem.addActionListener(new DeleteBookActionListener());
        JMenuItem exportDocumentItem = new JMenuItem("Export", KeyEvent.VK_P);
        exportDocumentItem.addActionListener(new ExportBookActionListener());
        JMenuItem saveAllItem = new JMenuItem("Save All", KeyEvent.VK_S);
        saveAllItem.addActionListener(new SaveAllActoinListener());
        bookMenu.add(newDocumentMenu);
        bookMenu.add(openDocumentItem);
        bookMenu.add(viewDocumentItem);
        bookMenu.add(editDocumentItem);
        bookMenu.add(deleteDocumentItem);
        bookMenu.add(exportDocumentItem);
        bookMenu.add(saveAllItem);

        chapterMenu.setMnemonic(KeyEvent.VK_C);
        JMenuItem newChapterItem = new JMenuItem("New Chapter", KeyEvent.VK_N);
        newChapterItem.addActionListener(new NewChapterActionListener());
        JMenuItem editChapterItem = new JMenuItem("Edit Chapter", KeyEvent.VK_E);
        editChapterItem.addActionListener(new EditChapterActionListener());
        JMenuItem deleteChapterItem = new JMenuItem("Delete Chapter", KeyEvent.VK_D);
        deleteChapterItem.addActionListener(new DeleteChapterActionListener());
        chapterMenu.add(newChapterItem);
        chapterMenu.add(editChapterItem);
        chapterMenu.add(deleteChapterItem);

        noteMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newNoteItem = new JMenuItem("New Note", KeyEvent.VK_N);
        newNoteItem.addActionListener(new NewBookNoteActionListener());
        JMenuItem viewNoteItem = new JMenuItem("View This Note", KeyEvent.VK_V);
        viewNoteItem.addActionListener(new ViewBookNoteActionListener());
        JMenuItem editNoteItem = new JMenuItem("Edit This Note", KeyEvent.VK_E);
        editNoteItem.addActionListener(new EditBookNoteActionListener());
        JMenuItem deleteNoteItem = new JMenuItem("Delete This Note", KeyEvent.VK_D);
        deleteNoteItem.addActionListener(new DeleteBookNoteActionListener());
        noteMenu.add(newNoteItem);
        noteMenu.add(viewNoteItem);
        noteMenu.add(editNoteItem);
        noteMenu.add(deleteNoteItem);

        searchMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchNoteItem = new JMenuItem("Search Notes", KeyEvent.VK_N);
        searchNoteItem.addActionListener(new SearchNoteActionListener());
        searchMenu.add(searchNoteItem);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem aboutItem = new JMenuItem("About EasyNote", KeyEvent.VK_A);
        aboutItem.addActionListener(new AboutActionListener());
        JMenuItem preferencesItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesItem.addActionListener(new OpenPreferencesActionListener());
        helpMenu.add(aboutItem);
        helpMenu.add(preferencesItem);

        menuBar.add(bookMenu);
        menuBar.add(chapterMenu);
        menuBar.add(noteMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Creates chapter scroll pane with current book's chapter data.
     */
    private void createChapterScrollPane() {
        int chaptersNumber = BookHome.get().getCurrentBook().getChaptersMap().size();
        int counter = 0;
        String[] chaptersTitle = new String[chaptersNumber];
        for (Map.Entry<Long, Chapter> entry : BookHome.get().getCurrentBook().getChaptersMap()
                .entrySet()) {
            BookHome.get().getCurrentChapterList().add(entry.getValue());
            chaptersTitle[counter] = entry.getKey() + ". " + entry.getValue().getChapterTitle();
            counter++;
        }
        JList chaptersList = new JList(chaptersTitle);
        int chaptersListWidth = 265;
        chaptersList.setCellRenderer(new ChapterListCellRenderer(chaptersListWidth));
        chaptersList.setFixedCellWidth(chaptersListWidth);
        chaptersList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chaptersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chaptersList.addListSelectionListener(new ChapterListSelectionListener());
        chaptersList.addMouseListener(new ChapterListMouseListener());
        JScrollPane chaptersScrollPane = new JScrollPane(chaptersList);
        chaptersScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
        chaptersPanel = new JPanel();
        chaptersPanel.setLayout(new BoxLayout(chaptersPanel, BoxLayout.PAGE_AXIS));
        chaptersPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chaptersPanel.add(chaptersScrollPane);
    }

    /**
     * Creates the default menu bar when no document is selected.
     */
    private void createDefaultMenuBar() {
        menuBar = new JMenuBar();
        JMenu documentMenu = new JMenu("Document");
        JMenu searchMenu = new JMenu("Search");
        JMenu helpMenu = new JMenu("Help");

        documentMenu.setMnemonic(KeyEvent.VK_D);
        JMenuItem openDocumentItem = new JMenuItem("Open Document", KeyEvent.VK_O);
        openDocumentItem.addActionListener(new OpenDocumentActionListener());
        JMenu newDocumentMenu = new JMenu("New Document");
        newDocumentMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newArticleItem = new JMenuItem("Article", KeyEvent.VK_A);
        newArticleItem.addActionListener(new NewArticleActionListener());
        newDocumentMenu.add(newArticleItem);
        JMenuItem newBookItem = new JMenuItem("Book", KeyEvent.VK_B);
        newBookItem.addActionListener(new NewBookActionListener());
        newDocumentMenu.add(newBookItem);
        JMenuItem saveAllItem = new JMenuItem("Save All", KeyEvent.VK_S);
        saveAllItem.addActionListener(new SaveAllActoinListener());
        documentMenu.add(openDocumentItem);
        documentMenu.add(newDocumentMenu);
        documentMenu.add(saveAllItem);

        searchMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchNoteItem = new JMenuItem("Search Notes", KeyEvent.VK_N);
        searchNoteItem.addActionListener(new SearchNoteActionListener());
        searchMenu.add(searchNoteItem);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem aboutItem = new JMenuItem("About EasyNote", KeyEvent.VK_A);
        aboutItem.addActionListener(new AboutActionListener());
        JMenuItem preferencesItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesItem.addActionListener(new OpenPreferencesActionListener());
        helpMenu.add(aboutItem);
        helpMenu.add(preferencesItem);

        menuBar.add(documentMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Creates an empty book note scroll pane.
     */
    private void createEmptyBookNoteScrollPane() {
        JList notesList = new JList();
        int notesListWidth = 945;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notesList.addMouseListener(new BookNoteListMouseListener());
        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
        notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.PAGE_AXIS));
        notesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        notesPanel.add(notesScrollPane);
    }

    /**
     * Gets the chapters' panel.
     *
     * @return {@code JPanel} The chapters' panel.
     */
    public JPanel getChaptersPanel() {
        return chaptersPanel;
    }

    /**
     * Gets the current mode.
     *
     * @return {@code SystemMode} The current mode.
     */
    public SystemMode getCurrentMode() {
        return currentMode;
    }

    /**
     * Gets the notes' panel.
     *
     * @return {@code JPanel} The notes' panel.
     */
    public JPanel getNotesPane() {
        return notesPanel;
    }

    /**
     * Sets up the panel when opening an article.
     *
     * @param article The article that is being opened.
     */
    public void setArticlePanel(Article article) {
        clearAllTemporaryData();
        ArticleHome home = ArticleHome.get();

        // Set current mode to "Article".
        setCurrentMode(SystemMode.ARTICLE);

        // Change panel's title.
        setTitle(article.getDocumentTitle());

        // Clear deprecated components.
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            remove(com);
        }

        // Update temporary data in article home.
        home.updateTemporaryData(article.getDocumentId(), null);

        // Set up the menu bar
        createArticleMenuBar();

        // Set up the note scroll pane.
        updateArticleNotePanel();

        // Put everything together, using the content pane's BorderLayout.
        add(menuBar, BorderLayout.NORTH);
        add(notesPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    /**
     * Sets up the panel when opening a book.
     *
     * @param book The book that is being opened.
     */
    public void setBookPanel(Book book) {
        clearAllTemporaryData();
        BookHome home = BookHome.get();

        // Set current mode to "Book".
        setCurrentMode(SystemMode.BOOK);

        // Change panel's title.
        setTitle(book.getDocumentTitle());

        // Clear deprecated components.
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            remove(com);
        }

        // Update temporary data in book home.
        home.updateTemporaryData(book.getDocumentId(), null, null);

        // Set up the menu bar
        createBookMenuBar();

        // Set up the chapter scroll pane.
        createChapterScrollPane();

        // Create an empty note scroll pane.
        createEmptyBookNoteScrollPane();

        // Put everything together, using the content pane's BorderLayout.
        add(menuBar, BorderLayout.NORTH);
        add(chaptersPanel, BorderLayout.WEST);
        add(notesPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    /**
     * Sets the chapters' panel.
     *
     * @param chaptersPanel The chapters' panel to set.
     */
    public void setChaptersPanel(JPanel chaptersPanel) {
        this.chaptersPanel = chaptersPanel;
    }

    /**
     * Sets the current mode.
     *
     * @param currentMode The current mode to set
     */
    public void setCurrentMode(SystemMode currentMode) {
        this.currentMode = currentMode;
    }

    /**
     * Sets up the default panel when no document is opened.
     */
    public void setDefaultPanel() {
        // Clear deprecated components.
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            remove(com);
        }

        // Set up the default menu bar.
        createDefaultMenuBar();

        add(menuBar, BorderLayout.NORTH);

        setTitle("EasyNote - Take Your Note Today!");

        validate();
        repaint();
    }

    /**
     * Sets the notes' panel.
     *
     * @param notesPanel The notes' panel to set.
     */
    public void setNotesPanel(JPanel notesPanel) {
        this.notesPanel = notesPanel;
    }

    /**
     * Updates the article note panel with the current temporary data. No note is selected.
     */
    public void updateArticleNotePanel() {
        ArticleHome home = ArticleHome.get();
        if (notesPanel != null) {
            remove(notesPanel);
        }
        // createEmptyBookNoteScrollPane();

        // Get current notes data.
        Object[] notesObject = new ArticleNote[home.getCurrentArticleNotesList().size()];
        for (int i = 0; i < home.getCurrentArticleNotesList().size(); i++) {
            notesObject[i] = home.getCurrentArticleNotesList().get(i);
        }

        // Create note scroll pane for each chapter.
        JList notesList = new JList(notesObject);
        int notesListWidth = 1235;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new ArticleNoteListMouseListener());
        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));

        JPanel notesPane = new JPanel();
        notesPane.setLayout(new BoxLayout(notesPane, BoxLayout.PAGE_AXIS));
        notesPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesPane.add(notesScrollPane);

        setNotesPanel(notesPane);
        add(notesPane, BorderLayout.CENTER);

        home.setCurrentArticleNote(null);

        add(notesPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

    /**
     * Update the book note panel with the current temporary data. No temporary data is reloaded.
     */
    public void updateBookNotePanel() {
        BookHome home = BookHome.get();
        remove(notesPanel);

        // Get current notes data.
        Object[] notesObject = new BookNote[home.getCurrentBookNotesList().size()];
        for (int i = 0; i < home.getCurrentBookNotesList().size(); i++) {
            notesObject[i] = home.getCurrentBookNotesList().get(i);
        }

        // Create note scroll pane for each chapter.
        JList notesList = new JList(notesObject);
        int notesListWidth = 945;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new BookNoteListMouseListener());
        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));

        JPanel notesPane = new JPanel();
        notesPane.setLayout(new BoxLayout(notesPane, BoxLayout.PAGE_AXIS));
        notesPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        notesPane.add(notesScrollPane);

        setNotesPanel(notesPane);
        add(notesPane, BorderLayout.CENTER);

        home.setCurrentBookNote(null);

        validate();
    }

    /**
     * Update the book note panel with the provided chapter. Reload some temporary data is reloaded
     * before the update.
     *
     * @param currentChapter The chapter that the update is based on.
     */
    public void updateBookNotePanel(Chapter currentChapter) {
        BookHome home = BookHome.get();
        remove(notesPanel);

        // Get current notes data.
        home.setCurrentChapter(currentChapter);
        List<BookNote> notesDataList = home.getCurrentChapterNotesMap().get(
                currentChapter.getChapterId());
        home.setCurrentBookNotesList(notesDataList);
        Object[] notesObject = new BookNote[home.getCurrentBookNotesList().size()];
        for (int i = 0; i < home.getCurrentBookNotesList().size(); i++) {
            notesObject[i] = home.getCurrentBookNotesList().get(i);
        }

        // Create note scroll pane for each chapter.
        JList notesList = new JList(notesObject);
        int notesListWidth = 945;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new BookNoteListMouseListener());
        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));

        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.PAGE_AXIS));
        notesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        notesPanel.add(notesScrollPane);

        setNotesPanel(notesPanel);
        add(notesPanel, BorderLayout.CENTER);

        home.setCurrentBookNote(null);

        validate();
    }

    /**
     * Updates the chapter panel with the current temporary data, and creates a new empty note
     * panel. No chapter or note is selected.
     */
    public void updateChapterPanel() {
        remove(chaptersPanel);
        remove(notesPanel);
        createChapterScrollPane();
        createEmptyBookNoteScrollPane();
        add(chaptersPanel, BorderLayout.WEST);
        add(notesPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

}

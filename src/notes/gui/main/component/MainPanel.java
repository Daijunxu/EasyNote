package notes.gui.main.component;

import lombok.Getter;
import lombok.Setter;
import notes.businesslogic.ArticleBusinessLogic;
import notes.businesslogic.BookBusinessLogic;
import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.SystemMode;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;
import notes.businessobjects.book.Book;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.book.Chapter;
import notes.businessobjects.workset.Workset;
import notes.businessobjects.workset.Worksheet;
import notes.businessobjects.workset.WorksheetNote;
import notes.dao.impl.DocumentNoteDAO;
import notes.data.cache.CacheDelegate;
import notes.data.persistence.Property;
import notes.gui.article.event.DeleteArticleActionListener;
import notes.gui.article.event.DeleteArticleNoteActionListener;
import notes.gui.article.event.EditArticleActionListener;
import notes.gui.article.event.EditArticleNoteActionListener;
import notes.gui.article.event.ExportArticleEventListener;
import notes.gui.article.event.NewArticleActionListener;
import notes.gui.article.event.NewArticleNoteActionListener;
import notes.gui.book.component.ChapterListCellRenderer;
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
import notes.gui.main.event.AboutActionListener;
import notes.gui.main.event.MainPanelComponentListener;
import notes.gui.main.event.MainPanelWindowListener;
import notes.gui.main.event.MainPanelWindowStateListener;
import notes.gui.main.event.OpenPreferencesActionListener;
import notes.gui.main.event.document.CloseDocumentEventListener;
import notes.gui.main.event.document.OpenDocumentActionListener;
import notes.gui.main.event.mouse.IndexListMouseListener;
import notes.gui.main.event.mouse.NoteListMouseListener;
import notes.gui.main.event.note.NoteListSelectionListener;
import notes.gui.main.event.search.SearchNoteActionListener;
import notes.gui.workset.component.WorksheetListCellRenderer;
import notes.gui.workset.component.WorksheetNoteListCellRenderer;
import notes.gui.workset.event.DeleteWorksetActionListener;
import notes.gui.workset.event.DeleteWorksheetActionListener;
import notes.gui.workset.event.DeleteWorksheetNoteActionListener;
import notes.gui.workset.event.EditWorksetActionListener;
import notes.gui.workset.event.EditWorksheetActionListener;
import notes.gui.workset.event.EditWorksheetNoteActionListener;
import notes.gui.workset.event.ExportWorksetActionListener;
import notes.gui.workset.event.NewWorksetActionListener;
import notes.gui.workset.event.NewWorksheetActionListener;
import notes.gui.workset.event.NewWorksheetNoteActionListener;
import notes.gui.workset.event.WorksheetListSelectionListener;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

/**
 * The main panel.
 * <p/>
 * Author: Rui Du
 */
public class MainPanel extends JFrame {

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 800;
    private static final int BOOK_CHAPTER_LIST_WIDTH = 265;
    private static final int WORKSET_WORKSHEET_LIST_WIDTH = 265;
    private static final int ARTICLE_NOTE_LIST_PANEL_WIDTH_INDENTATION = 40; // For Ubuntu this value should be 28.
    private static final int BOOK_NOTE_LIST_PANEL_WIDTH_INDENTATION = 342; // For Ubuntu this value should be 328.
    private static final int WORKSHEET_NOTE_LIST_PANEL_WIDTH_INDENTATION = 342; // For Ubuntu this value should be 328.
    private static final int BOOK_NOTE_LIST_WIDTH = 945;
    private static final int WORKSHEET_NOTE_LIST_WIDTH = 945;

    private static final WorksetBusinessLogic worksetHome = WorksetBusinessLogic.get();
    private static final BookBusinessLogic bookHome = BookBusinessLogic.get();
    private static final ArticleBusinessLogic articleHome = ArticleBusinessLogic.get();

    private static final int PORT = 9999;

    /**
     * The single instance of {@code MainPanel}.
     */
    public static final MainPanel instance = new MainPanel();
    /**
     * The current mode indicating the type of the current opened document.
     */
    @Getter
    @Setter
    private SystemMode currentMode;
    /**
     * The flag of whether search dialog is popped  up.
     */
    @Getter
    @Setter
    private boolean isSearchMode;
    /**
     * The menu bar on the main panel that contains different operations for this application.
     */
    private JMenuBar menuBar;
    /**
     * The index panel that contains the information of chapters/worksheets in a book/workSet.
     */
    @Getter
    @Setter
    private JPanel indexPanel;
    /**
     * The note panel that contains the information of notes in current document/chapter.
     */
    @Getter
    @Setter
    private JPanel notesPanel;

    /**
     * Constructs an instance of {@code MainPanel}.
     */
    private MainPanel() {

        setDefaultPanel();

        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        addWindowListener(new MainPanelWindowListener());
        addWindowStateListener(new MainPanelWindowStateListener());
        addComponentListener(new MainPanelComponentListener());
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        // Fix the form to locate in the middle of screen.

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - DEFAULT_WIDTH) / 2;
        int y = (screen.height - DEFAULT_HEIGHT) / 2;
        setBounds(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

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

        // Bind the port so that only one application is running at a time.
        try {
            new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
        } catch (BindException e) {
            System.err.println("Already running.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unexpected error.");
            e.printStackTrace();
            System.exit(2);
        }

        Property property = Property.get();

        if (CacheDelegate.get() == null || CacheDelegate.hasProblem) {
            System.out.println("CacheDelegate is having problem!");
            SoundFactory.playNotify();
            int result = JOptionPane.showConfirmDialog(null,
                    "Missing data file or having invalid data file. Choose a new data file?",
                    "Confirm Dialog", JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                SoundFactory.playPopup();
                new InitialChooseDataLocationDialog();
            } else {
                System.exit(0);
            }
        } else {
            // Data has loaded successfully.

            SoundFactory.playOn();

            if (property.isShowLastDocumentOnOpening() && property.getLastOpenedDocumentId() != null) {
                MainPanel.get().openLastDocument(property.getLastOpenedDocumentId());
            }

            MainPanel.get().setVisible(true);
            MainPanel.get().setLocation(MainPanel.get().getLocationOnScreen());

            while (true) {
                try {
                    Thread.sleep(1000);
                    if (CacheDelegate.get().isCacheChanged()) {
                        CacheDelegate.get().resetCacheChanged();
                        CacheDelegate.get().saveAllCachesToXML();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void openLastDocument(Long documentId) {
        Document document = DocumentNoteDAO.get().findDocumentById(documentId);
        if (document instanceof Workset) {
            MainPanel.get().setWorksetPanel((Workset) document, null);
        } else if (document instanceof Book) {
            MainPanel.get().setBookPanel((Book) document, null);
        } else if (document instanceof Article) {
            MainPanel.get().setArticlePanel((Article) document, null);
        }
    }

    /**
     * Clears all temporary data in logic beans.
     */
    public void clearAllTemporaryData() {
        articleHome.clearAllTemporaryData();
        bookHome.clearAllTemporaryData();
        worksetHome.clearAllTemporaryData();
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
        JMenuItem newWorksetItem = new JMenuItem("Workset", KeyEvent.VK_W);
        newWorksetItem.addActionListener(new NewWorksetActionListener());
        newDocumentMenu.add(newWorksetItem);
        JMenuItem newArticleItem = new JMenuItem("Article", KeyEvent.VK_A);
        newArticleItem.addActionListener(new NewArticleActionListener());
        newDocumentMenu.add(newArticleItem);
        JMenuItem newBookItem = new JMenuItem("Book", KeyEvent.VK_B);
        newBookItem.addActionListener(new NewBookActionListener());
        newDocumentMenu.add(newBookItem);
        JMenuItem openDocumentItem = new JMenuItem("Open Document", KeyEvent.VK_O);
        openDocumentItem.addActionListener(new OpenDocumentActionListener());
        JMenuItem editDocumentItem = new JMenuItem("Article Information", KeyEvent.VK_I);
        editDocumentItem.addActionListener(new EditArticleActionListener());
        JMenuItem deleteDocumentItem = new JMenuItem("Delete This Article", KeyEvent.VK_D);
        deleteDocumentItem.addActionListener(new DeleteArticleActionListener());
        JMenuItem exportDocumentItem = new JMenuItem("Export", KeyEvent.VK_P);
        exportDocumentItem.addActionListener(new ExportArticleEventListener());
        JMenuItem closeItem = new JMenuItem("Close Article", KeyEvent.VK_C);
        closeItem.addActionListener(new CloseDocumentEventListener());
        articleMenu.add(newDocumentMenu);
        articleMenu.add(openDocumentItem);
        articleMenu.add(editDocumentItem);
        articleMenu.add(deleteDocumentItem);
        articleMenu.add(exportDocumentItem);
        articleMenu.add(closeItem);

        noteMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newNoteItem = new JMenuItem("New Note", KeyEvent.VK_N);
        newNoteItem.addActionListener(new NewArticleNoteActionListener());
        JMenuItem editNoteItem = new JMenuItem("Edit This Note", KeyEvent.VK_E);
        editNoteItem.addActionListener(new EditArticleNoteActionListener());
        JMenuItem deleteNoteItem = new JMenuItem("Delete This Note", KeyEvent.VK_D);
        deleteNoteItem.addActionListener(new DeleteArticleNoteActionListener());
        noteMenu.add(newNoteItem);
        noteMenu.add(editNoteItem);
        noteMenu.add(deleteNoteItem);

        searchMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchNoteItem = new JMenuItem("Search Notes", KeyEvent.VK_N);
        searchNoteItem.addActionListener(new SearchNoteActionListener());
        searchMenu.add(searchNoteItem);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem preferencesItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesItem.addActionListener(new OpenPreferencesActionListener());
        JMenuItem aboutItem = new JMenuItem("About EasyNote", KeyEvent.VK_A);
        aboutItem.addActionListener(new AboutActionListener());
        helpMenu.add(preferencesItem);
        helpMenu.add(aboutItem);

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
        JMenuItem newWorksetItem = new JMenuItem("Workset", KeyEvent.VK_W);
        newWorksetItem.addActionListener(new NewWorksetActionListener());
        newDocumentMenu.add(newWorksetItem);
        JMenuItem newArticleItem = new JMenuItem("Article", KeyEvent.VK_A);
        newArticleItem.addActionListener(new NewArticleActionListener());
        newDocumentMenu.add(newArticleItem);
        JMenuItem newBookItem = new JMenuItem("Book", KeyEvent.VK_B);
        newBookItem.addActionListener(new NewBookActionListener());
        newDocumentMenu.add(newBookItem);
        JMenuItem openDocumentItem = new JMenuItem("Open Document", KeyEvent.VK_O);
        openDocumentItem.addActionListener(new OpenDocumentActionListener());
        JMenuItem editDocumentItem = new JMenuItem("Book Information", KeyEvent.VK_I);
        editDocumentItem.addActionListener(new EditBookActionListener());
        JMenuItem deleteDocumentItem = new JMenuItem("Delete This Book", KeyEvent.VK_D);
        deleteDocumentItem.addActionListener(new DeleteBookActionListener());
        JMenuItem exportDocumentItem = new JMenuItem("Export", KeyEvent.VK_P);
        exportDocumentItem.addActionListener(new ExportBookActionListener());
        JMenuItem closeItem = new JMenuItem("Close Book", KeyEvent.VK_C);
        closeItem.addActionListener(new CloseDocumentEventListener());
        bookMenu.add(newDocumentMenu);
        bookMenu.add(openDocumentItem);
        bookMenu.add(editDocumentItem);
        bookMenu.add(deleteDocumentItem);
        bookMenu.add(exportDocumentItem);
        bookMenu.add(closeItem);

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
        JMenuItem editNoteItem = new JMenuItem("Edit This Note", KeyEvent.VK_E);
        editNoteItem.addActionListener(new EditBookNoteActionListener());
        JMenuItem deleteNoteItem = new JMenuItem("Delete This Note", KeyEvent.VK_D);
        deleteNoteItem.addActionListener(new DeleteBookNoteActionListener());
        noteMenu.add(newNoteItem);
        noteMenu.add(editNoteItem);
        noteMenu.add(deleteNoteItem);

        searchMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchNoteItem = new JMenuItem("Search Notes", KeyEvent.VK_N);
        searchNoteItem.addActionListener(new SearchNoteActionListener());
        searchMenu.add(searchNoteItem);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem preferencesItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesItem.addActionListener(new OpenPreferencesActionListener());
        JMenuItem aboutItem = new JMenuItem("About EasyNote", KeyEvent.VK_A);
        aboutItem.addActionListener(new AboutActionListener());
        helpMenu.add(preferencesItem);
        helpMenu.add(aboutItem);

        menuBar.add(bookMenu);
        menuBar.add(chapterMenu);
        menuBar.add(noteMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Creates the menu bar when a workset is opened.
     */
    private void createWorksetMenuBar() {
        menuBar = new JMenuBar();
        JMenu worksetMenu = new JMenu("Workset");
        JMenu worksheetMenu = new JMenu("Worksheet");
        JMenu noteMenu = new JMenu("Note");
        JMenu searchMenu = new JMenu("Search");
        JMenu helpMenu = new JMenu("Help");

        worksetMenu.setMnemonic(KeyEvent.VK_W);
        JMenu newDocumentMenu = new JMenu("New Document");
        newDocumentMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newWorksetItem = new JMenuItem("Workset", KeyEvent.VK_W);
        newWorksetItem.addActionListener(new NewWorksetActionListener());
        newDocumentMenu.add(newWorksetItem);
        JMenuItem newArticleItem = new JMenuItem("Article", KeyEvent.VK_A);
        newArticleItem.addActionListener(new NewArticleActionListener());
        newDocumentMenu.add(newArticleItem);
        JMenuItem newBookItem = new JMenuItem("Book", KeyEvent.VK_B);
        newBookItem.addActionListener(new NewBookActionListener());
        newDocumentMenu.add(newBookItem);
        JMenuItem openDocumentItem = new JMenuItem("Open Document", KeyEvent.VK_O);
        openDocumentItem.addActionListener(new OpenDocumentActionListener());
        JMenuItem editDocumentItem = new JMenuItem("Workset Information", KeyEvent.VK_I);
        editDocumentItem.addActionListener(new EditWorksetActionListener());
        JMenuItem deleteDocumentItem = new JMenuItem("Delete This Workset", KeyEvent.VK_D);
        deleteDocumentItem.addActionListener(new DeleteWorksetActionListener());
        JMenuItem exportDocumentItem = new JMenuItem("Export", KeyEvent.VK_P);
        exportDocumentItem.addActionListener(new ExportWorksetActionListener());
        JMenuItem closeItem = new JMenuItem("Close Workset", KeyEvent.VK_C);
        closeItem.addActionListener(new CloseDocumentEventListener());
        worksetMenu.add(newDocumentMenu);
        worksetMenu.add(openDocumentItem);
        worksetMenu.add(editDocumentItem);
        worksetMenu.add(deleteDocumentItem);
        worksetMenu.add(exportDocumentItem);
        worksetMenu.add(closeItem);

        worksheetMenu.setMnemonic(KeyEvent.VK_E);
        JMenuItem newWorksheetItem = new JMenuItem("New Worksheet", KeyEvent.VK_N);
        newWorksheetItem.addActionListener(new NewWorksheetActionListener());
        JMenuItem editWorksheetItem = new JMenuItem("Edit Worksheet", KeyEvent.VK_E);
        editWorksheetItem.addActionListener(new EditWorksheetActionListener());
        JMenuItem deleteWorksheetItem = new JMenuItem("Delete Worksheet", KeyEvent.VK_D);
        deleteWorksheetItem.addActionListener(new DeleteWorksheetActionListener());
        worksheetMenu.add(newWorksheetItem);
        worksheetMenu.add(editWorksheetItem);
        worksheetMenu.add(deleteWorksheetItem);

        noteMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newNoteItem = new JMenuItem("New Note", KeyEvent.VK_N);
        newNoteItem.addActionListener(new NewWorksheetNoteActionListener());
        JMenuItem editNoteItem = new JMenuItem("Edit This Note", KeyEvent.VK_E);
        editNoteItem.addActionListener(new EditWorksheetNoteActionListener());
        JMenuItem deleteNoteItem = new JMenuItem("Delete This Note", KeyEvent.VK_D);
        deleteNoteItem.addActionListener(new DeleteWorksheetNoteActionListener());
        noteMenu.add(newNoteItem);
        noteMenu.add(editNoteItem);
        noteMenu.add(deleteNoteItem);

        searchMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchNoteItem = new JMenuItem("Search Notes", KeyEvent.VK_N);
        searchNoteItem.addActionListener(new SearchNoteActionListener());
        searchMenu.add(searchNoteItem);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem preferencesItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesItem.addActionListener(new OpenPreferencesActionListener());
        JMenuItem aboutItem = new JMenuItem("About EasyNote", KeyEvent.VK_A);
        aboutItem.addActionListener(new AboutActionListener());
        helpMenu.add(preferencesItem);
        helpMenu.add(aboutItem);

        menuBar.add(worksetMenu);
        menuBar.add(worksheetMenu);
        menuBar.add(noteMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Creates chapter scroll panel.
     */
    private void createChapterScrollPane(int width, Long selectedChapterId) {
        int chaptersNumber = bookHome.getCurrentBook().getChaptersMap().size();
        int counter = 0;
        String[] chaptersTitle = new String[chaptersNumber];
        for (Map.Entry<Long, Chapter> entry : bookHome.getCurrentBook().getChaptersMap().entrySet()) {
            chaptersTitle[counter] = entry.getKey() + ". " + entry.getValue().getChapterTitle();
            counter++;
        }
        JList chaptersList = new JList(chaptersTitle);
        if (selectedChapterId != null) {
            chaptersList.setSelectedIndex(bookHome.getIndexForChapter(selectedChapterId));
        }
        chaptersList.setCellRenderer(new ChapterListCellRenderer(width));
        chaptersList.setFixedCellWidth(width);
        chaptersList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chaptersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chaptersList.addListSelectionListener(new ChapterListSelectionListener());
        chaptersList.addMouseListener(new IndexListMouseListener());
        JScrollPane chaptersScrollPane = new JScrollPane(chaptersList);
        chaptersScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
        indexPanel = new JPanel();
        indexPanel.setLayout(new BoxLayout(indexPanel, BoxLayout.PAGE_AXIS));
        indexPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        indexPanel.add(chaptersScrollPane);
    }

    /**
     * Creates worksheet scroll panel.
     */
    private void createWorksheetScrollPanel(int width, Long selectedWorksheetId) {
        Workset currentWorkset = worksetHome.getCurrentWorkset();
        int worksheetsNumber = currentWorkset.getWorksheetsMap().size();
        String[] worksheetsTitle = new String[worksheetsNumber];
        int counter = 0;
        for (Long worksheetId : currentWorkset.getWorksheetIdsList()) {
            Worksheet worksheet = currentWorkset.getWorksheetsMap().get(worksheetId);
            worksheetsTitle[counter] = worksheet.getWorksheetTitle();
            counter++;
        }
        JList worksheetsList = new JList(worksheetsTitle);
        if (selectedWorksheetId != null) {
            int worksheetIndex = worksetHome.getIndexForWorksheet(selectedWorksheetId);
            worksheetsList.setSelectedIndex(worksheetIndex);
        }
        worksheetsList.setCellRenderer(new WorksheetListCellRenderer(width));
        worksheetsList.setFixedCellWidth(width);
        worksheetsList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        worksheetsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        worksheetsList.addListSelectionListener(new WorksheetListSelectionListener());
        worksheetsList.addMouseListener(new IndexListMouseListener());
        JScrollPane worksheetsScrollPane = new JScrollPane(worksheetsList);
        worksheetsScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
        indexPanel = new JPanel();
        indexPanel.setLayout(new BoxLayout(indexPanel, BoxLayout.PAGE_AXIS));
        indexPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        indexPanel.add(worksheetsScrollPane);
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
        JMenuItem newWorksetItem = new JMenuItem("Workset", KeyEvent.VK_W);
        newWorksetItem.addActionListener(new NewWorksetActionListener());
        newDocumentMenu.add(newWorksetItem);
        JMenuItem newArticleItem = new JMenuItem("Article", KeyEvent.VK_A);
        newArticleItem.addActionListener(new NewArticleActionListener());
        newDocumentMenu.add(newArticleItem);
        JMenuItem newBookItem = new JMenuItem("Book", KeyEvent.VK_B);
        newBookItem.addActionListener(new NewBookActionListener());
        newDocumentMenu.add(newBookItem);
        documentMenu.add(openDocumentItem);
        documentMenu.add(newDocumentMenu);

        searchMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchNoteItem = new JMenuItem("Search Notes", KeyEvent.VK_N);
        searchNoteItem.addActionListener(new SearchNoteActionListener());
        searchMenu.add(searchNoteItem);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem preferencesItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesItem.addActionListener(new OpenPreferencesActionListener());
        JMenuItem aboutItem = new JMenuItem("About EasyNote", KeyEvent.VK_A);
        aboutItem.addActionListener(new AboutActionListener());
        helpMenu.add(preferencesItem);
        helpMenu.add(aboutItem);

        menuBar.add(documentMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Creates an empty note scroll pane.
     */
    private void createEmptyNoteScrollPane(int notesListWidth) {
        JList notesList = new JList();
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notesList.addMouseListener(new NoteListMouseListener());

        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
        notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.PAGE_AXIS));
        notesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        notesPanel.add(notesScrollPane);
    }

    /**
     * Sets up the panel when opening an article.
     *
     * @param article The article that is being opened.
     */
    public void setArticlePanel(Article article, Long selectedNoteId) {
        // Update temporary data in article logic.
        articleHome.updateTemporaryData(article.getDocumentId(), selectedNoteId);

        // Set current mode to "Article".
        setCurrentMode(SystemMode.ARTICLE);

        // Change panel's title.
        setTitle(article.getDocumentTitle());

        // Clear deprecated components.
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            remove(com);
        }

        // Set up the menu bar
        createArticleMenuBar();

        // Set up the note scroll pane.
        updateArticleNotePanel(selectedNoteId);

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
    public void setBookPanel(Book book, Long selectedChapterId) {
        clearAllTemporaryData();

        // Set current mode to "Book".
        setCurrentMode(SystemMode.BOOK);

        // Change panel's title.
        setTitle(book.getDocumentTitle());

        // Clear deprecated components.
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            remove(com);
        }

        // Update temporary data in book logic.
        bookHome.updateTemporaryData(book.getDocumentId(), selectedChapterId, null);

        // Set up the menu bar
        createBookMenuBar();

        // Set up the chapter scroll panel.
        createChapterScrollPane(BOOK_CHAPTER_LIST_WIDTH, selectedChapterId);

        // Create an empty note scroll pane.
        createEmptyNoteScrollPane(BOOK_NOTE_LIST_WIDTH);

        // Put everything together, using the content pane's BorderLayout.
        add(menuBar, BorderLayout.NORTH);
        add(indexPanel, BorderLayout.WEST);
        add(notesPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    /**
     * Sets up the panel when opening a workset.
     *
     * @param workset The workset that is being opened.
     */
    public void setWorksetPanel(Workset workset, Long selectedWorksheetId) {
        clearAllTemporaryData();

        // Set current mode to "Workset".
        setCurrentMode(SystemMode.WORKSET);

        // Change panel's title.
        setTitle(workset.getDocumentTitle());

        // Clear deprecated components.
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            remove(com);
        }

        // Update temporary data in workset logic.
        worksetHome.updateTemporaryData(workset.getDocumentId(), selectedWorksheetId, null);

        // Set up the menu bar
        createWorksetMenuBar();

        // Set up the worksheet scroll panel.
        createWorksheetScrollPanel(WORKSET_WORKSHEET_LIST_WIDTH, selectedWorksheetId);

        // Create an empty note scroll pane.
        createEmptyNoteScrollPane(WORKSHEET_NOTE_LIST_WIDTH);

        // Put everything together, using the content pane's BorderLayout.
        add(menuBar, BorderLayout.NORTH);
        add(indexPanel, BorderLayout.WEST);
        add(notesPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    /**
     * Sets up the default panel when no document is opened.
     */
    public void setDefaultPanel() {
        // Clear all temporary data in logic objects.
        ArticleBusinessLogic.get().clearAllTemporaryData();
        BookBusinessLogic.get().clearAllTemporaryData();
        WorksetBusinessLogic.get().clearAllTemporaryData();

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
     * Updates the article note panel with the current temporary data. No note is selected.
     */
    public void updateArticleNotePanel(Long selectedNoteId) {
        if (notesPanel != null) {
            remove(notesPanel);
        }

        articleHome.updateTemporaryData(articleHome.getCurrentArticle().getDocumentId(), selectedNoteId);

        // Get current notes data.
        List<Note> notesDataList = articleHome.getAllNotesForCurrentArticle();
        Object[] notesObject = new ArticleNote[notesDataList.size()];
        for (int i = 0; i < notesDataList.size(); i++) {
            notesObject[i] = notesDataList.get(i);
        }

        // Create note scroll pane for the article.
        JList notesList = new JList(notesObject);

        if (selectedNoteId != null) {
            notesList.setSelectedIndex(articleHome.getIndexForNote(selectedNoteId));
        }

        int notesListWidth = getWidth() - ARTICLE_NOTE_LIST_PANEL_WIDTH_INDENTATION;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new NoteListMouseListener());
        if (articleHome.getCurrentNote() != null) {
            notesList.setSelectedValue(articleHome.getCurrentNote(), false);
        }
        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));

        JPanel notesPane = new JPanel();
        notesPane.setLayout(new BoxLayout(notesPane, BoxLayout.PAGE_AXIS));
        notesPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesPane.add(notesScrollPane);

        setNotesPanel(notesPane);
        add(notesPane, BorderLayout.CENTER);

        add(notesPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

    /**
     * Update the book note panel with the provided chapter. Reload some temporary data is reloaded
     * before the update.
     *
     * @param currentChapter The chapter that the update is based on.
     */
    public void updateBookNotePanel(Chapter currentChapter, Long selectedNoteId) {
        remove(notesPanel);

        JList notesList = new JList();

        // Set the notes panel for current chapter.
        if (currentChapter != null) {
            // Update temporary data in the BookBusinessLogic.
            bookHome.updateTemporaryData(bookHome.getCurrentBook().getDocumentId(), currentChapter.getChapterId(),
                    selectedNoteId);
            List<BookNote> notesDataList = bookHome.getNotesListForCurrentChapter();

            // Get current notes data.
            Object[] notesObject = new BookNote[notesDataList.size()];
            for (int i = 0; i < notesDataList.size(); i++) {
                notesObject[i] = notesDataList.get(i);
            }
            notesList.setListData(notesObject);

            if (selectedNoteId != null) {
                notesList.setSelectedIndex(bookHome.getIndexForNote(selectedNoteId));
            }
        }

        // Create note scroll pane for all chapters in the book.
        int notesListWidth = getWidth() - BOOK_NOTE_LIST_PANEL_WIDTH_INDENTATION;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new NoteListMouseListener());

        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));

        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.PAGE_AXIS));
        notesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        notesPanel.add(notesScrollPane);

        setNotesPanel(notesPanel);
        add(notesPanel, BorderLayout.CENTER);

        validate();
    }

    /**
     * Update the worksheet note panel with the provided chapter. Reload some temporary data is reloaded
     * before the update.
     *
     * @param currentWorksheet The worksheet that the update is based on.
     */
    public void updateWorksheetNotePanel(Worksheet currentWorksheet, Long selectedNoteId) {
        remove(notesPanel);

        JList notesList = new JList();

        // Set the notes panel for current worksheet.
        if (currentWorksheet != null) {
            // Update temporary data in the WorksetBusinessLogic.
            worksetHome.updateTemporaryData(worksetHome.getCurrentWorkset().getDocumentId(),
                    currentWorksheet.getWorksheetId(), selectedNoteId);
            List<WorksheetNote> notesDataList = worksetHome.getNotesListForCurrentWorksheet();

            // Get current notes data.
            Object[] notesObject = new WorksheetNote[notesDataList.size()];
            for (int i = 0; i < notesDataList.size(); i++) {
                notesObject[i] = notesDataList.get(i);
            }
            notesList.setListData(notesObject);

            if (selectedNoteId != null) {
                notesList.setSelectedIndex(worksetHome.getIndexForNote(selectedNoteId));
            }
        }

        // Create note scroll pane for each worksheet.
        int notesListWidth = getWidth() - WORKSHEET_NOTE_LIST_PANEL_WIDTH_INDENTATION;
        notesList.setCellRenderer(new WorksheetNoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new NoteListMouseListener());

        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));

        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.PAGE_AXIS));
        notesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        notesPanel.add(notesScrollPane);

        setNotesPanel(notesPanel);
        add(notesPanel, BorderLayout.CENTER);

        validate();
    }

    /**
     * Updates the book panel (index panel and note panel) with the current data.
     */
    public void updateBookPanel(Long documentId, Long chapterId, Long noteId) {
        remove(indexPanel);
        remove(notesPanel);
        bookHome.updateTemporaryData(documentId, chapterId, noteId);
        createChapterScrollPane(BOOK_CHAPTER_LIST_WIDTH, chapterId);
        if (chapterId == null) {
            createEmptyNoteScrollPane(BOOK_NOTE_LIST_WIDTH);
        } else {
            updateBookNotePanel(bookHome.getCurrentChapter(), noteId);
        }
        add(indexPanel, BorderLayout.WEST);
        add(notesPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

    /**
     * Updates the workset panel (index panel and note panel) with the current data.
     */
    public void updateWorksetPanel(Long documentId, Long worksheetId, Long noteId) {
        remove(indexPanel);
        remove(notesPanel);
        worksetHome.updateTemporaryData(documentId, worksheetId, noteId);
        createWorksheetScrollPanel(WORKSET_WORKSHEET_LIST_WIDTH, worksheetId);
        if (worksheetId == null) {
            createEmptyNoteScrollPane(WORKSHEET_NOTE_LIST_WIDTH);
        } else {
            updateWorksheetNotePanel(worksetHome.getCurrentWorksheet(), noteId);
        }
        add(indexPanel, BorderLayout.WEST);
        add(notesPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }
}

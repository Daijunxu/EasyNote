package notes.gui.main.component;

import lombok.Getter;
import lombok.Setter;
import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.data.cache.Cache;
import notes.data.cache.Property;
import notes.entity.Document;
import notes.entity.SystemMode;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;
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
import notes.gui.main.event.MainPanelComponentListener;
import notes.gui.main.event.MainPanelWindowListener;
import notes.gui.main.event.MainPanelWindowStateListener;
import notes.gui.main.event.NoteListSelectionListener;
import notes.gui.main.event.OpenDocumentActionListener;
import notes.gui.main.event.OpenPreferencesActionListener;
import notes.gui.main.event.SaveAllActoinListener;
import notes.gui.main.event.SearchNoteActionListener;
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
import notes.gui.workset.event.ViewWorksetActionListener;
import notes.gui.workset.event.ViewWorksheetNoteActionListener;
import notes.gui.workset.event.WorksheetListMouseListener;
import notes.gui.workset.event.WorksheetListSelectionListener;
import notes.gui.workset.event.WorksheetNoteListMouseListener;
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

    private static final WorksetHome worksetHome = WorksetHome.get();
    private static final BookHome bookHome = BookHome.get();
    private static final ArticleHome articleHome = ArticleHome.get();

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

            if (property.isShowLastDocumentOnOpening() && property.getLastOpenedDocumentId() != null) {
                MainPanel.get().openLastDocument(property.getLastOpenedDocumentId());
            }

            MainPanel.get().setVisible(true);
            MainPanel.get().setLocation(MainPanel.get().getLocationOnScreen());
        }

    }

    private void openLastDocument(Long documentId) {
        // TODO fix this.
        Document document = bookHome.getBookNoteDAO().findDocumentById(documentId);
        if (document instanceof Workset) {
            MainPanel.get().setWorksetPanel((Workset) document);
        } else if (document instanceof Book) {
            MainPanel.get().setBookPanel((Book) document);
        } else if (document instanceof Article) {
            MainPanel.get().setArticlePanel((Article) document);
        }
    }

    /**
     * Clears all temporary data in home beans.
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
        JMenuItem viewDocumentItem = new JMenuItem("View Workset Info", KeyEvent.VK_V);
        viewDocumentItem.addActionListener(new ViewWorksetActionListener());
        JMenuItem editDocumentItem = new JMenuItem("Edit Workset Info", KeyEvent.VK_E);
        editDocumentItem.addActionListener(new EditWorksetActionListener());
        JMenuItem deleteDocumentItem = new JMenuItem("Delete This Workset", KeyEvent.VK_D);
        deleteDocumentItem.addActionListener(new DeleteWorksetActionListener());
        JMenuItem exportDocumentItem = new JMenuItem("Export", KeyEvent.VK_P);
        exportDocumentItem.addActionListener(new ExportWorksetActionListener());
        JMenuItem saveAllItem = new JMenuItem("Save All", KeyEvent.VK_S);
        saveAllItem.addActionListener(new SaveAllActoinListener());
        worksetMenu.add(newDocumentMenu);
        worksetMenu.add(openDocumentItem);
        worksetMenu.add(viewDocumentItem);
        worksetMenu.add(editDocumentItem);
        worksetMenu.add(deleteDocumentItem);
        worksetMenu.add(exportDocumentItem);
        worksetMenu.add(saveAllItem);

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
        JMenuItem viewNoteItem = new JMenuItem("View This Note", KeyEvent.VK_V);
        viewNoteItem.addActionListener(new ViewWorksheetNoteActionListener());
        JMenuItem editNoteItem = new JMenuItem("Edit This Note", KeyEvent.VK_E);
        editNoteItem.addActionListener(new EditWorksheetNoteActionListener());
        JMenuItem deleteNoteItem = new JMenuItem("Delete This Note", KeyEvent.VK_D);
        deleteNoteItem.addActionListener(new DeleteWorksheetNoteActionListener());
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

        menuBar.add(worksetMenu);
        menuBar.add(worksheetMenu);
        menuBar.add(noteMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
    }

    /**
     * Creates chapter scroll panel.
     */
    private void createChapterScrollPane(int width) {
        int chaptersNumber = bookHome.getCurrentBook().getChaptersMap().size();
        int counter = 0;
        String[] chaptersTitle = new String[chaptersNumber];
        for (Map.Entry<Long, Chapter> entry : bookHome.getCurrentBook().getChaptersMap()
                .entrySet()) {
            bookHome.getCurrentChapterList().add(entry.getValue());
            chaptersTitle[counter] = entry.getKey() + ". " + entry.getValue().getChapterTitle();
            counter++;
        }
        JList chaptersList = new JList(chaptersTitle);
        chaptersList.setCellRenderer(new ChapterListCellRenderer(width));
        chaptersList.setFixedCellWidth(width);
        chaptersList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chaptersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chaptersList.addListSelectionListener(new ChapterListSelectionListener());
        chaptersList.addMouseListener(new ChapterListMouseListener());
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
    private void createWorksheetScrollPanel(int width) {
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
        worksheetsList.setCellRenderer(new WorksheetListCellRenderer(width));
        worksheetsList.setFixedCellWidth(width);
        worksheetsList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        worksheetsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        worksheetsList.addListSelectionListener(new WorksheetListSelectionListener());
        worksheetsList.addMouseListener(new WorksheetListMouseListener());
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
     * Creates an empty note scroll pane.
     */
    private void createEmptyNoteScrollPane(int notesListWidth) {
        JList notesList = new JList();
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (currentMode.equals(SystemMode.WORKSET)) {
            notesList.addMouseListener(new WorksheetNoteListMouseListener());
        } else if (currentMode.equals(SystemMode.BOOK)) {
            notesList.addMouseListener(new BookNoteListMouseListener());
        }
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
    public void setArticlePanel(Article article) {
        clearAllTemporaryData();

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
        articleHome.updateTemporaryData(article.getDocumentId(), null);

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
        bookHome.updateTemporaryData(book.getDocumentId(), null, null);

        // Set up the menu bar
        createBookMenuBar();

        // Set up the chapter scroll panel.
        createChapterScrollPane(BOOK_CHAPTER_LIST_WIDTH);

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
    public void setWorksetPanel(Workset workset) {
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

        // Update temporary data in workset home.
        worksetHome.updateTemporaryData(workset.getDocumentId(), null, null);

        // Set up the menu bar
        createWorksetMenuBar();

        // Set up the worksheet scroll panel.
        createWorksheetScrollPanel(WORKSET_WORKSHEET_LIST_WIDTH);

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
    public void updateArticleNotePanel() {
        if (notesPanel != null) {
            remove(notesPanel);
        }

        // Get current notes data.
        List<ArticleNote> notesDataList = articleHome.getAllNotesForCurrentArticle();
        Object[] notesObject = new ArticleNote[notesDataList.size()];
        for (int i = 0; i < notesDataList.size(); i++) {
            notesObject[i] = notesDataList.get(i);
        }

        // Create note scroll pane for the article.
        JList notesList = new JList(notesObject);
        int notesListWidth = getWidth() - ARTICLE_NOTE_LIST_PANEL_WIDTH_INDENTATION;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new ArticleNoteListMouseListener());
        if (articleHome.getCurrentArticleNote() != null) {
            notesList.setSelectedValue(articleHome.getCurrentArticleNote(), false);
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
    public void updateBookNotePanel(Chapter currentChapter) {
        remove(notesPanel);

        JList notesList = new JList();

        // Set the notes panel for current chapter.
        if (currentChapter != null) {
            bookHome.setCurrentChapter(currentChapter);
            List<BookNote> notesDataList = bookHome.getAllNotesForCurrentChapter();

            // Get current notes data.
            Object[] notesObject = new BookNote[notesDataList.size()];
            for (int i = 0; i < notesDataList.size(); i++) {
                notesObject[i] = notesDataList.get(i);
            }
            notesList.setListData(notesObject);
        }

        // Create note scroll pane for all chapters in the book.
        int notesListWidth = getWidth() - BOOK_NOTE_LIST_PANEL_WIDTH_INDENTATION;
        notesList.setCellRenderer(new NoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new BookNoteListMouseListener());

        if (bookHome.getCurrentBookNote() != null) {
            notesList.setSelectedValue(bookHome.getCurrentBookNote(), false);
        }

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
    public void updateWorksheetNotePanel(Worksheet currentWorksheet) {
        remove(notesPanel);

        JList notesList = new JList();

        // Set the notes panel for current chapter.
        if (currentWorksheet != null) {
            worksetHome.setCurrentWorksheet(currentWorksheet);
            List<WorksheetNote> notesDataList = worksetHome.getAllNotesForCurrentWorksheet();

            // Get current notes data.
            Object[] notesObject = new WorksheetNote[notesDataList.size()];
            for (int i = 0; i < notesDataList.size(); i++) {
                notesObject[i] = notesDataList.get(i);
            }
            notesList.setListData(notesObject);
        }

        // Create note scroll pane for each worksheet.
        int notesListWidth = getWidth() - WORKSHEET_NOTE_LIST_PANEL_WIDTH_INDENTATION;
        notesList.setCellRenderer(new WorksheetNoteListCellRenderer(notesListWidth - 7));
        notesList.setFixedCellWidth(notesListWidth);
        notesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesList.addListSelectionListener(new NoteListSelectionListener());
        notesList.addMouseListener(new WorksheetNoteListMouseListener());

        if (worksetHome.getCurrentWorksheetNote() != null) {
            notesList.setSelectedValue(worksetHome.getCurrentWorksheetNote(), false);
        }

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
     * Updates the index panel with the current temporary data, and creates a new empty note
     * panel. No item in the index panel or note in the note panel is selected.
     */
    public void updateIndexPanel() {
        remove(indexPanel);
        remove(notesPanel);
        if (currentMode.equals(SystemMode.WORKSET)) {
            createWorksheetScrollPanel(WORKSET_WORKSHEET_LIST_WIDTH);
            createEmptyNoteScrollPane(WORKSHEET_NOTE_LIST_WIDTH);
        } else if (currentMode.equals(SystemMode.BOOK)) {
            createChapterScrollPane(BOOK_CHAPTER_LIST_WIDTH);
            createEmptyNoteScrollPane(BOOK_NOTE_LIST_WIDTH);
        }
        add(indexPanel, BorderLayout.WEST);
        add(notesPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

}

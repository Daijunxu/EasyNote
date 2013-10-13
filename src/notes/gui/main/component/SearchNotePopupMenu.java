package notes.gui.main.component;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.entity.Note;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.event.ViewNoteActionListener;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pops up when right clicking a note in the result panel in search note dialog.
 *
 * @author Rui Du
 */
public class SearchNotePopupMenu extends JPopupMenu {

    private final JMenuItem viewItem;
    private final JMenuItem openDocumentItem;

    /**
     * Creates an instance of {@code SearchNotePopupMenu}.
     *
     * @param selectedNote The selected note in the search note panel.
     */
    public SearchNotePopupMenu(final Note selectedNote) {
        viewItem = new JMenuItem("View");
        viewItem.addActionListener(new ViewNoteActionListener());

        openDocumentItem = new JMenuItem("Open This Document");
        openDocumentItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                MainPanel mainPanel = MainPanel.get();
                mainPanel.setSearchMode(false);
                SearchNoteDialog.get().setVisible(false);
                if (selectedNote instanceof ArticleNote) {
                    Article article = (Article) ArticleHome.get().getArticleNoteDAO()
                            .findDocumentById(selectedNote.getDocumentId());
                    mainPanel.setArticlePanel(article);
                } else if (selectedNote instanceof BookNote) {
                    BookHome bookHome = BookHome.get();
                    Book book = (Book) bookHome.getBookNoteDAO().findDocumentById(selectedNote.getDocumentId());
                    Long chapterId = ((BookNote) selectedNote).getChapterId();
                    mainPanel.setBookPanel(book, chapterId);
                    Chapter chapter = book.getChaptersMap().get(chapterId);
                    mainPanel.updateBookNotePanel(chapter, selectedNote.getNoteId());
                } else if (selectedNote instanceof WorksheetNote) {
                    WorksetHome worksetHome = WorksetHome.get();
                    Workset workset = (Workset) worksetHome.getWorksheetNoteDAO().findDocumentById(selectedNote.getDocumentId());
                    Long worksheetId = ((WorksheetNote) selectedNote).getWorksheetId();
                    mainPanel.setWorksetPanel(workset, worksheetId);
                    Worksheet worksheet = workset.getWorksheetsMap().get(worksheetId);
                    mainPanel.updateWorksheetNotePanel(worksheet, selectedNote.getNoteId());
                }
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playUpdate();
                }
            }
        });

        add(viewItem);
        add(openDocumentItem);
    }
}

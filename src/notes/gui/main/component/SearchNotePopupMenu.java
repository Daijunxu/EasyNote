package notes.gui.main.component;

import notes.dao.impl.DocumentNoteDAO;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.article.Article;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.event.EditNoteActionListener;
import notes.utils.SoundFactory;

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
        viewItem.addActionListener(new EditNoteActionListener());

        openDocumentItem = new JMenuItem("Open This Document");
        openDocumentItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                MainPanel mainPanel = MainPanel.get();
                mainPanel.setSearchMode(false);
                SearchNoteDialog.get().setVisible(false);
                Document document = DocumentNoteDAO.get().findDocumentById(selectedNote.getDocumentId());
                if (document instanceof Article) {
                    mainPanel.setArticlePanel((Article) document, selectedNote.getNoteId());
                } else if (document instanceof Book) {
                    Long chapterId = ((BookNote) selectedNote).getChapterId();
                    mainPanel.setBookPanel((Book) document, chapterId);
                    Chapter chapter = ((Book) document).getChaptersMap().get(chapterId);
                    mainPanel.updateBookNotePanel(chapter, selectedNote.getNoteId());
                } else if (selectedNote instanceof WorksheetNote) {
                    Long worksheetId = ((WorksheetNote) selectedNote).getWorksheetId();
                    mainPanel.setWorksetPanel((Workset) document, worksheetId);
                    Worksheet worksheet = ((Workset) document).getWorksheetsMap().get(worksheetId);
                    mainPanel.updateWorksheetNotePanel(worksheet, selectedNote.getNoteId());
                }
                SoundFactory.playUpdate();
            }
        });

        add(viewItem);
        add(openDocumentItem);
    }
}

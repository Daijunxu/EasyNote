package notes.gui.main.component;

import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.data.cache.Property;
import notes.entity.Note;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.gui.main.event.EditNoteActionListener;
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
    private final JMenuItem editItem;
    private final JMenuItem openDocumentItem;

    /**
     * Creates an instance of {@code SearchNotePopupMenu}.
     *
     * @param selectedNote The selected note in the search note panel.
     */
    public SearchNotePopupMenu(final Note selectedNote) {
        viewItem = new JMenuItem("View");
        viewItem.addActionListener(new ViewNoteActionListener());

        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditNoteActionListener());

        openDocumentItem = new JMenuItem("Open This Document");
        openDocumentItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (selectedNote instanceof ArticleNote) {
                    Article article = (Article) ArticleHome.get().getArticleNoteDAO()
                            .findDocumentById(selectedNote.getDocumentId());
                    MainPanel.get().setSearchMode(false);
                    SearchNoteDialog.get().setVisible(false);
                    MainPanel.get().setArticlePanel(article);
                } else if (selectedNote instanceof BookNote) {
                    Book book = (Book) BookHome.get().getBookNoteDAO()
                            .findDocumentById(selectedNote.getDocumentId());
                    MainPanel.get().setSearchMode(false);
                    SearchNoteDialog.get().setVisible(false);
                    MainPanel.get().setBookPanel(book);
                }
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playUpdate();
                }
            }
        });

        add(viewItem);
        add(editItem);
        add(openDocumentItem);
    }
}

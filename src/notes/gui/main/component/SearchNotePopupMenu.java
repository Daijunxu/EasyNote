/**
 *
 */
package notes.gui.main.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import notes.article.entity.Article;
import notes.article.entity.ArticleNote;
import notes.bean.ArticleHome;
import notes.bean.BookHome;
import notes.book.entity.Book;
import notes.book.entity.BookNote;
import notes.data.cache.Property;
import notes.entity.Note;
import notes.gui.main.event.ViewNoteActionListener;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

/**
 * Pops up when right clicking a note in the result panel in search note dialog.
 *
 * @author Rui Du
 * @version 1.0
 */
public class SearchNotePopupMenu extends JPopupMenu {

    private static final long serialVersionUID = -4152182730661665682L;
    private JMenuItem viewItem;
    private JMenuItem openDocumentItem;

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
                if (selectedNote instanceof ArticleNote) {
                    Article article = (Article) ArticleHome.get().getArticleNoteDAO()
                            .findDocumentById(selectedNote.getDocumentId());
                    SearchNoteDialog.get().setVisible(false);
                    MainPanel.get().setArticlePanel(article);
                } else if (selectedNote instanceof BookNote) {
                    Book book = (Book) BookHome.get().getBookNoteDAO()
                            .findDocumentById(selectedNote.getDocumentId());
                    SearchNoteDialog.get().setVisible(false);
                    MainPanel.get().setBookPanel(book);
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

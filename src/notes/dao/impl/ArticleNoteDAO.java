package notes.dao.impl;

import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;
import notes.data.cache.CacheDelegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data access object for article notes.
 * <p/>
 * Author: Rui Du
 */
public class ArticleNoteDAO extends DocumentNoteDAO {

    private static final ArticleNoteDAO INSTANCE = new ArticleNoteDAO();
    private static final CacheDelegate CACHE = CacheDelegate.get();

    private ArticleNoteDAO() {
    }

    public static ArticleNoteDAO get() {
        return INSTANCE;
    }

    @Override
    public void deleteDocument(Document document) {
        Article cachedArticle = (Article) CACHE.getDocumentCache().find(document.getDocumentId());

        // Remove all notes under the document.
        for (Long noteId : cachedArticle.getNotesList()) {
            CACHE.getNoteCache().remove(noteId);
        }

        // Remove the document in the document CACHE.
        CACHE.getDocumentCache().remove(cachedArticle.getDocumentId());
    }

    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Article article = (Article) CACHE.getDocumentCache().find(note.getDocumentId());
        article.getNotesList().remove(noteId);

        // Remove the note in the note CACHE.
        CACHE.getNoteCache().remove(noteId);

        // Update article's last updated time.
        article.setLastUpdatedTime(new Date());
    }

    /**
     * Finds all articles.
     *
     * @return {@code Set<Long>} The set of document IDs.
     */
    public Set<Long> findAllArticles() {
        Set<Long> resultSet = new HashSet<Long>();
        for (Document document : CACHE.getDocumentCache().findAll()) {
            if (document instanceof Article) {
                resultSet.add(document.getDocumentId());
            }
        }
        return resultSet;
    }

    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        Article article = (Article) (CACHE.getDocumentCache().find(documentId));
        List<Note> noteList = new ArrayList<Note>();

        for (Long noteId : article.getNotesList()) {
            noteList.add(CACHE.getNoteCache().find(noteId));
        }
        Collections.sort(noteList);
        return noteList;
    }

    @Override
    public Note updateNote(Note note) {
        Article article = (Article) CACHE.getDocumentCache().find(note.getDocumentId());

        ArticleNote cachedNote = (ArticleNote) (CACHE.getNoteCache().update(note));

        // Update article's last updated time.
        article.setLastUpdatedTime(new Date());

        return cachedNote;
    }

    @Override
    public Note saveNote(Note note) {
        if (note instanceof ArticleNote) {
            ArticleNote savedNote = (ArticleNote) CACHE.getNoteCache().insert(note);

            // Add the note ID to corresponding notes list in the article.
            Article article = (Article) (CACHE.getDocumentCache().find(savedNote.getDocumentId()));
            article.getNotesList().add(savedNote.getNoteId());

            // Update article's last updated time.
            article.setLastUpdatedTime(new Date());

            return savedNote;
        }
        return null;
    }

}

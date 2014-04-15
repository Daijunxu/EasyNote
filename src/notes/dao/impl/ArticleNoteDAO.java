package notes.dao.impl;

import notes.dao.DuplicateRecordException;
import notes.data.cache.Cache;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data access object for article notes.
 * <p/>
 * Author: Rui Du
 */
public class ArticleNoteDAO extends DocumentNoteDAO {

    private static final ArticleNoteDAO INSTANCE = new ArticleNoteDAO();
    private static final Cache CACHE = Cache.get();

    private ArticleNoteDAO() {
    }

    public static ArticleNoteDAO get() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocument(Document document) {
        Article cachedArticle = (Article) CACHE.getDocumentCache().getDocumentMap()
                .get(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        for (Long noteId : cachedArticle.getNotesList()) {
            noteMap.remove(noteId);
        }

        // Remove the document in the document CACHE.
        CACHE.getDocumentCache().getDocumentMap().remove(cachedArticle.getDocumentId());
        CACHE.getDocumentCache().getDocumentTitleIdMap()
                .remove(cachedArticle.getDocumentTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Article article = (Article) CACHE.getDocumentCache().getDocumentMap()
                .get(note.getDocumentId());
        article.getNotesList().remove(noteId);

        // Remove the note in the note CACHE.
        CACHE.getNoteCache().getNoteMap().remove(noteId);

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
        for (Document document : CACHE.getDocumentCache().getDocumentMap().values()) {
            if (document instanceof Article) {
                resultSet.add(document.getDocumentId());
            }
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        Article article = (Article) (CACHE.getDocumentCache().getDocumentMap()
                .get(documentId));
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        List<Note> noteList = new ArrayList<Note>();

        for (Long noteId : article.getNotesList()) {
            noteList.add(noteMap.get(noteId));
        }
        Collections.sort(noteList);
        return noteList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document updateDocument(Document document) {
        Article updateArticle = (Article) (CACHE.getDocumentCache().getDocumentMap()
                .get(document.getDocumentId()));
        if (updateArticle != null) {
            CACHE.getDocumentCache().getDocumentTitleIdMap()
                    .remove(updateArticle.getDocumentTitle());
            updateArticle.setDocumentTitle(document.getDocumentTitle());
            updateArticle.setAuthorsList(((Article) document).getAuthorsList());
            updateArticle.setComment(document.getComment());
            updateArticle.setSource(((Article) document).getSource());
            if (((Article) document).getLastUpdatedTime() == null) {
                updateArticle.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                updateArticle.setLastUpdatedTime(((Article) document).getLastUpdatedTime());
            }
            CACHE.getDocumentCache().getDocumentTitleIdMap()
                    .put(updateArticle.getDocumentTitle(), updateArticle.getDocumentId());
            return updateArticle;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note updateNote(Note note) {
        Article article = (Article) CACHE.getDocumentCache().getDocumentMap()
                .get(note.getDocumentId());
        ArticleNote updateNote = (ArticleNote) (CACHE.getNoteCache().getNoteMap().get(note
                .getNoteId()));
        if (updateNote != null) {
            updateNote.setDocumentId(note.getDocumentId());
            updateNote.setTagIds(note.getTagIds());
            updateNote.setNoteText(note.getNoteText());

            // Update article's last updated time.
            article.setLastUpdatedTime(new Date());

            return updateNote;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document saveDocument(Document document) {
        if (document instanceof Article) {
            Article newArticle = new Article();
            if (document.getDocumentId() == null) {
                newArticle.setDocumentId(CACHE.getDocumentCache().getMaxDocumentId() + 1L);
            } else {
                newArticle.setDocumentId(document.getDocumentId());
            }
            newArticle.setDocumentTitle(document.getDocumentTitle());
            newArticle.setAuthorsList(((Article) document).getAuthorsList());
            newArticle.setComment(document.getComment());
            newArticle.setSource(((Article) document).getSource());
            if (((Article) document).getCreatedTime() == null) {
                newArticle.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newArticle.setCreatedTime(((Article) document).getCreatedTime());
            }
            if (((Article) document).getLastUpdatedTime() == null) {
                newArticle.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                newArticle.setLastUpdatedTime(((Article) document).getLastUpdatedTime());
            }
            if (((Article) document).getNotesList() == null) {
                newArticle.setNotesList(new ArrayList<Long>());
            } else {
                newArticle.setNotesList(((Article) document).getNotesList());
            }

            // Add the document to document CACHE.
            try {
                if (CACHE.getDocumentCache().getDocumentMap()
                        .containsKey(newArticle.getDocumentId())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document ID!");
                }
                if (CACHE.getDocumentCache().getDocumentTitleIdMap()
                        .containsKey(newArticle.getDocumentTitle())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document title!");
                }

                CACHE.getDocumentCache().getDocumentMap()
                        .put(newArticle.getDocumentId(), newArticle);
                CACHE.getDocumentCache().getDocumentTitleIdMap()
                        .put(newArticle.getDocumentTitle(), newArticle.getDocumentId());

                // Update the max document ID in document CACHE.
                if (CACHE.getDocumentCache().getMaxDocumentId() < newArticle.getDocumentId()) {
                    CACHE.getDocumentCache().setMaxDocumentId(newArticle.getDocumentId());
                }

                return newArticle;
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note saveNote(Note note) {
        if (note instanceof ArticleNote) {
            ArticleNote newNote = new ArticleNote();
            if (note.getNoteId() == null) {
                newNote.setNoteId(CACHE.getNoteCache().getMaxNoteId() + 1L);
            } else {
                newNote.setNoteId(note.getNoteId());
            }
            newNote.setDocumentId(note.getDocumentId());
            newNote.setTagIds(note.getTagIds());
            newNote.setNoteText(note.getNoteText());
            if (note.getCreatedTime() == null) {
                newNote.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newNote.setCreatedTime(note.getCreatedTime());
            }

            // Add the note to note CACHE.
            try {
                if (CACHE.getNoteCache().getNoteMap().containsKey(newNote.getNoteId())) {
                    throw new DuplicateRecordException("Duplicate note exception: same note ID!");
                }
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
            CACHE.getNoteCache().getNoteMap().put(newNote.getNoteId(), newNote);

            // Update the max note ID in note CACHE.
            if (CACHE.getNoteCache().getMaxNoteId() < newNote.getNoteId()) {
                CACHE.getNoteCache().setMaxNoteId(newNote.getNoteId());
            }

            // Add the note ID to corresponding notes list.
            Article article = (Article) (CACHE.getDocumentCache().getDocumentMap()
                    .get(newNote.getDocumentId()));
            article.getNotesList().add(newNote.getNoteId());

            // Update article's last updated time.
            article.setLastUpdatedTime(new Date());

            return newNote;
        }
        return null;
    }

}

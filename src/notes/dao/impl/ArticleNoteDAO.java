package notes.dao.impl;

import notes.dao.DuplicateRecordException;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Data access object for article notes.
 *
 * @author Rui Du
 */
public class ArticleNoteDAO extends AbstractNoteDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocument(Document document) {
        Article cachedArticle = (Article) Cache.get().getDocumentCache().getDocumentMap()
                .get(document.getDocumentId());

        // Remove all notes under the document.
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
        for (Long noteId : cachedArticle.getNotesList()) {
            noteMap.remove(noteId);
        }

        // Remove the document in the document cache.
        Cache.get().getDocumentCache().getDocumentMap().remove(cachedArticle.getDocumentId());
        Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .remove(cachedArticle.getDocumentTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNote(Note note) {
        Long noteId = note.getNoteId();

        // Update the note list in the corresponding document.
        Article article = (Article) Cache.get().getDocumentCache().getDocumentMap()
                .get(note.getDocumentId());
        article.getNotesList().remove(noteId);

        // Remove the note in the note cache.
        Cache.get().getNoteCache().getNoteMap().remove(noteId);

        // Update article's last updated time.
        article.setLastUpdatedTime(new Date());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        Article article = (Article) (Cache.get().getDocumentCache().getDocumentMap()
                .get(documentId));
        Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
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
        Article updateArticle = (Article) (Cache.get().getDocumentCache().getDocumentMap()
                .get(document.getDocumentId()));
        if (updateArticle != null) {
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
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
            Cache.get().getDocumentCache().getDocumentTitleIdMap()
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
        Article article = (Article) Cache.get().getDocumentCache().getDocumentMap()
                .get(note.getDocumentId());
        ArticleNote updateNote = (ArticleNote) (Cache.get().getNoteCache().getNoteMap().get(note
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
                newArticle.setDocumentId(Cache.get().getDocumentCache().getMaxDocumentId() + 1L);
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

            // Add the document to document cache.
            try {
                if (Cache.get().getDocumentCache().getDocumentMap()
                        .containsKey(newArticle.getDocumentId())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document ID!");
                }
                if (Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .containsKey(newArticle.getDocumentTitle())) {
                    throw new DuplicateRecordException("Duplicate document exception: same document title!");
                }

                Cache.get().getDocumentCache().getDocumentMap()
                        .put(newArticle.getDocumentId(), newArticle);
                Cache.get().getDocumentCache().getDocumentTitleIdMap()
                        .put(newArticle.getDocumentTitle(), newArticle.getDocumentId());

                // Update the max document ID in document cache.
                if (Cache.get().getDocumentCache().getMaxDocumentId() < newArticle.getDocumentId()) {
                    Cache.get().getDocumentCache().setMaxDocumentId(newArticle.getDocumentId());
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
                newNote.setNoteId(Cache.get().getNoteCache().getMaxNoteId() + 1L);
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

            // Add the note to note cache.
            try {
                if (Cache.get().getNoteCache().getNoteMap().containsKey(newNote.getNoteId())) {
                    throw new DuplicateRecordException("Duplicate note exception: same note ID!");
                }
            } catch (DuplicateRecordException e) {
                e.printStackTrace();
            }
            Cache.get().getNoteCache().getNoteMap().put(newNote.getNoteId(), newNote);

            // Update the max note ID in note cache.
            if (Cache.get().getNoteCache().getMaxNoteId() < newNote.getNoteId()) {
                Cache.get().getNoteCache().setMaxNoteId(newNote.getNoteId());
            }

            // Add the note ID to corresponding notes list.
            Article article = (Article) (Cache.get().getDocumentCache().getDocumentMap()
                    .get(newNote.getDocumentId()));
            article.getNotesList().add(newNote.getNoteId());

            // Update article's last updated time.
            article.setLastUpdatedTime(new Date());

            return newNote;
        }
        return null;
    }

}

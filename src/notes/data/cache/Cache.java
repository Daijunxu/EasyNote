/**
 *
 */
package notes.data.cache;

import notes.article.Article;
import notes.article.ArticleNote;
import notes.book.Book;
import notes.book.BookNote;
import notes.book.Chapter;
import notes.entity.Note;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The data cache composed of multiple caches.
 *
 * @author Rui Du
 * @version 1.0
 */
public class Cache {

    /**
     * The flag of whether the cache is having a problem.
     */
    public static boolean hasProblem = false;
    /**
     * The single instance that is used in the system.
     */
    private static Cache instance;
    /**
     * The document cache.
     */
    private DocumentCache documentCache;
    /**
     * The tag cache.
     */
    private TagCache tagCache;
    /**
     * The note cache.
     */
    private NoteCache noteCache;

    /**
     * Constructs an instance of {@code Cache}.
     */
    private Cache() {
        loadAllCaches();
    }

    /**
     * Gets the instance of {@code Cache}.
     *
     * @return {@code Cache} The instance of {@code Cache}.
     */
    public static Cache get() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    /**
     * Builds the notes list in document cache.
     */
    private void buildNotesList() {
        for (Note note : noteCache.getNoteMap().values()) {
            if (note instanceof BookNote) {
                Book book = (Book) (documentCache.getDocumentMap().get(note.getDocumentId()));
                Chapter chapter = book.getChaptersMap().get(((BookNote) note).getChapterId());
                chapter.getNotesList().add(note.getNoteId());
            } else if (note instanceof ArticleNote) {
                Article article = (Article) (documentCache.getDocumentMap().get(note
                        .getDocumentId()));
                article.getNotesList().add(note.getNoteId());
            }
        }
    }

    /**
     * Removes all data stored in the cache.
     */
    private void clear() {
        if (documentCache != null) {
            documentCache.clear();
        }
        if (tagCache != null) {
            tagCache.clear();
        }
        if (noteCache != null) {
            noteCache.clear();
        }
    }

    /**
     * Gets the document cache.
     *
     * @return {@code DocumentCache} The document cache.
     */
    public DocumentCache getDocumentCache() {
        return documentCache;
    }

    /**
     * Sets the document cache.
     *
     * @param documentCache The document cache to set.
     */
    public void setDocumentCache(DocumentCache documentCache) {
        this.documentCache = documentCache;
    }

    /**
     * Gets the note cache.
     *
     * @return {@code NoteCache} The note cache.
     */
    public NoteCache getNoteCache() {
        return noteCache;
    }

    /**
     * Sets the note cache.
     *
     * @param noteCache The note cache to set.
     */
    public void setNoteCache(NoteCache noteCache) {
        this.noteCache = noteCache;
    }

    /**
     * Gets the tag cache.
     *
     * @return {@code TagCache} The tag cache.
     */
    public TagCache getTagCache() {
        return tagCache;
    }

    /**
     * Sets the tag cache.
     *
     * @param tagCache The tag cache to set.
     */
    public void setTagCache(TagCache tagCache) {
        this.tagCache = tagCache;
    }

    /**
     * Reads all data into memory.
     */
    private void loadAllCaches() {
        try {
            String path = Property.get().getDataLocation();
            BufferedReader input = new BufferedReader(new FileReader(path));

            documentCache = new DocumentCache(input);
            tagCache = new TagCache(input);
            noteCache = new NoteCache(input);
            buildNotesList();

            input.close();

        } catch (IOException e) {
            Cache.hasProblem = true;
            e.printStackTrace();
        }
    }

    /**
     * Removes all data stored in the cache, and loads all latest data into the cache.
     */
    public void reload() {
        instance.clear();
        instance.loadAllCaches();
    }

    /**
     * Writes all data into disk.
     */
    public void saveAllCaches() {
        try {
            String path = Property.get().getDataLocation();
            BufferedWriter output = new BufferedWriter(new FileWriter(path));

            documentCache.saveDocumentCache(output);
            tagCache.saveTagCache(output);
            noteCache.saveNoteCache(output);

            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

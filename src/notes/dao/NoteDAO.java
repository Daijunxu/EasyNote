package notes.dao;

import notes.entity.Document;
import notes.entity.Note;
import notes.entity.Tag;

import java.util.List;
import java.util.Set;

/**
 * An interface to describe the data access object for notes.
 *
 * @author Rui Du
 */
public interface NoteDAO<N extends Note, D extends Document> {

    /**
     * Deletes a document and all its notes.
     *
     * @param document The document to delete.
     */
    void deleteDocument(D document);

    /**
     * Deletes a note.
     *
     * @param note The note object that should be deleted.
     */
    void deleteNote(N note);

    /**
     * Deletes a tag.
     *
     * @param tag The tag object that should be deleted.
     */
    void deleteTag(Tag tag);

    /**
     * Finds all documents ordered by document ID.
     *
     * @return {@code List<D>} The list of documents.
     */
    List<D> findAllDocuments();

    /**
     * Finds all notes ordered by note ID.
     *
     * @return {@code List<N>} The list of notes.
     */
    List<N> findAllNotes();

    /**
     * Finds all notes by the document ID.
     *
     * @param documentId The document ID.
     * @return {@code List<N>} All notes by the document ID. Empty if no notes are found.
     */
    List<N> findAllNotesByDocumentId(Long documentId);

    /**
     * Finds all notes with the a particular tag ID.
     *
     * @param tagId The search tag ID.
     * @return {@code List<N>} All notes by the tag ID. Empty if no notes are found.
     */
    List<N> findAllNotesByTagId(Long tagId);

    /**
     * Finds all notes containing a particular text.
     *
     * @param candidateDocuments The candidate documents. If this para is null, will search in all documents.
     * @param text               The search text.
     * @param caseSensitive      True if the search requires case sensitive; false otherwise.
     * @param exactSearch        True if the search requires exact search; false otherwise.
     * @return {@code List<N>} All notes containing the text. Empty if no notes are found.
     */
    List<N> findAllNotesContainingText(Set<Long> candidateDocuments, String text, boolean caseSensitive, boolean exactSearch);

    /**
     * Finds all tags ordered by tag ID.
     *
     * @return {@code List<Tag>} The list of tags.
     */
    List<Tag> findAllTags();

    /**
     * Finds the document by the document ID.
     *
     * @param documentId The document ID.
     * @return {@code D} The found document. Null if no document is found.
     */
    D findDocumentById(Long documentId);

    /**
     * Finds the document by the document title.
     *
     * @param documentTitle The document title.
     * @return {@code D} The found document. Null if no document is found.
     */
    D findDocumentByTitle(String documentTitle);

    /**
     * Finds the note by the note ID.
     *
     * @param noteId The note ID.
     * @return {@code N} The found note. Null if no note is found.
     */
    N findNoteById(Long noteId);

    /**
     * Finds the tag by the tag ID.
     *
     * @param tagId The tag ID.
     * @return {@code Tag} The found tag. Null if no tag is found.
     */
    Tag findTagById(Long tagId);

    /**
     * Finds the tag by the tag text.
     *
     * @param tagText The tag text.
     * @return {@code Tag} The found tag. Null if no tag is found.
     */
    Tag findTagByText(String tagText);

    /**
     * Updates a document.
     *
     * @param document The document to update.
     * @return {@code D} The updated document.
     */
    D updateDocument(D document);

    /**
     * Updates a note.
     *
     * @param note The note to update.
     * @return {@code N} The updated note object.
     */
    N updateNote(N note);

    /**
     * Updates a tag.
     *
     * @param tag The tag to update.
     * @return {@code Tag} The updated tag object.
     */
    Tag updateTag(Tag tag);

    /**
     * Saves a document.
     *
     * @param document The document to save.
     * @return {@code D} The saved document.
     */
    D saveDocument(D document);

    /**
     * Saves a note.
     *
     * @param note The note to save.
     * @return {@code N} The saved note.
     */
    N saveNote(N note);

    /**
     * Saves a tag.
     *
     * @param tag The tag to save.
     * @return {@code Tag} The saved tag.
     */
    Tag saveTag(Tag tag);
}

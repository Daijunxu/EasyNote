/**
 *
 */
package notes.entity;

import java.util.Date;
import java.util.List;

/**
 * An interface defining the basic operations of a note object.
 *
 * @author Rui Du
 * @version 1.0
 */
public interface Note extends Comparable<Note>, XMLSerializable<Note> {

    /**
     * Gets the note's created time.
     *
     * @return {@code Date} The note's created time.
     */
    Date getCreatedTime();

    /**
     * Sets the note's created time.
     *
     * @param createdTime The note's created time to set.
     */
    void setCreatedTime(Date createdTime);

    /**
     * Gets the document identifier.
     *
     * @return {@code Long} The document identifier.
     */
    public Long getDocumentId();

    /**
     * Sets the document identifier.
     *
     * @param documentId The document identifier to set.
     */
    public void setDocumentId(Long documentId);

    /**
     * Gets the note identifier.
     *
     * @return {@code Long} The note identifier.
     */
    Long getNoteId();

    /**
     * Sets the note identifier.
     *
     * @param noteId The note identifier to set.
     */
    void setNoteId(Long noteId);

    /**
     * Gets the note's text.
     *
     * @return {@code String} The note's text.
     */
    String getNoteText();

    /**
     * Sets the note's text.
     *
     * @param noteText The note's text to set.
     */
    void setNoteText(String noteText);

    /**
     * Gets the tag identifiers.
     *
     * @return {@code List<Long>} The tag identifiers.
     */
    List<Long> getTagIds();

    /**
     * Sets the tag identifiers.
     *
     * @param tagIds The list of tag identifiers to set.
     */
    void setTagIds(List<Long> tagIds);
}

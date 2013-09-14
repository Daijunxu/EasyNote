/**
 *
 */
package notes.entity.impl;

import notes.entity.Note;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Date;
import java.util.List;

/**
 * An abstract entity class specifying the basic members and methods of a note.
 *
 * @author Rui Du
 * @version 1.0
 */
public abstract class AbstractNote implements Note {

    /**
     * The unique identifier for the note.
     */
    private Long noteId;
    /**
     * The document identifier.
     */
    private Long documentId;
    /**
     * The tag identifiers.
     */
    private List<Long> tagIds;
    /**
     * The note's text.
     */
    private String noteText;
    /**
     * The created time of this note.
     */
    private Date createdTime;

    /**
     * Compares this object with the specified object for sorting.
     *
     * @param other The object to be compared.
     * @return int A negative integer, zero, or a positive integer as this object is less than,
     *         equal to, or greater than the specified object.
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(Note other) {
        return new CompareToBuilder().append(getNoteId(), other.getNoteId()).toComparison();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * {@inheritDoc}
     */
    public Long getDocumentId() {
        return documentId;
    }

    /**
     * {@inheritDoc}
     */
    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getNoteId() {
        return noteId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNoteText() {
        return noteText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getTagIds() {
        return tagIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}

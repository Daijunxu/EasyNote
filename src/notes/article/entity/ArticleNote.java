package notes.article.entity;

import java.util.Date;
import java.util.List;

import notes.entity.impl.AbstractNote;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entity class to describe an article note.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ArticleNote extends AbstractNote {

    /**
     * Constructs a default instance of {@code ArticleNote}.
     */
    public ArticleNote() {
    }

    /**
     * Constructs an instance of {@code ArticleNote}.
     *
     * @param noteId     The note identifier.
     * @param documentId The document identifier.
     * @param tagIds     The list of tag identifiers.
     * @param noteText   The note's text.
     * @throws IllegalArgumentException
     */
    public ArticleNote(final Long noteId, final Long documentId, final List<Long> tagIds,
                       final String noteText) throws IllegalArgumentException {
        setNoteId(noteId);
        setDocumentId(documentId);
        setTagIds(tagIds);
        setNoteText(noteText);
        setCreatedTime(new Date(System.currentTimeMillis()));
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj The reference object with which to compare.
     * @return boolean Returns true if this object is the same as the obj argument; false otherwise.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ArticleNote
                && new EqualsBuilder().append(getNoteId(), ((ArticleNote) obj).getNoteId())
                .append(getDocumentId(), ((ArticleNote) obj).getDocumentId())
                .append(getTagIds(), ((ArticleNote) obj).getTagIds())
                .append(getNoteText(), ((ArticleNote) obj).getNoteText())
                .append(getCreatedTime(), ((ArticleNote) obj).getCreatedTime()).isEquals();
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return int A hash code value for this object.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getNoteId()).append(getDocumentId())
                .append(getTagIds()).append(getNoteText()).append(getCreatedTime()).toHashCode();
    }

    /**
     * Returns a string representation of the object.
     *
     * @return {@code String} A string representation of the object.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("noteId", getNoteId())
                .append("documentId", getDocumentId()).append("tagIds", getTagIds())
                .append("noteText", getNoteText()).append("createdTime", getCreatedTime())
                .toString();
    }

}

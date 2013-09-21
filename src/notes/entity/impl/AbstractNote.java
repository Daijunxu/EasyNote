/**
 *
 */
package notes.entity.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
@EqualsAndHashCode
public abstract class AbstractNote implements Note {

    /**
     * The unique identifier for the note.
     */
    @Getter
    @Setter
    protected Long noteId;
    /**
     * The document identifier.
     */
    @Getter
    @Setter
    protected Long documentId;
    /**
     * The tag identifiers.
     */
    @Getter
    @Setter
    protected List<Long> tagIds;
    /**
     * The note's text.
     */
    @Getter
    @Setter
    protected String noteText;
    /**
     * The created time of this note.
     */
    @Getter
    @Setter
    protected Date createdTime;

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
}

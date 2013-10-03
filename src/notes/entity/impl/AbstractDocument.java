/**
 *
 */
package notes.entity.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import notes.entity.Document;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * An abstract entity class specifying the basic members and methods of a document.
 *
 * @author Rui Du
 * @version 1.0
 */
@EqualsAndHashCode
public abstract class AbstractDocument implements Document {

    /**
     * The document identifier.
     */
    @Getter
    @Setter
    protected Long documentId;
    /**
     * The document's title.
     */
    @Getter
    @Setter
    protected String documentTitle;
    /**
     * The comment.
     */
    @Getter
    @Setter
    protected String comment;

    /**
     * Compares this object with the specified object for sorting.
     *
     * @param other The object to be compared.
     * @return int A negative integer, zero, or a positive integer as this object is less than,
     *         equal to, or greater than the specified object.
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(Document other) {
        return new CompareToBuilder().append(getDocumentId(), other.getDocumentId()).toComparison();
    }


}

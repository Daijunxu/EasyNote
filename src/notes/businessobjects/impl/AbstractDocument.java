package notes.businessobjects.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import notes.businessobjects.Document;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * An abstract entity class specifying the basic members and methods of a document.
 * <p/>
 * Author: Rui Du
 */
@EqualsAndHashCode
@ToString(includeFieldNames = true)
public abstract class AbstractDocument implements Document {

    /**
     * A unique identifier of the document.
     */
    @Getter
    @Setter
    protected Long documentId;
    /**
     * A unique title of the document.
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

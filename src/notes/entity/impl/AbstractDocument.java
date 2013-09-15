/**
 *
 */
package notes.entity.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import notes.entity.Document;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Date;
import java.util.List;

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
    private Long documentId;
    /**
     * The document's title.
     */
    @Getter
    @Setter
    private String documentTitle;
    /**
     * The list of authors.
     */
    @Getter
    @Setter
    private List<String> authorsList;
    /**
     * The comment.
     */
    @Getter
    @Setter
    private String comment;
    /**
     * The create time of this document.
     */
    @Getter
    @Setter
    private Date createdTime;
    /**
     * The last update time of this document.
     */
    @Getter
    @Setter
    private Date lastUpdatedTime;

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

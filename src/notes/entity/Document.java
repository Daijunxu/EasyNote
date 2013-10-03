/**
 *
 */
package notes.entity;

/**
 * An interface to describe basic operations of a document.
 *
 * @author Rui Du
 * @version 1.0
 */
public interface Document extends Comparable<Document>, XMLSerializable<Document> {

    /**
     * Gets the comment.
     *
     * @return {@code String} The comment.
     */
    String getComment();

    /**
     * Sets the comment.
     *
     * @param comment The comment to set.
     */
    void setComment(String comment);

    /**
     * Gets the document identifier.
     *
     * @return {@code Long} The document identifier.
     */
    Long getDocumentId();

    /**
     * Sets the document identifier.
     *
     * @param documentId The document identifier to set.
     */
    void setDocumentId(Long documentId);

    /**
     * Gets the document title.
     *
     * @return {@code String} The document title.
     */
    String getDocumentTitle();

    /**
     * Sets the document title.
     *
     * @param documentTitle The document title to set.
     */
    void setDocumentTitle(String documentTitle);

    /**
     * Gets the number of notes in the document.
     *
     * @return {@code int} The number of notes in the document.
     */
    int getNotesCount();

}

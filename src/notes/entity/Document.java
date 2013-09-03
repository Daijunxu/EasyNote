/**
 *
 */
package notes.entity;

import java.util.Date;
import java.util.List;

/**
 * An interface to describe basic operations of a document.
 *
 * @author Rui Du
 * @version 1.0
 */
public interface Document extends Comparable<Document> {

    /**
     * Gets the list of authors.
     *
     * @return {@code List<String>} The list of authors.
     */
    List<String> getAuthorsList();

    /**
     * Gets the comment.
     *
     * @return {@code String} The comment.
     */
    String getComment();

    /**
     * Gets the document's created time.
     *
     * @return {@code Date} The document's created time.
     */
    Date getCreatedTime();

    /**
     * Gets the document identifier.
     *
     * @return {@code Long} The document identifier.
     */
    Long getDocumentId();

    /**
     * Gets the document title.
     *
     * @return {@code String} The document title.
     */
    String getDocumentTitle();

    /**
     * Gets the document's last updated time.
     *
     * @return {@code Date} The document's last updated time.
     */
    Date getLastUpdatedTime();

    /**
     * Gets the number of notes in the document.
     *
     * @return {@code int} The number of notes in the document.
     */
    int getNotesCount();

    /**
     * Sets the list of authors.
     *
     * @param authorsList The list of authors to set.
     */
    void setAuthorsList(List<String> authorsList);

    /**
     * Sets the comment.
     *
     * @param comment The comment to set.
     */
    void setComment(String comment);

    /**
     * Sets the document's created time.
     *
     * @param createTime The document's created time to set.
     */
    void setCreatedTime(Date createTime);

    /**
     * Sets the document identifier.
     *
     * @param documentId The document identifier to set.
     */
    void setDocumentId(Long documentId);

    /**
     * Sets the document title.
     *
     * @param documentTitle The document title to set.
     */
    void setDocumentTitle(String documentTitle);

    /**
     * Sets the document's last updated time.
     *
     * @param lastUpdatedTime The document's last updated time.
     */
    void setLastUpdatedTime(Date lastUpdatedTime);

}

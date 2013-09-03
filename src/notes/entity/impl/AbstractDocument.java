/**
 * 
 */
package notes.entity.impl;

import java.util.Date;
import java.util.List;

import notes.entity.Document;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * An abstract entity class specifying the basic members and methods of a document.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public abstract class AbstractDocument implements Document {

	/**
	 * The document identifier.
	 */
	private Long documentId;

	/**
	 * The document's title.
	 */
	private String documentTitle;

	/**
	 * The list of authors.
	 */
	private List<String> authorsList;

	/**
	 * The comment.
	 */
	private String comment;

	/**
	 * The create time of this document.
	 */
	private Date createdTime;

	/**
	 * The last update time of this document.
	 */
	private Date lastUpdatedTime;

	/**
	 * Compares this object with the specified object for sorting.
	 * 
	 * @param other
	 *            The object to be compared.
	 * 
	 * @return int A negative integer, zero, or a positive integer as this object is less than,
	 *         equal to, or greater than the specified object.
	 * 
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(Document other) {
		return new CompareToBuilder().append(getDocumentId(), other.getDocumentId()).toComparison();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getAuthorsList() {
		return authorsList;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getCreatedTime() {
		return createdTime;
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
	public String getDocumentTitle() {
		return documentTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAuthorsList(final List<String> authorsList) {
		this.authorsList = authorsList;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDocumentId(final Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDocumentTitle(final String documentTitle) {
		this.documentTitle = documentTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

}

/**
 * 
 */
package notes.entity.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import notes.entity.Note;

/**
 * An abstract entity class specifying the basic members and methods of a note.
 * 
 * @author Rui Du
 * @version 1.0
 * 
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
	 * @param other
	 *            The object to be compared.
	 * 
	 * @return int A negative integer, zero, or a positive integer as this object is less than,
	 *         equal to, or greater than the specified object.
	 * 
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(Note other) {
		return new CompareToBuilder().append(getNoteId(), other.getNoteId()).toComparison();
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
	public Long getNoteId() {
		return noteId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getNoteText() {
		return noteText;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Long> getTagIds() {
		return tagIds;
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
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}
}

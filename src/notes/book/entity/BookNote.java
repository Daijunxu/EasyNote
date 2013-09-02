package notes.book.entity;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import notes.entity.impl.AbstractNote;

/**
 * Entity class to describe a book note.
 * 
 * @author Rui Du
 * @version 1.0
 */
public class BookNote extends AbstractNote {

	/**
	 * The chapter identifier.
	 */
	private Long chapterId;

	/**
	 * Constructs a default instance of {@code BookNote}.
	 */
	public BookNote() {
	}

	/**
	 * Constructs an instance of {@code BookNote}.
	 * 
	 * @param noteId
	 *            The note identifier.
	 * @param documentId
	 *            The document identifier.
	 * @param chapterId
	 *            The chapter identifier.
	 * @param tagIds
	 *            The list of tag identifiers.
	 * @param noteText
	 *            The note's text.
	 * @throws IllegalArgumentException
	 */
	public BookNote(final Long noteId, final Long documentId, final Long chapterId,
			final List<Long> tagIds, final String noteText) throws IllegalArgumentException {
		setNoteId(noteId);
		setDocumentId(documentId);
		setChapterId(chapterId);
		setTagIds(tagIds);
		setNoteText(noteText);
		setCreatedTime(new Date(System.currentTimeMillis()));
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 *            The reference object with which to compare.
	 * 
	 * @return boolean Returns true if this object is the same as the obj argument; false otherwise.
	 * 
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		return obj instanceof BookNote
				&& new EqualsBuilder().append(getNoteId(), ((BookNote) obj).getNoteId())
						.append(getDocumentId(), ((BookNote) obj).getDocumentId())
						.append(getChapterId(), ((BookNote) obj).getChapterId())
						.append(getTagIds(), ((BookNote) obj).getTagIds())
						.append(getNoteText(), ((BookNote) obj).getNoteText())
						.append(getCreatedTime(), ((BookNote) obj).getCreatedTime()).isEquals();
	}

	/**
	 * Gets the chapter identifier.
	 * 
	 * @return {@code Long} The chapter identifier.
	 */
	public Long getChapterId() {
		return chapterId;
	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return int A hash code value for this object.
	 * 
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getNoteId()).append(getDocumentId())
				.append(getChapterId()).append(getTagIds()).append(getNoteText())
				.append(getCreatedTime()).toHashCode();
	}

	/**
	 * Sets the chapter identifier.
	 * 
	 * @param chapterId
	 *            The chapter identifier to set.
	 */
	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return {@code String} A string representation of the object.
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("noteId", getNoteId())
				.append("documentId", getDocumentId()).append("chapterId", getChapterId())
				.append("tagIds", getTagIds()).append("noteText", getNoteText())
				.append("createdTime", getCreatedTime()).toString();
	}
}

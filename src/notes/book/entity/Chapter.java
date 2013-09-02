package notes.book.entity;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entity class to describe a chapter. A book contains multiple chapters, while a chapter belongs to
 * only one book.
 * 
 * @author Rui Du
 * @version 1.0
 */
public class Chapter {

	/**
	 * The chapter identifier.
	 */
	private Long chapterId;

	/**
	 * The chapter's title.
	 */
	private String chapterTitle;

	/**
	 * The list of note identifiers.
	 */
	private List<Long> notesList;

	/**
	 * Constructs a default instance of {@code Chapter}.
	 */
	public Chapter() {
	}

	/**
	 * Constructs an instance of {@code Chapter}.
	 * 
	 * @param chapterId
	 *            The chapter identifier.
	 * @param chapterTitle
	 *            The chapter's title.
	 * @param notesList
	 *            The list of note identifiers.
	 * @throws IllegalArgumentException
	 */
	public Chapter(final Long chapterId, final String chapterTitle, final List<Long> notesList)
			throws IllegalArgumentException {
		setChapterId(chapterId);
		setChapterTitle(chapterTitle);
		setNotesList(notesList);
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
		return obj instanceof Chapter
				&& new EqualsBuilder().append(getChapterId(), ((Chapter) obj).getChapterId())
						.append(getChapterTitle(), ((Chapter) obj).getChapterTitle())
						.append(getNotesList(), ((Chapter) obj).getNotesList()).isEquals();
	}

	/**
	 * Gets the chapter identifier.
	 * 
	 * @return {@code Long} the chapter identifier.
	 */
	public Long getChapterId() {
		return chapterId;
	}

	/**
	 * Gets the chapter's title.
	 * 
	 * @return {@code String} The chapter's title.
	 */
	public String getChapterTitle() {
		return chapterTitle;
	}

	/**
	 * Gets the list of note identifiers.
	 * 
	 * @return {@code List<Long>} The list of note identifiers.
	 */
	public List<Long> getNotesList() {
		return notesList;
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
		return new HashCodeBuilder().append(getChapterId()).append(getChapterTitle())
				.append(getNotesList()).toHashCode();
	}

	/**
	 * Sets the chapter identifier.
	 * 
	 * @param chapterId
	 *            The chapter identifier to set.
	 */
	public void setChapterId(final Long chapterId) {
		this.chapterId = chapterId;
	}

	/**
	 * Sets the chapter's title.
	 * 
	 * @param chapterTitle
	 *            The chapter's title to set.
	 */
	public void setChapterTitle(final String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}

	/**
	 * Sets the list of note identifiers.
	 * 
	 * @param notesList
	 *            The list of note identifiers to set.
	 */
	public void setNotesList(final List<Long> notesList) {
		this.notesList = notesList;
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
		return new ToStringBuilder(this).append("chapterId", getChapterId())
				.append("chapterTitle", getChapterTitle()).append("notesList", getNotesList())
				.toString();
	}
}

/**
 * 
 */
package notes.article.entity;

import java.util.Date;
import java.util.List;

import notes.entity.impl.AbstractDocument;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entity class to describe an article.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class Article extends AbstractDocument {

	/**
	 * The article's source, could be an URL or a description text.
	 */
	private String source;

	/**
	 * The list of note identifiers.
	 */
	private List<Long> notesList;

	/**
	 * Constructs a default instance of {@code Article}
	 */
	public Article() {
	}

	/**
	 * Constructs an instance of {@code Article}.
	 * 
	 * @param documentId
	 *            The document identifier.
	 * @param documentTitle
	 *            The document's title.
	 * @param authorsList
	 *            The list of authors.
	 * @param comment
	 *            The comment.
	 * @param source
	 *            The article's source.
	 * @param notesList
	 *            The list of note identifiers.
	 * @throws IllegalArgumentException
	 */
	public Article(final Long documentId, final String documentTitle,
			final List<String> authorsList, final String comment, final String source,
			final List<Long> notesList) throws IllegalArgumentException {
		setDocumentId(documentId);
		setDocumentTitle(documentTitle);
		setAuthorsList(authorsList);
		setComment(comment);
		setSource(source);
		setNotesList(notesList);
		setCreatedTime(new Date(System.currentTimeMillis()));
		setLastUpdatedTime(new Date(System.currentTimeMillis()));
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
		return obj instanceof Article
				&& new EqualsBuilder().append(getDocumentId(), ((Article) obj).getDocumentId())
						.append(getDocumentTitle(), ((Article) obj).getDocumentTitle())
						.append(getAuthorsList(), ((Article) obj).getAuthorsList())
						.append(getComment(), ((Article) obj).getComment())
						.append(getSource(), ((Article) obj).getSource())
						.append(getCreatedTime(), ((Article) obj).getCreatedTime())
						.append(getLastUpdatedTime(), ((Article) obj).getLastUpdatedTime())
						.append(getNotesList(), ((Article) obj).getNotesList()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNotesCount() {
		return notesList.size();
	}

	/**
	 * Gets the list of note identifiers.
	 * 
	 * @return {@code List<Integer>} The list of note identifiers.
	 */
	public List<Long> getNotesList() {
		return notesList;
	}

	/**
	 * Gets the article's source.
	 * 
	 * @return {@code String} The article's source.
	 */
	public String getSource() {
		return source;
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
		return new HashCodeBuilder().append(getDocumentId()).append(getDocumentTitle())
				.append(getAuthorsList()).append(getComment()).append(getSource())
				.append(getCreatedTime()).append(getLastUpdatedTime()).append(getNotesList())
				.toHashCode();
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
	 * Sets the article's source.
	 * 
	 * @param source
	 *            The article's source to set.
	 */
	public void setSource(String source) {
		this.source = source;
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
		return new ToStringBuilder(this).append("documentId", getDocumentId())
				.append("documentTitle", getDocumentTitle()).append("authorList", getAuthorsList())
				.append("comment", getComment()).append("edition", getSource())
				.append("createdTime", getCreatedTime())
				.append("lastUpdatedTime", getLastUpdatedTime())
				.append("notesList", getNotesList()).toString();
	}
}

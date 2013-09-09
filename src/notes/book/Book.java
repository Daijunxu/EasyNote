package notes.book;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import notes.entity.impl.AbstractDocument;

/**
 * Entity class to describe a book.
 *
 * @author Rui Du
 * @version 1.0
 */
public class Book extends AbstractDocument {

    /**
     * The book's edition number.
     */
    private Integer edition;

    /**
     * The book's published year.
     */
    private Integer publishedYear;

    /**
     * The book's ISBN.
     */
    private String isbn;

    /**
     * The ordered map for chapters.
     */
    private TreeMap<Long, Chapter> chaptersMap;

    /**
     * Constructs a default instance of {@code Book}.
     */
    public Book() {
    }

    /**
     * Constructs an instance of {@code Book}.
     *
     * @param documentId    The document identifier.
     * @param documentTitle The document's title.
     * @param authorsList   The list of authors.
     * @param comment       The comment.
     * @param edition       The book's edition.
     * @param publishedYear The book's published year.
     * @param isbn          The book's ISBN.
     * @param chaptersMap   The ordered map for chapters.
     * @throws IllegalArgumentException
     */
    public Book(final Long documentId, final String documentTitle, final List<String> authorsList,
                final String comment, final Integer edition, final Integer publishedYear,
                final String isbn, final TreeMap<Long, Chapter> chaptersMap)
            throws IllegalArgumentException {
        setDocumentId(documentId);
        setDocumentTitle(documentTitle);
        setAuthorsList(authorsList);
        setComment(comment);
        setEdition(edition);
        setPublishedYear(publishedYear);
        setIsbn(isbn);
        setChaptersMap(chaptersMap);
        setCreatedTime(new Date(System.currentTimeMillis()));
        setLastUpdatedTime(new Date(System.currentTimeMillis()));
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj The reference object with which to compare.
     * @return boolean Returns true if this object is the same as the obj argument; false otherwise.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Book
                && new EqualsBuilder().append(getDocumentId(), ((Book) obj).getDocumentId())
                .append(getDocumentTitle(), ((Book) obj).getDocumentTitle())
                .append(getAuthorsList(), ((Book) obj).getAuthorsList())
                .append(getComment(), ((Book) obj).getComment())
                .append(getEdition(), ((Book) obj).getEdition())
                .append(getPublishedYear(), ((Book) obj).getPublishedYear())
                .append(getIsbn(), ((Book) obj).getIsbn())
                .append(getChaptersMap(), ((Book) obj).getChaptersMap())
                .append(getCreatedTime(), ((Book) obj).getCreatedTime())
                .append(getLastUpdatedTime(), ((Book) obj).getLastUpdatedTime()).isEquals();
    }

    /**
     * Gets the ordered map for chapters.
     *
     * @return {@code TreeMap} The ordered map for chapters.
     */
    public TreeMap<Long, Chapter> getChaptersMap() {
        return chaptersMap;
    }

    /**
     * Gets the book's edition number.
     *
     * @return {@code Integer} The book's edition number.
     */
    public Integer getEdition() {
        return edition;
    }

    /**
     * Gets the book's ISBN.
     *
     * @return {@code String} The book's ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNotesCount() {
        if (chaptersMap == null || chaptersMap.isEmpty()) {
            return 0;
        }

        int result = 0;
        for (Chapter chapter : chaptersMap.values()) {
            result += chapter.getNotesList().size();
        }
        return result;
    }

    /**
     * Gets book's published year.
     *
     * @return {@code Integer} The book's published year.
     */
    public Integer getPublishedYear() {
        return publishedYear;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return int A hash code value for this object.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getDocumentId()).append(getDocumentTitle())
                .append(getAuthorsList()).append(getComment()).append(getEdition())
                .append(getPublishedYear()).append(getIsbn()).append(getChaptersMap())
                .append(getCreatedTime()).append(getLastUpdatedTime()).toHashCode();
    }

    /**
     * Sets the ordered map for chapters.
     *
     * @param chaptersMap The ordered map for chapters to set.
     */
    public void setChaptersMap(final TreeMap<Long, Chapter> chaptersMap) {
        this.chaptersMap = chaptersMap;
    }

    /**
     * Sets the book's edition number.
     *
     * @param edition The book's edition number to set.
     */
    public void setEdition(final Integer edition) {
        this.edition = edition;
    }

    /**
     * Sets the book's ISBN.
     *
     * @param isbn The book's ISBN to set.
     */
    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    /**
     * Sets the book's published year.
     *
     * @param publishedYear The book's published year to set.
     */
    public void setPublishedYear(final Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return {@code String} A string representation of the object.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("documentId", getDocumentId())
                .append("documentTitle", getDocumentTitle()).append("authorList", getAuthorsList())
                .append("comment", getComment()).append("edition", getEdition())
                .append("publishedYear", getPublishedYear()).append("ISBN", getIsbn())
                .append("chapterMap", getChaptersMap()).append("createdTime", getCreatedTime())
                .append("lastUpdatedTime", getLastUpdatedTime()).toString();
    }
}

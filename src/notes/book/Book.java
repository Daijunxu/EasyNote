package notes.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.impl.AbstractDocument;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Entity class to describe a book.
 *
 * @author Rui Du
 * @version 1.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
public class Book extends AbstractDocument {

    /**
     * The book's edition number.
     */
    @Getter
    @Setter
    private Integer edition;
    /**
     * The book's published year.
     */
    @Getter
    @Setter
    private Integer publishedYear;
    /**
     * The book's ISBN.
     */
    @Getter
    @Setter
    private String isbn;
    /**
     * The ordered map for chapters.
     */
    @Getter
    @Setter
    private TreeMap<Long, Chapter> chaptersMap;

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
}

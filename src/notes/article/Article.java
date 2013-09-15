/**
 *
 */
package notes.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.impl.AbstractDocument;

import java.util.Date;
import java.util.List;

/**
 * Entity class to describe an article.
 *
 * @author Rui Du
 * @version 1.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Article extends AbstractDocument {

    /**
     * The article's source, could be an URL or a description text.
     */
    @Getter
    @Setter
    private String source;
    /**
     * The list of note identifiers.
     */
    @Getter
    @Setter
    private List<Long> notesList;

    /**
     * Constructs an instance of {@code Article}.
     *
     * @param documentId    The document identifier.
     * @param documentTitle The document's title.
     * @param authorsList   The list of authors.
     * @param comment       The comment.
     * @param source        The article's source.
     * @param notesList     The list of note identifiers.
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
     * {@inheritDoc}
     */
    @Override
    public int getNotesCount() {
        return notesList.size();
    }
}

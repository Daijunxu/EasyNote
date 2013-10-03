package notes.entity.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.aware.AuthorsAware;
import notes.entity.aware.CommentAware;
import notes.entity.aware.CreatedTimeAware;
import notes.entity.aware.LastUpdatedTimeAware;
import notes.entity.impl.AbstractDocument;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.Date;
import java.util.List;

/**
 * Entity class to describe an article.
 *
 * @author Rui Du
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Article extends AbstractDocument
        implements AuthorsAware, CreatedTimeAware, LastUpdatedTimeAware, CommentAware {

    /**
     * The list of authors.
     */
    @Getter
    @Setter
    private List<String> authorsList;
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
     * The create time of this article.
     */
    @Getter
    @Setter
    private Date createdTime;
    /**
     * The last update time of this article.
     */
    @Getter
    @Setter
    private Date lastUpdatedTime;

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
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.authorsList = authorsList;
        this.comment = comment;
        this.source = source;
        this.notesList = notesList;
        this.createdTime = new Date(System.currentTimeMillis());
        this.lastUpdatedTime = new Date(System.currentTimeMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNotesCount() {
        return notesList.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element articleElement = new DefaultElement("Article");

        articleElement.addAttribute("DocumentId", documentId.toString());
        articleElement.addAttribute("DocumentTitle", documentTitle);
        articleElement.addAttribute("AuthorsList", EntityHelper.buildEntityStrFromList(authorsList));
        articleElement.addAttribute("Comment", comment);
        articleElement.addAttribute("Source", source);
        articleElement.addAttribute("NotesList", EntityHelper.buildEntityStrFromList(notesList));
        articleElement.addAttribute("CreatedTime", String.valueOf(createdTime.getTime()));
        articleElement.addAttribute("LastUpdatedTime", String.valueOf(lastUpdatedTime.getTime()));

        return articleElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Article buildFromXMLElement(Element element) {
        documentId = Long.parseLong(element.attributeValue("DocumentId"));
        documentTitle = element.attributeValue("DocumentTitle");
        authorsList = EntityHelper.buildAuthorsStrList(element.attributeValue("AuthorsList"));
        comment = element.attributeValue("Comment");
        source = element.attributeValue("Source");
        notesList = EntityHelper.buildIDsList(element.attributeValue("NotesList"));
        createdTime = new Date(Long.parseLong(element.attributeValue("CreatedTime")));
        lastUpdatedTime = new Date(Long.parseLong(element.attributeValue("LastUpdatedTime")));

        return this;
    }
}

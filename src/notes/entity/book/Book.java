package notes.entity.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.aware.AuthorsAware;
import notes.entity.aware.CreatedTimeAware;
import notes.entity.aware.LastUpdatedTimeAware;
import notes.entity.impl.AbstractDocument;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Entity class to describe a book.
 *
 * @author Rui Du
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
public class Book extends AbstractDocument implements AuthorsAware, CreatedTimeAware, LastUpdatedTimeAware {

    /**
     * The list of authors.
     */
    @Getter
    @Setter
    private List<String> authorsList;
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
     * The create time of this book.
     */
    @Getter
    @Setter
    private Date createdTime;
    /**
     * The last update time of this book.
     */
    @Getter
    @Setter
    private Date lastUpdatedTime;

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
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.authorsList = authorsList;
        this.comment = comment;
        this.edition = edition;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
        this.chaptersMap = chaptersMap;
        this.createdTime = new Date(System.currentTimeMillis());
        this.lastUpdatedTime = new Date(System.currentTimeMillis());
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
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element bookElement = new DefaultElement("Book");

        bookElement.addAttribute("DocumentId", documentId.toString());
        bookElement.addAttribute("DocumentTitle", documentTitle);
        bookElement.addAttribute("AuthorsList", EntityHelper.buildEntityStrFromList(authorsList));
        bookElement.addAttribute("Comment", comment);
        bookElement.addAttribute("Edition", (edition == null) ? null : edition.toString());
        bookElement.addAttribute("PublishedYear", (publishedYear == null) ? null : publishedYear.toString());
        bookElement.addAttribute("ISBN", isbn);
        bookElement.addAttribute("CreatedTime", String.valueOf(createdTime.getTime()));
        bookElement.addAttribute("LastUpdatedTime", String.valueOf(lastUpdatedTime.getTime()));

        for (Long chapterId : chaptersMap.keySet()) {
            bookElement.add(chaptersMap.get(chapterId).toXMLElement());
        }

        return bookElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Book buildFromXMLElement(Element element) {
        documentId = Long.parseLong(element.attributeValue("DocumentId"));
        documentTitle = element.attributeValue("DocumentTitle");
        authorsList = EntityHelper.buildAuthorsStrList(element.attributeValue("AuthorsList"));
        comment = element.attributeValue("Comment");
        if (element.attributeValue("Edition") != null) {
            edition = Integer.parseInt(element.attributeValue("Edition"));
        }
        if (element.attributeValue("PublishedYear") != null) {
            publishedYear = Integer.parseInt(element.attributeValue("PublishedYear"));
        }
        isbn = element.attributeValue("ISBN");
        createdTime = new Date(Long.parseLong(element.attributeValue("CreatedTime")));
        lastUpdatedTime = new Date(Long.parseLong(element.attributeValue("LastUpdatedTime")));

        chaptersMap = new TreeMap<Long, Chapter>();
        for (Element chapterElement : element.elements()) {
            Chapter newChapter = new Chapter().buildFromXMLElement(chapterElement);
            chaptersMap.put(newChapter.getChapterId(), newChapter);
        }

        return this;
    }
}

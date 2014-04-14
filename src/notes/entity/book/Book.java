package notes.entity.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.bean.BookHome;
import notes.entity.aware.AuthorsAware;
import notes.entity.aware.CommentAware;
import notes.entity.aware.CreatedTimeAware;
import notes.entity.aware.LastUpdatedTimeAware;
import notes.entity.impl.AbstractDocument;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.IOException;
import java.io.Writer;
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
public class Book extends AbstractDocument
        implements AuthorsAware, CreatedTimeAware, LastUpdatedTimeAware, CommentAware {

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

    /**
     * {inheritDoc}
     */
    @Override
    public void export(Writer writer) {
        BookHome home = BookHome.get();
        try {
            writer.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF8'><title>");
            writer.append(getDocumentTitle());
            writer.append("</title></head>");
            writer.append("<body><a name='title'/><h2>").append(getDocumentTitle()).append("</h2>");

            // Output document ID.
            writer.append("<b>Document ID: </b>").append(getDocumentId().toString()).append("<br>");

            // Output authors.
            if (!getAuthorsList().isEmpty()) {
                writer.append("<b>Author(s): </b>");
                StringBuilder authorsBuilder = new StringBuilder();
                for (String author : getAuthorsList()) {
                    authorsBuilder.append(author).append(", ");
                }
                authorsBuilder.delete(authorsBuilder.length() - 2, authorsBuilder.length());
                writer.append(authorsBuilder);
                writer.append("<br>");
            }

            // Output edition.
            if (getEdition() != null) {
                writer.append("<b>Edition: </b>").append(getEdition().toString()).append("<br>");
            }

            // Output published year.
            if (getPublishedYear() != null) {
                writer.append("<b>Published Year: </b>").append(getPublishedYear().toString()).append("<br>");
            }

            // Output ISBN.
            if (getIsbn() != null && !getIsbn().equals("")) {
                writer.append("<b>ISBN: </b>").append(getIsbn()).append("<br>");
            }

            // Output created time.
            writer.append("<b>Created Time: </b>").append(getCreatedTime().toString()).append("<br>");

            // Output last updated time.
            writer.append("<b>Last Updated Time: </b>").append(getLastUpdatedTime().toString()).append("<br>");

            // Output number of notes.
            writer.append("<b>Number of Notes: </b>").append(String.valueOf(getNotesCount())).append("<br>");

            // Output comment.
            writer.append("<b>Comment: </b><br>");
            writer.append(getComment());
            writer.append("<br>");

            writer.append("<br>");

            // Output anchors for chapters.
            for (Long chapterId : getChaptersMap().keySet()) {
                writer.append("<a href='#chapter").append(String.valueOf(chapterId)).append("'><b>Chapter ")
                        .append(String.valueOf(chapterId)).append(": ");
                writer.append(getChaptersMap().get(chapterId).getChapterTitle());
                writer.append("</b></a><br>");
            }

            writer.append("<br><hr>");

            // Output notes for each chapter.
            for (Long chapterId : getChaptersMap().keySet()) {
                // Output chapter title.
                Chapter chapter = getChaptersMap().get(chapterId);
                writer.append("<a name='chapter").append(String.valueOf(chapterId)).append("'/><h3>Chapter ")
                        .append(String.valueOf(chapterId)).append(": ");
                writer.append(chapter.getChapterTitle());
                writer.append("</h3>");

                // Output anchor to title.
                writer.append("<a href='#title'>Back to Top</a>");

                // Output all notes in the chapter.
                List<BookNote> noteList = home.getNotesListForCurrentChapter();
                for (BookNote note : noteList) {
                    writer.append("<p>");

                    // Output note text.
                    String noteText = note.getNoteText();
                    noteText = noteText.replaceAll("&", "&amp;");
                    noteText = noteText.replaceAll("<", "&lt;");
                    noteText = noteText.replaceAll("\n", "<br>");
                    noteText = noteText.replaceAll("  ", "&emsp;");
                    noteText = noteText.replaceAll("\t", "&emsp;&emsp;");
                    writer.append(noteText);
                    writer.append("<br><i>");

                    // Output tags.
                    if (!note.getTagIds().isEmpty()) {
                        for (Long tagId : note.getTagIds()) {
                            writer.append("[").append(home.getBookNoteDAO().findTagById(tagId).getTagText())
                                    .append("] ");
                        }
                    }

                    // Output note ID.
                    writer.append("ID: ").append(note.getNoteId().toString());

                    writer.append("</i><br></p>");
                }
                writer.append("<hr>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

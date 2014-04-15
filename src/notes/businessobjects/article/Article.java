package notes.businessobjects.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.businesslogic.ArticleBusinessLogic;
import notes.businessobjects.Note;
import notes.businessobjects.aware.AuthorsAware;
import notes.businessobjects.aware.CommentAware;
import notes.businessobjects.aware.CreatedTimeAware;
import notes.businessobjects.aware.LastUpdatedTimeAware;
import notes.businessobjects.impl.AbstractDocument;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * Entity class to describe an article.
 * <p/>
 * Author: Rui Du
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

    /**
     * {inheritDoc}
     */
    @Override
    public void export(Writer writer) {
        ArticleBusinessLogic logic = ArticleBusinessLogic.get();
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

            // Output source.
            if (getSource() != null && !getSource().equals("")) {
                writer.append("<b>Source: </b>");
                writer.append(getSource());
                writer.append("<br>");
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

            writer.append("<br><hr>");

            // Output all notes in the article.
            List<Note> noteList = logic.getAllNotesForCurrentArticle();
            for (Note note : noteList) {
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
                        writer.append("[").append(logic.getArticleNoteDAO().findTagById(tagId).getTagText()).append("] ");
                    }
                }

                // Output note ID.
                writer.append("ID: ").append(note.getNoteId().toString());

                writer.append("</i><br></p>");
            }
            writer.append("<hr>");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

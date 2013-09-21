package notes.article;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import notes.entity.impl.AbstractNote;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.Date;
import java.util.List;

/**
 * Entity class to describe an article note.
 *
 * @author Rui Du
 * @version 1.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
public class ArticleNote extends AbstractNote {

    /**
     * Constructs an instance of {@code ArticleNote}.
     *
     * @param noteId     The note identifier.
     * @param documentId The document identifier.
     * @param tagIds     The list of tag identifiers.
     * @param noteText   The note's text.
     * @throws IllegalArgumentException
     */
    public ArticleNote(final Long noteId, final Long documentId, final List<Long> tagIds,
                       final String noteText) throws IllegalArgumentException {
        this.noteId = noteId;
        this.documentId = documentId;
        this.tagIds = tagIds;
        this.noteText = noteText;
        this.createdTime = new Date(System.currentTimeMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element articleNoteElement = new DefaultElement("ArticleNote");

        articleNoteElement.addAttribute("NoteId", noteId.toString());
        articleNoteElement.addAttribute("DocumentId", documentId.toString());
        articleNoteElement.addAttribute("TagIds", EntityHelper.buildEntityStrFromList(tagIds));
        articleNoteElement.addAttribute("CreatedTime", createdTime.toString());
        articleNoteElement.addText(noteText);

        return articleNoteElement;
    }

}

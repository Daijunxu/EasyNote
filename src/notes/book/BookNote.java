package notes.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.impl.AbstractNote;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.Date;
import java.util.List;

/**
 * Entity class to describe a book note.
 *
 * @author Rui Du
 * @version 1.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class BookNote extends AbstractNote {

    /**
     * The chapter identifier.
     */
    @Getter
    @Setter
    private Long chapterId;

    /**
     * Constructs an instance of {@code BookNote}.
     *
     * @param noteId     The note identifier.
     * @param documentId The document identifier.
     * @param chapterId  The chapter identifier.
     * @param tagIds     The list of tag identifiers.
     * @param noteText   The note's text.
     * @throws IllegalArgumentException
     */
    public BookNote(final Long noteId, final Long documentId, final Long chapterId,
                    final List<Long> tagIds, final String noteText) throws IllegalArgumentException {
        this.noteId = noteId;
        this.documentId = documentId;
        this.chapterId = chapterId;
        this.tagIds = tagIds;
        this.noteText = noteText;
        this.createdTime = new Date(System.currentTimeMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element bookNoteElement = new DefaultElement("BookNote");

        bookNoteElement.addAttribute("NoteId", noteId.toString());
        bookNoteElement.addAttribute("DocumentId", documentId.toString());
        bookNoteElement.addAttribute("ChapterId", chapterId.toString());
        bookNoteElement.addAttribute("TagIds", EntityHelper.buildEntityStrFromList(tagIds));
        bookNoteElement.addAttribute("CreatedTime", String.valueOf(createdTime.getTime()));
        bookNoteElement.addText(noteText);

        return bookNoteElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookNote buildFromXMLElement(Element element) {
        noteId = Long.parseLong(element.attributeValue("NoteId"));
        documentId = Long.parseLong(element.attributeValue("DocumentId"));
        chapterId = Long.parseLong(element.attributeValue("ChapterId"));
        tagIds = EntityHelper.buildIDsList(element.attributeValue("TagIds"));
        noteText = element.getText();
        createdTime = new Date(Long.parseLong(element.attributeValue("CreatedTime")));

        return this;
    }
}

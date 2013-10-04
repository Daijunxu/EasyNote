package notes.entity.workset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.NoteStatus;
import notes.entity.impl.AbstractNote;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.Date;
import java.util.List;

/**
 * Entity class to describe a note in the worksheet.
 * <p/>
 * User: rui
 * Date: 9/30/13
 * Time: 1:36 AM
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
public class WorksheetNote extends AbstractNote {

    /**
     * The worksheet identifier.
     */
    @Getter
    @Setter
    private Long worksheetId;

    /**
     * The status of the note.
     */
    @Getter
    @Setter
    private NoteStatus noteStatus;

    /**
     * Constructs an instance of {@code WorksheetNote}.
     *
     * @param noteId      The note identifier.
     * @param documentId  The document identifier.
     * @param worksheetId The worksheet identifier.
     * @param tagIds      The list of tag identifiers.
     * @param noteText    The note's text.
     * @param noteStatus  The status of the note.
     * @throws IllegalArgumentException
     */
    public WorksheetNote(final Long noteId, final Long documentId, final Long worksheetId,
                         final List<Long> tagIds, final String noteText, final NoteStatus noteStatus)
            throws IllegalArgumentException {
        this.noteId = noteId;
        this.documentId = documentId;
        this.worksheetId = worksheetId;
        this.tagIds = tagIds;
        this.noteText = noteText;
        this.noteStatus = noteStatus;
        this.createdTime = new Date(System.currentTimeMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element worksheetNoteElement = new DefaultElement("WorksheetNote");

        worksheetNoteElement.addAttribute("NoteId", noteId.toString());
        worksheetNoteElement.addAttribute("DocumentId", documentId.toString());
        worksheetNoteElement.addAttribute("WorksheetId", worksheetId.toString());
        worksheetNoteElement.addAttribute("TagIds", EntityHelper.buildEntityStrFromList(tagIds));
        // TODO: remove this null check after adding note status in the GUI, should have a default value.
        if (noteStatus != null) {
            worksheetNoteElement.addAttribute("NoteStatus", String.valueOf(noteStatus.ordinal()));
        }
        worksheetNoteElement.addAttribute("CreatedTime", String.valueOf(createdTime.getTime()));
        worksheetNoteElement.addText(noteText);

        return worksheetNoteElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorksheetNote buildFromXMLElement(Element element) {
        noteId = Long.parseLong(element.attributeValue("NoteId"));
        documentId = Long.parseLong(element.attributeValue("DocumentId"));
        worksheetId = Long.parseLong(element.attributeValue("WorksheetId"));
        tagIds = EntityHelper.buildIDsList(element.attributeValue("TagIds"));
        noteText = element.getText();
        // TODO: remove this null check after adding note status in the GUI, should have a default value.
        if (element.attributeValue("NoteStatus") != null) {
            noteStatus = NoteStatus.values()[Integer.parseInt(element.attributeValue("NoteStatus"))];
        }
        createdTime = new Date(Long.parseLong(element.attributeValue("CreatedTime")));

        return this;
    }
}

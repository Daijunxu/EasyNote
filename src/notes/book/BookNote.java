package notes.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.impl.AbstractNote;

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
        setNoteId(noteId);
        setDocumentId(documentId);
        setChapterId(chapterId);
        setTagIds(tagIds);
        setNoteText(noteText);
        setCreatedTime(new Date(System.currentTimeMillis()));
    }
}

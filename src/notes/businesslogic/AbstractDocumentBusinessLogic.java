package notes.businesslogic;

import notes.businessobjects.Note;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * The abstract class of business logic for document types.
 * <p/>
 * Author: Rui Du
 * Date: 4/22/14
 * Time: 9:20 PM
 */
public abstract class AbstractDocumentBusinessLogic implements Serializable {

    public boolean canMoveCurrentNoteUp() {
        validateCurrentNote();
        int index = getIndexForNote(getCurrentNote().getNoteId());
        return (index > 0);
    }

    public boolean canMoveCurrentNoteDown() {
        validateCurrentNote();
        int index = getIndexForNote(getCurrentNote().getNoteId());
        return (index < getCurrentNoteList().size() - 1);
    }

    public void moveCurrentNoteUp() {
        validateCurrentNote();
        int index = getIndexForNote(getCurrentNote().getNoteId());
        Collections.swap(getCurrentNoteList(), index, index - 1);
    }

    public void moveCurrentNoteDown() {
        validateCurrentNote();
        int index = getIndexForNote(getCurrentNote().getNoteId());
        Collections.swap(getCurrentNoteList(), index, index + 1);
    }

    public void moveCurrentNoteToTop() {
        validateCurrentNote();
        int index = getIndexForNote(getCurrentNote().getNoteId());
        List<Long> noteIdsList = getCurrentNoteList();

        for (int i = index; i > 0; i--) {
            Collections.swap(noteIdsList, i, i - 1);
        }
    }

    public void moveCurrentNoteToBottom() {
        int index = getIndexForNote(getCurrentNote().getNoteId());
        List<Long> noteIdsList = getCurrentNoteList();

        for (int i = index; i < noteIdsList.size() - 1; i++) {
            Collections.swap(noteIdsList, i, i + 1);
        }
    }

    /**
     * Gets the index of the given note in the current worksheet.
     *
     * @param noteId The ID of the note to find.
     * @return int The index of the note.
     */
    public abstract int getIndexForNote(Long noteId);

    /**
     * Get the current note stored in the business logic object. This is the note object that user is focused on.
     *
     * @return {@code Note} The current note object.
     */
    public abstract Note getCurrentNote();

    /**
     * Get the list of note ids for the lowest level of note collection. For example, for {@code Article} this is
     * the note list of the article; For {@code Book} this is the note list of the selected chapter.
     *
     * @return {@code List<Long>} The list of note ids.
     */
    protected abstract List<Long> getCurrentNoteList();

    private void validateCurrentNote() {
        if (getCurrentNote() == null) {
            throw new UnsupportedOperationException("Current note is null!");
        }
    }
}

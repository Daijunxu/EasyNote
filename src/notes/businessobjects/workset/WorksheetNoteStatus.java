package notes.businessobjects.workset;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

/**
 * An enumeration type to indicate the status of a note.
 * <p/>
 * Author: Rui Du
 * Date: 9/30/13
 * Time: 1:43 AM
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WorksheetNoteStatus {
    NO_ACTION("No Action", null),
    NEED_ACTION("Need Action", new Color(255, 255, 0)),
    WORKING("Working", new Color(51, 153, 255)),
    ALERT("Blocked", new Color(220, 0, 0)),
    COMPLETED("Completed", new Color(0, 220, 0));

    /**
     * The description of the note status that is shown in the UI.
     */
    @Getter
    private final String description;

    /**
     * The color of the note under current status.
     */
    @Getter
    private final Color noteColor;

    /**
     * Get the WorksheetNoteStatus object from its description.
     *
     * @param description The description of the WorksheetNoteStatus object.
     * @return {@code WorksheetNoteStatus} The corresponding WorksheetNoteStatus object.
     */
    public static WorksheetNoteStatus getNoteStatusFromDescription(String description) {
        for (WorksheetNoteStatus noteStatus : WorksheetNoteStatus.values()) {
            if (noteStatus.getDescription().equals(description)) {
                return noteStatus;
            }
        }
        return null;
    }

}

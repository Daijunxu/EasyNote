package notes.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

/**
 * An enumeration type to indicate the status of a note.
 * <p/>
 * User: rui
 * Date: 9/30/13
 * Time: 1:43 AM
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NoteStatus {
    NO_ACTION("No Action", null),
    NEED_ACTION("Need Action", new Color(255, 255, 0)),
    WORKING("Working", new Color(51, 153, 255)),
    ALERT("Blocked", new Color(220,0,0)),
    DONE("Done", new Color(0, 220, 0));

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
     * Get the NoteStatus object from its description.
     *
     * @param description The description of the NoteStatus object.
     * @return {@code NoteStatus} The corresponding NoteStatus object.
     */
    public static NoteStatus getNoteStatusFromDescription(String description) {
        for (NoteStatus noteStatus : NoteStatus.values()) {
            if (noteStatus.getDescription().equals(description)) {
                return noteStatus;
            }
        }
        return null;
    }

}

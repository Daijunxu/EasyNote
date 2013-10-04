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
    NEED_ACTION("Need Action", Color.YELLOW),
    WORKING("Working", Color.BLUE),
    ALERT("Blocked", Color.RED),
    DONE("Done", Color.GREEN);

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

}

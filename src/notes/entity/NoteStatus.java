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
    NO_ACTION(null),
    NEED_ACTION(Color.YELLOW),
    ALERT(Color.RED),
    DONE(Color.GREEN);

    /**
     * The color of the note under current status.
     */
    @Getter
    private final Color noteColor;

}

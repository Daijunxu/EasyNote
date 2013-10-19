package notes.gui.main.validation;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Validates that the input note text is valid.
 * <p/>
 * User: rui
 * Date: 10/19/13
 * Time: 10:30 AM
 */
public class NoteTextInputValidator {
    private static final CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();

    private static boolean isPureAscii(String text) {
        return asciiEncoder.canEncode(text);
    }

    public static String hasError(String noteText) {
        if (noteText == null || noteText.trim().equals("")) {
            return "Note text cannot be empty!";
        }
        if (!isPureAscii(noteText)) {
            return "Note text contains special character!";
        }
        return null;
    }
}

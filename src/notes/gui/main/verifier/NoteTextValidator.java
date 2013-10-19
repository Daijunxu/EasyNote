package notes.gui.main.verifier;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Verifies that the input text in {@link javax.swing.JTextField} is valid.
 * <p/>
 * User: rui
 * Date: 10/19/13
 * Time: 10:30 AM
 */
public class NoteTextValidator {
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

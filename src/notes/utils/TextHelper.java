package notes.utils;

/**
 * A helper class to process texts in the note.
 * <p/>
 * User: rui
 * Date: 10/18/13
 * Time: 7:22 PM
 */
public class TextHelper {

    public static String processInputText(String rawText) {
        String resultText = rawText.replaceAll("\t", "    ");
        return resultText;
    }
}

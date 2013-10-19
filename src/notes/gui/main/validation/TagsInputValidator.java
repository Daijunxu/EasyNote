package notes.gui.main.validation;

import java.util.List;

/**
 * Validates that the input tags is valid.
 * <p/>
 * User: rui
 * Date: 10/19/13
 * Time: 11:28 AM
 */
public class TagsInputValidator {

    public static String hasError(List<String> tagsList) {
        if (tagsList.size() > 5) {
            return "A note can have at most 5 tags!";
        }
        for (String tagStr : tagsList) {
            if (tagStr.length() > 30) {
                return "A tag can have at most 30 characters!";
            }
        }
        return null;
    }
}

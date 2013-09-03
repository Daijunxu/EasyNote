/**
 *
 */
package notes.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Builds a list of tag strings from a string.
 *
 * @author Rui Du
 * @version 1.0
 */
public class TagsStrListBuilder {

    /**
     * Builds an list of tag strings from one input string. Note: The tags in the input string are
     * separated by ",".
     *
     * @param input The input string of tags.
     * @return {@code List<String>} The list of tag string.
     */
    public static List<String> buildTagsStrList(String input) {
        List<String> tagsStrList = new ArrayList<String>();
        if (input == null) {
            return tagsStrList;
        }
        String[] tagStrs = input.split(",");
        for (String tagStr : tagStrs) {
            tagStr = WordUtils.capitalize(tagStr.trim());
            if (!tagStr.equals("")) {
                tagsStrList.add(tagStr);
            }
        }
        return tagsStrList;
    }
}

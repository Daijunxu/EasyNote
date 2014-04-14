package notes.utils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for entities classes.
 *
 * Author: Rui Du
 */
public class EntityHelper {

    /**
     * Builds an list of author strings from one input string.
     * Note: The authors in the input string are separated by ",".
     *
     * @param input The input string of authors.
     * @return {@code List<String>} The list of author string.
     */
    public static List<String> buildAuthorsStrList(String input) {
        List<String> authorsStrList = new ArrayList<String>();
        if (input == null) {
            return authorsStrList;
        }
        input = WordUtils.capitalize(input);
        String[] authorStrs = input.split(",");
        for (String authorStr : authorStrs) {
            authorStr = authorStr.trim();
            if (!authorStr.equals("")) {
                authorsStrList.add(authorStr);
            }
        }
        return authorsStrList;
    }

    /**
     * Builds an list of tag strings from one input string.
     * Note: The tags in the input string are separated by ",".
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
            tagStr = tagStr.replaceAll("\t+", " ");
            tagStr = WordUtils.capitalize(tagStr.trim());
            if (!tagStr.equals("")) {
                tagsStrList.add(tagStr);
            }
        }
        return tagsStrList;
    }

    /**
     * Builds an list of entity IDs from one input string.
     * Note: The IDs in the input string are separated by ",".
     *
     * @param input The input string of entity IDs.
     * @return {@code List<Long>} The list of entity IDs.
     */
    public static List<Long> buildIDsList(String input) {
        List<Long> idList = new ArrayList<Long>();
        if (input == null) {
            return idList;
        }
        String[] idStrs = input.split(",");
        for (String idStr : idStrs) {
            idList.add(Long.parseLong(idStr));
        }
        return idList;
    }

    /**
     * Builds a String of entities from a list of entities.
     * Each entity in the string is separated by ",".
     *
     * @param entityList A list of entities.
     * @return {@code String} A string representation of entities.
     */
    public static String buildEntityStrFromList(List<?> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (Object entity : entityList) {
            sb.append(entity.toString()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}

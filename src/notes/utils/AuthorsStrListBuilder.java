/**
 * 
 */
package notes.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Builds a list of author strings from a string.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class AuthorsStrListBuilder {

	/**
	 * Builds an list of author strings from one input string. Note: The authors in the input string
	 * are separated by ",".
	 * 
	 * @param input
	 *            The input string of authors.
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
}

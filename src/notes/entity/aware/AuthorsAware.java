package notes.entity.aware;

import java.util.List;

/**
 * Aware interface for entities that require information of authors.
 * <p/>
 * Author: Rui Du
 * Date: 10/3/13
 * Time: 1:22 AM
 */
public interface AuthorsAware {

    /**
     * Gets the list of authors.
     *
     * @return {@code List<String>} The list of authors.
     */
    List<String> getAuthorsList();

    /**
     * Sets the list of authors.
     *
     * @param authorsList The list of authors to set.
     */
    void setAuthorsList(List<String> authorsList);
}

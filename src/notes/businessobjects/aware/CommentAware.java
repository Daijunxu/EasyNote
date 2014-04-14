package notes.businessobjects.aware;

/**
 * Aware interface for entities that require comment.
 * <p/>
 * Author: Rui Du
 * Date: 10/3/13
 * Time: 6:08 AM
 */
public interface CommentAware {

    /**
     * Gets the comment.
     *
     * @return {@code String} The comment.
     */
    String getComment();

    /**
     * Sets the comment.
     *
     * @param comment The comment.
     */
    void setComment(String comment);
}

package notes.entity.aware;

import java.util.Date;

/**
 * Aware interface for entities that require information of created time.
 * <p/>
 * User: rui
 * Date: 10/3/13
 * Time: 1:54 AM
 */
public interface CreatedTimeAware {

    /**
     * Gets the document's created time.
     *
     * @return {@code Date} The document's created time.
     */
    Date getCreatedTime();

    /**
     * Sets the document's created time.
     *
     * @param createTime The document's created time to set.
     */
    void setCreatedTime(Date createTime);
}

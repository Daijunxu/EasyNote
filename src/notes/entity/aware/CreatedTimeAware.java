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
     * Gets the created time.
     *
     * @return {@code Date} The created time.
     */
    Date getCreatedTime();

    /**
     * Sets the created time.
     *
     * @param createTime The created time to set.
     */
    void setCreatedTime(Date createTime);
}

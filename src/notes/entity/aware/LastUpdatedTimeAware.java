package notes.entity.aware;

import java.util.Date;

/**
 * Aware interface for entities that require information of last updated time.
 * <p/>
 * User: rui
 * Date: 10/3/13
 * Time: 2:04 AM
 */
public interface LastUpdatedTimeAware {

    /**
     * Gets the document's last updated time.
     *
     * @return {@code Date} The document's last updated time.
     */
    Date getLastUpdatedTime();

    /**
     * Sets the document's last updated time.
     *
     * @param lastUpdatedTime The document's last updated time.
     */
    void setLastUpdatedTime(Date lastUpdatedTime);
}

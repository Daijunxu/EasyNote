package notes.entity.aware;

import java.util.Date;

/**
 * Aware interface for entities that require information of last updated time.
 * <p/>
 * Author: Rui Du
 * Date: 10/3/13
 * Time: 2:04 AM
 */
public interface LastUpdatedTimeAware {

    /**
     * Gets last updated time.
     *
     * @return {@code Date} The last updated time.
     */
    Date getLastUpdatedTime();

    /**
     * Sets last updated time.
     *
     * @param lastUpdatedTime The last updated time.
     */
    void setLastUpdatedTime(Date lastUpdatedTime);
}

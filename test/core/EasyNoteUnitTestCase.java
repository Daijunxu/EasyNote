package core;

import notes.data.cache.Cache;
import notes.data.cache.Property;
import org.junit.Before;

/**
 * Basic unit test cases for the system.
 * <p/>
 * User: rui
 * Date: 9/7/13
 * Time: 11:56 PM
 */
public class EasyNoteUnitTestCase {

    protected static final String TEST_DATA_LOCATION_OVERRIDE = "./test/reading_notes.data";

    /**
     * Load and refresh the cache using test data.
     *
     * @throws java.lang.Exception
     */
    @Before
    public void initializeCache() throws Exception {
        Property.get().setDataLocation(TEST_DATA_LOCATION_OVERRIDE);
        Cache.get().reload();
    }
}

/**
 * 
 */
package notes.data.cache.note;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import notes.data.cache.Cache;
import notes.data.cache.CacheUnitTests;
import notes.data.cache.Property;
import notes.entity.Note;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the {@code NoteCache}.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class NoteCacheUnitTests {

	/**
	 * Data required for unit tests. NOTE: A new instance should be created for each unit test.
	 */
	public static class UnitTestData extends CacheUnitTests.UnitTestData {
	}

	/**
	 * Load the cache if it has not been initialized.
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initializeCache() throws Exception {
		Property.get().setDataLocation("./resources/testData/reading_notes.data");
		Cache.get();
	}

	/**
	 * Reload the cache.
	 * 
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void reloadCache() throws Exception {
		Cache.get().reload();
	}

	/**
	 * Test method for {@link notes.data.cache.note.NoteCache#clear()}.
	 */
	@Test
	public void testClear() {
		Cache.get().getNoteCache().clear();
		assertNotNull(Cache.get().getNoteCache());
		assertTrue(Cache.get().getNoteCache().getNoteMap().isEmpty());
		assertTrue(Cache.get().getNoteCache().getMaxNoteId() == Long.MIN_VALUE);
		Cache.get().reload();
	}

	/**
	 * Test method for {@link notes.data.cache.note.NoteCache#getMaxNoteId()}.
	 */
	@Test
	public void testGetMaxNoteId() {
		final UnitTestData testData = new UnitTestData();
		assertNotNull(Cache.get().getNoteCache().getMaxNoteId());
		assertEquals(testData.maxNoteId, Cache.get().getNoteCache().getMaxNoteId());
	}

	/**
	 * Test method for {@link notes.data.cache.note.NoteCache#getNoteMap()}.
	 */
	@Test
	public void testGetNoteMap() {
		final UnitTestData testData = new UnitTestData();
		assertNotNull(Cache.get().getNoteCache().getNoteMap());
		Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
		assertFalse(noteMap.isEmpty());
		assertEquals(testData.noteMap.keySet(), noteMap.keySet());
		for (Note note : testData.noteMap.values()) {
			assertEquals(note, noteMap.get(note.getNoteId()));
		}
	}
}

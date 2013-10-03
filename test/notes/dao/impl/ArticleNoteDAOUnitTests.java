package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.data.cache.Cache;
import notes.entity.Note;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@code ArticleNoteDAO}.
 *
 * @author Rui Du
 */
public class ArticleNoteDAOUnitTests extends EasyNoteUnitTestCase {

    /**
     * The data access object for the {@code ArticleNoteDAO}.
     */
    private ArticleNoteDAO dao = new ArticleNoteDAO();

    /**
     * Test method for
     * {@link notes.dao.impl.ArticleNoteDAO#deleteDocument(notes.entity.Document)}.
     */
    @Test
    public void testDeleteDocument() {
        UnitTestData testData = new UnitTestData();
        Article deleteDocument = (Article) testData.documentMap.get(2L);
        dao.deleteDocument(deleteDocument);
        assertNotNull(Cache.get().getDocumentCache().getDocumentMap());
        assertNotNull(Cache.get().getDocumentCache().getDocumentTitleIdMap());
        assertFalse(Cache.get().getDocumentCache().getDocumentMap().isEmpty());
        assertFalse(Cache.get().getDocumentCache().getDocumentTitleIdMap().isEmpty());
        assertNull(Cache.get().getDocumentCache().getDocumentMap()
                .get(deleteDocument.getDocumentId()));
        assertFalse(Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .containsKey(deleteDocument.getDocumentTitle()));
        assertFalse(Cache.get().getNoteCache().getNoteMap().containsKey(2L));
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#deleteNote(notes.entity.Note)}.
     */
    @Test
    public void testDeleteNote() {
        UnitTestData testData = new UnitTestData();
        ArticleNote deleteNote = (ArticleNote) testData.noteMap.get(2L);
        dao.deleteNote(deleteNote);
        assertNotNull(Cache.get().getNoteCache().getNoteMap());
        assertFalse(Cache.get().getNoteCache().getNoteMap().isEmpty());
        assertNull(Cache.get().getNoteCache().getNoteMap().get(deleteNote.getNoteId()));
        Article article = (Article) Cache.get().getDocumentCache().getDocumentMap()
                .get(deleteNote.getDocumentId());
        assertFalse(article.getNotesList().contains(deleteNote.getNoteId()));
    }

    /**
     * Test method for
     * {@link notes.dao.impl.ArticleNoteDAO#findAllNotesByDocumentId(java.lang.Long)}.
     */
    @Test
    public void testFindAllNotesByDocumentId() {
        UnitTestData testData = new UnitTestData();
        List<Note> noteList = dao.findAllNotesByDocumentId(2L);
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == 1);
        assertEquals(testData.noteMap.get(noteList.get(0).getNoteId()), noteList.get(0));
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#updateDocument(notes.entity.Document)}
     * .
     */
    @Test
    public void testMergeDocument() {
        UnitTestData testData = new UnitTestData();
        Article testArticle = (Article) testData.documentMap.get(2L);
        Article newArticle = new Article();
        newArticle.setDocumentId(testArticle.getDocumentId());
        newArticle.setDocumentTitle(testArticle.getDocumentTitle());
        newArticle.setAuthorsList(testArticle.getAuthorsList());
        newArticle.setComment("This article is not worth reading!");
        newArticle.setSource("Unknown source!");
        Article updatedArticle = (Article) dao.updateDocument(newArticle);

        assertNotNull(updatedArticle);
        assertEquals(updatedArticle, Cache.get().getDocumentCache().getDocumentMap().get(newArticle.getDocumentId()));
        assertFalse(updatedArticle.equals(testArticle));
        assertEquals(updatedArticle.getComment(), newArticle.getComment());
        assertEquals(updatedArticle.getSource(), newArticle.getSource());
        assertEquals(testArticle.getDocumentId(), updatedArticle.getDocumentId());
        assertEquals(testArticle.getCreatedTime(), updatedArticle.getCreatedTime());
        assertTrue(testArticle.getLastUpdatedTime().compareTo(updatedArticle.getLastUpdatedTime()) < 0);
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#updateNote(notes.entity.Note)}.
     */
    @Test
    public void testMergeNote() {
        UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = (ArticleNote) testData.noteMap.get(2L);
        ArticleNote newArticleNote = new ArticleNote();
        newArticleNote.setNoteId(testArticleNote.getNoteId());
        newArticleNote.setDocumentId(testArticleNote.getDocumentId());
        newArticleNote.setTagIds(testArticleNote.getTagIds());
        newArticleNote.setNoteText("A new note text.");
        ArticleNote updatedArticleNote = (ArticleNote) dao.updateNote(newArticleNote);

        assertNotNull(updatedArticleNote);
        assertEquals(updatedArticleNote, Cache.get().getNoteCache().getNoteMap().get(newArticleNote.getNoteId()));
        assertFalse(updatedArticleNote.equals(testArticleNote));
        assertEquals(updatedArticleNote.getNoteText(), newArticleNote.getNoteText());
        assertFalse(updatedArticleNote.getNoteText().equals(testArticleNote.getNoteText()));
        assertEquals(testArticleNote.getNoteId(), updatedArticleNote.getNoteId());
        assertEquals(testArticleNote.getCreatedTime(), updatedArticleNote.getCreatedTime());
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#saveDocument(notes.entity.Document)}.
     */
    @Test
    public void testSaveDocument() {
        Article newArticle = new Article();
        newArticle.setDocumentId(4L);
        newArticle.setDocumentTitle("Data Mining");
        newArticle.setAuthorsList(new ArrayList<String>(Arrays.asList("Author")));
        newArticle.setComment("Good article.");
        newArticle.setSource("Unknown source.");
        Article savedArticle = (Article) dao.saveDocument(newArticle);

        assertEquals(savedArticle,
                Cache.get().getDocumentCache().getDocumentMap().get(newArticle.getDocumentId()));
        assertTrue(Cache.get().getDocumentCache().getDocumentTitleIdMap()
                .containsKey(newArticle.getDocumentTitle()));
        assertEquals(Cache.get().getDocumentCache().getMaxDocumentId(), newArticle.getDocumentId());
        assertNotNull(savedArticle.getCreatedTime());
        assertNotNull(savedArticle.getLastUpdatedTime());
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#saveNote(notes.entity.Note)}.
     */
    @Test
    public void testSaveNote() {
        ArticleNote newArticleNote = new ArticleNote();
        newArticleNote.setNoteId(4L);
        newArticleNote.setDocumentId(2L);
        newArticleNote.setTagIds(new ArrayList<Long>(Arrays.asList(2L)));
        newArticleNote.setNoteText("New note.");
        ArticleNote savedArticleNote = (ArticleNote) dao.saveNote(newArticleNote);

        assertEquals(savedArticleNote,
                Cache.get().getNoteCache().getNoteMap().get(newArticleNote.getNoteId()));
        assertEquals(Cache.get().getNoteCache().getMaxNoteId(), newArticleNote.getNoteId());
        assertNotNull(savedArticleNote.getCreatedTime());
        Article article = (Article) Cache.get().getDocumentCache().getDocumentMap()
                .get(savedArticleNote.getDocumentId());
        assertTrue(article.getNotesList().contains(savedArticleNote.getNoteId()));
    }

}

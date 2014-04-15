package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;
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
 * Author: Rui Du
 */
public class ArticleNoteDAOUnitTests extends EasyNoteUnitTestCase {

    /**
     * The data access object for the {@code ArticleNoteDAO}.
     */
    private ArticleNoteDAO dao = ArticleNoteDAO.get();

    /**
     * Test method for
     * {@link notes.dao.impl.ArticleNoteDAO#deleteDocument(notes.businessobjects.Document)}.
     */
    @Test
    public void testDeleteDocument() {
        UnitTestData testData = new UnitTestData();
        Article deleteDocument = (Article) testData.documentMap.get(2L);
        dao.deleteDocument(deleteDocument);
        assertNotNull(CACHE.getDocumentCache().getDocumentMap());
        assertNotNull(CACHE.getDocumentCache().getDocumentTitleIdMap());
        assertFalse(CACHE.getDocumentCache().getDocumentMap().isEmpty());
        assertFalse(CACHE.getDocumentCache().getDocumentTitleIdMap().isEmpty());
        assertNull(CACHE.getDocumentCache().getDocumentMap()
                .get(deleteDocument.getDocumentId()));
        assertFalse(CACHE.getDocumentCache().getDocumentTitleIdMap()
                .containsKey(deleteDocument.getDocumentTitle()));
        assertFalse(CACHE.getNoteCache().getNoteMap().containsKey(2L));
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#deleteNote(notes.businessobjects.Note)}.
     */
    @Test
    public void testDeleteNote() {
        UnitTestData testData = new UnitTestData();
        ArticleNote deleteNote = (ArticleNote) testData.noteMap.get(2L);
        dao.deleteNote(deleteNote);
        assertNotNull(CACHE.getNoteCache().getNoteMap());
        assertFalse(CACHE.getNoteCache().getNoteMap().isEmpty());
        assertNull(CACHE.getNoteCache().getNoteMap().get(deleteNote.getNoteId()));
        Article article = (Article) CACHE.getDocumentCache().getDocumentMap()
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
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#updateDocument(notes.businessobjects.Document)}
     * .
     */
    @Test
    public void testUpdateDocument() {
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
        assertEquals(updatedArticle, CACHE.getDocumentCache().getDocumentMap().get(newArticle.getDocumentId()));
        assertFalse(updatedArticle.equals(testArticle));
        assertEquals(updatedArticle.getComment(), newArticle.getComment());
        assertEquals(updatedArticle.getSource(), newArticle.getSource());
        assertEquals(testArticle.getDocumentId(), updatedArticle.getDocumentId());
        assertEquals(testArticle.getCreatedTime(), updatedArticle.getCreatedTime());
        assertTrue(testArticle.getLastUpdatedTime().compareTo(updatedArticle.getLastUpdatedTime()) < 0);
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#updateNote(notes.businessobjects.Note)}.
     */
    @Test
    public void testUpdateNote() {
        UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = (ArticleNote) testData.noteMap.get(2L);
        ArticleNote newArticleNote = new ArticleNote();
        newArticleNote.setNoteId(testArticleNote.getNoteId());
        newArticleNote.setDocumentId(testArticleNote.getDocumentId());
        newArticleNote.setTagIds(testArticleNote.getTagIds());
        newArticleNote.setNoteText("A new note text.");
        ArticleNote updatedArticleNote = (ArticleNote) dao.updateNote(newArticleNote);

        assertNotNull(updatedArticleNote);
        assertEquals(updatedArticleNote, CACHE.getNoteCache().getNoteMap().get(newArticleNote.getNoteId()));
        assertFalse(updatedArticleNote.equals(testArticleNote));
        assertEquals(updatedArticleNote.getNoteText(), newArticleNote.getNoteText());
        assertFalse(updatedArticleNote.getNoteText().equals(testArticleNote.getNoteText()));
        assertEquals(testArticleNote.getNoteId(), updatedArticleNote.getNoteId());
        assertEquals(testArticleNote.getCreatedTime(), updatedArticleNote.getCreatedTime());
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#saveDocument(notes.businessobjects.Document)}.
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
                CACHE.getDocumentCache().getDocumentMap().get(newArticle.getDocumentId()));
        assertTrue(CACHE.getDocumentCache().getDocumentTitleIdMap()
                .containsKey(newArticle.getDocumentTitle()));
        assertEquals(CACHE.getDocumentCache().getMaxDocumentId(), newArticle.getDocumentId());
        assertNotNull(savedArticle.getCreatedTime());
        assertNotNull(savedArticle.getLastUpdatedTime());
    }

    /**
     * Test method for {@link notes.dao.impl.ArticleNoteDAO#saveNote(notes.businessobjects.Note)}.
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
                CACHE.getNoteCache().getNoteMap().get(newArticleNote.getNoteId()));
        assertEquals(CACHE.getNoteCache().getMaxNoteId(), newArticleNote.getNoteId());
        assertNotNull(savedArticleNote.getCreatedTime());
        Article article = (Article) CACHE.getDocumentCache().getDocumentMap()
                .get(savedArticleNote.getDocumentId());
        assertTrue(article.getNotesList().contains(savedArticleNote.getNoteId()));
    }

}

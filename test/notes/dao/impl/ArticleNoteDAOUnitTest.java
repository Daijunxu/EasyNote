package notes.dao.impl;

import core.EasyNoteUnitTestCase;
import notes.businessobjects.Note;
import notes.businessobjects.article.Article;
import notes.businessobjects.article.ArticleNote;
import notes.dao.DuplicateRecordException;
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
 * <p/>
 * Author: Rui Du
 */
public class ArticleNoteDAOUnitTest extends EasyNoteUnitTestCase {

    private ArticleNoteDAO dao = ArticleNoteDAO.get();

    @Test
    public void testDeleteDocument() {
        UnitTestData testData = new UnitTestData();
        Article deleteDocument = testData.getArticle();
        dao.deleteDocument(deleteDocument);
        assertFalse(CACHE.getDocumentCache().findAll().isEmpty());
        assertNull(CACHE.getDocumentCache().find(deleteDocument.getDocumentId()));
        assertNull(CACHE.getNoteCache().find(2L));
    }

    @Test
    public void testDeleteNote() {
        UnitTestData testData = new UnitTestData();
        ArticleNote deleteNote = testData.getArticleNote();
        dao.deleteNote(deleteNote);
        assertFalse(CACHE.getNoteCache().findAll().isEmpty());
        assertNull(CACHE.getNoteCache().find(deleteNote.getNoteId()));
        Article article = (Article) CACHE.getDocumentCache().find(deleteNote.getDocumentId());
        assertFalse(article.getNotesList().contains(deleteNote.getNoteId()));
    }

    @Test
    public void testFindAllNotesByDocumentId() {
        UnitTestData testData = new UnitTestData();
        Article testArticle = testData.getArticle();
        ArticleNote testArticleNote = testData.getArticleNote();
        List<Note> noteList = dao.findAllNotesByDocumentId(testArticle.getDocumentId());
        assertNotNull(noteList);
        assertFalse(noteList.isEmpty());
        assertTrue(noteList.size() == testArticle.getNotesCount());
        assertEquals(testArticleNote, noteList.get(0));
    }

    @Test
    public void testUpdateDocument() throws DuplicateRecordException {
        UnitTestData testData = new UnitTestData();
        Article testArticle = testData.getArticle();
        Article newArticle = new Article();
        newArticle.setDocumentId(testArticle.getDocumentId());
        newArticle.setDocumentTitle(testArticle.getDocumentTitle());
        newArticle.setAuthorsList(testArticle.getAuthorsList());
        newArticle.setComment("This article is not worth reading!");
        newArticle.setSource("Unknown source!");
        Article updatedArticle = (Article) dao.updateDocument(newArticle);

        assertNotNull(updatedArticle);
        assertEquals(updatedArticle, CACHE.getDocumentCache().find(newArticle.getDocumentId()));
        assertFalse(updatedArticle.equals(testArticle));
        assertEquals(updatedArticle.getComment(), newArticle.getComment());
        assertEquals(updatedArticle.getSource(), newArticle.getSource());
        assertEquals(testArticle.getDocumentId(), updatedArticle.getDocumentId());
        assertEquals(testArticle.getCreatedTime(), updatedArticle.getCreatedTime());
        assertTrue(testArticle.getLastUpdatedTime().compareTo(updatedArticle.getLastUpdatedTime()) < 0);
    }

    @Test
    public void testUpdateNote() {
        UnitTestData testData = new UnitTestData();
        ArticleNote testArticleNote = testData.getArticleNote();
        ArticleNote newArticleNote = new ArticleNote();
        newArticleNote.setNoteId(testArticleNote.getNoteId());
        newArticleNote.setDocumentId(testArticleNote.getDocumentId());
        newArticleNote.setTagIds(testArticleNote.getTagIds());
        newArticleNote.setNoteText("A new note text.");
        ArticleNote updatedArticleNote = (ArticleNote) dao.updateNote(newArticleNote);

        assertNotNull(updatedArticleNote);
        assertEquals(updatedArticleNote, CACHE.getNoteCache().find(newArticleNote.getNoteId()));
        assertFalse(updatedArticleNote.equals(testArticleNote));
        assertEquals(updatedArticleNote.getNoteText(), newArticleNote.getNoteText());
        assertFalse(updatedArticleNote.getNoteText().equals(testArticleNote.getNoteText()));
        assertEquals(testArticleNote.getNoteId(), updatedArticleNote.getNoteId());
        assertEquals(testArticleNote.getCreatedTime(), updatedArticleNote.getCreatedTime());
    }

    @Test
    public void testSaveDocument() throws DuplicateRecordException {
        Article newArticle = new Article();
        newArticle.setDocumentId(4L);
        newArticle.setDocumentTitle("Data Mining");
        newArticle.setAuthorsList(new ArrayList<String>(Arrays.asList("Author")));
        newArticle.setComment("Good article.");
        newArticle.setSource("Unknown source.");
        Article savedArticle = (Article) dao.saveDocument(newArticle);

        assertEquals(savedArticle, CACHE.getDocumentCache().find(newArticle.getDocumentId()));
        assertNotNull(savedArticle.getCreatedTime());
        assertNotNull(savedArticle.getLastUpdatedTime());
    }

    @Test
    public void testSaveNote() {
        ArticleNote newArticleNote = new ArticleNote();
        newArticleNote.setNoteId(4L);
        newArticleNote.setDocumentId(2L);
        newArticleNote.setTagIds(new ArrayList<Long>(Arrays.asList(2L)));
        newArticleNote.setNoteText("New note.");
        ArticleNote savedArticleNote = (ArticleNote) dao.saveNote(newArticleNote);

        assertEquals(savedArticleNote, CACHE.getNoteCache().find(newArticleNote.getNoteId()));
        assertNotNull(savedArticleNote.getCreatedTime());
        Article article = (Article) CACHE.getDocumentCache().find(savedArticleNote.getDocumentId());
        assertTrue(article.getNotesList().contains(savedArticleNote.getNoteId()));
    }

}

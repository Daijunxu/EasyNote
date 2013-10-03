package core;

import notes.data.cache.Cache;
import notes.data.cache.Property;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.NoteStatus;
import notes.entity.Tag;
import notes.entity.article.Article;
import notes.entity.article.ArticleNote;
import notes.entity.book.Book;
import notes.entity.book.BookNote;
import notes.entity.book.Chapter;
import notes.entity.workset.Workset;
import notes.entity.workset.Worksheet;
import notes.entity.workset.WorksheetNote;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Basic unit test cases for the system.
 * <p/>
 * User: rui
 * Date: 9/7/13
 * Time: 11:56 PM
 */
public class EasyNoteUnitTestCase {

    protected static final String TEST_DATA_XML_LOCATION_OVERRIDE = "./test/reading_notes.xml";

    /**
     * Load and refresh the cache using test data.
     *
     * @throws java.lang.Exception
     */
    @Before
    public void initializeCache() throws Exception {
        Property.get().setXmlDataLocation(TEST_DATA_XML_LOCATION_OVERRIDE);
        Cache.get().loadAllCachesFromXML();
    }

    /**
     * Data required for unit tests. NOTE: A new instance should be created for each unit test.
     */
    public static class UnitTestData {
        public Map<Long, Document> documentMap;
        public Map<String, Long> documentTitleIdMap;
        public Long maxDocumentId;
        public Map<Long, Tag> tagIdMap;
        public Map<String, Tag> tagTextMap;
        public Long maxTagId;
        public Map<Long, Note> noteMap;
        public Long maxNoteId;

        public UnitTestData() {

            documentMap = new HashMap<Long, Document>();
            documentTitleIdMap = new HashMap<String, Long>();
            tagIdMap = new HashMap<Long, Tag>();
            tagTextMap = new HashMap<String, Tag>();
            noteMap = new HashMap<Long, Note>();

            Book document1 = new Book();
            document1.setDocumentId(1L);
            document1.setDocumentTitle("Head First Design Patterns");
            document1.setAuthorsList(new ArrayList<String>(Arrays.asList("Eric Freeman",
                    "Elisabeth Freeman")));
            document1
                    .setComment("A must read book if you want to learn design patterns! Very easy to read!");
            document1.setEdition(1);
            document1.setPublishedYear(2005);
            document1.setIsbn("978-7-5641-0165-7");
            TreeMap<Long, Chapter> chaptersMap = new TreeMap<Long, Chapter>();
            Chapter chapter1 = new Chapter(1L, "Welcome to Design Patterns: an introduction",
                    new ArrayList<Long>());
            chapter1.getNotesList().add(1L);
            Chapter chapter2 = new Chapter(2L,
                    "Keeping your Objects in the know: the Observer Pattern", new ArrayList<Long>());
            chaptersMap.put(1L, chapter1);
            chaptersMap.put(2L, chapter2);
            document1.setChaptersMap(chaptersMap);
            document1.setCreatedTime(new Date(1341429512312L));
            document1.setLastUpdatedTime(new Date(1341429512312L));
            documentMap.put(document1.getDocumentId(), document1);

            Article document2 = new Article();
            document2.setDocumentId(2L);
            document2.setDocumentTitle("Flexible Mixture Model for Collaborative Filtering");
            document2.setAuthorsList(new ArrayList<String>(Arrays.asList("Luo Si", "Rong Jin")));
            document2.setComment("Required reading for IR course.");
            document2.setSource("Published in ICML 2003.");
            document2.setCreatedTime(new Date(1341429512312L));
            document2.setLastUpdatedTime(new Date(1341429512312L));
            document2.setNotesList(new ArrayList<Long>(Arrays.asList(2L)));
            documentMap.put(document2.getDocumentId(), document2);

            Workset document3 = new Workset();
            document3.setDocumentId(3L);
            document3.setDocumentTitle("My Workset");
            document3.setAuthorsList(new ArrayList<String>(Arrays.asList("Rui Du")));
            document3.setComment("This is my workset.");
            Map<Long, Worksheet> worksheetsMap = new HashMap<Long, Worksheet>();
            Worksheet worksheet1 = new Worksheet();
            worksheet1.setWorksheetId(1L);
            worksheet1.setWorksheetTitle("Worksheet1");
            worksheet1.setNotesList(new ArrayList<Long>());
            worksheet1.setCreatedTime(new Date(1341429512312L));
            worksheet1.setLastUpdatedTime(new Date(1341429512312L));
            worksheet1.getNotesList().add(3L);
            Worksheet worksheet2 = new Worksheet();
            worksheet2.setWorksheetId(2L);
            worksheet2.setWorksheetTitle("Worksheet2");
            worksheet2.setNotesList(new ArrayList<Long>());
            worksheet2.setCreatedTime(new Date(1341429512312L));
            worksheet2.setLastUpdatedTime(new Date(1341429512312L));
            worksheetsMap.put(1L, worksheet1);
            worksheetsMap.put(2L, worksheet2);
            document3.setWorksheetsMap(worksheetsMap);
            List<Long> worksheetIdsList = new ArrayList<Long>();
            worksheetIdsList.add(1L);
            worksheetIdsList.add(2L);
            document3.setWorksheetIdsList(worksheetIdsList);
            document3.setCreatedTime(new Date(1341429512312L));
            document3.setLastUpdatedTime(new Date(1341429512312L));
            documentMap.put(document3.getDocumentId(), document3);

            documentTitleIdMap.put(document1.getDocumentTitle(), document1.getDocumentId());
            documentTitleIdMap.put(document2.getDocumentTitle(), document2.getDocumentId());
            documentTitleIdMap.put(document3.getDocumentTitle(), document3.getDocumentId());

            maxDocumentId = 3L;

            Tag tag1 = new Tag(1L, "Algorithm");
            tagIdMap.put(tag1.getTagId(), tag1);

            Tag tag2 = new Tag(2L, "Design Pattern");
            tagIdMap.put(tag2.getTagId(), tag2);

            tagTextMap.put(tag1.getTagText(), tag1);
            tagTextMap.put(tag2.getTagText(), tag2);

            maxTagId = 2L;

            BookNote note1 = new BookNote();
            note1.setNoteId(1L);
            note1.setDocumentId(1L);
            note1.setChapterId(1L);
            note1.setTagIds(new ArrayList<Long>(Arrays.asList(2L)));
            note1.setNoteText("Separating what changes from what stays the same.");
            note1.setCreatedTime(new Date(1341429578719L));
            noteMap.put(note1.getNoteId(), note1);

            ArticleNote note2 = new ArticleNote();
            note2.setNoteId(2L);
            note2.setDocumentId(2L);
            note2.setTagIds(new ArrayList<Long>(Arrays.asList(1L)));
            note2.setNoteText("FMM extends existing partitioning/clustering algorithms for "
                    + "collaborative filtering by clustering both users and items together "
                    + "simultaneously without assuming that each user and item should only "
                    + "belong to a single cluster.\nThis is the second line of note text.");
            note2.setCreatedTime(new Date(1341429591369L));
            noteMap.put(note2.getNoteId(), note2);

            WorksheetNote note3 = new WorksheetNote();
            note3.setNoteId(3L);
            note3.setDocumentId(3L);
            note3.setWorksheetId(1L);
            note3.setTagIds(new ArrayList<Long>(Arrays.asList(2L)));
            note3.setNoteText("A note in the worksheet.");
            note3.setNoteStatus(NoteStatus.DONE);
            note3.setCreatedTime(new Date(1341429578719L));
            noteMap.put(note3.getNoteId(), note3);

            maxNoteId = 3L;
        }

    }
}

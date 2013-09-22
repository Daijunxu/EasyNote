/**
 *
 */
package notes.data.cache;

import lombok.Getter;
import lombok.Setter;
import notes.article.Article;
import notes.book.Book;
import notes.book.Chapter;
import notes.entity.Document;
import notes.entity.XMLSerializable;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Stores all the documents.
 *
 * @author Rui Du
 * @version 1.0
 */
public class DocumentCache implements XMLSerializable {

    /**
     * The map of all documents from document IDs to the documents.
     */
    @Getter
    @Setter
    private Map<Long, Document> documentMap;
    /**
     * The map of all documents' titles to their IDs.
     */
    @Getter
    @Setter
    private Map<String, Long> documentTitleIdMap;
    /**
     * The maximum document ID in the data.
     */
    @Getter
    @Setter
    private Long maxDocumentId = 0L;

    /**
     * Constructs an instance of {@code DocumentCache}. Should only be called by Cache.
     *
     * @param input The {@code BufferedReader} instance in use.
     */
    public DocumentCache(BufferedReader input) {
        documentMap = new HashMap<Long, Document>();
        documentTitleIdMap = new HashMap<String, Long>();
        loadDocumentCache(input);
    }

    /**
     * Removes all data stored in the document cache.
     */
    public void clear() {
        this.documentMap.clear();
        this.documentTitleIdMap.clear();
        this.maxDocumentId = Long.MIN_VALUE;
    }

    /**
     * Reads all documents' data from data file.
     *
     * @param input The {@code BufferedReader} in use.
     */
    @Deprecated
    private void loadDocumentCache(BufferedReader input) {
        String line;

        try {
            line = input.readLine();
            if (!line.equals("#DOCUMENTS")) {
                throw new InvalidDataFormatException("No document head: expecting \"#DOCUMENTS\".");
            }

            // Start reading documents.
            Document newDocument;
            do {
                newDocument = readNextDocument(input);
                if (newDocument != null) {
                    getDocumentMap().put(newDocument.getDocumentId(), newDocument);
                    getDocumentTitleIdMap().put(newDocument.getDocumentTitle(),
                            newDocument.getDocumentId());
                    if (getMaxDocumentId() < newDocument.getDocumentId()) {
                        setMaxDocumentId(newDocument.getDocumentId());
                    }
                }
            } while (newDocument != null);
        } catch (Exception e) {
            Cache.hasProblem = true;
            e.printStackTrace();
        }
    }

    /**
     * Reads the next block of document data and creates a new document.
     *
     * @param input The {@code BufferedReader} in use.
     * @return {@code Document} The created document.
     * @throws InvalidDataFormatException
     */
    @Deprecated
    private Document readNextDocument(BufferedReader input) throws InvalidDataFormatException {
        try {
            String line = input.readLine();
            if (line.equals("#BOOK")) {
                Book newBook = new Book();

                newBook.setDocumentId(Long.parseLong(input.readLine()));
                newBook.setDocumentTitle(input.readLine());
                newBook.setAuthorsList(Arrays.asList(input.readLine().split(",")));
                if (!(line = input.readLine()).equals("")) {
                    newBook.setComment(line);
                }
                if (!(line = input.readLine()).equals("-1")) {
                    newBook.setEdition(Integer.parseInt(line));
                }
                if (!(line = input.readLine()).equals("-1")) {
                    newBook.setPublishedYear(Integer.parseInt(line));
                }
                if (!(line = input.readLine()).equals("")) {
                    newBook.setIsbn(line);
                }
                TreeMap<Long, Chapter> chapterMap = new TreeMap<Long, Chapter>();
                if (!(line = input.readLine()).equals("")) {
                    String chapters[] = line.split("\t");
                    for (String chapter : chapters) {
                        Chapter newChapter = new Chapter();
                        newChapter.setChapterId(Long.parseLong(chapter.substring(0,
                                chapter.indexOf("#"))));
                        newChapter.setChapterTitle(chapter.substring(chapter.indexOf("#") + 1));
                        newChapter.setNotesList(new ArrayList<Long>());
                        chapterMap.put(newChapter.getChapterId(), newChapter);
                    }
                }
                newBook.setChaptersMap(chapterMap);
                newBook.setCreatedTime(new Date(Long.parseLong(input.readLine())));
                newBook.setLastUpdatedTime(new Date(Long.parseLong(input.readLine())));

                return newBook;

            } else if (line.equals("#ARTICLE")) {
                Article newArticle = new Article();

                newArticle.setDocumentId(Long.parseLong(input.readLine()));
                newArticle.setDocumentTitle(input.readLine());
                newArticle.setAuthorsList(Arrays.asList(input.readLine().split(",")));
                if (!(line = input.readLine()).equals("")) {
                    newArticle.setComment(line);
                }
                if (!(line = input.readLine()).equals("")) {
                    newArticle.setSource(line);
                }
                newArticle.setCreatedTime(new Date(Long.parseLong(input.readLine())));
                newArticle.setLastUpdatedTime(new Date(Long.parseLong(input.readLine())));
                newArticle.setNotesList(new ArrayList<Long>());

                return newArticle;

            } else if (line.equals("#END_DOCUMENTS")) {
                return null;
            } else {
                throw new InvalidDataFormatException("Unexpected data \"" + line
                        + "\": expecting beginning tag of a document or end documents tag.");
            }
        } catch (IOException e) {
            Cache.hasProblem = true;
            e.printStackTrace();
        } catch (Exception e) {
            throw new InvalidDataFormatException(e.getMessage(), e.getCause());
        }
        return null;
    }

    /**
     * Writes all documents into data file.
     *
     * @param output The {@code BufferedWriter} in use.
     */
    @Deprecated
    public void saveDocumentCache(BufferedWriter output) {
        try {
            output.append("#DOCUMENTS\n");
            for (Document document : documentMap.values()) {
                // Write each document.
                StringBuilder sb = new StringBuilder();
                if (document instanceof Book) {
                    sb.append("#BOOK\n");
                    sb.append(document.getDocumentId());
                    sb.append("\n");
                    sb.append(document.getDocumentTitle());
                    sb.append("\n");
                    if (!document.getAuthorsList().isEmpty()) {
                        for (String author : document.getAuthorsList()) {
                            sb.append(author).append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("\n");
                    if (document.getComment() != null) {
                        sb.append(document.getComment());
                    }
                    sb.append("\n");
                    if (((Book) document).getEdition() == null) {
                        sb.append("-1");
                    } else {
                        sb.append(((Book) document).getEdition());
                    }
                    sb.append("\n");
                    if (((Book) document).getPublishedYear() == null) {
                        sb.append("-1");
                    } else {
                        sb.append(((Book) document).getPublishedYear());
                    }
                    sb.append("\n");
                    if (((Book) document).getIsbn() != null) {
                        sb.append(((Book) document).getIsbn());
                    }
                    sb.append("\n");
                    if (!((Book) document).getChaptersMap().isEmpty()) {
                        for (Long chapterId : ((Book) document).getChaptersMap().keySet()) {
                            Chapter chapter = ((Book) document).getChaptersMap().get(chapterId);
                            sb.append(chapterId);
                            sb.append("#");
                            sb.append(chapter.getChapterTitle());
                            sb.append("\t");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("\n");
                    sb.append(document.getCreatedTime().getTime());
                    sb.append("\n");
                    sb.append(document.getLastUpdatedTime().getTime());
                    sb.append("\n");

                } else if (document instanceof Article) {
                    sb.append("#ARTICLE\n");
                    sb.append(document.getDocumentId());
                    sb.append("\n");
                    sb.append(document.getDocumentTitle());
                    sb.append("\n");
                    if (!document.getAuthorsList().isEmpty()) {
                        for (String author : document.getAuthorsList()) {
                            sb.append(author).append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("\n");
                    if (document.getComment() != null) {
                        sb.append(document.getComment());
                    }
                    sb.append("\n");
                    if (((Article) document).getSource() != null) {
                        sb.append(((Article) document).getSource());
                    }
                    sb.append("\n");
                    sb.append(document.getCreatedTime().getTime());
                    sb.append("\n");
                    sb.append(document.getLastUpdatedTime().getTime());
                    sb.append("\n");
                } else {
                    throw new Exception("Unknown document type!");
                }
                output.append(sb.toString());
            }
            output.append("#END_DOCUMENTS\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element documentCacheElement = new DefaultElement("Documents");

        for (Document document : documentMap.values()) {
            documentCacheElement.add(document.toXMLElement());
        }

        return documentCacheElement;
    }
}

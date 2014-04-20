package notes.data.cache;

import notes.businessobjects.Document;
import notes.businessobjects.article.Article;
import notes.businessobjects.book.Book;
import notes.businessobjects.workset.Workset;
import notes.dao.DuplicateRecordException;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all the documents.
 * <p/>
 * Author: Rui Du
 */
public class DocumentCache implements Cache<Document> {

    /**
     * The single instance that is used in this system.
     */
    private static final DocumentCache instance = new DocumentCache();
    /**
     * The map of all documents from document IDs to the documents.
     */
    private final Map<Long, Document> documentMap;
    /**
     * The maximum document ID in the data.
     */
    private Long maxDocumentId = 0L;

    /**
     * Constructs an instance of {@code DocumentCache}. Should only be called by CacheDelegate.
     */
    public DocumentCache() {
        documentMap = new HashMap<Long, Document>();
    }

    /**
     * Gets the instance of {@code DocumentCache}.
     *
     * @return {@code DocumentCache} The instance of {@code DocumentCache}.
     */
    public static DocumentCache get() {
        return instance;
    }

    /**
     * Removes all data stored in the document cache.
     */
    public void clear() {
        this.documentMap.clear();
        this.maxDocumentId = Long.MIN_VALUE;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentCache buildFromXMLElement(Element element) {
        // Clear data in the document cache.
        clear();

        for (Element documentElement : element.elements()) {
            Document newDocument;
            if (documentElement.getName().equals("Book")) {
                newDocument = new Book().buildFromXMLElement(documentElement);
            } else if (documentElement.getName().equals("Article")) {
                newDocument = new Article().buildFromXMLElement(documentElement);
            } else if (documentElement.getName().equals("Workset")) {
                newDocument = new Workset().buildFromXMLElement(documentElement);
            } else {
                throw new UnsupportedOperationException("Unsupported document type: " + documentElement.getName());
            }

            documentMap.put(newDocument.getDocumentId(), newDocument);
            if (maxDocumentId < newDocument.getDocumentId()) {
                maxDocumentId = newDocument.getDocumentId();
            }
        }
        return this;
    }

    @Override
    public Document insert(Document document) throws DuplicateRecordException {
        Long documentId = (document.getDocumentId() != null) ? document.getDocumentId() : maxDocumentId + 1L;

        if (isExistingDocument(documentId, document.getDocumentTitle())) {
            throw new DuplicateRecordException("Duplicate document with id = " + documentId
                    + " and title = " + document.getDocumentTitle());
        }

        if (document instanceof Book) {
            Book newBook = new Book();
            newBook.setDocumentId(documentId);
            newBook.setDocumentTitle(document.getDocumentTitle());
            newBook.setAuthorsList(((Book) document).getAuthorsList());
            newBook.setComment(document.getComment());
            newBook.setEdition(((Book) document).getEdition());
            newBook.setPublishedYear(((Book) document).getPublishedYear());
            newBook.setIsbn(((Book) document).getIsbn());
            newBook.setChaptersMap(((Book) document).getChaptersMap());
            if (((Book) document).getCreatedTime() == null) {
                newBook.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newBook.setCreatedTime(((Book) document).getCreatedTime());
            }
            if (((Book) document).getLastUpdatedTime() == null) {
                newBook.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                newBook.setLastUpdatedTime(((Book) document).getLastUpdatedTime());
            }

            documentMap.put(newBook.getDocumentId(), newBook);

            // Update the max document ID in document CACHE.
            if (maxDocumentId < newBook.getDocumentId()) {
                maxDocumentId = newBook.getDocumentId();
            }

            return newBook;
        } else if (document instanceof Article) {
            Article newArticle = new Article();
            newArticle.setDocumentId(documentId);
            newArticle.setDocumentTitle(document.getDocumentTitle());
            newArticle.setAuthorsList(((Article) document).getAuthorsList());
            newArticle.setComment(document.getComment());
            newArticle.setSource(((Article) document).getSource());
            if (((Article) document).getCreatedTime() == null) {
                newArticle.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newArticle.setCreatedTime(((Article) document).getCreatedTime());
            }
            if (((Article) document).getLastUpdatedTime() == null) {
                newArticle.setLastUpdatedTime(new Date(System.currentTimeMillis()));
            } else {
                newArticle.setLastUpdatedTime(((Article) document).getLastUpdatedTime());
            }
            if (((Article) document).getNotesList() == null) {
                newArticle.setNotesList(new ArrayList<Long>());
            } else {
                newArticle.setNotesList(((Article) document).getNotesList());
            }

            documentMap.put(newArticle.getDocumentId(), newArticle);

            // Update the max document ID in document CACHE.
            if (maxDocumentId < newArticle.getDocumentId()) {
                maxDocumentId = newArticle.getDocumentId();
            }

            return newArticle;
        } else if (document instanceof Workset) {
            Workset newWorkset = new Workset();
            newWorkset.setDocumentId(documentId);
            newWorkset.setDocumentTitle(document.getDocumentTitle());
            newWorkset.setAuthorsList(((Workset) document).getAuthorsList());
            newWorkset.setComment(document.getComment());
            newWorkset.setWorksheetIdsList(((Workset) document).getWorksheetIdsList());
            newWorkset.setWorksheetsMap(((Workset) document).getWorksheetsMap());
            if (((Workset) document).getCreatedTime() == null) {
                newWorkset.setCreatedTime(new Date());
            } else {
                newWorkset.setCreatedTime(((Workset) document).getCreatedTime());
            }
            if (((Workset) document).getLastUpdatedTime() == null) {
                newWorkset.setLastUpdatedTime(new Date());
            } else {
                newWorkset.setLastUpdatedTime(((Workset) document).getLastUpdatedTime());
            }

            documentMap.put(newWorkset.getDocumentId(), newWorkset);

            // Update the max document ID in document cache.
            if (maxDocumentId < newWorkset.getDocumentId()) {
                maxDocumentId = newWorkset.getDocumentId();
            }

            return newWorkset;
        }

        return null;
    }

    @Override
    public void remove(Long id) {
        documentMap.remove(id);
    }

    @Override
    public Document update(Document document) throws DuplicateRecordException {

        if (isExistingDocumentTitle(document.getDocumentId(), document.getDocumentTitle())) {
            throw new DuplicateRecordException("Duplicate document title: " + document.getDocumentTitle());
        }

        if (document instanceof Book) {
            Book cachedBook = (Book) (documentMap.get(document.getDocumentId()));
            if (cachedBook != null) {
                cachedBook.setDocumentTitle(document.getDocumentTitle());
                cachedBook.setAuthorsList(((Book) document).getAuthorsList());
                cachedBook.setComment(document.getComment());
                cachedBook.setEdition(((Book) document).getEdition());
                cachedBook.setPublishedYear(((Book) document).getPublishedYear());
                cachedBook.setIsbn(((Book) document).getIsbn());
                cachedBook.setChaptersMap(((Book) document).getChaptersMap());
                if (((Book) document).getLastUpdatedTime() == null) {
                    cachedBook.setLastUpdatedTime(new Date(System.currentTimeMillis()));
                } else {
                    cachedBook.setLastUpdatedTime(((Book) document).getLastUpdatedTime());
                }
                return cachedBook;
            }
        } else if (document instanceof Article) {
            Article cachedArticle = (Article) (documentMap.get(document.getDocumentId()));
            if (cachedArticle != null) {
                cachedArticle.setDocumentTitle(document.getDocumentTitle());
                cachedArticle.setAuthorsList(((Article) document).getAuthorsList());
                cachedArticle.setComment(document.getComment());
                cachedArticle.setSource(((Article) document).getSource());
                if (((Article) document).getLastUpdatedTime() == null) {
                    cachedArticle.setLastUpdatedTime(new Date(System.currentTimeMillis()));
                } else {
                    cachedArticle.setLastUpdatedTime(((Article) document).getLastUpdatedTime());
                }
                return cachedArticle;
            }
        } else if (document instanceof Workset) {
            Workset cachedWorkset = (Workset) (documentMap.get(document.getDocumentId()));
            if (cachedWorkset != null) {
                cachedWorkset.setDocumentTitle(document.getDocumentTitle());
                cachedWorkset.setAuthorsList(((Workset) document).getAuthorsList());
                cachedWorkset.setComment(document.getComment());
                cachedWorkset.setWorksheetsMap(((Workset) document).getWorksheetsMap());
                cachedWorkset.setWorksheetIdsList(((Workset) document).getWorksheetIdsList());
                if (((Workset) document).getLastUpdatedTime() == null) {
                    cachedWorkset.setLastUpdatedTime(new Date());
                } else {
                    cachedWorkset.setLastUpdatedTime(((Workset) document).getLastUpdatedTime());
                }
                return cachedWorkset;
            }
        }

        return null;
    }

    @Override
    public Document find(Long id) {
        return documentMap.get(id);
    }

    @Override
    public List<Document> findAll() {
        List<Document> documentList = new ArrayList<Document>();
        for (Document document : documentMap.values()) {
            documentList.add(document);
        }
        Collections.sort(documentList);
        return documentList;
    }

    private boolean isExistingDocument(Long documentId, String documentTitle) {
        if (documentMap.containsKey(documentId)) {
            return true;
        }

        for (Document existingDocument : documentMap.values()) {
            if (existingDocument.getDocumentTitle().equals(documentTitle)) {
                return true;
            }
        }

        return false;
    }

    private boolean isExistingDocumentTitle(Long documentId, String documentTitle) {
        for (Document existingDocument : documentMap.values()) {
            if (existingDocument.getDocumentTitle().equals(documentTitle)
                    && (!documentId.equals(existingDocument.getDocumentId()))) {
                return true;
            }
        }

        return false;
    }
}

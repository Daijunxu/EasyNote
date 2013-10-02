/**
 *
 */
package notes.data.cache;

import lombok.Getter;
import lombok.Setter;
import notes.entity.Document;
import notes.entity.XMLSerializable;
import notes.entity.article.Article;
import notes.entity.book.Book;
import notes.entity.workset.Workset;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all the documents.
 *
 * @author Rui Du
 * @version 1.0
 */
public class DocumentCache implements XMLSerializable {

    /**
     * The single instance that is used in this system.
     */
    private static final DocumentCache instance = new DocumentCache();
    /**
     * The map of all documents from document IDs to the documents.
     */
    @Getter
    private final Map<Long, Document> documentMap;
    /**
     * The map of all documents' titles to their IDs.
     */
    @Getter
    private final Map<String, Long> documentTitleIdMap;
    /**
     * The maximum document ID in the data.
     */
    @Getter
    @Setter
    private Long maxDocumentId = 0L;

    /**
     * Constructs an instance of {@code DocumentCache}. Should only be called by Cache.
     */
    public DocumentCache() {
        documentMap = new HashMap<Long, Document>();
        documentTitleIdMap = new HashMap<String, Long>();
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
        this.documentTitleIdMap.clear();
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
            } else if (documentElement.getName().equals("WORKSET")) {
                newDocument = new Workset().buildFromXMLElement(documentElement);
            } else {
                throw new UnsupportedOperationException("Unsupported document type: " + documentElement.getName());
            }

            documentMap.put(newDocument.getDocumentId(), newDocument);
            documentTitleIdMap.put(newDocument.getDocumentTitle(), newDocument.getDocumentId());
            if (maxDocumentId < newDocument.getDocumentId()) {
                maxDocumentId = newDocument.getDocumentId();
            }
        }
        return this;
    }
}

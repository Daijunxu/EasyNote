package notes.data.cache;

import junit.framework.Assert;
import lombok.Getter;
import notes.entity.XMLSerializable;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The data cache composed of multiple caches.
 *
 * @author Rui Du
 */
public class Cache implements XMLSerializable<Cache> {

    /**
     * The flag of whether the cache is having a problem.
     */
    public static boolean hasProblem = false;
    /**
     * The single instance that is used in the system.
     */
    private static Cache instance;
    /**
     * The document cache.
     */
    @Getter
    private final DocumentCache documentCache;
    /**
     * The tag cache.
     */
    @Getter
    private final TagCache tagCache;
    /**
     * The note cache.
     */
    @Getter
    private final NoteCache noteCache;

    /**
     * Constructs an instance of {@code Cache}.
     */
    private Cache() {
        documentCache = DocumentCache.get();
        tagCache = TagCache.get();
        noteCache = NoteCache.get();

        loadAllCachesFromXML();
    }

    /**
     * Gets the instance of {@code Cache}.
     *
     * @return {@code Cache} The instance of {@code Cache}.
     */
    public static Cache get() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    /**
     * Reads all data into memory from XML file.
     */
    public void loadAllCachesFromXML() {
        try {
            String path = Property.get().getXmlDataLocation();

            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(path));

            // Validate that the XML data is valid before building the cache.
            validateXMLData(document);

            buildFromXMLElement(document.getRootElement());
        } catch (DocumentException e) {
            Cache.hasProblem = true;
            e.printStackTrace();
        }
    }

    private void validateXMLData(Document document) {
        Element rootElement = document.getRootElement();
        Assert.assertNotNull(rootElement);
        Assert.assertNotNull(rootElement.element("Documents"));
        Assert.assertNotNull(rootElement.element("Tags"));
        Assert.assertNotNull(rootElement.element("Notes"));
    }

    public void saveAllCachesToXML() {
        try {
            Document document = DocumentHelper.createDocument(this.toXMLElement());

            String outputPath = Property.get().getXmlDataLocation();

            XMLWriter writer = new XMLWriter(
                    new FileWriter(outputPath)
            );
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Element toXMLElement() {
        Element cacheElement = new DefaultElement("Data");

        cacheElement.add(documentCache.toXMLElement());
        cacheElement.add(tagCache.toXMLElement());
        cacheElement.add(noteCache.toXMLElement());

        return cacheElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cache buildFromXMLElement(Element element) {
        documentCache.buildFromXMLElement(element.element("Documents"));
        tagCache.buildFromXMLElement(element.element("Tags"));
        noteCache.buildFromXMLElement(element.element("Notes"));

        return this;
    }

}

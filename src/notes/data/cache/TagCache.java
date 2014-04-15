package notes.data.cache;

import lombok.Getter;
import lombok.Setter;
import notes.businessobjects.Tag;
import notes.businessobjects.XMLSerializable;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all the tags.
 * <p/>
 * Author: Rui Du
 */
public class TagCache implements XMLSerializable<TagCache> {

    /**
     * The single instance that is used in this system.
     */
    private static final TagCache instance = new TagCache();
    /**
     * The map of all tags from tag IDs to the tags.
     */
    @Getter
    private final Map<Long, Tag> tagIdMap;
    /**
     * The map of all tags from tag texts to the tags.
     */
    @Getter
    private final Map<String, Tag> tagTextMap;
    /**
     * The maximum tag ID in the data.
     */
    @Getter
    @Setter
    private Long maxTagId = 0L;

    /**
     * Constructs an instance of {@code TagCache}. Should only be called by Cache.
     */
    public TagCache() {
        tagIdMap = new HashMap<Long, Tag>();
        tagTextMap = new HashMap<String, Tag>();
    }

    /**
     * Gets the instance of {@code TagCache}.
     *
     * @return {@code TagCache} The instance of {@code TagCache}.
     */
    public static TagCache get() {
        return instance;
    }

    /**
     * Removes all data stored in the tag cache.
     */
    public void clear() {
        this.tagIdMap.clear();
        this.tagTextMap.clear();
        this.maxTagId = Long.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element tagCacheElement = new DefaultElement("Tags");

        for (Tag tag : tagIdMap.values()) {
            tagCacheElement.add(tag.toXMLElement());
        }

        return tagCacheElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagCache buildFromXMLElement(Element element) {
        // Clear data in the tag cache.
        clear();

        for (Element tagElement : element.elements()) {
            Tag newTag = new Tag().buildFromXMLElement(tagElement);
            tagIdMap.put(newTag.getTagId(), newTag);
            tagTextMap.put(newTag.getTagText(), newTag);
            if (maxTagId < newTag.getTagId()) {
                maxTagId = newTag.getTagId();
            }
        }
        return this;
    }
}

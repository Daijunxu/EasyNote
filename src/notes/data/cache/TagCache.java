package notes.data.cache;

import notes.businessobjects.Tag;
import notes.dao.DuplicateRecordException;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all the tags.
 * <p/>
 * Author: Rui Du
 */
public class TagCache implements Cache<Tag> {

    /**
     * The single instance that is used in this system.
     */
    private static final TagCache instance = new TagCache();
    /**
     * The map of all tags from tag IDs to the tags.
     */
    private final Map<Long, Tag> tagIdMap;
    /**
     * The map of all tags from tag texts to the tags.
     */
    private final Map<String, Tag> tagTextMap;
    /**
     * The maximum tag ID in the data.
     */
    private Long maxTagId = 0L;

    /**
     * Constructs an instance of {@code TagCache}. Should only be called by CacheDelegate.
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

    @Override
    public Tag insert(Tag tag) {
        Tag newTag = new Tag();
        if (tag.getTagId() == null) {
            newTag.setTagId(maxTagId + 1L);
        } else {
            newTag.setTagId(tag.getTagId());
        }
        newTag.setTagText(tag.getTagText());

        // Add the tag to tag cache.
        try {
            if (tagIdMap.containsKey(newTag.getTagId())) {
                throw new DuplicateRecordException("Duplicate tag exception: same tag ID!");
            }
            if (tagTextMap.containsKey(newTag.getTagText())) {
                throw new DuplicateRecordException("Duplicate tag exception: same tag text!");
            }
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
        }
        tagIdMap.put(newTag.getTagId(), newTag);
        tagTextMap.put(newTag.getTagText(), newTag);

        // Update max note id in tag cache.
        if (maxTagId < newTag.getTagId()) {
            maxTagId = newTag.getTagId();
        }

        return newTag;
    }

    @Override
    public Tag remove(Long id) {
        Tag tag = tagIdMap.get(id);
        tagIdMap.remove(id);
        tagTextMap.remove(tag.getTagText());
        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        Tag updateTag = tagIdMap.get(tag.getTagId());
        if (updateTag != null) {
            tagTextMap.remove(updateTag.getTagText());
            updateTag.setTagText(tag.getTagText());
            tagTextMap.put(updateTag.getTagText(), updateTag);
            return updateTag;
        }
        return null;
    }

    @Override
    public Tag find(Long id) {
        return tagIdMap.get(id);
    }

    /**
     * Find the tag from its text.
     *
     * @param tagText The text part of the tag.
     * @return {@code Tag} The found tag; null if not found.
     */
    public Tag find(String tagText) {
        return tagTextMap.get(tagText);
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> tagList = new ArrayList<Tag>();
        for (Tag tag : tagIdMap.values()) {
            tagList.add(tag);
        }
        return tagList;
    }
}

/**
 *
 */
package notes.data.cache;

import lombok.Getter;
import lombok.Setter;
import notes.entity.Tag;
import notes.entity.XMLSerializable;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores all the tags.
 *
 * @author Rui Du
 * @version 1.0
 */
public class TagCache implements XMLSerializable {

    /**
     * The map of all tags from tag IDs to the tags.
     */
    @Getter
    @Setter
    private Map<Long, Tag> tagIdMap;
    /**
     * The map of all tags from tag texts to the tags.
     */
    @Getter
    @Setter
    private Map<String, Tag> tagTextMap;
    /**
     * The maximum tag ID in the data.
     */
    @Getter
    @Setter
    private Long maxTagId = 0L;

    /**
     * Constructs an instance of {@code TagCache}. Should only be called by Cache.
     *
     * @param input The {@code BufferedReader} instance in use.
     */
    public TagCache(BufferedReader input) {
        setTagIdMap(new HashMap<Long, Tag>());
        setTagTextMap(new HashMap<String, Tag>());
        loadTagCache(input);
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
     * Reads all tags' data from data file.
     *
     * @param input The {@code BufferedReader} in use.
     */
    @Deprecated
    private void loadTagCache(BufferedReader input) {
        String line;

        try {
            line = input.readLine();
            if (!line.equals("#TAGS")) {
                throw new InvalidDataFormatException("No tag head: expecting \"#TAGS\".");
            }

            // Start reading tags.
            Tag newTag;
            do {
                newTag = readNextTag(input);
                if (newTag != null) {
                    tagIdMap.put(newTag.getTagId(), newTag);
                    tagTextMap.put(newTag.getTagText(), newTag);
                    if (maxTagId < newTag.getTagId()) {
                        maxTagId = newTag.getTagId();
                    }
                }
            } while (newTag != null);
        } catch (Exception e) {
            Cache.hasProblem = true;
            e.printStackTrace();
        }
    }

    /**
     * Reads the next block of tag data and creates a new tag.
     *
     * @param input The {@code BufferedReader} in use.
     * @return {@code Tag} The created tag.
     * @throws InvalidDataFormatException
     */
    @Deprecated
    private Tag readNextTag(BufferedReader input) throws InvalidDataFormatException {
        try {
            String line = input.readLine();
            if (line.equals("#END_TAGS")) {
                return null;
            } else {
                Tag newTag = new Tag();

                String[] tokens = line.split(",");
                newTag.setTagId(Long.parseLong(tokens[0]));
                newTag.setTagText(tokens[1]);

                return newTag;
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
     * Writes all tags into data file.
     *
     * @param output The {@code BufferedWriter} in use.
     */
    @Deprecated
    public void saveTagCache(BufferedWriter output) {
        try {
            output.append("#TAGS\n");
            for (Tag tag : tagIdMap.values()) {
                output.append(tag.getTagId().toString()).append(",").append(tag.getTagText());
                output.newLine();
            }
            output.append("#END_TAGS\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}

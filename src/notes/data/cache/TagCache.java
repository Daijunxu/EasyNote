/**
 *
 */
package notes.data.cache;

import notes.entity.Tag;

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
public class TagCache {

    /**
     * The map of all tags from tag IDs to the tags.
     */
    private Map<Long, Tag> tagIdMap;
    /**
     * The map of all tags from tag texts to the tags.
     */
    private Map<String, Tag> tagTextMap;
    /**
     * The maximum tag ID in the data.
     */
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
     * Gets the maximum tag ID.
     *
     * @return {@code Long} The maximum tag ID.
     */
    public Long getMaxTagId() {
        return maxTagId;
    }

    /**
     * Sets the maximum tag ID.
     *
     * @param maxTagId The maximum tag ID to set.
     */
    public void setMaxTagId(Long maxTagId) {
        this.maxTagId = maxTagId;
    }

    /**
     * Gets the tag ID map.
     *
     * @return {@code Map} The tag ID map.
     */
    public Map<Long, Tag> getTagIdMap() {
        return tagIdMap;
    }

    /**
     * Sets the tag ID map.
     *
     * @param tagIdMap The tag ID map to set.
     */
    public void setTagIdMap(Map<Long, Tag> tagIdMap) {
        this.tagIdMap = tagIdMap;
    }

    /**
     * Gets the tag text map.
     *
     * @return {@code Map} The tag text map.
     */
    public Map<String, Tag> getTagTextMap() {
        return tagTextMap;
    }

    /**
     * Sets the tag text map.
     *
     * @param tagTextMap The tag text map to set.
     */
    public void setTagTextMap(Map<String, Tag> tagTextMap) {
        this.tagTextMap = tagTextMap;
    }

    /**
     * Reads all tags' data from data file.
     *
     * @param input The {@code BufferedReader} in use.
     */
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
}

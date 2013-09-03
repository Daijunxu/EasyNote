package notes.entity;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Tag implements Comparable<Tag> {

    /**
     * The tag identifier.
     */
    private Long tagId;

    /**
     * The tag's text.
     */
    private String tagText;

    /**
     * Constructs a default instance of {@code Tag}.
     */
    public Tag() {
    }

    /**
     * Constructs an instance of {@code Tag}.
     *
     * @param tagId   The tag identifier.
     * @param tagText The tag's text.
     * @throws IllegalArgumentException
     */
    public Tag(final Long tagId, final String tagText) throws IllegalArgumentException {
        setTagId(tagId);
        setTagText(tagText);
    }

    /**
     * Compares this object with the specified object for sorting.
     *
     * @param other The object to be compared.
     * @return int A negative integer, zero, or a positive integer as this object is less than,
     *         equal to, or greater than the specified object.
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(Tag other) {
        return new CompareToBuilder().append(getTagId(), other.getTagId()).toComparison();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj The reference object with which to compare.
     * @return boolean Returns true if this object is the same as the obj argument; false otherwise.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Tag
                && new EqualsBuilder().append(getTagId(), ((Tag) obj).getTagId())
                .append(getTagText(), ((Tag) obj).getTagText()).isEquals();
    }

    /**
     * Gets the tag identifier.
     *
     * @return {@code Long} The tag identifier.
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * Gets the tag's text.
     *
     * @return {@code String} The tag's text.
     */
    public String getTagText() {
        return tagText;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return int A hash code value for this object.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getTagId()).append(getTagText()).toHashCode();
    }

    /**
     * Sets the tag identifier.
     *
     * @param tagId the tag identifier to set.
     */
    public void setTagId(final Long tagId) {
        this.tagId = tagId;
    }

    /**
     * Sets the tag's text.
     *
     * @param tagText the tag's text to set.
     */
    public void setTagText(final String tagText) {
        this.tagText = tagText;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return {@code String} A string representation of the object.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("tagId", getTagId())
                .append("tagText", getTagText()).toString();
    }
}

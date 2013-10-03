package notes.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * Entity class to describe a tag.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Tag implements Comparable<Tag>, XMLSerializable<Tag> {

    /**
     * The tag identifier.
     */
    @Getter
    @Setter
    private Long tagId;
    /**
     * The tag's text.
     */
    @Getter
    @Setter
    private String tagText;

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
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element tagElement = new DefaultElement("Tag");

        tagElement.addAttribute("TagId", tagId.toString());
        tagElement.addText(tagText);

        return tagElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag buildFromXMLElement(Element element) {
        tagId = Long.parseLong(element.attributeValue("TagId"));
        tagText = element.getText();

        return this;
    }
}

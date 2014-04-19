package notes.businessobjects;

import org.dom4j.Element;

/**
 * Identifies that a type can be serialized to and deserialized from XML format data.
 * <p/>
 * Author: Rui Du
 * Date: 9/21/13
 * Time: 11:39 AM
 */
public interface XMLSerializable<T> {

    /**
     * Convert the object into an XML format object.
     *
     * @return The XML format object.
     */
    public Element toXMLElement();

    /**
     * Build the instance from an XML format object.
     *
     * @param element The XML format object.
     * @return The instance of the class.
     */
    public T buildFromXMLElement(Element element);


}

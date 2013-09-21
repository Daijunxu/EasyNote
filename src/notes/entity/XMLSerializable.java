package notes.entity;

import org.dom4j.Element;

/**
 * User: rui
 * Date: 9/21/13
 * Time: 11:39 AM
 */
public interface XMLSerializable {

    /**
     * Convert the object into a XML format object.
     *
     * @return The XML format object.
     */
    public Element toXMLElement();

}

package notes.entity.workset;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.XMLSerializable;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.List;

/**
 * Entity class to describe a worksheet. A worksheet contains multiple notes.
 * <p/>
 * User: rui
 * Date: 9/30/13
 * Time: 1:20 AM
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Worksheet implements XMLSerializable<Worksheet> {

    /**
     * The worksheet identifier.
     */
    @Getter
    @Setter
    private Long worksheetId;
    /**
     * The workSheet's title.
     */
    @Getter
    @Setter
    private String worksheetTitle;
    /**
     * The list of note identifiers.
     */
    @Getter
    @Setter
    private List<Long> notesList;

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element workSheetElement = new DefaultElement("Worksheet");

        workSheetElement.addAttribute("WorksheetId", worksheetId.toString());
        workSheetElement.addAttribute("WorksheetTitle", worksheetTitle);
        workSheetElement.addAttribute("NotesList", EntityHelper.buildEntityStrFromList(notesList));

        return workSheetElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Worksheet buildFromXMLElement(Element element) {
        worksheetId = Long.parseLong(element.attributeValue("WorksheetId"));
        worksheetTitle = element.attributeValue("WorksheetTitle");
        notesList = EntityHelper.buildIDsList(element.attributeValue("NotesList"));

        return this;
    }
}

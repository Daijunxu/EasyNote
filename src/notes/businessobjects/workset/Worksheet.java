package notes.businessobjects.workset;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.businessobjects.XMLSerializable;
import notes.businessobjects.aware.CommentAware;
import notes.businessobjects.aware.CreatedTimeAware;
import notes.businessobjects.aware.LastUpdatedTimeAware;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.Date;
import java.util.List;

/**
 * Entity class to describe a worksheet. A worksheet contains multiple notes.
 * <p/>
 * Author: Rui Du
 * Date: 9/30/13
 * Time: 1:20 AM
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Worksheet implements XMLSerializable<Worksheet>, CreatedTimeAware, LastUpdatedTimeAware, CommentAware {

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
     * The comment.
     */
    @Getter
    @Setter
    private String comment;
    /**
     * The list of note identifiers.
     */
    @Getter
    @Setter
    private List<Long> notesList;
    /**
     * The create time of this worksheet.
     */
    @Getter
    @Setter
    private Date createdTime;
    /**
     * The last update time of this worksheet.
     */
    @Getter
    @Setter
    private Date lastUpdatedTime;
    /**
     * The status of this worksheet.
     */
    @Getter
    @Setter
    private WorksheetStatus status = WorksheetStatus.ACTIVE;

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element workSheetElement = new DefaultElement("Worksheet");

        workSheetElement.addAttribute("WorksheetId", worksheetId.toString());
        workSheetElement.addAttribute("WorksheetTitle", worksheetTitle);
        workSheetElement.addAttribute("Comment", comment);
        workSheetElement.addAttribute("NotesList", EntityHelper.buildEntityStrFromList(notesList));
        workSheetElement.addAttribute("CreatedTime", String.valueOf(createdTime.getTime()));
        workSheetElement.addAttribute("LastUpdatedTime", String.valueOf(lastUpdatedTime.getTime()));
        workSheetElement.addAttribute("Status", String.valueOf(status.ordinal()));

        return workSheetElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Worksheet buildFromXMLElement(Element element) {
        worksheetId = Long.parseLong(element.attributeValue("WorksheetId"));
        worksheetTitle = element.attributeValue("WorksheetTitle");
        comment = element.attributeValue("Comment");
        notesList = EntityHelper.buildIDsList(element.attributeValue("NotesList"));
        createdTime = new Date(Long.parseLong(element.attributeValue("CreatedTime")));
        lastUpdatedTime = new Date(Long.parseLong(element.attributeValue("LastUpdatedTime")));
        status = WorksheetStatus.values()[Integer.parseInt(element.attributeValue("Status"))];

        return this;
    }
}

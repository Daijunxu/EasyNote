package notes.entity.workset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.aware.AuthorsAware;
import notes.entity.aware.CommentAware;
import notes.entity.aware.CreatedTimeAware;
import notes.entity.aware.LastUpdatedTimeAware;
import notes.entity.impl.AbstractDocument;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity class to describe a workset. A workset contains a collection of worksheets.
 * <p/>
 * User: rui
 * Date: 9/30/13
 * Time: 1:16 AM
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
public class Workset extends AbstractDocument
        implements AuthorsAware, CreatedTimeAware, LastUpdatedTimeAware, CommentAware {

    /**
     * The list of authors.
     */
    @Getter
    @Setter
    private List<String> authorsList;
    /**
     * The ordered list of worksheetIds.
     */
    @Getter
    @Setter
    private List<Long> worksheetIdsList;
    /**
     * The map from worksheetId to worksheet.
     */
    @Getter
    @Setter
    private Map<Long, Worksheet> worksheetsMap;
    /**
     * The create time of this workset.
     */
    @Getter
    @Setter
    private Date createdTime;
    /**
     * The last update time of this workset.
     */
    @Getter
    @Setter
    private Date lastUpdatedTime;

    /**
     * Constructs an instance of {@code WORKSET}.
     *
     * @param documentId    The document identifier.
     * @param documentTitle The document's title.
     * @param authorsList   The list of authors.
     * @param comment       The comment.
     * @param worksheetsMap The ordered map for worksheets.
     * @throws IllegalArgumentException
     */
    public Workset(final Long documentId, final String documentTitle, final List<String> authorsList,
                   final String comment, final List<Long> worksheetIdsList, final Map<Long, Worksheet> worksheetsMap)
            throws IllegalArgumentException {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.authorsList = authorsList;
        this.comment = comment;
        this.worksheetIdsList = worksheetIdsList;
        this.worksheetsMap = worksheetsMap;
        this.createdTime = new Date(System.currentTimeMillis());
        this.lastUpdatedTime = new Date(System.currentTimeMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNotesCount() {
        if (worksheetsMap == null || worksheetsMap.isEmpty()) {
            return 0;
        }

        int result = 0;
        for (Worksheet worksheet : worksheetsMap.values()) {
            result += worksheet.getNotesList().size();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element workSetElement = new DefaultElement("Workset");

        workSetElement.addAttribute("DocumentId", documentId.toString());
        workSetElement.addAttribute("DocumentTitle", documentTitle);
        workSetElement.addAttribute("AuthorsList", EntityHelper.buildEntityStrFromList(authorsList));
        workSetElement.addAttribute("Comment", comment);
        workSetElement.addAttribute("CreatedTime", String.valueOf(createdTime.getTime()));
        workSetElement.addAttribute("LastUpdatedTime", String.valueOf(lastUpdatedTime.getTime()));

        for (Long workSheetId : worksheetIdsList) {
            workSetElement.add(worksheetsMap.get(workSheetId).toXMLElement());
        }

        return workSetElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Workset buildFromXMLElement(Element element) {
        documentId = Long.parseLong(element.attributeValue("DocumentId"));
        documentTitle = element.attributeValue("DocumentTitle");
        authorsList = EntityHelper.buildAuthorsStrList(element.attributeValue("AuthorsList"));
        comment = element.attributeValue("Comment");
        createdTime = new Date(Long.parseLong(element.attributeValue("CreatedTime")));
        lastUpdatedTime = new Date(Long.parseLong(element.attributeValue("LastUpdatedTime")));

        worksheetIdsList = new ArrayList<Long>();
        worksheetsMap = new HashMap<Long, Worksheet>();
        for (Element worksheetElement : element.elements()) {
            Worksheet newWorksheet = new Worksheet().buildFromXMLElement(worksheetElement);
            worksheetIdsList.add(newWorksheet.getWorksheetId());
            worksheetsMap.put(newWorksheet.getWorksheetId(), newWorksheet);
        }

        return this;
    }

    /**
     * Generates a new worksheet id for the workset.
     *
     * @return The Id for the new worksheet.
     */
    public Long generateNewWorksheetId() {
        return getMaxWorksheetId() + 1;
    }

    private Long getMaxWorksheetId() {
        Long maxWorksheetId = Long.MIN_VALUE;
        for (Long worksheetId : worksheetIdsList) {
            if (maxWorksheetId < worksheetId) {
                maxWorksheetId = worksheetId;
            }
        }
        return maxWorksheetId;
    }
}

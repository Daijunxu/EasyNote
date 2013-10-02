package notes.entity.workset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.entity.impl.AbstractDocument;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

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
public class Workset extends AbstractDocument {

    /**
     * The ordered map for worksheets.
     */
    @Getter
    @Setter
    private TreeMap<Long, Worksheet> worksheetsMap;

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
                   final String comment, final TreeMap<Long, Worksheet> worksheetsMap)
            throws IllegalArgumentException {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.authorsList = authorsList;
        this.comment = comment;
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
        Element workSetElement = new DefaultElement("WORKSET");

        workSetElement.addAttribute("DocumentId", documentId.toString());
        workSetElement.addAttribute("DocumentTitle", documentTitle);
        workSetElement.addAttribute("AuthorsList", EntityHelper.buildEntityStrFromList(authorsList));
        workSetElement.addAttribute("Comment", comment);
        workSetElement.addAttribute("CreatedTime", String.valueOf(createdTime.getTime()));
        workSetElement.addAttribute("LastUpdatedTime", String.valueOf(lastUpdatedTime.getTime()));

        for (Long workSheetId : worksheetsMap.keySet()) {
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

        worksheetsMap = new TreeMap<Long, Worksheet>();
        for (Element worksheetElement : element.elements()) {
            Worksheet newWorksheet = new Worksheet().buildFromXMLElement(worksheetElement);
            worksheetsMap.put(newWorksheet.getWorksheetId(), newWorksheet);
        }

        return this;
    }
}

package notes.businessobjects.workset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.aware.AuthorsAware;
import notes.businessobjects.aware.CommentAware;
import notes.businessobjects.aware.CreatedTimeAware;
import notes.businessobjects.aware.LastUpdatedTimeAware;
import notes.businessobjects.impl.AbstractDocument;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity class to describe a workset. A workset contains a collection of worksheets.
 * <p/>
 * Author: Rui Du
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
     * {inheritDoc}
     */
    @Override
    public void export(Writer writer) {
        WorksetBusinessLogic logic = WorksetBusinessLogic.get();
        try {
            writer.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF8'><title>");
            writer.append(getDocumentTitle());
            writer.append("</title></head>");
            writer.append("<body><a name='title'/><h2>").append(getDocumentTitle()).append("</h2>");

            // Output document ID.
            writer.append("<b>Document ID: </b>").append(getDocumentId().toString()).append("<br>");

            // Output authors.
            if (!getAuthorsList().isEmpty()) {
                writer.append("<b>Author(s): </b>");
                StringBuilder authorsBuilder = new StringBuilder();
                for (String author : getAuthorsList()) {
                    authorsBuilder.append(author).append(", ");
                }
                authorsBuilder.delete(authorsBuilder.length() - 2, authorsBuilder.length());
                writer.append(authorsBuilder);
                writer.append("<br>");
            }

            // Output created time.
            writer.append("<b>Created Time: </b>").append(getCreatedTime().toString()).append("<br>");

            // Output last updated time.
            writer.append("<b>Last Updated Time: </b>").append(getLastUpdatedTime().toString()).append("<br>");

            // Output number of notes.
            writer.append("<b>Number of Notes: </b>").append(String.valueOf(getNotesCount())).append("<br>");

            // Output comment.
            writer.append("<b>Comment: </b><br>");
            writer.append(getComment());
            writer.append("<br>");

            writer.append("<br>");

            // Output anchors for worksheets.
            for (Long worksheetId : getWorksheetsMap().keySet()) {
                writer.append("<a href='#worksheet").append(String.valueOf(worksheetId)).append("'><b>Worksheet ")
                        .append(String.valueOf(worksheetId)).append(": ");
                writer.append(getWorksheetsMap().get(worksheetId).getWorksheetTitle());
                writer.append("</b></a><br>");
            }

            writer.append("<br><hr>");

            // Output notes for each worksheet.
            for (Long worksheetId : getWorksheetsMap().keySet()) {
                // Output worksheet title.
                Worksheet worksheet = getWorksheetsMap().get(worksheetId);
                writer.append("<a name='worksheet").append(String.valueOf(worksheetId)).append("'/><h3>Worksheet ")
                        .append(String.valueOf(worksheetId)).append(": ");
                writer.append(worksheet.getWorksheetTitle());
                writer.append("</h3>");

                // Output anchor to title.
                writer.append("<a href='#title'>Back to Top</a>");

                // Output all notes in the worksheet.
                List<WorksheetNote> noteList = logic.getNotesListForCurrentWorksheet();
                for (WorksheetNote note : noteList) {
                    writer.append("<p>");

                    // Output note text.
                    String noteText = note.getNoteText();
                    noteText = noteText.replaceAll("&", "&amp;");
                    noteText = noteText.replaceAll("<", "&lt;");
                    noteText = noteText.replaceAll("\n", "<br>");
                    noteText = noteText.replaceAll("  ", "&emsp;");
                    noteText = noteText.replaceAll("\t", "&emsp;&emsp;");
                    writer.append(noteText);
                    writer.append("<br><i>");

                    // Output tags.
                    if (!note.getTagIds().isEmpty()) {
                        for (Long tagId : note.getTagIds()) {
                            writer.append("[").append(logic.getWorksheetNoteDAO().findTagById(tagId).getTagText())
                                    .append("] ");
                        }
                    }

                    // Output note ID.
                    writer.append("ID: ").append(note.getNoteId().toString());

                    writer.append("</i><br></p>");
                }
                writer.append("<hr>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

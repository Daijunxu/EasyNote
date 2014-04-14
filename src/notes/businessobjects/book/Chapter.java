package notes.businessobjects.book;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import notes.businessobjects.XMLSerializable;
import notes.utils.EntityHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.List;

/**
 * Entity class to describe a chapter. A book contains multiple chapters, while a chapter belongs to
 * only one book.
 *
 * Author: Rui Du
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Chapter implements XMLSerializable<Chapter> {

    /**
     * The chapter identifier.
     */
    @Getter
    @Setter
    private Long chapterId;
    /**
     * The chapter's title.
     */
    @Getter
    @Setter
    private String chapterTitle;
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
        Element chapterElement = new DefaultElement("Chapter");

        chapterElement.addAttribute("ChapterId", chapterId.toString());
        chapterElement.addAttribute("ChapterTitle", chapterTitle);
        chapterElement.addAttribute("NotesList", EntityHelper.buildEntityStrFromList(notesList));

        return chapterElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chapter buildFromXMLElement(Element element) {
        chapterId = Long.parseLong(element.attributeValue("ChapterId"));
        chapterTitle = element.attributeValue("ChapterTitle");
        notesList = EntityHelper.buildIDsList(element.attributeValue("NotesList"));

        return this;
    }
}

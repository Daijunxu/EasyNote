package notes.book;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Entity class to describe a chapter. A book contains multiple chapters, while a chapter belongs to
 * only one book.
 *
 * @author Rui Du
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, includeFieldNames = true)
public class Chapter {

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

}

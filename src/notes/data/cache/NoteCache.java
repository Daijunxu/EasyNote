/**
 *
 */
package notes.data.cache;

import lombok.Getter;
import lombok.Setter;
import notes.entity.Note;
import notes.entity.XMLSerializable;
import notes.entity.article.ArticleNote;
import notes.entity.book.BookNote;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all the notes.
 *
 * @author Rui Du
 * @version 1.0
 */
public class NoteCache implements XMLSerializable {

    /**
     * The single instance that is used in this system.
     */
    private static final NoteCache instance = new NoteCache();
    /**
     * The map of all notes from note IDs to the notes.
     */
    @Getter
    private final Map<Long, Note> noteMap;
    /**
     * The maximum note ID in the data.
     */
    @Getter
    @Setter
    private Long maxNoteId = 0L;

    /**
     * Constructs an instance of {@code NoteCache}. Should only be called by Cache.
     */
    public NoteCache() {
        noteMap = new HashMap<Long, Note>();
    }

    /**
     * Gets the instance of {@code NoteCache}.
     *
     * @return {@code NoteCache} The instance of {@code NoteCache}.
     */
    public static NoteCache get() {
        return instance;
    }

    /**
     * Removes all data stored in the note cache.
     */
    public void clear() {
        this.noteMap.clear();
        this.maxNoteId = Long.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element toXMLElement() {
        Element noteCacheElement = new DefaultElement("Notes");

        for (Note note : noteMap.values()) {
            noteCacheElement.add(note.toXMLElement());
        }

        return noteCacheElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoteCache buildFromXMLElement(Element element) {
        // Clear data in the note cache.
        clear();

        for (Element noteElement : element.elements()) {
            Note newNote;
            if (noteElement.getName().equals("BookNote")) {
                newNote = new BookNote().buildFromXMLElement(noteElement);
            } else if (noteElement.getName().equals("ArticleNote")) {
                newNote = new ArticleNote().buildFromXMLElement(noteElement);
            } else {
                throw new UnsupportedOperationException("Unsupported note type: " + noteElement.getName());
            }

            noteMap.put(newNote.getNoteId(), newNote);
            if (maxNoteId < newNote.getNoteId()) {
                maxNoteId = newNote.getNoteId();
            }
        }
        return this;
    }
}

package notes.data.cache;

import lombok.Getter;
import lombok.Setter;
import notes.businessobjects.Note;
import notes.businessobjects.article.ArticleNote;
import notes.businessobjects.book.BookNote;
import notes.businessobjects.workset.WorksheetNote;
import notes.dao.DuplicateRecordException;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all the notes.
 * <p/>
 * Author: Rui Du
 */
public class NoteCache implements Cache<Note> {

    /**
     * The single instance that is used in this system.
     */
    private static final NoteCache instance = new NoteCache();
    /**
     * The map of all notes from note IDs to the notes.
     */
    private final Map<Long, Note> noteMap;
    /**
     * The maximum note ID in the data.
     */
    private Long maxNoteId = 0L;
    /**
     * A flag indicating whether the data in the cache has been changed.
     */
    @Getter
    @Setter
    private volatile boolean cacheChanged = false;

    /**
     * Constructs an instance of {@code NoteCache}. Should only be called by CacheDelegate.
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

    @Override
    public void initialize() {
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
        initialize();

        for (Element noteElement : element.elements()) {
            Note newNote;
            if (noteElement.getName().equals("BookNote")) {
                newNote = new BookNote().buildFromXMLElement(noteElement);
            } else if (noteElement.getName().equals("ArticleNote")) {
                newNote = new ArticleNote().buildFromXMLElement(noteElement);
            } else if (noteElement.getName().equals("WorksheetNote")) {
                newNote = new WorksheetNote().buildFromXMLElement(noteElement);
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

    @Override
    public Note insert(Note note) throws DuplicateRecordException {
        Long noteId = (note.getNoteId() != null) ? note.getNoteId() : maxNoteId + 1L;
        if (noteMap.containsKey(noteId)) {
            throw new DuplicateRecordException("Duplicate note exception: same note ID!");
        }

        if (note instanceof WorksheetNote) {
            WorksheetNote newNote = new WorksheetNote();
            newNote.setNoteId(noteId);
            newNote.setDocumentId(note.getDocumentId());
            newNote.setWorksheetId(((WorksheetNote) note).getWorksheetId());
            newNote.setTagIds(note.getTagIds());
            newNote.setNoteText(note.getNoteText());
            newNote.setNoteStatus(((WorksheetNote) note).getNoteStatus());
            if (note.getCreatedTime() == null) {
                newNote.setCreatedTime(new Date());
            } else {
                newNote.setCreatedTime(note.getCreatedTime());
            }

            noteMap.put(newNote.getNoteId(), newNote);

            // Update the max note ID in note cache.
            if (maxNoteId < newNote.getNoteId()) {
                maxNoteId = newNote.getNoteId();
            }

            return newNote;
        } else if (note instanceof ArticleNote) {
            ArticleNote newNote = new ArticleNote();
            newNote.setNoteId(noteId);
            newNote.setDocumentId(note.getDocumentId());
            newNote.setTagIds(note.getTagIds());
            newNote.setNoteText(note.getNoteText());
            if (note.getCreatedTime() == null) {
                newNote.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newNote.setCreatedTime(note.getCreatedTime());
            }

            noteMap.put(newNote.getNoteId(), newNote);

            // Update the max note ID in note CACHE.
            if (maxNoteId < newNote.getNoteId()) {
                maxNoteId = newNote.getNoteId();
            }

            return newNote;
        } else if (note instanceof BookNote) {
            BookNote newNote = new BookNote();
            newNote.setNoteId(noteId);
            newNote.setDocumentId(note.getDocumentId());
            newNote.setChapterId(((BookNote) note).getChapterId());
            newNote.setTagIds(note.getTagIds());
            newNote.setNoteText(note.getNoteText());
            if (note.getCreatedTime() == null) {
                newNote.setCreatedTime(new Date(System.currentTimeMillis()));
            } else {
                newNote.setCreatedTime(note.getCreatedTime());
            }

            noteMap.put(newNote.getNoteId(), newNote);

            // Update the max note ID in note CACHE.
            if (maxNoteId < newNote.getNoteId()) {
                maxNoteId = newNote.getNoteId();
            }

            return newNote;
        }

        return null;
    }

    @Override
    public Note remove(Long id) {
        Note note = noteMap.get(id);
        noteMap.remove(id);
        return note;
    }

    @Override
    public Note update(Note note) {
        if (note instanceof WorksheetNote) {
            WorksheetNote cachedNote = (WorksheetNote) (noteMap.get(note.getNoteId()));
            if (cachedNote != null) {
                cachedNote.setDocumentId(note.getDocumentId());
                cachedNote.setWorksheetId(((WorksheetNote) note).getWorksheetId());
                cachedNote.setTagIds(note.getTagIds());
                cachedNote.setNoteText(note.getNoteText());
                cachedNote.setNoteStatus(((WorksheetNote) note).getNoteStatus());

                return cachedNote;
            }
        } else if (note instanceof ArticleNote) {
            ArticleNote cachedNote = (ArticleNote) (noteMap.get(note.getNoteId()));
            if (cachedNote != null) {
                cachedNote.setDocumentId(note.getDocumentId());
                cachedNote.setTagIds(note.getTagIds());
                cachedNote.setNoteText(note.getNoteText());

                return cachedNote;
            }
        } else if (note instanceof BookNote) {
            BookNote cachedNote = (BookNote) (noteMap.get(note.getNoteId()));
            if (cachedNote != null) {
                cachedNote.setDocumentId(note.getDocumentId());
                cachedNote.setChapterId(((BookNote) note).getChapterId());
                cachedNote.setTagIds(note.getTagIds());
                cachedNote.setNoteText(note.getNoteText());

                return cachedNote;
            }
        }

        return null;
    }

    @Override
    public Note find(Long id) {
        return noteMap.get(id);
    }

    @Override
    public List<Note> findAll() {
        List<Note> noteList = new ArrayList<Note>();
        for (Note note : noteMap.values()) {
            noteList.add(note);
        }
        return noteList;
    }
}

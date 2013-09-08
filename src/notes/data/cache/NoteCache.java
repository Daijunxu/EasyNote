/**
 *
 */
package notes.data.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notes.article.ArticleNote;
import notes.book.BookNote;
import notes.dao.DuplicateRecordException;
import notes.entity.Note;

/**
 * Stores all the notes.
 *
 * @author Rui Du
 * @version 1.0
 */
public class NoteCache {

    /**
     * The map of all notes from note IDs to the notes.
     */
    private Map<Long, Note> noteMap;

    /**
     * The maximum note ID in the data.
     */
    private Long maxNoteId = 0L;

    /**
     * Constructs an instance of {@code NoteCache}. Should only be called by Cache.
     *
     * @param input The {@code BufferedReader} instance in use.
     */
    public NoteCache(BufferedReader input) {
        setNoteMap(new HashMap<Long, Note>());
        loadNoteCache(input);
    }

    /**
     * Removes all data stored in the note cache.
     */
    public void clear() {
        this.noteMap.clear();
        this.maxNoteId = Long.MIN_VALUE;
    }

    /**
     * Gets the maximum note ID.
     *
     * @return {@code Long} The maximum note ID.
     */
    public Long getMaxNoteId() {
        return maxNoteId;
    }

    /**
     * Gets the note map.
     *
     * @return {@code Map<Long, Note>} The noteMap.
     */
    public Map<Long, Note> getNoteMap() {
        return noteMap;
    }

    /**
     * Reads all notes' data from data file.
     *
     * @param input The {@code BufferedReader} in use.
     * @return True if note cache has been successfully loaded, otherwise false.
     */
    private void loadNoteCache(BufferedReader input) {
        String line = "";

        try {
            line = input.readLine();
            if (line.equals("#NOTES") == false) {
                throw new InvalidDataFormatException("No note head: expecting \"#NOTES\".");
            }

            // Start reading notes.
            Note newNote;
            do {
                newNote = readNextNote(input);
                if (newNote != null) {
                    getNoteMap().put(newNote.getNoteId(), newNote);
                    if (getMaxNoteId() < newNote.getNoteId()) {
                        setMaxNoteId(newNote.getNoteId());
                    }
                }
            } while (newNote != null);
        } catch (Exception e) {
            Cache.hasProblem = true;
            e.printStackTrace();
        }
    }

    /**
     * Reads the next block of note data and creates a new note.
     *
     * @param input The {@code BufferedReader} in use.
     * @return {@code Note} The created note.
     * @throws InvalidDataFormatException
     */
    private Note readNextNote(BufferedReader input) throws InvalidDataFormatException {
        try {
            String line = input.readLine();
            if (line.equals("#BOOK")) {
                BookNote newNote = new BookNote();

                newNote.setNoteId(Long.parseLong(input.readLine()));
                newNote.setDocumentId(Long.parseLong(input.readLine()));
                newNote.setChapterId(Long.parseLong(input.readLine()));

                List<Long> tagIds = new ArrayList<Long>();
                if ((line = input.readLine()).equals("0") == false) {
                    String[] tags = line.split(",");
                    for (String tag : tags) {
                        tagIds.add(Long.parseLong(tag));
                    }
                }
                newNote.setTagIds(tagIds);

                StringBuilder noteTextBuilder = new StringBuilder();
                while ((line = input.readLine()).equals("#END_TEXT") == false) {
                    noteTextBuilder.append(line);
                    noteTextBuilder.append("\n");
                }
                noteTextBuilder.deleteCharAt(noteTextBuilder.length() - 1);
                newNote.setNoteText(noteTextBuilder.toString());
                newNote.setCreatedTime(new Date(Long.parseLong(input.readLine())));

                return newNote;

            } else if (line.equals("#ARTICLE")) {
                ArticleNote newNote = new ArticleNote();

                newNote.setNoteId(Long.parseLong(input.readLine()));
                newNote.setDocumentId(Long.parseLong(input.readLine()));

                List<Long> tagIds = new ArrayList<Long>();
                if ((line = input.readLine()).equals("0") == false) {
                    String[] tags = line.split(",");
                    for (String tag : tags) {
                        tagIds.add(Long.parseLong(tag));
                    }
                }
                newNote.setTagIds(tagIds);

                StringBuilder noteTextBuilder = new StringBuilder();
                while ((line = input.readLine()).equals("#END_TEXT") == false) {
                    noteTextBuilder.append(line);
                    noteTextBuilder.append("\n");
                }
                noteTextBuilder.deleteCharAt(noteTextBuilder.length() - 1);
                newNote.setNoteText(noteTextBuilder.toString());
                newNote.setCreatedTime(new Date(Long.parseLong(input.readLine())));

                return newNote;

            } else if (line.equals("#END_NOTES")) {
                return null;
            } else {
                throw new InvalidDataFormatException("Unexpected data \"" + line
                        + "\": expecting beginning tag of a document or end documents tag.");
            }
        } catch (IOException e) {
            Cache.hasProblem = true;
            e.printStackTrace();
        } catch (Exception e) {
            throw new InvalidDataFormatException(e.getMessage(), e.getCause());
        }
        return null;
    }

    /**
     * Writes all notes into data file.
     *
     * @param output The {@code BufferedWriter} in use.
     */
    public void saveNoteCache(BufferedWriter output) {
        try {
            output.append("#NOTES\n");
            for (Note note : noteMap.values()) {
                // Write each note.
                StringBuilder sb = new StringBuilder();
                if (note instanceof BookNote) {
                    sb.append("#BOOK\n");
                    sb.append(note.getNoteId());
                    sb.append("\n");
                    sb.append(note.getDocumentId());
                    sb.append("\n");
                    sb.append(((BookNote) note).getChapterId());
                    sb.append("\n");
                    if (note.getTagIds().isEmpty()) {
                        sb.append("0\n");
                    } else {
                        for (Long tagId : note.getTagIds()) {
                            sb.append(tagId);
                            sb.append(",");
                        }
                        sb.replace(sb.length() - 1, sb.length(), "\n");
                    }
                    sb.append(note.getNoteText());
                    sb.append("\n#END_TEXT\n");
                    sb.append(note.getCreatedTime().getTime());
                    sb.append("\n");
                } else if (note instanceof ArticleNote) {
                    sb.append("#ARTICLE\n");
                    sb.append(note.getNoteId());
                    sb.append("\n");
                    sb.append(note.getDocumentId());
                    sb.append("\n");
                    if (note.getTagIds().isEmpty()) {
                        sb.append("0\n");
                    } else {
                        for (Long tagId : note.getTagIds()) {
                            sb.append(tagId);
                            sb.append(",");
                        }
                        sb.replace(sb.length() - 1, sb.length(), "\n");
                    }
                    sb.append(note.getNoteText());
                    sb.append("\n#END_TEXT\n");
                    sb.append(note.getCreatedTime().getTime());
                    sb.append("\n");
                } else {
                    throw new DuplicateRecordException("Unknown note type!");
                }
                output.append(sb.toString());
            }
            output.append("#END_NOTES\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the maximum note ID.
     *
     * @param maxNoteId The maximum note ID to set.
     */
    public void setMaxNoteId(Long maxNoteId) {
        this.maxNoteId = maxNoteId;
    }

    /**
     * Sets the note map.
     *
     * @param noteMap The note map to set.
     */
    public void setNoteMap(Map<Long, Note> noteMap) {
        this.noteMap = noteMap;
    }
}

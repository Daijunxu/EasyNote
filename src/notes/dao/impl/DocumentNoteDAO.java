package notes.dao.impl;

import notes.dao.DuplicateRecordException;
import notes.dao.NoteDAO;
import notes.data.cache.Cache;
import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An abstract class implementing basic members and methods of a NoteDAO.
 * <p/>
 * Author: Rui Du
 */
public class DocumentNoteDAO implements NoteDAO<Note, Document> {

    private static final DocumentNoteDAO INSTANCE = new DocumentNoteDAO();
    private static final Cache CACHE = Cache.get();

    protected DocumentNoteDAO() {
    }

    public static DocumentNoteDAO get() {
        return INSTANCE;
    }

    @Override
    public void deleteDocument(Document document) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    @Override
    public void deleteNote(Note note) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTag(Tag tag) {
        Long tagId = tag.getTagId();

        // Remove all occurrences of the tag in notes.
        for (Note note : CACHE.getNoteCache().getNoteMap().values()) {
            note.getTagIds().remove(tagId);
        }

        // Remove the tag from the tag cache.
        CACHE.getTagCache().getTagIdMap().remove(tagId);
        CACHE.getTagCache().getTagTextMap().remove(tag.getTagText());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> findAllDocuments() {
        List<Document> documentList = new ArrayList<Document>();
        Map<Long, Document> documentMap = CACHE.getDocumentCache().getDocumentMap();
        for (Document document : documentMap.values()) {
            documentList.add(document);
        }
        Collections.sort(documentList);
        return documentList;
    }

    public boolean isExistingDocument(Document document) {
        if (findDocumentById(document.getDocumentId()) != null) {
            return true;
        }

        List<Document> existingDocuments = findAllDocuments();
        for (Document existingDocument : existingDocuments) {
            if (existingDocument.getDocumentTitle().equals(document.getDocumentTitle())) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAllNotes() {
        List<Note> noteList = new ArrayList<Note>();
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        for (Note note : noteMap.values()) {
            noteList.add(note);
        }
        Collections.sort(noteList);
        return noteList;
    }

    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAllNotesByTagId(Long tagId) {
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        List<Note> noteList = new ArrayList<Note>();
        for (Note note : noteMap.values()) {
            if (note.getTagIds().contains(tagId)) {
                noteList.add(note);
            }
        }
        Collections.sort(noteList);
        return noteList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAllNotesContainingText(Set<Long> candidateDocuments, String text, boolean caseSensitive,
                                                 boolean exactSearch) {
        Map<Long, Note> noteMap = CACHE.getNoteCache().getNoteMap();
        List<Note> resultList = new ArrayList<Note>();

        if (!caseSensitive) {
            if (!exactSearch) {
                // Not case sensitive, not exact search.
                String[] tokens = text.trim().toLowerCase().split("\\s+");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].replaceAll("[^0-9A-Za-z\\-']", "");
                }

                boolean isResult;
                for (Note note : noteMap.values()) {
                    if (candidateDocuments != null && !candidateDocuments.contains(note.getDocumentId())) {
                        continue;
                    }
                    isResult = true;
                    String[] noteTokens = note.getNoteText().toLowerCase().split("\\s+");
                    Set<String> tokensSet = new HashSet<String>();
                    for (String noteToken : noteTokens) {
                        tokensSet.add(noteToken.replaceAll("[^0-9A-Za-z\\-']", ""));
                    }
                    for (String token : tokens) {
                        if (!tokensSet.contains(token)) {
                            isResult = false;
                            break;
                        }
                    }
                    if (isResult) {
                        resultList.add(note);
                    }
                }
            } else {
                // not case sensitive, exact search.
                for (Note note : noteMap.values()) {
                    if (candidateDocuments != null && !candidateDocuments.contains(note.getDocumentId())) {
                        continue;
                    }
                    if (note.getNoteText().toLowerCase().contains(text.toLowerCase())) {
                        resultList.add(note);
                    }
                }
            }

        } else {
            if (!exactSearch) {
                // Case sensitive, not exact search.
                String[] tokens = text.trim().split("\\s+");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].replaceAll("[^0-9A-Za-z\\-']", "");
                }

                boolean isResult;
                for (Note note : noteMap.values()) {
                    if (candidateDocuments != null && !candidateDocuments.contains(note.getDocumentId())) {
                        continue;
                    }
                    isResult = true;
                    String[] noteTokens = note.getNoteText().split("\\s+");
                    Set<String> tokensSet = new HashSet<String>();
                    for (String noteToken : noteTokens) {
                        tokensSet.add(noteToken.replaceAll("[^0-9A-Za-z\\-']", ""));
                    }
                    for (String token : tokens) {
                        if (!tokensSet.contains(token)) {
                            isResult = false;
                            break;
                        }
                    }
                    if (isResult) {
                        resultList.add(note);
                    }
                }
            } else {
                // Case sensitive, exact search.
                for (Note note : noteMap.values()) {
                    if (candidateDocuments != null && !candidateDocuments.contains(note.getDocumentId())) {
                        continue;
                    }
                    if (note.getNoteText().contains(text)) {
                        resultList.add(note);
                    }
                }
            }
        }

        Collections.sort(resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tag> findAllTags() {
        List<Tag> tagList = new ArrayList<Tag>();
        Map<Long, Tag> tagMap = CACHE.getTagCache().getTagIdMap();
        for (Tag tag : tagMap.values()) {
            tagList.add(tag);
        }
        Collections.sort(tagList);
        return tagList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document findDocumentById(Long documentId) {
        return CACHE.getDocumentCache().getDocumentMap().get(documentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note findNoteById(Long noteId) {
        return CACHE.getNoteCache().getNoteMap().get(noteId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag findTagById(Long tagId) {
        return CACHE.getTagCache().getTagIdMap().get(tagId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag findTagByText(String tagText) {
        return CACHE.getTagCache().getTagTextMap().get(tagText);
    }

    @Override
    public Document updateDocument(Document document) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    @Override
    public Note updateNote(Note note) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag updateTag(Tag tag) {
        Tag updateTag = CACHE.getTagCache().getTagIdMap().get(tag.getTagId());
        if (updateTag != null) {
            CACHE.getTagCache().getTagTextMap().remove(updateTag.getTagText());
            updateTag.setTagText(tag.getTagText());
            CACHE.getTagCache().getTagTextMap().put(updateTag.getTagText(), updateTag);
            return updateTag;
        }
        return null;
    }

    @Override
    public Document saveDocument(Document document) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    @Override
    public Note saveNote(Note note) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag saveTag(Tag tag) {
        Tag newTag = new Tag();
        if (tag.getTagId() == null) {
            newTag.setTagId(CACHE.getTagCache().getMaxTagId() + 1L);
        } else {
            newTag.setTagId(tag.getTagId());
        }
        newTag.setTagText(tag.getTagText());

        // Add the tag to tag cache.
        try {
            if (CACHE.getTagCache().getTagIdMap().containsKey(newTag.getTagId())) {
                throw new DuplicateRecordException("Duplicate tag exception: same tag ID!");
            }
            if (CACHE.getTagCache().getTagTextMap().containsKey(newTag.getTagText())) {
                throw new DuplicateRecordException("Duplicate tag exception: same tag text!");
            }
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
        }
        CACHE.getTagCache().getTagIdMap().put(newTag.getTagId(), newTag);
        CACHE.getTagCache().getTagTextMap().put(newTag.getTagText(), newTag);

        // Update max note id in tag cache.
        if (CACHE.getTagCache().getMaxTagId() < newTag.getTagId()) {
            CACHE.getTagCache().setMaxTagId(newTag.getTagId());
        }

        return newTag;
    }

}

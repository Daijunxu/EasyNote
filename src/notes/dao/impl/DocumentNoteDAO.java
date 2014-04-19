package notes.dao.impl;

import notes.businessobjects.Document;
import notes.businessobjects.Note;
import notes.businessobjects.Tag;
import notes.dao.NoteDAO;
import notes.data.cache.CacheDelegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An abstract class implementing basic members and methods of a NoteDAO.
 * <p/>
 * Author: Rui Du
 */
public class DocumentNoteDAO implements NoteDAO<Note, Document> {

    private static final DocumentNoteDAO INSTANCE = new DocumentNoteDAO();
    private static final CacheDelegate CACHE = CacheDelegate.get();

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

    @Override
    public void deleteTag(Tag tag) {
        Long tagId = tag.getTagId();

        // Remove all occurrences of the tag in notes.
        for (Note note : CACHE.getNoteCache().findAll()) {
            note.getTagIds().remove(tagId);
        }

        // Remove the tag from the tag cache.
        CACHE.getTagCache().remove(tagId);
    }

    @Override
    public List<Document> findAllDocuments() {
        return CACHE.getDocumentCache().findAll();
    }

    @Override
    public List<Note> findAllNotes() {
        return CACHE.getNoteCache().findAll();
    }

    @Override
    public List<Note> findAllNotesByDocumentId(Long documentId) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    @Override
    public List<Note> findAllNotesByTagId(Long tagId) {
        List<Note> noteList = new ArrayList<Note>();
        for (Note note : CACHE.getNoteCache().findAll()) {
            if (note.getTagIds().contains(tagId)) {
                noteList.add(note);
            }
        }
        Collections.sort(noteList);
        return noteList;
    }

    @Override
    public List<Note> findAllNotesContainingText(Set<Long> candidateDocuments, String text, boolean caseSensitive,
                                                 boolean exactSearch) {
        List<Note> resultList = new ArrayList<Note>();

        if (!caseSensitive) {
            if (!exactSearch) {
                // Not case sensitive, not exact search.
                String[] tokens = text.trim().toLowerCase().split("\\s+");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].replaceAll("[^0-9A-Za-z\\-']", "");
                }

                boolean isResult;
                for (Note note : CACHE.getNoteCache().findAll()) {
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
                for (Note note : CACHE.getNoteCache().findAll()) {
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
                for (Note note : CACHE.getNoteCache().findAll()) {
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
                for (Note note : CACHE.getNoteCache().findAll()) {
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

    @Override
    public List<Tag> findAllTags() {
        return CACHE.getTagCache().findAll();
    }

    @Override
    public Document findDocumentById(Long documentId) {
        return CACHE.getDocumentCache().find(documentId);
    }

    @Override
    public Note findNoteById(Long noteId) {
        return CACHE.getNoteCache().find(noteId);
    }

    @Override
    public Tag findTagById(Long tagId) {
        return CACHE.getTagCache().find(tagId);
    }

    @Override
    public Tag findTagByText(String tagText) {
        return CACHE.getTagCache().find(tagText);
    }

    @Override
    public Document updateDocument(Document document) {
        return CACHE.getDocumentCache().update(document);
    }

    @Override
    public Note updateNote(Note note) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    @Override
    public Tag updateTag(Tag tag) {
        return CACHE.getTagCache().update(tag);
    }

    @Override
    public Document saveDocument(Document document) {
        return CACHE.getDocumentCache().insert(document);
    }

    @Override
    public Note saveNote(Note note) {
        throw new UnsupportedOperationException("This method should not be called here.");
    }

    @Override
    public Tag saveTag(Tag tag) {
        return CACHE.getTagCache().insert(tag);
    }

}

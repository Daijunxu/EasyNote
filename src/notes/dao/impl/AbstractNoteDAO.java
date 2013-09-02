/**
 * 
 */
package notes.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import notes.article.entity.Article;
import notes.book.entity.Book;
import notes.book.entity.Chapter;
import notes.dao.NoteDAO;
import notes.dao.TagException;
import notes.data.cache.Cache;
import notes.entity.Document;
import notes.entity.Note;
import notes.entity.Tag;

/**
 * An abstract class implementing basic members and methods of a NoteDAO.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public abstract class AbstractNoteDAO implements NoteDAO<Note, Document> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTag(Tag tag) {
		Long tagId = tag.getTagId();

		// Remove all occurrences of the tag in notes.
		for (Note note : Cache.get().getNoteCache().getNoteMap().values()) {
			note.getTagIds().remove(tagId);
		}

		// Remove the tag from the tag cache.
		Cache.get().getTagCache().getTagIdMap().remove(tagId);
		Cache.get().getTagCache().getTagTextMap().remove(tag.getTagText());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Document> findAllDocuments() {
		List<Document> documentList = new ArrayList<Document>();
		Map<Long, Document> documentMap = Cache.get().getDocumentCache().getDocumentMap();
		for (Document document : documentMap.values()) {
			documentList.add(document);
		}
		Collections.sort(documentList);
		return documentList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Note> findAllNotes() {
		List<Note> noteList = new ArrayList<Note>();
		Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
		for (Note note : noteMap.values()) {
			noteList.add(note);
		}
		Collections.sort(noteList);
		return noteList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Note> findAllNotesByTagId(Long tagId) {
		Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
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
	public List<Note> findAllNotesContainingText(Long documentId, String text,
			boolean caseSensitive, boolean exactSearch) {
		Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
		List<Long> candidateList = new ArrayList<Long>();
		List<Note> noteList = new ArrayList<Note>();

		// Get all note IDs in the document.
		Document document = Cache.get().getDocumentCache().getDocumentMap().get(documentId);
		if (document instanceof Article) {
			for (Long noteId : ((Article) document).getNotesList()) {
				candidateList.add(noteId);
			}
		} else if (document instanceof Book) {
			for (Chapter chapter : ((Book) document).getChaptersMap().values()) {
				for (Long noteId : chapter.getNotesList()) {
					candidateList.add(noteId);
				}
			}
		}

		if (caseSensitive == false) {
			if (exactSearch == false) {
				// Not case sensitive, not exact search.
				String[] tokens = text.trim().toLowerCase().split("\\s+");
				for (int i = 0; i < tokens.length; i++) {
					tokens[i] = tokens[i].replaceAll("[^0-9A-Za-z\\-']", "");
				}

				boolean isResult;
				for (Long noteId : candidateList) {
					Note note = noteMap.get(noteId);
					isResult = true;
					String[] noteTokens = note.getNoteText().toLowerCase().split("\\s+");
					Set<String> tokensSet = new HashSet<String>();
					for (String noteToken : noteTokens) {
						tokensSet.add(noteToken.replaceAll("[^0-9A-Za-z\\-']", ""));
					}
					for (String token : tokens) {
						if (tokensSet.contains(token) == false) {
							isResult = false;
							break;
						}
					}
					if (isResult) {
						noteList.add(note);
					}
				}
			} else {
				// not case sensitive, exact search.
				for (Long noteId : candidateList) {
					Note note = noteMap.get(noteId);
					if (note.getNoteText().toLowerCase().contains(text.toLowerCase())) {
						noteList.add(note);
					}
				}
			}

		} else {
			if (exactSearch == false) {
				// Case sensitive, not exact search.
				String[] tokens = text.trim().split("\\s+");
				for (int i = 0; i < tokens.length; i++) {
					tokens[i] = tokens[i].replaceAll("[^0-9A-Za-z\\-']", "");
				}

				boolean isResult;
				for (Long noteId : candidateList) {
					Note note = noteMap.get(noteId);
					isResult = true;
					String[] noteTokens = note.getNoteText().split("\\s+");
					Set<String> tokensSet = new HashSet<String>();
					for (String noteToken : noteTokens) {
						tokensSet.add(noteToken.replaceAll("[^0-9A-Za-z\\-']", ""));
					}
					for (String token : tokens) {
						if (tokensSet.contains(token) == false) {
							isResult = false;
							break;
						}
					}
					if (isResult) {
						noteList.add(note);
					}
				}
			} else {
				// Case sensitive, exact search.
				for (Long noteId : candidateList) {
					Note note = noteMap.get(noteId);
					if (note.getNoteText().contains(text)) {
						noteList.add(note);
					}
				}
			}
		}

		candidateList.clear();

		Collections.sort(noteList);
		return noteList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Note> findAllNotesContainingText(String text, boolean caseSensitive,
			boolean exactSearch) {
		Map<Long, Note> noteMap = Cache.get().getNoteCache().getNoteMap();
		List<Note> noteList = new ArrayList<Note>();

		if (caseSensitive == false) {
			if (exactSearch == false) {
				// Not case sensitive, not exact search.
				String[] tokens = text.trim().toLowerCase().split("\\s+");
				for (int i = 0; i < tokens.length; i++) {
					tokens[i] = tokens[i].replaceAll("[^0-9A-Za-z\\-']", "");
				}

				boolean isResult;
				for (Note note : noteMap.values()) {
					isResult = true;
					String[] noteTokens = note.getNoteText().toLowerCase().split("\\s+");
					Set<String> tokensSet = new HashSet<String>();
					for (String noteToken : noteTokens) {
						tokensSet.add(noteToken.replaceAll("[^0-9A-Za-z\\-']", ""));
					}
					for (String token : tokens) {
						if (tokensSet.contains(token) == false) {
							isResult = false;
							break;
						}
					}
					if (isResult) {
						noteList.add(note);
					}
				}
			} else {
				// not case sensitive, exact search.
				for (Note note : noteMap.values()) {
					if (note.getNoteText().toLowerCase().contains(text.toLowerCase())) {
						noteList.add(note);
					}
				}
			}

		} else {
			if (exactSearch == false) {
				// Case sensitive, not exact search.
				String[] tokens = text.trim().split("\\s+");
				for (int i = 0; i < tokens.length; i++) {
					tokens[i] = tokens[i].replaceAll("[^0-9A-Za-z\\-']", "");
				}

				boolean isResult;
				for (Note note : noteMap.values()) {
					isResult = true;
					String[] noteTokens = note.getNoteText().split("\\s+");
					Set<String> tokensSet = new HashSet<String>();
					for (String noteToken : noteTokens) {
						tokensSet.add(noteToken.replaceAll("[^0-9A-Za-z\\-']", ""));
					}
					for (String token : tokens) {
						if (tokensSet.contains(token) == false) {
							isResult = false;
							break;
						}
					}
					if (isResult) {
						noteList.add(note);
					}
				}
			} else {
				// Case sensitive, exact search.
				for (Note note : noteMap.values()) {
					if (note.getNoteText().contains(text)) {
						noteList.add(note);
					}
				}
			}
		}

		Collections.sort(noteList);
		return noteList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Tag> findAllTags() {
		List<Tag> tagList = new ArrayList<Tag>();
		Map<Long, Tag> tagMap = Cache.get().getTagCache().getTagIdMap();
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
		return Cache.get().getDocumentCache().getDocumentMap().get(documentId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Document findDocumentByTitle(String documentTitle) {
		Long documentId = Cache.get().getDocumentCache().getDocumentTitleIdMap().get(documentTitle);
		return findDocumentById(documentId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Note findNoteById(Long noteId) {
		return Cache.get().getNoteCache().getNoteMap().get(noteId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tag findTagById(Long tagId) {
		return Cache.get().getTagCache().getTagIdMap().get(tagId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tag findTagByText(String tagText) {
		return Cache.get().getTagCache().getTagTextMap().get(tagText);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tag mergeTag(Tag tag) {
		Tag updateTag = Cache.get().getTagCache().getTagIdMap().get(tag.getTagId());
		if (updateTag != null) {
			Cache.get().getTagCache().getTagTextMap().remove(updateTag.getTagText());
			updateTag.setTagText(tag.getTagText());
			Cache.get().getTagCache().getTagTextMap().put(updateTag.getTagText(), updateTag);
			return updateTag;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tag saveTag(Tag tag) {
		Tag newTag = new Tag();
		if (tag.getTagId() == null) {
			newTag.setTagId(Cache.get().getTagCache().getMaxTagId() + 1L);
		} else {
			newTag.setTagId(tag.getTagId());
		}
		newTag.setTagText(tag.getTagText());

		// Add the tag to tag cache.
		try {
			if (Cache.get().getTagCache().getTagIdMap().containsKey(newTag.getTagId())) {
				throw new TagException("Duplicate tag exception: same tag ID!");
			}
			if (Cache.get().getTagCache().getTagTextMap().containsKey(newTag.getTagText())) {
				throw new TagException("Duplicate tag exception: same tag text!");
			}
		} catch (TagException e) {
			e.printStackTrace();
		}
		Cache.get().getTagCache().getTagIdMap().put(newTag.getTagId(), newTag);
		Cache.get().getTagCache().getTagTextMap().put(newTag.getTagText(), newTag);

		// Update max note id in tag cache.
		if (Cache.get().getTagCache().getMaxTagId() < newTag.getTagId()) {
			Cache.get().getTagCache().setMaxTagId(newTag.getTagId());
		}

		return newTag;
	}

}

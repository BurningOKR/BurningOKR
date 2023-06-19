package org.burningokr.service.okr;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.repositories.okr.NoteKeyResultRepository;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class NoteService {

  private NoteRepository noteRepository;
  private NoteKeyResultRepository noteKeyResultRepository;
  private ActivityService activityService;

  @Autowired
  public NoteService(
    NoteRepository noteRepository,
    NoteKeyResultRepository noteKeyResultRepository,
    ActivityService activityService
  ) {
    this.noteRepository = noteRepository;
    this.noteKeyResultRepository = noteKeyResultRepository;
    this.activityService = activityService;
  }

  public Note findById(Long noteId) {
    return noteRepository.findByIdOrThrow(noteId);
  }

  public Note findByIdExtendedRepositories(Long noteId) {
    Optional<NoteKeyResult> noteKeyResult = noteKeyResultRepository.findById(noteId);
    if (noteKeyResult.isPresent()) {
      return noteKeyResult.get();
    }

    return null;
  }

  /**
   * Updates a Note.
   *
   * @param updatedNote a {@link Note} object
   * @return a {@link Note} object
   */
  @Transactional
  public Note updateNote(Note updatedNote) {
    Note referencedNote = findById(updatedNote.getId());
    referencedNote.setText(updatedNote.getText());
    referencedNote = noteRepository.save(referencedNote);
    activityService.createActivity(referencedNote, Action.EDITED);
    return referencedNote;
  }

  /**
   * Deletes a Note.
   *
   * @param noteId a long value
   */
  @Transactional
  public void deleteNote(Long noteId) {
    Note note = findById(noteId);

    noteRepository.deleteById(noteId);

    activityService.createActivity(note, Action.DELETED);
    log.info("Deleted Note with id " + noteId);
  }
}

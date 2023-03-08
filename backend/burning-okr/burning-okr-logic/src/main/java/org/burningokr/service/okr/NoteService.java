package org.burningokr.service.okr;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.okr.NoteKeyResultRepository;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NoteService {

  private final Logger logger = LoggerFactory.getLogger(NoteService.class);

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
   * @param IUser       an {@link IUser} object
   * @return a {@link Note} object
   */
  @Transactional
  public Note updateNote(Note updatedNote, IUser IUser) {
    Note referencedNote = findById(updatedNote.getId());
    referencedNote.setText(updatedNote.getText());
    referencedNote = noteRepository.save(referencedNote);
    activityService.createActivity(IUser, referencedNote, Action.EDITED);
    return referencedNote;
  }

  /**
   * Deletes a Note.
   *
   * @param noteId a long value
   * @param IUser  an {@link IUser} object
   */
  @Transactional
  public void deleteNote(Long noteId, IUser IUser) {
    Note note = findById(noteId);

    noteRepository.deleteById(noteId);

    activityService.createActivity(IUser, note, Action.DELETED);
    logger.info("Deleted Note with id " + noteId);
  }
}

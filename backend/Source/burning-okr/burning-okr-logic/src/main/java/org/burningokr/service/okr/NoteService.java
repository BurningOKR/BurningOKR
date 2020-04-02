package org.burningokr.service.okr;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Note;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {

  private final Logger logger = LoggerFactory.getLogger(NoteService.class);

  private NoteRepository noteRepository;
  private ActivityService activityService;

  @Autowired
  public NoteService(NoteRepository noteRepository, ActivityService activityService) {
    this.noteRepository = noteRepository;
    this.activityService = activityService;
  }

  public Note findById(Long noteId) {
    return noteRepository.findByIdOrThrow(noteId);
  }

  /**
   * Updates a Note.
   *
   * @param updatedNote a {@link Note} object
   * @param user an {@link User} object
   * @return a {@link Note} object
   */
  @Transactional
  public Note updateNote(Note updatedNote, User user) {
    Note referencedNote = findById(updatedNote.getId());
    referencedNote.setText(updatedNote.getText());
    referencedNote = noteRepository.save(referencedNote);
    activityService.createActivity(user, referencedNote, Action.EDITED);
    return referencedNote;
  }

  /**
   * Deletes a Note.
   *
   * @param noteId a long value
   * @param user an {@link User} object
   */
  @Transactional
  public void deleteNote(Long noteId, User user) {
    Note note = findById(noteId);

    noteRepository.deleteById(noteId);

    activityService.createActivity(user, note, Action.DELETED);
    logger.info("Deleted Note with id " + noteId);
  }
}

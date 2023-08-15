package org.burningokr.service.okr;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.userhandling.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

  private static Long originalId;
  private static UUID originalUserId;
  private static String originalText;
  private static String changedText;
  @Mock
  private NoteRepository noteRepository;
  @Mock
  private ActivityService activityService;
  @InjectMocks
  private NoteService noteService;
  private Note originalNote;
  private Note changedNote;

  @Mock
  private User authorizedIUser;

  @BeforeAll
  public static void init() {
    originalId = 100L;
    originalUserId = UUID.randomUUID();
    originalText = "OriginalText";
    changedText = "ChangedText";
  }

  @BeforeEach
  public void refresh() {
    originalNote = createOriginalNote();
    changedNote = createOriginalNote();
    changedNote.setText(changedText);
  }

  private Note createOriginalNote() {
    Note createdNote = new Note();
    createdNote.setId(originalId);
    createdNote.setUserId(originalUserId);
    createdNote.setText(originalText);
    return createdNote;
  }

  @Test
  public void findById_expectedNotFoundException() {
    when(noteRepository.findByIdOrThrow(originalId)).thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      noteService.findById(originalId);
    });

  }

  @Test
  public void updateNote_expectedUserNotifications() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    noteService.updateNote(changedNote);

    verify(activityService).createActivity(null, Action.EDITED);
  }

  @Test
  public void updateNote_expectedRepositorySave() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    ArgumentCaptor<Note> capturedNotes = ArgumentCaptor.forClass(Note.class);
    noteService.updateNote(changedNote);

    verify(noteRepository).save(capturedNotes.capture());
    Note capturedNote = capturedNotes.getValue();
    assertEquals(changedText, capturedNote.getText());
    assertEquals(originalId, capturedNote.getId());
    assertEquals(originalUserId, capturedNote.getUserId());
  }

  @Test
  public void updateNote_expectedCreatedActivity() {
    Note returnedNote = new Note();

    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);
    when(noteRepository.save(any())).thenReturn(returnedNote);

    ArgumentCaptor<Note> capturedNotes = ArgumentCaptor.forClass(Note.class);
    ArgumentCaptor<Action> capturedActions = ArgumentCaptor.forClass(Action.class);
    noteService.updateNote(changedNote);

    verify(activityService)
      .createActivity(capturedNotes.capture(), capturedActions.capture());
    assertEquals(returnedNote, capturedNotes.getValue());
    assertEquals(Action.EDITED, capturedActions.getValue());
  }

  @Test
  public void updateNote_expectedCorrectReturn() {
    Note returnedNote = new Note();

    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);
    when(noteRepository.save(any())).thenReturn(returnedNote);

    Note actualNote = noteService.updateNote(changedNote);

    assertEquals(actualNote, returnedNote);
  }

  @Test
  public void deleteNote_expectedDeleteCall() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    noteService.deleteNote(originalId);

    verify(noteRepository).deleteById(originalId);
  }

  @Test
  public void deleteNote_expectedActivityCall() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    noteService.deleteNote(originalId);

    verify(activityService).createActivity(originalNote, Action.DELETED);
  }
}

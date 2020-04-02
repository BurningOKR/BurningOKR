package org.burningokr.service.okr;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.userhandling.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceTest {

  @Mock private NoteRepository noteRepository;
  @Mock private ActivityService activityService;

  @Mock private UserService userService;

  @InjectMocks private NoteService noteService;

  private static Long originalId;
  private static UUID originalUserId;
  private static String originalText;
  private static KeyResult originalParentKeyResult;
  private static Long changedId;
  private static UUID changedUserId;
  private static String changedText;

  private Note originalNote;
  private Note changedNote;

  @Mock private User authorizedUser;

  @BeforeClass
  public static void init() {
    originalId = 100L;
    changedId = 200L;
    originalUserId = UUID.randomUUID();
    changedUserId = UUID.randomUUID();
    originalText = "OriginalText";
    changedText = "ChangedText";
    originalParentKeyResult = new KeyResult();
  }

  @Before
  public void refresh() {
    originalNote = createOriginalNote();
    changedNote = createOriginalNote();
    changedNote.setText(changedText);
  }

  private Note createOriginalNote() {
    Note createdNote = new Note();
    createdNote.setId(originalId);
    createdNote.setUserId(originalUserId);
    createdNote.setParentKeyResult(originalParentKeyResult);
    createdNote.setText(originalText);
    return createdNote;
  }

  @Test(expected = EntityNotFoundException.class)
  public void findById_expectedNotFoundException() {
    when(noteRepository.findByIdOrThrow(originalId)).thenThrow(new EntityNotFoundException());

    noteService.findById(originalId);
  }

  @Test
  public void updateNote_expectedUserNotifications() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    noteService.updateNote(changedNote, authorizedUser);

    verify(activityService).createActivity(authorizedUser, null, Action.EDITED);
  }

  @Test
  public void updateNote_expectedRepositorySave() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    ArgumentCaptor<Note> capturedNotes = ArgumentCaptor.forClass(Note.class);
    noteService.updateNote(changedNote, authorizedUser);

    verify(noteRepository).save(capturedNotes.capture());
    Note capturedNote = capturedNotes.getValue();
    Assert.assertEquals(changedText, capturedNote.getText());
    Assert.assertEquals(originalId, capturedNote.getId());
    Assert.assertEquals(originalUserId, capturedNote.getUserId());
    Assert.assertEquals(originalParentKeyResult, capturedNote.getParentKeyResult());
  }

  @Test
  public void updateNote_expectedCreatedActivity() {
    Note returnedNote = new Note();

    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);
    when(noteRepository.save(any())).thenReturn(returnedNote);

    ArgumentCaptor<User> capturedUsers = ArgumentCaptor.forClass(User.class);
    ArgumentCaptor<Note> capturedNotes = ArgumentCaptor.forClass(Note.class);
    ArgumentCaptor<Action> capturedActions = ArgumentCaptor.forClass(Action.class);
    noteService.updateNote(changedNote, authorizedUser);

    verify(activityService)
        .createActivity(
            capturedUsers.capture(), capturedNotes.capture(), capturedActions.capture());
    Assert.assertEquals(authorizedUser, capturedUsers.getValue());
    Assert.assertEquals(returnedNote, capturedNotes.getValue());
    Assert.assertEquals(Action.EDITED, capturedActions.getValue());
  }

  @Test
  public void updateNote_expectedCorrectReturn() {
    Note returnedNote = new Note();

    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);
    when(noteRepository.save(any())).thenReturn(returnedNote);

    Note actualNote = noteService.updateNote(changedNote, authorizedUser);

    Assert.assertEquals(actualNote, returnedNote);
  }

  @Test
  public void deleteNote_expectedDeleteCall() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    noteService.deleteNote(originalId, authorizedUser);

    verify(noteRepository).deleteById(originalId);
  }

  @Test
  public void deleteNote_expectedActivityCall() {
    when(noteRepository.findByIdOrThrow(originalId)).thenReturn(originalNote);

    noteService.deleteNote(originalId, authorizedUser);

    verify(activityService).createActivity(authorizedUser, originalNote, Action.DELETED);
  }
}

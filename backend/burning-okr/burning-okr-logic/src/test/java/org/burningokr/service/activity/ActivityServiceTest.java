package org.burningokr.service.activity;

import org.burningokr.model.users.IUser;
import org.burningokr.repositories.activity.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

  @Mock
  ActivityRepository activityRepository;
  IUser IUser;
  @InjectMocks
  ActivityService activityService;
  private UUID uuid;
  private String userPrincipalName;

  @BeforeEach
  public void init() {
    uuid = UUID.randomUUID();
    IUser = mock(IUser.class);

    when(IUser.getId()).thenReturn(uuid);
  }
  // TODO fix test

//
//  private void testActivityCreation(UUID userId, String userPrincipalName, Action expectedAction) {
//    ArgumentCaptor<Activity> capturedActivityArgument = ArgumentCaptor.forClass(Activity.class);
//    MockTrackable expectedTrackable = new MockTrackable();
//    String expectedUserId = userId + " (" + userPrincipalName + ")";
//    String expectedObject =
//      expectedTrackable.getClass().getSimpleName()
//        + " - "
//        + expectedTrackable.getName()
//        + " (id:"
//        + expectedTrackable.getId()
//        + ")";
//
//    activityService.createActivity(expectedTrackable, expectedAction);
//
//    verify(activityRepository).save(capturedActivityArgument.capture());
//    Activity capturedActivity = capturedActivityArgument.getValue();
//
//    assertEquals(expectedUserId, capturedActivity.getUserId());
//    assertEquals(expectedObject, capturedActivity.getObject());
//    assertEquals(expectedAction, capturedActivity.getAction());
//    boolean isTimeSimiliarEnough =
//      LocalDateTime.now().minusMinutes(1).isBefore(capturedActivity.getDate());
//    assertTrue(isTimeSimiliarEnough);
//  }
// TODO fix test

//  @Test
//  public void createActivity_exampleActivity1_expectedCorrectActivitySave() {
//    userPrincipalName = "test2";
//    when(IUser.getMail()).thenReturn(userPrincipalName);
//    testActivityCreation(uuid, userPrincipalName, Action.CREATED);
//  }
// TODO fix test

//  @Test
//  public void createActivity_exampleActivity2_expectedCorrectActivitySave() {
//    userPrincipalName = "test4";
//    when(IUser.getMail()).thenReturn(userPrincipalName);
//    testActivityCreation(uuid, userPrincipalName, Action.DELETED);
//  }
//
//  private class MockTrackable implements Trackable {
//    @Override
//    public String getName() {
//      return "TestName";
//    }
//
//    @Override
//    public Object getId() {
//      return "TestId";
//    }
//  }
}

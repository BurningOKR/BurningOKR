package org.burningokr.service.activity;

import org.burningokr.model.activity.Action;
import org.burningokr.model.activity.Activity;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.users.User;
import org.burningokr.repositories.activity.ActivityRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceTest {

  @Mock
  ActivityRepository activityRepository;
  User user;
  @InjectMocks
  ActivityService activityService;
  private UUID uuid;
  private String userPrincipalName;

  @Before
  public void init() {
    uuid = UUID.randomUUID();
    user = mock(User.class);

    when(user.getId()).thenReturn(uuid);
  }

  private void testActivityCreation(UUID userId, String userPrincipalName, Action expectedAction) {
    ArgumentCaptor<Activity> capturedActivityArgument = ArgumentCaptor.forClass(Activity.class);
    MockTrackable expectedTrackable = new MockTrackable();
    String expectedUserId = userId + " (" + userPrincipalName + ")";
    String expectedObject =
      expectedTrackable.getClass().getSimpleName()
        + " - "
        + expectedTrackable.getName()
        + " (id:"
        + expectedTrackable.getId()
        + ")";

    activityService.createActivity(user, expectedTrackable, expectedAction);

    verify(activityRepository).save(capturedActivityArgument.capture());
    Activity capturedActivity = capturedActivityArgument.getValue();

    Assert.assertEquals(expectedUserId, capturedActivity.getUserId());
    Assert.assertEquals(expectedObject, capturedActivity.getObject());
    Assert.assertEquals(expectedAction, capturedActivity.getAction());
    boolean isTimeSimiliarEnough =
      LocalDateTime.now().minusMinutes(1).isBefore(capturedActivity.getDate());
    Assert.assertTrue(isTimeSimiliarEnough);
  }

  @Test
  public void createActivity_exampleActivity1_expectedCorrectActivitySave() {
    userPrincipalName = "test2";
    when(user.getMail()).thenReturn(userPrincipalName);
    testActivityCreation(uuid, userPrincipalName, Action.CREATED);
  }

  @Test
  public void createActivity_exampleActivity2_expectedCorrectActivitySave() {
    userPrincipalName = "test4";
    when(user.getMail()).thenReturn(userPrincipalName);
    testActivityCreation(uuid, userPrincipalName, Action.DELETED);
  }

  private class MockTrackable implements Trackable {
    @Override
    public String getName() {
      return "TestName";
    }

    @Override
    public Object getId() {
      return "TestId";
    }
  }
}

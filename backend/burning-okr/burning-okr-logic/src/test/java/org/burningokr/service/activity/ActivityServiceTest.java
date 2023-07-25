package org.burningokr.service.activity;

import org.burningokr.model.activity.Action;
import org.burningokr.model.activity.Activity;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.users.User;
import org.burningokr.repositories.activity.ActivityRepository;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextServiceKeycloak;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

  @Mock
  ActivityRepository activityRepository;
  @Mock
  AuthenticationUserContextServiceKeycloak authUserContextService;

  @InjectMocks
  ActivityService activityService;

  @Test
  public void createActivity_exampleActivity1_expectedCorrectActivitySave() {
    String userPrincipalName = "test2";
    UUID uuid = UUID.randomUUID();

    User user = new User();
    user.setMail(userPrincipalName);
    user.setId(uuid);

    when(authUserContextService.getAuthenticatedUser()).thenReturn(user);

    testActivityCreation(uuid, userPrincipalName, Action.CREATED);
  }

  @Test
  public void createActivity_exampleActivity2_expectedCorrectActivitySave() {
    String userPrincipalName = "test4";
    UUID uuid = UUID.randomUUID();

    User user = new User();
    user.setMail(userPrincipalName);
    user.setId(uuid);

    when(authUserContextService.getAuthenticatedUser()).thenReturn(user);

    testActivityCreation(uuid, userPrincipalName, Action.DELETED);
  }

  private void testActivityCreation(UUID userId, String userPrincipalName, Action expectedAction) {
    MockTrackable expectedTrackable = new MockTrackable();
    String expectedUserId = userId + " (" + userPrincipalName + ")";
    String expectedObject = getExpectedObjectFormat(expectedTrackable);

    activityService.createActivity(expectedTrackable, expectedAction);

    Activity capturedActivity = captureActivity();

    assertEquals(expectedUserId, capturedActivity.getUserId());
    assertEquals(expectedObject, capturedActivity.getObject());
    assertEquals(expectedAction, capturedActivity.getAction());
    assertTimeIsSimilarEnough(capturedActivity);
  }

  private Activity captureActivity() {
    ArgumentCaptor<Activity> capturedActivity = ArgumentCaptor.forClass(Activity.class);
    verify(activityRepository).save(capturedActivity.capture());

    return capturedActivity.getValue();
  }

  private void assertTimeIsSimilarEnough(Activity capturedActivity) {
    boolean isTimeSimiliarEnough =
            LocalDateTime.now().minusMinutes(1).isBefore(capturedActivity.getDate());
    assertTrue(isTimeSimiliarEnough);
  }

  private String getExpectedObjectFormat(MockTrackable expectedTrackable) {
    return expectedTrackable.getClass().getSimpleName()
                    + " - "
                    + expectedTrackable.getName()
                    + " (id:"
                    + expectedTrackable.getId()
                    + ")";
  }

  private class MockTrackable implements Trackable<Object> {
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

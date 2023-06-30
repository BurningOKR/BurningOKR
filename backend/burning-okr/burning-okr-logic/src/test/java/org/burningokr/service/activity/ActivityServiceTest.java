package org.burningokr.service.activity;

import org.burningokr.model.activity.Action;
import org.burningokr.model.activity.Activity;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.users.User;
import org.burningokr.repositories.activity.ActivityRepository;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

  @Mock
  ActivityRepository activityRepository;
  @InjectMocks
  ActivityService activityService;
  @Mock
  AuthorizationUserContextService authorizationUserContextService;
  private UUID uuid;
  private String userPrincipalName;

  @Captor
  ArgumentCaptor<Activity> capturedActivityArgument;

  @BeforeEach
  public void init() {
    uuid = UUID.randomUUID();
    User authenticatedMockUser = new User();
    authenticatedMockUser.setId(uuid);
    authenticatedMockUser.setGivenName("Max");
    authenticatedMockUser.setSurname("Mustermann");
    when(authorizationUserContextService.getAuthenticatedUser()).thenReturn(authenticatedMockUser);
  }

  private void testActivityCreation(UUID userId, String userPrincipalName, Action expectedAction) {
//    ArgumentCaptor<Activity> capturedActivityArgument = ArgumentCaptor.forClass(Activity.class);
    MockTrackable expectedTrackable = new MockTrackable();
    String expectedUserId = userId + " (" + userPrincipalName + ")";
    String expectedObject =
      expectedTrackable.getClass().getSimpleName()
        + " - "
        + expectedTrackable.getName()
        + " (id:"
        + expectedTrackable.getId()
        + ")";

    activityService.createActivity(expectedTrackable, expectedAction);
//    activityRepository.findById(userId);

    verify(activityRepository).save(capturedActivityArgument.capture());
    Activity capturedActivity = capturedActivityArgument.getValue();

    assertEquals(expectedUserId, capturedActivity.getUserId());
//    assertEquals(expectedObject, capturedActivity.getObject());
//    assertEquals(expectedAction, capturedActivity.getAction());
//    boolean isTimeSimiliarEnough =
//      LocalDateTime.now().minusMinutes(1).isBefore(capturedActivity.getDate());
//    assertTrue(isTimeSimiliarEnough);
  }

  @Test
  public void createActivity_exampleActivity1_expectedCorrectActivitySave() {
    userPrincipalName = "test2";
    testActivityCreation(uuid, userPrincipalName, Action.CREATED);
  }

  @Test
  public void createActivity_exampleActivity2_expectedCorrectActivitySave() {
    userPrincipalName = "test4";
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

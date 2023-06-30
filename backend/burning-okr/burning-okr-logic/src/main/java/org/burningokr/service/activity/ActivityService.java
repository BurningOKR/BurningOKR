package org.burningokr.service.activity;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.activity.Activity;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.users.User;
import org.burningokr.repositories.activity.ActivityRepository;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityService {
  private final ActivityRepository activityRepository;
  private final AuthorizationUserContextService authorizationUserContextService;

  /**
   * Creates an Activity.
   *
   * @param t      a {@link T} object
   * @param action an {@link Action} object
   * @param <T>    generic Type extends Trackable Long
   */
  public <T extends Trackable<?>> void createActivity(T t, Action action) {
    User authenticatedUser = authorizationUserContextService.getAuthenticatedUser();
    Activity activity = new Activity();
    System.out.println(activity.getUserId());
    activity.setUserId(authenticatedUser.getId() + " (" + authenticatedUser.getMail() + ")");
    System.out.println(activity.getUserId());
    activity.setObject(
      t.getClass().getSimpleName() + " - " + t.getName() + " (id:" + t.getId() + ")");
    activity.setAction(action);
    activity.setDate(LocalDateTime.now());
    System.out.println(activity);
    activityRepository.save(activity);
  }
}

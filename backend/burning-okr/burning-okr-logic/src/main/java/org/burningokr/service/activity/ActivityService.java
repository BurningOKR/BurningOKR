package org.burningokr.service.activity;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.activity.Activity;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.users.User;
import org.burningokr.repositories.activity.ActivityRepository;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityService {
  private final ActivityRepository activityRepository;
  private final AuthenticationUserContextService authenticationUserContextService;

  /**
   * Creates an Activity.
   *
   * @param t      a {@link T} object
   * @param action an {@link Action} object
   * @param <T>    generic Type extends Trackable Long
   */

  //FIXME: (C.C.) activity is not created, to update the task in the taskboard
  public <T extends Trackable<?>> void createActivity(T t, Action action) {
    User authenticatedUser = authenticationUserContextService.getAuthenticatedUser();
    Activity activity = new Activity();
    activity.setUserId(authenticatedUser.getId() + " (" + authenticatedUser.getMail() + ")");
    activity.setObject(
      t.getClass().getSimpleName() + " - " + t.getName() + " (id:" + t.getId() + ")");
    activity.setAction(action);
    activity.setDate(LocalDateTime.now());
    activityRepository.save(activity);
  }
}

package org.burningokr.service.activity;

import org.burningokr.model.activity.Action;
import org.burningokr.model.activity.Activity;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.activity.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityService {

  private ActivityRepository activityRepository;

  @Autowired
  public ActivityService(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  /**
   * Creates an Activity.
   *
   * @param IUser  an {@link IUser} object
   * @param t      a {@link T} object
   * @param action an {@link Action} object
   * @param <T>    generic Type extends Trackable Long
   */
  public <T extends Trackable<?>> void createActivity(IUser IUser, T t, Action action) {
    Activity activity = new Activity();
    activity.setUserId(IUser.getId() + " (" + IUser.getMail() + ")");
    activity.setObject(
      t.getClass().getSimpleName() + " - " + t.getName() + " (id:" + t.getId() + ")");
    activity.setAction(action);
    activity.setDate(LocalDateTime.now());
    activityRepository.save(activity);
  }
}

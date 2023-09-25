package org.burningokr.model.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActivityTest {

  private Activity activity;
  private Long id;
  private LocalDateTime date;
  @Mock
  private Action action;
  private String userId;
  @Mock
  private String object;
  @BeforeEach
  public void setUp() {
    id = 1L;
    date = LocalDateTime.now();
    userId = "UUID + (User Principal Name)";

    activity = new Activity();
    activity.setId(id);
    activity.setDate(date);
    activity.setAction(action);
    activity.setUserId(userId);
    activity.setObject(object);
  }

  @Test
  public void getId_shouldReturnId() {
    assertEquals(id, activity.getId());
  }
  @Test
  public void getDate_shouldReturnDate() {
    assertEquals(date, activity.getDate());
  }

  @Test
  public void getAction_shouldReturnAction() {
    assertEquals(action, activity.getAction());
  }

  @Test
  public void getUserId_shouldReturnUserId() {
    assertEquals(userId, activity.getUserId());
  }

  @Test
  public void getObject_shouldReturnObject() {
    assertEquals(object, activity.getObject());
  }

}

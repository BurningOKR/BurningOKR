package org.burningokr.model.monitoring;

import org.burningokr.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserIdTest {

  UserId userId;

  @Mock
  User user;
  @BeforeEach
  public void init() {
    userId = new UserId(user);
  }

  @Test
  public void hashCode_shouldReturnHashCodeByUserId() {
    assertEquals(Objects.hash(userId.getUserId()), userId.hashCode());
  }
}

package org.burningokr.service.userhandling;

import org.burningokr.model.users.User;
import org.burningokr.repositories.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @InjectMocks
  UserService userService;
  @Mock
  private UserRepository mockedUserRepository;
  @Mock
  private User mockedUser;
  private User user;
  @Mock
  private List<User> mockedUsersList;
  @BeforeEach
  public void setUp() {
    user = new User();
    user.setActive(true);
    user.setId(UUID.randomUUID());

    userService = new UserService(mockedUserRepository);

  }

  @Test
  public void updateUser_shouldUpdateUser() {
    //Arrange
    doReturn(mockedUser).when(mockedUserRepository).save(any());

    //Act
    var result = userService.updateUser(mockedUser);

    //Assert
    assertEquals(mockedUser, result);
    verify(mockedUserRepository).save(mockedUser);
  }

  @Test
  public void findAll_shouldReturnAllUsers() {
    //Arrange
    doReturn(mockedUsersList).when(mockedUserRepository).findAll();

    //Act
    var result = userService.findAll();

    //Assert
    assertEquals(mockedUsersList, result);
  }

  @Test
  public void findAllActive_shouldReturnOnlyActiveUsers() {
    //Arrange
    doReturn(mockedUsersList).when(mockedUserRepository).findAllByActiveIsTrue();

    //Act
    var result = userService.findAllActive();

    //Assert
    assertEquals(mockedUsersList, result);
    verify(mockedUserRepository).findAllByActiveIsTrue();
  }

  @Test
  public void findAllInactive_shouldReturnOnlyInactiveUsers() {
    //Arrange
    doReturn(mockedUsersList).when(mockedUserRepository).findAllByActiveIsFalse();

    //Act
    var result = userService.findAllInactive();

    //Assert
    assertEquals(mockedUsersList, result);
    verify(mockedUserRepository).findAllByActiveIsFalse();
  }

  @Test
  public void findById_shouldFindUser() {
    //Arrange
    doReturn(Optional.of(user)).when(mockedUserRepository).findById(user.getId());

    //Act
    var result = userService.findById(user.getId());

    //Assert
    assertEquals(user, result.get());
    verify(mockedUserRepository).findById(user.getId());
  }
}

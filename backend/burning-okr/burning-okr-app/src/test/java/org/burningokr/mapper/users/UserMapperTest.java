package org.burningokr.mapper.users;

import org.burningokr.dto.users.UserDto;
import org.burningokr.model.users.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
  private static User user1;
  private static User user2;

  private static UserDto userDto1;
  private static UserDto userDto2;

  @InjectMocks
  public UserMapper userMapper;

  @BeforeAll
  public static void initClass() {
    LocalDateTime localDateTime = LocalDate.of(2022, 12, 2).atStartOfDay();
    LocalDateTime localDateTime2 = LocalDate.of(2023, 5, 3).atStartOfDay();

    UUID uuid = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();

    user1 = new User();
    user1.setId(uuid);
    user1.setGivenName("Name");
    user1.setSurname("Surname");
    user1.setMail("example@company.com");
    user1.setJobTitle("JobTitle");
    user1.setDepartment("OkrDepartment");
    user1.setPhoto("Photo");
    user1.setActive(true);
    user1.setAdmin(true);
    user1.setCreatedAt(localDateTime);

    userDto1 = new UserDto();
    userDto1.setId(uuid);
    userDto1.setGivenName("Name");
    userDto1.setSurname("Surname");
    userDto1.setMail("example@company.com");
    userDto1.setJobTitle("JobTitle");
    userDto1.setDepartment("OkrDepartment");
    userDto1.setPhoto("Photo");
    userDto1.setActive(true);
    userDto1.setAdmin(true);

    user2 = new User();
    user2.setId(uuid2);
    user2.setGivenName("Name2");
    user2.setSurname("Surname2");
    user2.setMail("example2@company.com");
    user2.setJobTitle("JobTitle2");
    user2.setDepartment("OkrDepartment2");
    user2.setPhoto("Photo2");
    user2.setActive(false);
    user2.setAdmin(false);
    user2.setCreatedAt(localDateTime2);

    userDto2 = new UserDto();
    userDto2.setId(uuid2);
    userDto2.setGivenName("Name2");
    userDto2.setSurname("Surname2");
    userDto2.setMail("example2@company.com");
    userDto2.setJobTitle("JobTitle2");
    userDto2.setDepartment("OkrDepartment2");
    userDto2.setPhoto("Photo2");
    userDto2.setActive(false);
    userDto2.setAdmin(false);
  }

  @Test
  public void test_mapEntityToDto_shouldMapCorrectly() {
    //Arrange
    UserDto expected = userDto1;

    //Act
    UserDto actual = userMapper.mapEntityToDto(user1);

    //Assert
    assertEquals(expected, actual);
  }

  @Test
  public void test_mapEntitiesToDtos_shouldMapCorrectly() {
    //Arrange
    Collection<User> users = Arrays.asList(user1, user2);
    Collection<UserDto> expected = Arrays.asList(userDto1, userDto2);

    //Act
    Collection<UserDto> actual = userMapper.mapEntitiesToDtos(users);

    //Assert
    assertEquals(expected, actual);
  }

  @Test
  public void test_mapDtoToEntity_shouldMapCorrectly() {
    //Arrange
    User expected = user1;
    expected.setCreatedAt(null);

    //Act
    User actual = userMapper.mapDtoToEntity(userDto1);

    //Assert
    assertEquals(expected, actual);
  }

  @Test
  public void test_mapDtosToEntities_shouldMapCorrectly() {
    //Arrange
    Collection<UserDto> userDtos = Arrays.asList(userDto1, userDto2);
    Collection<User> expected = Arrays.asList(user1, user2);
    expected.forEach(user -> user.setCreatedAt(null));

    //Act
    Collection<User> actual = userMapper.mapDtosToEntities(userDtos);

    //Assert
    assertEquals(expected, actual);
  }

}

//package org.burningokr.mapper.users;
//
//import org.burningokr.dto.users.UserDto;
//import org.burningokr.model.users.LocalUser;
//import org.burningokr.model.users.User;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserMapperTest {
//  private static User user1;
//  private static User user2;
//
//  private static UserDto userDto1;
//  private static UserDto userDto2;
//
//  @Mock
//  public AppEnvironment appEnvironment;
//
//  @InjectMocks
//  public UserMapper userMapper;
//
//  @BeforeAll
//  public static void initClass() {
//    user1 = mock(User.class);
//    when(user1.getId()).thenReturn(UUID.randomUUID());
//    when(user1.getMail()).thenReturn("example@company.com");
//    when(user1.getGivenName()).thenReturn("Name");
//    when(user1.getSurname()).thenReturn("Surname");
//    when(user1.getDepartment()).thenReturn("OkrDepartment");
//    when(user1.getJobTitle()).thenReturn("JobTitle");
//    when(user1.getPhoto()).thenReturn("Photo");
//    user2 = mock(User.class);
//    when(user2.getId()).thenReturn(UUID.randomUUID());
//    when(user2.getMail()).thenReturn("example2@company.com");
//    when(user2.getGivenName()).thenReturn("Name2");
//    when(user2.getSurname()).thenReturn("Surname2");
//    when(user2.getDepartment()).thenReturn("Department2");
//    when(user2.getJobTitle()).thenReturn("JobTitle2");
//    when(user2.getPhoto()).thenReturn("Photo2");
//
//    userDto1 = new UserDto();
//    userDto1.setId(UUID.randomUUID());
//    userDto1.setEmail("example@company.com");
//    userDto1.setGivenName("Name");
//    userDto1.setSurname("Surname");
//    userDto1.setDepartment("OkrDepartment");
//    userDto1.setJobTitle("JobTitle");
//    userDto1.setPhoto("Photo");
//    userDto2 = new UserDto();
//    userDto2.setId(UUID.randomUUID());
//    userDto2.setGivenName("Given Name");
//    userDto2.setSurname("Surname 2");
//    userDto2.setEmail("mail@example.com");
//    userDto2.setDepartment("Department2");
//    userDto2.setJobTitle("JobTitle2");
//    userDto2.setPhoto("Photo2");
//  }
//
//  @Test
//  public void test_mapDtoToEntity_aadUser_ShouldMapCorrectly() {
//    when(appEnvironment.getAuthMode()).thenReturn(AuthModes.AZURE);
//    User user = userMapper.mapDtoToEntity(userDto1);
//    assertEquals(user.getClass(), AadUser.class);
//    assertDtoWithUser(userDto1, user);
//  }
//
//  @Test
//  public void test_mapDtoToEntity_localUser_ShouldMapCorrectly() {
//    when(appEnvironment.getAuthMode()).thenReturn(AuthModes.LOCAL);
//    User user = userMapper.mapDtoToEntity(userDto1);
//    assertEquals(user.getClass(), LocalUser.class);
//    assertDtoWithUser(userDto1, user);
//  }
//
//  @Test
//  public void test_mapEntityToDtoShouldMapCorrectly() {
//    userDto1 = userMapper.mapEntityToDto(user1);
//    assertUserWithDto(user1, userDto1);
//  }
//
//  @Test
//  public void test_mapEntitiesToDtos() {
//    Collection<User> users = Arrays.asList(user1, user2);
//    Collection<UserDto> dtos = userMapper.mapEntitiesToDtos(users);
//    assertEquals(2, users.size());
//    assertEquals(users.size(), dtos.size());
//    Iterator<User> userIt = users.iterator();
//    Iterator<UserDto> dtoIt = dtos.iterator();
//    while (userIt.hasNext() && dtoIt.hasNext()) {
//      assertUserWithDto(userIt.next(), dtoIt.next());
//    }
//  }
//
//  @Test
//  public void test_mapDtosToEntities() {
//    when(appEnvironment.getAuthMode()).thenReturn(AuthModes.AZURE);
//    Collection<UserDto> dtos = Arrays.asList(userDto1, userDto2);
//    Collection<User> users = userMapper.mapDtosToEntities(dtos);
//    assertEquals(2, dtos.size());
//    assertEquals(dtos.size(), users.size());
//    Iterator<UserDto> dtoIt = dtos.iterator();
//    Iterator<User> userIt = users.iterator();
//    while (dtoIt.hasNext() && userIt.hasNext()) {
//      assertDtoWithUser(dtoIt.next(), userIt.next());
//    }
//  }
//
//  private void assertDtoWithUser(UserDto expected, User actual) {
//    assertEquals(expected.getId(), actual.getId());
//    assertEquals(expected.getEmail(), actual.getMail());
//    assertEquals(expected.getGivenName(), actual.getGivenName());
//    assertEquals(expected.getSurname(), actual.getSurname());
//    assertEquals(expected.getDepartment(), actual.getDepartment());
//    assertEquals(expected.getJobTitle(), actual.getJobTitle());
//    assertEquals(expected.getPhoto(), actual.getPhoto());
//  }
//
//  private void assertUserWithDto(User expected, UserDto actual) {
//    assertEquals(expected.getId(), actual.getId());
//    assertEquals(expected.getMail(), actual.getEmail());
//    assertEquals(expected.getGivenName(), actual.getGivenName());
//    assertEquals(expected.getSurname(), actual.getSurname());
//    assertEquals(expected.getDepartment(), actual.getDepartment());
//    assertEquals(expected.getJobTitle(), actual.getJobTitle());
//    assertEquals(expected.getPhoto(), actual.getPhoto());
//  }
//}

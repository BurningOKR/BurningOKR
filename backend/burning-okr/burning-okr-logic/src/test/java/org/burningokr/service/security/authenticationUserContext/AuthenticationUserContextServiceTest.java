package org.burningokr.service.security.authenticationUserContext;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authorization.InvalidTokenException;
import org.burningokr.service.userhandling.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationUserContextServiceTest {

  AuthenticationServiceTestImpl authenticationService;

  @Mock
  UserService userServiceMock;

  @Mock
  SystemProperties systemPropertiesMock;


  @BeforeEach
  void setUp() {
    this.authenticationService = spy(new AuthenticationServiceTestImpl(
      userServiceMock,
      systemPropertiesMock,
      "givenNameIdentifier",
      "familyNameIdentifier",
      "emailIdentifier"));
  }

  @Test
  void getAuthenticatedUser_shouldReturnUser() {
    //assemble
    UUID dummyUUID = UUID.randomUUID();
    User userMock = mock(User.class);
    authenticationService.userHashMap.put(dummyUUID, userMock);

    doReturn(dummyUUID).when(this.authenticationService).getUserIdFromContext();

    //act
    User result = this.authenticationService.getAuthenticatedUser();

    //assert
    Assertions.assertSame(userMock, result);
    Assertions.assertEquals(userMock, result);
  }

  @Test
  void getAuthenticatedUser_shouldThrowEntityNotFoundException() {
    //assemble
    doReturn(null).when(this.authenticationService).getUserIdFromContext();

    //act
    EntityNotFoundException exc = Assertions.assertThrows(EntityNotFoundException.class, () -> {
      this.authenticationService.getAuthenticatedUser();
    });

    //assert
    Assertions.assertEquals("user could not be found in hashmap", exc.getMessage());
  }

  @Test
  void checkIfStringIsEmpty_shouldDoNothing() {
    //assemble
    String attributeName = "someAttribute";
    String validatedString = "non empty string";

    //act + assert
    Assertions.assertDoesNotThrow(() -> {
      this.authenticationService.checkIfStringIsEmpty(attributeName, validatedString);
    });
  }

  @Test
  void checkIfStringIsEmpty_shouldThrowInvalidTokenException() {
    //assemble
    String attributeName = "someAttribute";
    String validatedString = "";

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationService.checkIfStringIsEmpty(attributeName, validatedString);
    });

    //assert
    Assertions.assertEquals("someAttribute attribute is empty", exc.getMessage());
  }

  @Test
  void getUserFromToken_shouldSetBasicFieldsOfUserCorrectly() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    UUID dummyUUID = UUID.randomUUID();
    List<String> userRoles = List.of();

    doReturn(dummyUUID).when(this.authenticationService).getUserIdFromToken(jwtMock);
    doReturn(userRoles).when(this.authenticationService).getRolesFromToken(jwtMock);
    doReturn("AdminRoleName").when(this.systemPropertiesMock).getOidcAdminGroupName();

    Map<String, Object> jwtClaims = new HashMap<>();
    jwtClaims.put(this.authenticationService.GIVEN_NAME_IDENTIFIER, "John");
    jwtClaims.put(this.authenticationService.FAMILY_NAME_IDENTIFIER, "Doe");
    jwtClaims.put(this.authenticationService.EMAIL_IDENTIFIER, "email");
    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    User result = this.authenticationService.getUserFromToken(jwtMock);
    LocalDateTime date = LocalDateTime.now();
    LocalDateTime startRange = date.minusSeconds(10);
    LocalDateTime endRange = date.plusSeconds(10);

    //assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(dummyUUID, result.getId());
    assertTrue(result.isActive());
    Assertions.assertEquals("John", result.getGivenName());
    Assertions.assertEquals("Doe", result.getSurname());
    Assertions.assertEquals("email", result.getMail());

    Assertions.assertTrue(result.getCreatedAt().isAfter(startRange));
    Assertions.assertTrue(result.getCreatedAt().isBefore(endRange));
  }

  @Test
  void getUserFromToken_shouldReturnAdminUser() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    UUID dummyUUID = UUID.randomUUID();
    String adminRoleName = "AdminRole";
    List<String> userRoles = List.of(adminRoleName);

    doReturn(dummyUUID).when(this.authenticationService).getUserIdFromToken(jwtMock);
    doReturn(userRoles).when(this.authenticationService).getRolesFromToken(jwtMock);
    doReturn(adminRoleName).when(this.systemPropertiesMock).getOidcAdminGroupName();

    doReturn(null).when(this.authenticationService).getAttributeFromJwt(jwtMock, this.authenticationService.GIVEN_NAME_IDENTIFIER);
    doReturn(null).when(this.authenticationService).getAttributeFromJwt(jwtMock, this.authenticationService.FAMILY_NAME_IDENTIFIER);
    doReturn(null).when(this.authenticationService).getAttributeFromJwt(jwtMock, this.authenticationService.EMAIL_IDENTIFIER);

    //act
    User result = this.authenticationService.getUserFromToken(jwtMock);

    //assert
    assertTrue(result.isAdmin());
  }

  @Test
  void getUserFromToken_shouldReturnNonAdminUser() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    UUID dummyUUID = UUID.randomUUID();
    String adminRoleName = "AdminRole";
    List<String> emptyUserRoles = List.of();

    doReturn(dummyUUID).when(this.authenticationService).getUserIdFromToken(jwtMock);
    doReturn(emptyUserRoles).when(this.authenticationService).getRolesFromToken(jwtMock);
    doReturn(adminRoleName).when(this.systemPropertiesMock).getOidcAdminGroupName();

    doReturn(null).when(this.authenticationService).getAttributeFromJwt(jwtMock, this.authenticationService.GIVEN_NAME_IDENTIFIER);
    doReturn(null).when(this.authenticationService).getAttributeFromJwt(jwtMock, this.authenticationService.FAMILY_NAME_IDENTIFIER);
    doReturn(null).when(this.authenticationService).getAttributeFromJwt(jwtMock, this.authenticationService.EMAIL_IDENTIFIER);

    //act
    User result = this.authenticationService.getUserFromToken(jwtMock);

    //assert
    assertFalse(result.isAdmin());
  }

  @Test
  void updateCachedAndDatabaseUser_shouldUpdateUserMapAndDatabase_whenCachedUserAndInputUserAreNotEqual() {
    //assemble
    User inputTokenUser = new User();
    inputTokenUser.setId(UUID.randomUUID());
    inputTokenUser.setGivenName("John Doe");
    User cachedUser = new User();
    inputTokenUser.setId(inputTokenUser.getId());
    inputTokenUser.setGivenName("Bob Ross");
    this.authenticationService.userHashMap.put(inputTokenUser.getId(), cachedUser);

    doReturn(false).when(this.authenticationService).isCachedUserEqualToTokenUser(cachedUser, inputTokenUser);
    doReturn(inputTokenUser).when(this.userServiceMock).updateUser(inputTokenUser);

    //act
    this.authenticationService.updateCachedAndDatabaseUser(inputTokenUser);

    //assert
    verify(this.userServiceMock, times(1)).updateUser(inputTokenUser);
    Assertions.assertSame(this.authenticationService.userHashMap.get(inputTokenUser.getId()), inputTokenUser);
    Assertions.assertEquals(this.authenticationService.userHashMap.get(inputTokenUser.getId()), inputTokenUser);
    verifyNoMoreInteractions(this.userServiceMock, this.systemPropertiesMock);
  }

  @Test
  void updateCachedAndDatabaseUser_shouldUpdateUserMapAndDatabase_whenCachedUserIsNull() {
    //assemble
    User inputTokenUser = new User();
    inputTokenUser.setId(UUID.randomUUID());
    User cachedUser = null;
    this.authenticationService.userHashMap.put(inputTokenUser.getId(), cachedUser);

    doReturn(inputTokenUser).when(this.userServiceMock).updateUser(inputTokenUser);

    //act
    this.authenticationService.updateCachedAndDatabaseUser(inputTokenUser);

    //assert
    verify(this.userServiceMock, times(1)).updateUser(inputTokenUser);
    Assertions.assertSame(this.authenticationService.userHashMap.get(inputTokenUser.getId()), inputTokenUser);
    Assertions.assertEquals(this.authenticationService.userHashMap.get(inputTokenUser.getId()), inputTokenUser);
    verifyNoMoreInteractions(this.userServiceMock, this.systemPropertiesMock);
  }

  @Test
  void updateCachedAndDatabaseUser_shouldDoNothing() {
    //assemble
    User inputTokenUser = new User();
    inputTokenUser.setId(UUID.randomUUID());
    inputTokenUser.setGivenName("John Doe");
    this.authenticationService.userHashMap.put(inputTokenUser.getId(), inputTokenUser);

    doReturn(true).when(this.authenticationService).isCachedUserEqualToTokenUser(inputTokenUser, inputTokenUser);

    //act
    this.authenticationService.updateCachedAndDatabaseUser(inputTokenUser);

    //assert
    verifyNoMoreInteractions(this.userServiceMock, this.systemPropertiesMock);
  }

  @Test
  void isCachedUserEqualToTokenUser_shouldBeTrue() {
    //assemble
    UUID uuid = UUID.randomUUID();
    String mail = "someone@something.com";
    String givenName = "John";
    String surname = "Doe";
    boolean isAdmin = true;

    User cachedUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    User tokenUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    //act + assert
    assertTrue(this.authenticationService.isCachedUserEqualToTokenUser(cachedUser, tokenUser));
  }

  @Test
  void isCachedUserEqualToTokenUser_shouldBeFalse_whenUUIDsAreDifferent() {
    //assemble
    UUID uuid = UUID.randomUUID();
    String mail = "someone@something.com";
    String givenName = "John";
    String surname = "Doe";
    boolean isAdmin = true;

    User cachedUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    User tokenUser = User.builder()
      .id(UUID.randomUUID())
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    //act + assert
    assertFalse(this.authenticationService.isCachedUserEqualToTokenUser(cachedUser, tokenUser));
  }

  @Test
  void isCachedUserEqualToTokenUser_shouldBeFalse_whenMailsAreDifferent() {
    //assemble
    UUID uuid = UUID.randomUUID();
    String mail = "someone@something.com";
    String givenName = "John";
    String surname = "Doe";
    boolean isAdmin = true;

    User cachedUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    User tokenUser = User.builder()
      .id(uuid)
      .mail("newaddress@something.com")
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    //act + assert
    assertFalse(this.authenticationService.isCachedUserEqualToTokenUser(cachedUser, tokenUser));
  }

  @Test
  void isCachedUserEqualToTokenUser_shouldBeFalse_whenGivenNamesAreDifferent() {
    //assemble
    UUID uuid = UUID.randomUUID();
    String mail = "someone@something.com";
    String givenName = "John";
    String surname = "Doe";
    boolean isAdmin = true;

    User cachedUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    User tokenUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName("Bob")
      .surname(surname)
      .admin(isAdmin)
      .build();

    //act + assert
    assertFalse(this.authenticationService.isCachedUserEqualToTokenUser(cachedUser, tokenUser));
  }

  @Test
  void isCachedUserEqualToTokenUser_shouldBeFalse_whenSurnamesAreDifferent() {
    //assemble
    UUID uuid = UUID.randomUUID();
    String mail = "someone@something.com";
    String givenName = "John";
    String surname = "Doe";
    boolean isAdmin = true;

    User cachedUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    User tokenUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname("Wick")
      .admin(isAdmin)
      .build();

    //act + assert
    assertFalse(this.authenticationService.isCachedUserEqualToTokenUser(cachedUser, tokenUser));
  }

  @Test
  void isCachedUserEqualToTokenUser_shouldBeFalse_whenAdminFlagsAreDifferent() {
    //assemble
    UUID uuid = UUID.randomUUID();
    String mail = "someone@something.com";
    String givenName = "John";
    String surname = "Doe";
    boolean isAdmin = true;

    User cachedUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(isAdmin)
      .build();

    User tokenUser = User.builder()
      .id(uuid)
      .mail(mail)
      .givenName(givenName)
      .surname(surname)
      .admin(!isAdmin)
      .build();

    //act + assert
    assertFalse(this.authenticationService.isCachedUserEqualToTokenUser(cachedUser, tokenUser));
  }

  @Test
  void checkIfKeysAreString_shouldBeTrue() {
    //assemble
    Set<?> inputSet = Set.of("String");

    //act
    boolean result = this.authenticationService.checkIfKeysAreString(inputSet);

    //assert
    Assertions.assertTrue(result);
  }

  @Test
  void checkIfKeysAreString_shouldBeFalse() {
    //assemble
    Set<?> inputSet = Set.of("String", 1);

    //act
    boolean result = this.authenticationService.checkIfKeysAreString(inputSet);

    //assert
    Assertions.assertFalse(result);
  }


  @Test
  void getAttributeFromJwt_shouldThrowInvalidTokenException_whenObjectIsNotAString() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    String attributeName = "key";
    int intValue = 42;
    jwtClaims.put(attributeName, intValue);
    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationService.getAttributeFromJwt(jwtMock, attributeName);
    });

    //assert
    Assertions.assertEquals("key value is not existent or not a string", exc.getMessage());
  }

  @Test
  void getAttributeFromJwt_shouldThrowInvalidTokenException_whenObjectIsEmptyString() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    String attributeName = "key";
    String emptyStringValue = "";
    jwtClaims.put(attributeName, emptyStringValue);
    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationService.getAttributeFromJwt(jwtMock, attributeName);
    });

    //assert
    Assertions.assertEquals("key attribute is empty", exc.getMessage());
  }

  @Test
  void getAttributeFromJwt_shouldReturnValidatedString() {
    ///assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    String attributeName = "key";
    String stringValue = "Hello World";
    jwtClaims.put(attributeName, stringValue);
    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    String result = this.authenticationService.getAttributeFromJwt(jwtMock, attributeName);

    //assert
    Assertions.assertEquals(stringValue, result);
  }

  @Test
  void validateString_shouldThrowInvalidTokenException_whenObjectIsNotAString() {
    //assemble
    Object inputObject = 1;
    String attributeName = "someAttribute";

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationService.validateString(inputObject, attributeName);
    });

    //assert
    Assertions.assertEquals("someAttribute value is not existent or not a string", exc.getMessage());
  }

  @Test
  void validateString_shouldThrowInvalidTokenException_whenObjectIsEmptyString() {
    //assemble
    Object inputObject = "";
    String attributeName = "someAttribute";

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationService.validateString(inputObject, attributeName);
    });

    //assert
    Assertions.assertEquals("someAttribute attribute is empty", exc.getMessage());
  }

  @Test
  void validateString_shouldReturnValidatedString() {
    //assemble
    Object inputObject = "a string";
    String attributeName = "someAttribute";

    //act
    String result = this.authenticationService.validateString(inputObject, attributeName);

    //assert
    Assertions.assertEquals(inputObject, result);
  }

  @Test
  void throwIfUserIsNull_shouldThrowEntityNotFoundException() {
    //act
    EntityNotFoundException exc = Assertions.assertThrows(EntityNotFoundException.class, () -> {
      this.authenticationService.throwIfUserIsNull(null);
    });

    //assert
    Assertions.assertEquals("user could not be found in hashmap", exc.getMessage());
  }

  @Test
  void throwIfUserIsNull_shouldDoNothing() {
    //assemble
    User user = new User();

    //act
    Assertions.assertDoesNotThrow(() -> {
      this.authenticationService.throwIfUserIsNull(user);
    });
  }


  @Test
  void checkIfListContainsStrings_shouldBeTrue() {
    //assemble
    ArrayList<Object> inputList = new ArrayList<>();
    inputList.add("a string");

    //act
    boolean result = this.authenticationService.checkIfListContainsStrings(inputList);

    //assert
    Assertions.assertTrue(result);
  }

  @Test
  void checkIfListContainsStrings_shouldBeFalse() {
    //assemble
    ArrayList<Object> inputList = new ArrayList<>();
    inputList.add("a string");
    inputList.add(1);

    //act
    boolean result = this.authenticationService.checkIfListContainsStrings(inputList);

    //assert
    Assertions.assertFalse(result);
  }


  private static class AuthenticationServiceTestImpl extends AuthenticationUserContextService {

    public AuthenticationServiceTestImpl(UserService userService, SystemProperties systemProperties, String GIVEN_NAME_IDENTIFIER, String FAMILY_NAME_IDENTIFIER, String EMAIL_IDENTIFIER) {
      super(userService, systemProperties, GIVEN_NAME_IDENTIFIER, FAMILY_NAME_IDENTIFIER, EMAIL_IDENTIFIER);
    }

    @Override
    protected UUID getUserIdFromToken(Jwt token) {
      return null;
    }

    @Override
    protected List<String> getRolesFromToken(Jwt userToken) throws InvalidTokenException {
      return null;
    }
  }
}

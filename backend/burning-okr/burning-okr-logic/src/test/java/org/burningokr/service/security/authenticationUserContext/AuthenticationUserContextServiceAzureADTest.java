package org.burningokr.unit.service.security.authenticationUserContext;

import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextServiceAzureAD;
import org.burningokr.service.security.authorization.InvalidTokenException;
import org.burningokr.service.userhandling.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthenticationUserContextServiceAzureADTest {

  @InjectMocks
  @Spy
  private AuthenticationUserContextServiceAzureAD authenticationServiceAD;

  @Mock
  private UserService userServiceMock;

  @Mock
  private SystemProperties systemPropertiesMock;


  @Test
  void getUserIdFromToken_shouldReturnUUID() {
    //assemble
    UUID uuid = UUID.randomUUID();

    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    jwtClaims.put("oid", uuid.toString());
    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    UUID result = this.authenticationServiceAD.getUserIdFromToken(jwtMock);

    //assert
    Assertions.assertEquals(uuid, result);
  }

  @Test
  void getRolesFromToken_shouldReturnEmptyList_whenUserRolesListIsNull() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    jwtClaims.put("roles", null);
    doReturn(jwtClaims).when(jwtMock).getClaims();


    //act
    List<String> results = this.authenticationServiceAD.getRolesFromToken(jwtMock);

    //assert
    Assertions.assertEquals(0, results.size());
  }

  @Test
  void getRolesFromToken_shouldThrowInvalidTokenException_whenRolesListContainsNonStrings() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    ArrayList<Object> rolesList = new ArrayList<>();
    rolesList.add("Admin");
    rolesList.add(1);
    Map<String, Object> jwtClaims = new HashMap<>();
    jwtClaims.put("roles", rolesList);

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationServiceAD.getRolesFromToken(jwtMock);
    });

    //assert
    Assertions.assertEquals("Not all roles are of type String", exc.getMessage());
  }

  @Test
  void getRolesFromToken_shouldReturnRolesList() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    ArrayList<Object> rolesList = new ArrayList<>();
    rolesList.add("Admin");
    Map<String, Object> jwtClaims = new HashMap<>();
    jwtClaims.put("roles", rolesList);

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    List<String> results = this.authenticationServiceAD.getRolesFromToken(jwtMock);

    //assert
    Assertions.assertEquals(1, results.size());
    Assertions.assertEquals("Admin", results.get(0));
  }

  @Test
  void getAttributeFromJwt_shouldReturnFirstName() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    String nameClaimKey = "name";
    jwtClaims.put(nameClaimKey, "Doe, John");

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    String result = this.authenticationServiceAD.getAttributeFromJwt(jwtMock, this.authenticationServiceAD.GIVEN_NAME_IDENTIFIER);

    //assert
    Assertions.assertEquals("John", result);
  }

  @Test
  void getAttributeFromJwt_shouldReturnSurname() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    String nameClaimKey = "name";
    jwtClaims.put(nameClaimKey, "Doe, John");

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    String result = this.authenticationServiceAD.getAttributeFromJwt(jwtMock, this.authenticationServiceAD.FAMILY_NAME_IDENTIFIER);

    //assert
    Assertions.assertEquals("Doe", result);
  }

  @Test
  void getAttributeFromJwt_shouldThrowInvalidTokenException_whenGettingFirstNameFromNonStringClaimValue() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    String nameClaimKey = "name";
    jwtClaims.put(nameClaimKey, 42);

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act + assert
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class,() -> {
      this.authenticationServiceAD.getAttributeFromJwt(jwtMock, this.authenticationServiceAD.GIVEN_NAME_IDENTIFIER);
    });

    Assertions.assertEquals("name value is not existent or not a string", exc.getMessage());
  }

  @Test
  void getAttributeFromJwt_shouldThrowInvalidTokenException_whenGettingSurnameFromNonStringClaimValue() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    String nameClaimKey = "name";
    jwtClaims.put(nameClaimKey, 42);

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act + assert
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class,() -> {
      this.authenticationServiceAD.getAttributeFromJwt(jwtMock, this.authenticationServiceAD.FAMILY_NAME_IDENTIFIER);
    });

    Assertions.assertEquals("name value is not existent or not a string", exc.getMessage());
  }

  @Test
  void getAttributeFromJwt_shouldReturnNameUnrelatedAttribute() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    //construct key different to both name keys by concatenation
    String claimKey = this.authenticationServiceAD.GIVEN_NAME_IDENTIFIER + this.authenticationServiceAD.FAMILY_NAME_IDENTIFIER;
    jwtClaims.put(claimKey, "some value");

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act
    String result = this.authenticationServiceAD.getAttributeFromJwt(jwtMock, claimKey);

    //assert
    Assertions.assertEquals("some value", result);
  }

  @Test
  void getAttributeFromJwt_shouldThrowInvalidTokenException_whenGettingNameUnrelatedNonStringAttribute() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaims = new HashMap<>();
    //construct key different to both name keys by concatenation
    String claimKey = this.authenticationServiceAD.GIVEN_NAME_IDENTIFIER + this.authenticationServiceAD.FAMILY_NAME_IDENTIFIER;
    jwtClaims.put(claimKey, 42);

    doReturn(jwtClaims).when(jwtMock).getClaims();

    //act + assert
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class,() -> {
      this.authenticationServiceAD.getAttributeFromJwt(jwtMock, claimKey);
    });

    Assertions.assertEquals(claimKey + " value is not existent or not a string", exc.getMessage());
  }
}

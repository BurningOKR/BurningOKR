package org.burningokr.unit.service.security.authenticationUserContext;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextServiceKeycloak;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)

class AuthenticationUserContextServiceKeycloakTest {
  @InjectMocks
  @Spy
  AuthenticationUserContextServiceKeycloak authenticationUserContextServiceKeycloak;

  @Mock
  UserService userServiceMock;

  @Mock
  SystemProperties systemPropertiesMock;

  @Test
  void getUserIdFromToken_shouldReturnUUID() {
    //assemble
    UUID uuid = UUID.randomUUID();

    Jwt jwtMock = mock(Jwt.class);
    doReturn(uuid.toString()).when(jwtMock).getSubject();

    //act
    UUID result = this.authenticationUserContextServiceKeycloak.getUserIdFromToken(jwtMock);

    //assert
    Assertions.assertEquals(uuid, result);
  }

  @Test
  void getRolesFromToken_shouldReturnEmptyRolesList_whenRealmsMapDoesNotExist() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> jwtClaimsWithoutRealms = new HashMap<>();
    doReturn(jwtClaimsWithoutRealms).when(jwtMock).getClaims();

    //act
    List<String> results = this.authenticationUserContextServiceKeycloak.getRolesFromToken(jwtMock);

    //assert
    Assertions.assertNotNull(results);
    Assertions.assertEquals(0, results.size());
  }

  @Test
  void getRolesFromToken_shouldReturnRoles_whenRealmsMapDoesExist() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> realmsMap = new LinkedTreeMap<>();
    List<String> roles = List.of("Admin", "CEO", "God Imperator");
    realmsMap.put("roles", roles);
    Map<String, Object> jwtClaimsWithRealms = new HashMap<>();
    jwtClaimsWithRealms.put("realm_access", realmsMap);

    doReturn(jwtClaimsWithRealms).when(jwtMock).getClaims();

    //act
    List<String> results = this.authenticationUserContextServiceKeycloak.getRolesFromToken(jwtMock);

    //assert
    Assertions.assertNotNull(results);
    Assertions.assertEquals(3, results.size());
    Assertions.assertEquals("Admin", results.get(0));
    Assertions.assertEquals("CEO", results.get(1));
    Assertions.assertEquals("God Imperator", results.get(2));
  }

  @Test
  void getRolesFromToken_shouldThrowInvalidTokenException_whenRealmsMapKeysContainNonStrings() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<Integer, Object> realmsMap = new LinkedTreeMap<>();
    List<String> roles = List.of("Admin", "CEO", "God Imperator");
    realmsMap.put(1, roles);
    Map<String, Object> jwtClaimsWithRealms = new HashMap<>();
    jwtClaimsWithRealms.put("realm_access", realmsMap);

    doReturn(jwtClaimsWithRealms).when(jwtMock).getClaims();

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationUserContextServiceKeycloak.getRolesFromToken(jwtMock);
    });

    //assert
    Assertions.assertEquals("Not all realm-access keys are of type String", exc.getMessage());
  }

  @Test
  void getRolesFromToken_shouldThrowInvalidTokenException_whenRolesListIsNotAnArrayList() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> realmsMap = new LinkedTreeMap<>();
    Object roles = new Object();
    realmsMap.put("roles", roles);
    Map<String, Object> jwtClaimsWithRealms = new HashMap<>();
    jwtClaimsWithRealms.put("realm_access", realmsMap);

    doReturn(jwtClaimsWithRealms).when(jwtMock).getClaims();

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationUserContextServiceKeycloak.getRolesFromToken(jwtMock);
    });

    //assert
    Assertions.assertEquals("Value of key 'roles' is not of type Collection", exc.getMessage());
  }

  @Test
  void getRolesFromToken_shouldThrowInvalidTokenException_whenRolesListContainsNonStrings() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    Map<String, Object> realmsMap = new LinkedTreeMap<>();
    List<Object> roles = List.of("Hello", 42);
    realmsMap.put("roles", roles);
    Map<String, Object> jwtClaimsWithRealms = new HashMap<>();
    jwtClaimsWithRealms.put("realm_access", realmsMap);

    doReturn(jwtClaimsWithRealms).when(jwtMock).getClaims();

    //act
    InvalidTokenException exc = Assertions.assertThrows(InvalidTokenException.class, () -> {
      this.authenticationUserContextServiceKeycloak.getRolesFromToken(jwtMock);
    });

    //assert
    Assertions.assertEquals("Not all roles are of type String", exc.getMessage());
  }
}

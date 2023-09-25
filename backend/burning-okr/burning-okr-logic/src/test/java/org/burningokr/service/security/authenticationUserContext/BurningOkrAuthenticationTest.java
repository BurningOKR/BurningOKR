package org.burningokr.service.security.authenticationUserContext;

import org.burningokr.model.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

import static org.mockito.Mockito.mock;

class BurningOkrAuthenticationTest {
  @Test
  void getPrincipal_shouldReturnPrincipalUser() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    User userMock = mock(User.class);
    BurningOkrAuthentication burningOkrAuthentication = new BurningOkrAuthentication(jwtMock, userMock);

    //act
    User result = burningOkrAuthentication.getPrincipal();

    //assert
    Assertions.assertSame(userMock, result);
    Assertions.assertEquals(userMock, result);
  }

  @Test
  void getTokenAttributes_shouldReturnNull() {
    //assemble
    Jwt jwtMock = mock(Jwt.class);
    User userMock = mock(User.class);
    BurningOkrAuthentication burningOkrAuthentication = new BurningOkrAuthentication(jwtMock, userMock);

    //act
    Map<String, Object> result = burningOkrAuthentication.getTokenAttributes();

    //assert
    Assertions.assertNull(result);
  }
}

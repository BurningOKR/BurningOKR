package org.burningokr.service;

import org.burningokr.config.WebSocketAuthentication;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebsocketUserServiceTest {

  @InjectMocks
  WebsocketUserService websocketUserService;

  @Mock
  UserService userServiceMock;

  @Test
  void findByAccessor_shouldThrowRuntimeException_whenStompHeaderSessionAttributesMapIsNull() {
    //assemble
    StompHeaderAccessor headerAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(null).when(headerAccessorMock).getSessionAttributes();

    //act + assert
    RuntimeException exc = Assertions.assertThrows(RuntimeException.class, () ->
      this.websocketUserService.findByAccessor(headerAccessorMock)
    );

    Assertions.assertEquals("StompHeaderAccessor does not contain (valid) userId.", exc.getMessage());
  }

  @Test
  void findByAccessor_shouldThrowRuntimeException_whenStompHeaderSessionAttributesMapDoesNotContainUserId() {
    //assemble
    StompHeaderAccessor headerAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(new HashMap<>()).when(headerAccessorMock).getSessionAttributes();

    //act + assert
    RuntimeException exc = Assertions.assertThrows(RuntimeException.class, () ->
      this.websocketUserService.findByAccessor(headerAccessorMock)
    );

    Assertions.assertEquals("StompHeaderAccessor does not contain (valid) userId.", exc.getMessage());
  }

  @Test
  void findByAccessor_shouldNoSuchElementException_whenUserCannotBeFetched() {
    //assemble
    StompHeaderAccessor headerAccessorMock = mock(StompHeaderAccessor.class);
    Map<String, Object> sessionAttributesMap = new HashMap<>();
    String userId = UUID.randomUUID().toString();
    sessionAttributesMap.put(WebSocketAuthentication.USER_SESSION_ATTRIBUTE_KEY, userId);
    doReturn(sessionAttributesMap).when(headerAccessorMock).getSessionAttributes();

    doReturn(Optional.empty()).when(this.userServiceMock).findById(any(UUID.class));

    //act + assert
    Assertions.assertThrows(NoSuchElementException.class, () ->
      this.websocketUserService.findByAccessor(headerAccessorMock)
    );

    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    verify(this.userServiceMock).findById(uuidArgumentCaptor.capture());
    Assertions.assertEquals(userId, uuidArgumentCaptor.getValue().toString());
  }

  @Test
  void findByAccessor_shouldReturnUser() {
    //assemble
    StompHeaderAccessor headerAccessorMock = mock(StompHeaderAccessor.class);
    Map<String, Object> sessionAttributesMap = new HashMap<>();
    String userId = UUID.randomUUID().toString();
    sessionAttributesMap.put(WebSocketAuthentication.USER_SESSION_ATTRIBUTE_KEY, userId);
    doReturn(sessionAttributesMap).when(headerAccessorMock).getSessionAttributes();

    User userMock = mock(User.class);
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    doReturn(Optional.of(userMock)).when(this.userServiceMock).findById(uuidArgumentCaptor.capture());

    //act + assert
    User result = this.websocketUserService.findByAccessor(headerAccessorMock);

    //assert
    Assertions.assertEquals(userId, uuidArgumentCaptor.getValue().toString());
    Assertions.assertEquals(result, userMock);
  }

}

package org.burningokr.unit.config;

import org.burningokr.config.StompHeaderInterceptor;
import org.burningokr.config.WebSocketAuthentication;
import org.burningokr.config.WebSocketConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.SimpleBrokerRegistration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketConfigTest {

  @InjectMocks
  private WebSocketConfig config;

  @Mock
  private WebSocketAuthentication socketAuthenticationMock;


  @Test
  void registerStompEndpoints() {
    //assemble
    StompEndpointRegistry registryMock = mock(StompEndpointRegistry.class);
    StompWebSocketEndpointRegistration wsRegistrationMock = mock(StompWebSocketEndpointRegistration.class);
    doReturn(wsRegistrationMock).when(registryMock).addEndpoint("/wsregistry");

    //act
    this.config.registerStompEndpoints(registryMock);

    //assert
    verify(registryMock).addEndpoint("/wsregistry");
    verify(wsRegistrationMock).setAllowedOrigins("*");
  }

  @Test
  void configureMessageBroker() {
    //assemble
    WebSocketConfig configSpy = spy(this.config);
    MessageBrokerRegistry registryMock = mock(MessageBrokerRegistry.class);
    SimpleBrokerRegistration simRegMock = mock(SimpleBrokerRegistration.class);
    doReturn(simRegMock).when(registryMock).enableSimpleBroker(any());
    doReturn(simRegMock).when(simRegMock).setTaskScheduler(any());
    doReturn(new ThreadPoolTaskScheduler()).when(configSpy).heartBeatScheduler();

    //act
    configSpy.configureMessageBroker(registryMock);

    //assert
    verify(registryMock).enableSimpleBroker("/topic");
    verify(simRegMock).setTaskScheduler(any(TaskScheduler.class));
    verify(registryMock).setApplicationDestinationPrefixes("/ws");
  }

  @Test
  void configureClientInboundChannel_shouldCallInterceptorWithStompHeaderInterceptorAsArg() {
    //assemble
    ChannelRegistration channelRegMock = mock(ChannelRegistration.class);
    doReturn(null).when(channelRegMock).interceptors(any());

    //act
    this.config.configureClientInboundChannel(channelRegMock);

    //assert
    verify(channelRegMock).interceptors(any(StompHeaderInterceptor.class));
  }

  @Test
  void heartBeatScheduler_shouldReturnTaskScheduler () {
    //act
    TaskScheduler actual = this.config.heartBeatScheduler();

    //assert
    Assertions.assertInstanceOf(ThreadPoolTaskScheduler.class, actual);
  }
}

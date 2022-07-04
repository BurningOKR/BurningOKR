package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSecurity
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  private WebsocketAuthenticationChannelInterceptor websocketAuthenticationChannelInterceptor;

  @Autowired
  public WebSocketConfig(
      WebsocketAuthenticationChannelInterceptor websocketAuthenticationChannelInterceptor) {
    this.websocketAuthenticationChannelInterceptor = websocketAuthenticationChannelInterceptor;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/wsregistry").setAllowedOrigins("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    long[] heartbeats = {10000l, 10000l};
    config
        .enableSimpleBroker("/topic")
        .setTaskScheduler(heartBeatScheduler())
        .setHeartbeatValue(heartbeats);
    config.setApplicationDestinationPrefixes("/ws");
  }

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
    message
        .nullDestMatcher()
        .permitAll()
        .simpTypeMatchers(SimpMessageType.CONNECT)
        .authenticated()
        .simpTypeMatchers(SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT)
        .authenticated()
        .simpDestMatchers("/ws/**")
        .authenticated()
        .simpSubscribeDestMatchers("/topic/**")
        .authenticated()
        .anyMessage()
        .denyAll();
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }

  @Override
  public void customizeClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(websocketAuthenticationChannelInterceptor);
  }

  @Bean
  @Qualifier("heartBeatScheduler")
  public TaskScheduler heartBeatScheduler() {
    return new ThreadPoolTaskScheduler();
  }
}

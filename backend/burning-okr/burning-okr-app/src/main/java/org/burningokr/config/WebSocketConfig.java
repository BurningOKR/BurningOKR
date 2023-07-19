package org.burningokr.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.burningokr.exceptions.AuthorizationHeaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

  private final WebSocketAuthentication socketAuth;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/wsregistry").setAllowedOrigins("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    long[] heartbeats = {10000L, 10000L};
    config.enableSimpleBroker("/topic").setTaskScheduler(heartBeatScheduler()).setHeartbeatValue(heartbeats);
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) throw new NullPointerException("StompHeaderAccessor is null");

        if (!socketAuth.isConnectionAttempt(accessor)) return message;

        try {
          socketAuth.tryToAuthenticate(accessor);
        } catch (AuthorizationHeaderException e) {
          logger.info(e.getMessage());
        }
        System.out.println("WebSocketConfig Class: " + message);
        return message;
      }
    });
  }

  @Bean
  @Qualifier("heartBeatScheduler")
  public TaskScheduler heartBeatScheduler() {
    return new ThreadPoolTaskScheduler();
  }
}

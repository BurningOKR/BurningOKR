package org.burningokr.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

  @Autowired
  JwtDecoder jwtDecoder;

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
    try {
      registration.interceptors(new ChannelInterceptor() {
        @Override
        public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
          try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (accessor == null) {
              throw new NullPointerException("StompHeaderAccessor is null");
            }

            if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
              return message;
            }

            final String accessToken = getToken(accessor);
            final Authentication authentication = getAuthentication(accessToken);

            if (!authentication.isAuthenticated()) return message;

            accessor.setUser(authentication);
            Objects.requireNonNull(accessor.getSessionAttributes()).put(WebsocketAuthenticationChannelInterceptor.USER_SESSION_ATTRIBUTE_KEY, authentication.getName());
          } catch (Exception e) {
            logger.info(e.getMessage());
          }
          return message;
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Authentication getAuthentication(String token) {
    if (token == null || token.isEmpty()) throw new NullPointerException("Token is either null or is empty");
    Jwt jwt = jwtDecoder.decode(token);
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    Authentication authentication = converter.convert(jwt);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return authentication;
  }

  private String getToken(StompHeaderAccessor accessor) {
    List<String> tokenList = accessor.getNativeHeader("Authorization");
    if (tokenList == null || tokenList.isEmpty()) return "";

    return tokenList.get(0).split(":")[1];
  }

  @Bean
  @Qualifier("heartBeatScheduler")
  public TaskScheduler heartBeatScheduler() {
    return new ThreadPoolTaskScheduler();
  }
}

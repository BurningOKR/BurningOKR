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

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

  private Authentication authentication;

  private final JwtDecoder jwtDecoder;
  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";
  private static final String WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY = "Authorization";

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/wsregistry").setAllowedOrigins("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    long[] heartbeats = {10000L, 10000L};
    config.enableSimpleBroker("/topic").setTaskScheduler(heartBeatScheduler()).setHeartbeatValue(heartbeats);
    config.setApplicationDestinationPrefixes("/ws");
  }

  @Override
  public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) throw new NullPointerException("StompHeaderAccessor is null");

        if (!isConnectionAttempt(accessor)) return message;

        try {
          tryToAuthenticate(accessor);
        } catch (AuthorizationHeaderException e) {
          logger.info(e.getMessage());
        }
        return message;
      }
    });
  }

  private void getAuthentication(String token) {
    if (token == null || token.isEmpty()) throw new NullPointerException("Token is either null or is empty");
    Jwt jwt = jwtDecoder.decode(token);
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    this.authentication = converter.convert(jwt);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private String getBearerToken(@NonNull StompHeaderAccessor header) {
    if (!header.containsNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY)) return "";
    final List<String> nativeHeader = header.getNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY);
    return nativeHeader != null && !nativeHeader.isEmpty() ? nativeHeader.get(0).split(":")[1] : "";
  }

  private boolean isConnectionAttempt(@NonNull StompHeaderAccessor accessor) {
    return StompCommand.CONNECT.equals(accessor.getCommand());
  }

//  private boolean isAuthenticated(@NonNull final Authentication current) {
//    return this.authentication.isAuthenticated() && current.isAuthenticated()
//        && this.authentication.getName().equals(current.getName());
//  }

  private void tryToAuthenticate(@NonNull StompHeaderAccessor header) throws AuthorizationHeaderException {
    final String bearerToken = getBearerToken(header);
    getAuthentication(bearerToken);

    if (!this.authentication.isAuthenticated()) {
      logger.warn("User could not be authenticated...");
      return;
    }

    header.setUser(this.authentication);

    if (header.getSessionAttributes() == null) throw new RuntimeException("Session Attributes are null");
    header.getSessionAttributes().put(USER_SESSION_ATTRIBUTE_KEY, this.authentication.getName());
  }

  @Bean
  @Qualifier("heartBeatScheduler")
  public TaskScheduler heartBeatScheduler() {
    return new ThreadPoolTaskScheduler();
  }
}

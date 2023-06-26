// TODO fix auth
package org.burningokr.config;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNullApi;
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
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.naming.AuthenticationException;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
/*  private final ResourceServerTokenServices resourceServerTokenServices;

  @Autowired(required = false)
  private AuthorizationServerEndpointsConfiguration endpoints;*/

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/wsregistry").setAllowedOrigins("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    long[] heartbeats = {10000L, 10000L};
    config
        .enableSimpleBroker("/topic")
        .setTaskScheduler(heartBeatScheduler())
        .setHeartbeatValue(heartbeats);
    config.setApplicationDestinationPrefixes("/ws");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
          try {
            StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            if (accessor == null) {
              throw new NullPointerException("StompHeaderAccessor is null");
            }

            if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
              return message;
            }

            final String token = getToken(accessor);
            Authentication authenticationByService = getAuthentication(token);

            if(authenticationByService == null) throw new AuthenticationException();

            accessor.setUser(authenticationByService);
          } catch (Exception e) {
            logger.info(e.getMessage());
          }
          return message;
        }
    });
  }

  private Authentication getAuthentication(String token) {
    if (token == null || token.isEmpty()) throw new NullPointerException("Token is either null or is empty");


    Authentication authentificationByService = null;
//    if (tokenStore != null) {
//      authByService = tokenStore.readAuthentication(token);
//    }
//    // DM 26.05.2021:
//    // The following code is for new authentication functions. This is usefull when there is no bean
//    // for TokenStore or ResourceServerTokenServices defined.
//
//    else if (endpoints != null) {
//      authByService = endpoints.getEndpointsConfigurer().getTokenStore().readAuthentication(token);
//    } else {
//      authByService = resourceServerTokenServices.loadAuthentication(token);
//    }
    return authentificationByService;
  }

  private String getToken(StompHeaderAccessor accessor) {
    List<String> tokenList = accessor.getNativeHeader("Authorization");
    if (tokenList == null || tokenList.isEmpty()) return "";

    return tokenList.get(0).split("\\s")[1];
  }

  @Bean
  @Qualifier("heartBeatScheduler")
  public TaskScheduler heartBeatScheduler() {
    return new ThreadPoolTaskScheduler();
  }
}

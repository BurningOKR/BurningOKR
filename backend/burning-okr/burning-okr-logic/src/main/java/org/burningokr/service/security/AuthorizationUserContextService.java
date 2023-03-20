package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationUserContextService {
  private final UserService userService;
  private final JwtDecoder jwtDecoder;

  public User getUserFromSecurityContext() {
    var user = SecurityContextHolder.getContext().getAuthentication();
    var token = user.getPrincipal();
    log.info(token.toString());
//    byte[] decodedBytes = Base64.getDecoder().decode(token.toString());
//    String decodedString = new String(decodedBytes);
    var userId = jwtDecoder.decode(token.toString()).getSubject();
    log.info(userId);
    return null;
  }
}

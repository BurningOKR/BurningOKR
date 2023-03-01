package org.burningokr.service.security;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.monitoring.UserId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
@Slf4j
public class UserFromContextService {

  // TODO Dependency injection doesn't work before newer envers (-> depends on newer spring), so this is just a workaround, copied from UserService. (MV)
  public static UserId extractUserIdFromSecurityContext() {
    Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
    LinkedHashMap<String, String> userCredentials;

    if (userAuthentication instanceof OAuth2Authentication) {
      userCredentials = (LinkedHashMap<String, String>) ((OAuth2Authentication) userAuthentication).getUserAuthentication()
        .getDetails();
    } else {
      throw new RuntimeException();
    }

    String userId = userCredentials.get("id");
    return new UserId(userId);
  }
}

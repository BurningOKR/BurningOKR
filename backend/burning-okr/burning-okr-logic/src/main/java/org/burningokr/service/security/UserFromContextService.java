package org.burningokr.service.security;

import org.burningokr.model.monitoring.UserId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class UserFromContextService {

  // TODO Dependency injection doesn't work before newer envers (-> depends on newer spring), so this is just a workaround, copied from UserService. (MV)
  public static UserId extractUserIdFromSecurityContext() {
    try {
      Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
      LinkedHashMap userCredentials = null;

      // Request through websocket
      if (userAuthentication.getDetails() instanceof LinkedHashMap) {
        userCredentials =
            (LinkedHashMap) SecurityContextHolder.getContext().getAuthentication().getDetails();
      }

      // Regular request
      else if (userAuthentication.getDetails() instanceof OAuth2AuthenticationDetails) {
        userCredentials =
            (LinkedHashMap)
                ((OAuth2AuthenticationDetails) userAuthentication.getDetails()).getDecodedDetails();
      }

      String userId = (String) userCredentials.get("id");
      return new UserId(userId);
    } catch (Exception e) {
      throw new IllegalStateException("Could not find (valid) user id in context", e);
    }
  }
}

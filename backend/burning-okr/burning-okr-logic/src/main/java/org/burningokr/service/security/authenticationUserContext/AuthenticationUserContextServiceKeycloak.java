package org.burningokr.service.security.authenticationUserContext;

import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.service.security.authorization.InvalidTokenException;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@ConditionalOnProperty(value = "system.configuration.provider", havingValue = "keycloak")
@Service
public class AuthenticationUserContextServiceKeycloak extends AuthenticationUserContextService {

  @Autowired
  public AuthenticationUserContextServiceKeycloak(UserService userService, SystemProperties systemProperties) {
    super(userService, systemProperties, "given_name", "family_name", "email");
  }

  @Override
  public UUID getUserIdFromToken(Jwt token) {
    return UUID.fromString(token.getSubject());
  }

  @Override
  public List<String> getRolesFromToken(Jwt userToken) throws InvalidTokenException {
    var userRoles = new ArrayList<String>();
    var realmsMap = (Map<?, ?>) userToken.getClaims().get("realm_access");

    if (realmsMap != null) {
      if (!checkIfKeysAreString(realmsMap.keySet())) {
        throw new InvalidTokenException("Not all realm-access keys are of type String");
      }

      if (!(realmsMap.get("roles") instanceof Collection<?> roles)) {
        throw new InvalidTokenException("Value of key 'roles' is not of type Collection");
      }

      if (!checkIfListContainsStrings(roles)) {
        throw new InvalidTokenException("Not all roles are of type String");
      }

      roles.forEach(role -> userRoles.add((String) role));
    }

    return userRoles;
  }
}

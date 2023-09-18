package org.burningokr.service.security.authenticationUserContext;

import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.service.security.authorization.InvalidTokenException;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(value = "system.configuration.provider", havingValue = "azureAD")
@Service
public class AuthenticationUserContextServiceAzureAD extends AuthenticationUserContextService {
  private final static String NAME_CLAIM_KEY = "name";
  private final static String PARSE_FIRST_NAME_COMMAND = "PARSE_FIRST_NAME_FROM_NAME_CLAIM";
  private final static String PARSE_LAST_NAME_COMMAND = "PARSE_LAST_NAME_FROM_NAME_CLAIM";

  @Autowired
  public AuthenticationUserContextServiceAzureAD(UserService userService, SystemProperties systemProperties) {
    super(
        userService,
        systemProperties,
        PARSE_FIRST_NAME_COMMAND,
        PARSE_LAST_NAME_COMMAND,
        "preferred_username"
    );
  }

  @Override
  public UUID getUserIdFromToken(Jwt token) {
    return UUID.fromString((String) token.getClaims().get("oid"));
  }

  @Override
  public List<String> getRolesFromToken(Jwt userToken) throws InvalidTokenException {
    var userRoles = (ArrayList<?>) userToken.getClaims().get("roles");

    if (userRoles == null) {
      return new ArrayList<>();
    } else if (!checkIfListContainsStrings(userRoles)) {
      throw new InvalidTokenException("Not all roles are of type String");
    }

    return userRoles.stream().map(role -> (String) role).toList();
  }

  /*
   * "commands" need to be used because azureAD does not save first and last name in different claim fields. The
   * commands trigger a dedicated parsing for first or last name. The combined field
   * (format: "<last_name>, <first_name>") gets seperated and returned.
   */
  @Override
  public String getAttributeFromJwt(Jwt userToken, String attributeName) throws InvalidTokenException {
    switch (attributeName) {
      case PARSE_FIRST_NAME_COMMAND -> {
        var nameClaimValue = userToken.getClaims().get(NAME_CLAIM_KEY);
        return validateString(nameClaimValue, NAME_CLAIM_KEY).split(", ")[1];
      }
      case PARSE_LAST_NAME_COMMAND -> {
        var nameClaimValue = userToken.getClaims().get(NAME_CLAIM_KEY);
        return validateString(nameClaimValue, NAME_CLAIM_KEY).split(", ")[0];
      }
    }

    return validateString(userToken.getClaims().get(attributeName), attributeName);
  }
}

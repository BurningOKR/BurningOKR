package org.burningokr.service.security.authenticationUserContext;

import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.service.security.authorization.InvalidTokenException;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
  protected UUID getUserIdFromToken(Jwt token) {
    return UUID.fromString((String) token.getClaims().get("oid"));
  }

  @Override
  protected ArrayList<String> getRolesFromToken(Jwt userToken) throws InvalidTokenException {
    return new ArrayList<>(); // TODO (C.K. 07.8.2023): implement when field name is known
  }

  /*
   * "commands" need to be used because azureAD does not save first and last name in different claim fields. The
   * commands trigger a dedicated parsing for first or last name. The combined field
   * (format: "<last_name>, <first_name>") gets seperated and returned.
   */
  @Override
  protected String getAttributeFromJwt(Jwt userToken, String attributeName) throws InvalidTokenException {
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

    return validateString(userToken.getClaims().get(attributeName), attributeName); // TODO no claims in azure ad token present
  }
}

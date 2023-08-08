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

  @Autowired
  public AuthenticationUserContextServiceAzureAD(UserService userService, SystemProperties systemProperties) {
    super(
        userService,
        systemProperties,
        "given_name", "family_name", "unique_name"
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
}

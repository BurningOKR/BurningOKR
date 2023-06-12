package org.burningokr.service.security;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationUserContextService {
  private final UserService userService;
  private final SystemProperties systemProperties;
  private final HashMap<UUID, User> userHashMap = new HashMap<>();

  private void checkIfStringIsEmpty(String attributeName, String validatedString) throws InvalidTokenException {
    if (validatedString.equals("")) {
      throw new InvalidTokenException("%s attribute is empty".formatted(attributeName));
    }
  }

  public User getAuthenticatedUser() throws EntityNotFoundException {
    var userToken = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    var userId = switch (systemProperties.getProvider()) {
      case "azureAD":
        yield getUserIdFromAzureAdToken(userToken);
      case "keycloak":
        yield UUID.fromString(userToken.getSubject());
      default:
        throw new RuntimeException("Unexpected value: " + systemProperties.getProvider());
    };

    var user = userHashMap.get(userId);
    if (user == null) {
      throw new RuntimeException("user could not be found in hashmap");
    }

    return user;
  }

  public void updateUserFromToken(Jwt userToken) throws InvalidTokenException {
    User tokenUser = switch (systemProperties.getProvider()) {
      case "azureAD":
        yield getUserFromAzureAdToken(userToken);
      case "keycloak":
        yield getUserFromKeycloakToken(userToken);
      default:
        throw new RuntimeException("provider specified in application yaml is not supported");
    };

    User cachedUser = userHashMap.get(tokenUser.getId());

    if (cachedUser == null || !isCachedUserEqualToTokenUser(cachedUser, tokenUser)) {
      updateCachedAndDatabaseUser(tokenUser);
    }
  }

  private User getUserFromAzureAdToken(Jwt userToken) {
    var userId = getUserIdFromAzureAdToken(userToken);
    var userRoles = getRolesFromAzureToken(userToken);
    var names = getGivenNameAndSurNameFromAzureAdToken(userToken);
    var surname = names[0];
    var givenName = names[1];
    var mail = getAttributeFromJwt(userToken, "preferred_username");

    return User.builder()
            .id(userId)
            .givenName(givenName)
            .surname(surname)
            .mail(mail)
            .admin(userRoles.contains(systemProperties.getOidcAdminGroupName()))
            .active(true)
            .createdAt(LocalDateTime.now())
            .build();
  }

  private String[] getGivenNameAndSurNameFromAzureAdToken(Jwt userToken) {
    String concatName = getAttributeFromJwt(userToken, "name");
    return concatName.replace(" ", "").split(","); // first string is family and second given name
  }

  private User getUserFromKeycloakToken(Jwt userToken) {
    var userId = UUID.fromString(userToken.getSubject());
    var userRoles = getRolesFromKeycloakToken(userToken);

    return User.builder()
            .id(userId)
            .admin(userRoles.contains(systemProperties.getOidcAdminGroupName()))
            .active(true)
            .givenName(getAttributeFromJwt(userToken, "given_name"))
            .surname(getAttributeFromJwt(userToken, "family_name"))
            .mail(getAttributeFromJwt(userToken, "email"))
            .createdAt(LocalDateTime.now())
            .build();
  }

  private boolean isCachedUserEqualToTokenUser(User cachedUser, User tokenUser) {
    return cachedUser.getId().equals(tokenUser.getId()) &&
            cachedUser.getMail().equals(tokenUser.getMail()) &&
            cachedUser.getGivenName().equals(tokenUser.getGivenName()) &&
            cachedUser.getSurname().equals(tokenUser.getSurname()) &&
            cachedUser.isAdmin() == tokenUser.isAdmin();
  }

  private UUID getUserIdFromAzureAdToken(Jwt jwt) {
    var realmsMap = jwt.getClaims();
    return UUID.fromString((String) realmsMap.get("oid"));
  }

  public void updateCachedAndDatabaseUser(User user) {
    log.info("updating user in cache and database");
    userHashMap.put(user.getId(), userService.updateUser(user));
  }

  private ArrayList<String> getRolesFromAzureToken(Jwt userToken) throws InvalidTokenException {
    var realmsMap = (LinkedTreeMap<?, ?>) userToken.getClaims().get("realm_access"); // TODO (C.K.): search for correct field
    var userRoles = new ArrayList<String>();

    if (realmsMap != null) {
      if (!checkIfKeysAreString(realmsMap.keySet())) {
        throw new InvalidTokenException("Not all realm-access keys are of type String");
      }

      if (!(realmsMap.get("roles") instanceof ArrayList<?> roles)) {
        throw new InvalidTokenException("roles-list is not of type ArrayList");
      }

      if (!checkIfListContainsStrings(roles)) {
        throw new InvalidTokenException("Not all roles are of type String");
      }

      roles.forEach(role -> userRoles.add((String) role));
    }

    return userRoles;
  }

  private ArrayList<String> getRolesFromKeycloakToken(Jwt userToken) throws InvalidTokenException {
    var realmsMap = (LinkedTreeMap<?, ?>) userToken.getClaims().get("realm_access");
    var userRoles = new ArrayList<String>();

    if (realmsMap != null) {
      if (!checkIfKeysAreString(realmsMap.keySet())) {
        throw new InvalidTokenException("Not all realm-access keys are of type String");
      }

      if (!(realmsMap.get("roles") instanceof ArrayList<?> roles)) {
        throw new InvalidTokenException("roles-list is not of type ArrayList");
      }

      if (!checkIfListContainsStrings(roles)) {
        throw new InvalidTokenException("Not all roles are of type String");
      }

      roles.forEach(role -> userRoles.add((String) role));
    }

    return userRoles;
  }

  private String getAttributeFromJwt(Jwt userToken, String attributeName) throws InvalidTokenException {
    return validateString(userToken.getClaims().get(attributeName), attributeName);
  }

  private boolean checkIfKeysAreString(Set<?> keySet) {
    return keySet.stream().allMatch(key -> key instanceof String);
  }

  private boolean checkIfListContainsStrings(ArrayList<?> list) {
    return list.stream().allMatch(key -> key instanceof String);
  }

  private String validateString(Object stringObject, String attributeName) throws InvalidTokenException {
    String validatedString = checkIfObjectIsInstanceOfString(stringObject, attributeName);
    checkIfStringIsEmpty(attributeName, validatedString);

    return validatedString;
  }

  private String checkIfObjectIsInstanceOfString(Object stringObject, String attributeName) throws
    InvalidTokenException {
    if (!(stringObject instanceof String validatedString)) {
      throw new InvalidTokenException("%s attribute is not existent or not a string".formatted(attributeName));
    }
    return validatedString;
  }
}

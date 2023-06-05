package org.burningokr.service.security;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final HashMap<UUID, User> userHashMap = new HashMap<>();

  private void checkIfStringIsEmpty(String attributeName, String validatedString) throws InvalidTokenException {
    if (validatedString.equals("")) {
      throw new InvalidTokenException("%s attribute is empty".formatted(attributeName));
    }
  }

  public User getAuthenticatedUser() throws EntityNotFoundException {
    var userToken = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userId = UUID.fromString(userToken.getSubject());

    var userOptional = userService.findById(userId);

    return userOptional.orElseThrow(() -> {
      log.error("user with uuid: %s is not present in the database".formatted(userId));
      return new EntityNotFoundException();
    });
  }

  public void updateUserFromToken(Jwt userToken) throws InvalidTokenException {
    var userId = UUID.fromString(userToken.getSubject());
    var userRoles = getRolesFromJwt(userToken);

    User user = userHashMap.get(userId);

    if (user == null) {
      var userOptional = userService.findById(userId);
      user = userOptional.orElseGet(() -> createNewUser(userId));
    }

    if (!user.equals(userHashMap.get(userId))) {
      updateUserEntityByTokenValues(userToken, userRoles, user);
    }
  }

  public void updateUserEntityByTokenValues(Jwt userToken, ArrayList<String> userRoles, User user) {
    log.info("updateUserEntityByTokenValues called");
    user.setAdmin(userRoles.contains("burning-okr-admin")); // TODO Auth: extract role name in env variable
    user.setGivenName(getAttributeFromJwt(userToken, "given_name"));
    user.setSurname(getAttributeFromJwt(userToken, "family_name"));
    user.setMail(getAttributeFromJwt(userToken, "email"));

    userHashMap.put(user.getId(), user);
    userService.updateUser(user);
  }

  private User createNewUser(UUID userId) {
    log.info("user with id: %s not present, registering new user".formatted(userId));
    User user = new User();
    user.setId(userId);
    user.setActive(true);
    user.setCreatedAt(LocalDateTime.now());
    return user;
  }

  private ArrayList<String> getRolesFromJwt(Jwt userToken) throws InvalidTokenException {
    var realmsMap = (LinkedTreeMap<?, ?>) userToken.getClaims().get("realm_access");
    var userRoles = new ArrayList<String>();

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

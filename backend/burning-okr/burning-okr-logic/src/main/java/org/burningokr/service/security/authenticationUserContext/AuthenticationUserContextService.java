package org.burningokr.service.security.authenticationUserContext;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authorization.InvalidTokenException;
import org.burningokr.service.userhandling.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public abstract class AuthenticationUserContextService {
  protected final UserService userService;
  protected final SystemProperties systemProperties;
  protected final HashMap<UUID, User> userHashMap = new HashMap<>();
  protected final String GIVEN_NAME_IDENTIFIER;
  protected final String FAMILY_NAME_IDENTIFIER;
  protected final String EMAIL_IDENTIFIER;

  public User getAuthenticatedUser() throws EntityNotFoundException {
    UUID userId = getUserIdFromContext();
    var user = userHashMap.get(userId);

    throwIfUserIsNull(user);

    return user;
  }

  protected UUID getUserIdFromContext() {
    return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
  }

  protected abstract UUID getUserIdFromToken(Jwt token);

  protected abstract List<String> getRolesFromToken(Jwt userToken) throws InvalidTokenException;

  protected void checkIfStringIsEmpty(String attributeName, String validatedString) throws InvalidTokenException {
    if (validatedString.equals("")) {
      throw new InvalidTokenException("%s attribute is empty".formatted(attributeName));
    }
  }

  public User getUserFromToken(Jwt userToken) throws InvalidTokenException {
    var userId = getUserIdFromToken(userToken);
    var userRoles = getRolesFromToken(userToken);

    return User.builder()
        .id(userId)
        .admin(userRoles.contains(systemProperties.getOidcAdminGroupName()))
        .active(true)
        .givenName(getAttributeFromJwt(userToken, GIVEN_NAME_IDENTIFIER))
        .surname(getAttributeFromJwt(userToken, FAMILY_NAME_IDENTIFIER))
        .mail(getAttributeFromJwt(userToken, EMAIL_IDENTIFIER))
        .createdAt(LocalDateTime.now())
        .build();
  }

  /**
   * checks if user data in token has changed compared to the user data from the hash map. if the data has changed the
   * user is updated in hashmap and database.
   *
   * @param tokenUser parsed user data from jwt token
   */
  public void updateCachedAndDatabaseUser(User tokenUser) {
    User cachedUser = userHashMap.get(tokenUser.getId());
    if (cachedUser == null || !isCachedUserEqualToTokenUser(cachedUser, tokenUser)) {
      log.info("detected change from user data in token compared to user data in hashmap. updating user in cache and" +
          " database");
      userHashMap.put(tokenUser.getId(), userService.updateUser(tokenUser));
    }
  }

  protected boolean isCachedUserEqualToTokenUser(User cachedUser, User tokenUser) {
    return cachedUser.getId().equals(tokenUser.getId()) &&
        cachedUser.getMail().equals(tokenUser.getMail()) &&
        cachedUser.getGivenName().equals(tokenUser.getGivenName()) &&
        cachedUser.getSurname().equals(tokenUser.getSurname()) &&
        cachedUser.isAdmin() == tokenUser.isAdmin();
  }

  protected boolean checkIfKeysAreString(Set<?> keySet) {
    return keySet.stream().allMatch(key -> key instanceof String);
  }

  protected String getAttributeFromJwt(Jwt userToken, String attributeName) throws InvalidTokenException {
    return validateString(userToken.getClaims().get(attributeName), attributeName); // TODO no claims in azure ad token present
  }

  protected String validateString(Object stringObject, String attributeName) throws InvalidTokenException {
    String validatedString = checkIfObjectIsInstanceOfString(stringObject, attributeName);
    checkIfStringIsEmpty(attributeName, validatedString);

    return validatedString;
  }

  private String checkIfObjectIsInstanceOfString(Object stringObject, String attributeName) throws
      InvalidTokenException {
    if (!(stringObject instanceof String validatedString)) {
      throw new InvalidTokenException("%s value is not existent or not a string".formatted(attributeName));
    }
    return validatedString;
  }

  protected void throwIfUserIsNull(User user) throws EntityNotFoundException {
    if (user == null) {
      throw new EntityNotFoundException("user could not be found in hashmap");
    }
  }

  protected boolean checkIfListContainsStrings(ArrayList<?> list) {
    return list.stream().allMatch(key -> key instanceof String);
  }
}

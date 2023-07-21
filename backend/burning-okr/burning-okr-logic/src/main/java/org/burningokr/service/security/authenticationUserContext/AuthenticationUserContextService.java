package org.burningokr.service.security.authenticationUserContext;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authorization.InvalidTokenException;
import org.burningokr.service.userhandling.UserService;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

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

    protected abstract UUID getUserIdFromContext();

    protected abstract ArrayList<String> getRolesFromToken(Jwt userToken) throws InvalidTokenException;

    protected void checkIfStringIsEmpty(String attributeName, String validatedString) throws InvalidTokenException {
        if (validatedString.equals("")) {
            throw new InvalidTokenException("%s attribute is empty".formatted(attributeName));
        }
    }

    public void updateUserFromToken(Jwt userToken) throws InvalidTokenException {
        var userId = getUserIdFromContext();
        var userRoles = getRolesFromToken(userToken);

        User tokenUser = User.builder()
                .id(userId)
                .admin(userRoles.contains(systemProperties.getOidcAdminGroupName()))
                .active(true)
                .givenName(getAttributeFromJwt(userToken, GIVEN_NAME_IDENTIFIER))
                .surname(getAttributeFromJwt(userToken, FAMILY_NAME_IDENTIFIER))
                .mail(getAttributeFromJwt(userToken, EMAIL_IDENTIFIER))
                .createdAt(LocalDateTime.now())
                .build();

        User cachedUser = userHashMap.get(tokenUser.getId());

        if (cachedUser == null || !isCachedUserEqualToTokenUser(cachedUser, tokenUser)) {
            updateCachedAndDatabaseUser(tokenUser);
        }
    }

    protected boolean isCachedUserEqualToTokenUser(User cachedUser, User tokenUser) {
        return cachedUser.getId().equals(tokenUser.getId()) &&
                cachedUser.getMail().equals(tokenUser.getMail()) &&
                cachedUser.getGivenName().equals(tokenUser.getGivenName()) &&
                cachedUser.getSurname().equals(tokenUser.getSurname()) &&
                cachedUser.isAdmin() == tokenUser.isAdmin();
    }

    public void updateCachedAndDatabaseUser(User user) {
        log.info("updating user in cache and database");
        userHashMap.put(user.getId(), userService.updateUser(user));
    }

    protected boolean checkIfKeysAreString(Set<?> keySet) {
        return keySet.stream().allMatch(key -> key instanceof String);
    }

    protected String getAttributeFromJwt(Jwt userToken, String attributeName) throws InvalidTokenException {
        return validateString(userToken.getClaims().get(attributeName), attributeName);
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

    protected void throwIfUserIsNull(User user) throws EntityNotFoundException {
        if (user == null) {
            throw new EntityNotFoundException("user could not be found in hashmap");
        }
    }

    protected boolean checkIfListContainsStrings(ArrayList<?> list) {
        return list.stream().allMatch(key -> key instanceof String);
    }
}

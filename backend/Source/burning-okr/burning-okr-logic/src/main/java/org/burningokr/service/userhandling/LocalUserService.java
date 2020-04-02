package org.burningokr.service.userhandling;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.users.ChangePasswordData;
import org.burningokr.model.users.ForgotPassword;
import org.burningokr.model.users.LocalUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.LocalUserRepository;
import org.burningokr.service.activity.ActivityService;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

@RequiredArgsConstructor
public class LocalUserService implements UserService {
  private final Logger logger = LoggerFactory.getLogger(LocalUserService.class);

  private final LocalUserRepository localUserRepository;
  private final PasswordService passwordService;
  private final ActivityService activityService;

  @Override
  public Collection<User> findAll() {
    return Lists.newArrayList(localUserRepository.findAll());
  }

  @Override
  public LocalUser getCurrentUser() {
    OAuth2Authentication auth =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    Object decodedDetails = ((OAuth2AuthenticationDetails) auth.getDetails()).getDecodedDetails();
    Gson g = new Gson();
    String userString = g.toJson(decodedDetails);
    return parseUserString(userString);
  }

  @Override
  public User findById(UUID userId) {
    return localUserRepository.findByIdOrThrow(userId);
  }

  @Override
  public boolean doesUserExist(UUID userId) {
    Optional<LocalUser> user = localUserRepository.findById(userId);
    return user.isPresent();
  }

  private LocalUser parseUserString(String userString) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper
          .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(userString, LocalUser.class);
    } catch (IOException ex) {
      logger.error("There was an error parsing the User Token.");
      logger.debug("parseUserString", ex);
    }
    return null;
  }

  /**
   * Create a Local User and send their password.
   *
   * @param user an {@link User} object
   * @return an {@link LocalUser} object
   */
  public LocalUser createLocalUser(User user) {
    return createLocalUser(user, true);
  }

  /**
   * Create a Local User.
   *
   * @param user an {@link User} object
   * @param sendPassword if set to true a change password email will be sent
   * @return an {@link LocalUser} object
   */
  public LocalUser createLocalUser(User user, boolean sendPassword) {
    LocalUser localUser = (LocalUser) user;
    localUser.setCreatedAt(LocalDateTime.now());
    LocalUser result = localUserRepository.save(localUser);

    if (sendPassword) {
      passwordService.sendPasswordLinkToNewUser(result);
    }

    activityService.createActivity(localUser, localUser, Action.CREATED);
    return result;
  }

  /**
   * Creates a list of Local Users.
   *
   * @param users a {@link Collection} of {@link User}
   * @return a {@link Collection} of {@link User}
   */
  public Collection<User> bulkCreateLocalUsers(Collection<User> users) {
    List<User> ret = new ArrayList<>();
    users.forEach(user -> ret.add(createLocalUser(user)));

    return ret;
  }

  public LocalUser retrieveLocalUserById(UUID id) {
    return localUserRepository.findByIdOrThrow(id);
  }

  public Collection<User> retrieveAllUsers() {
    return Lists.newArrayList(localUserRepository.findAll());
  }

  /**
   * Update a Local User.
   *
   * @param userId an {@link UUID} object
   * @param user an {@link User} object
   * @return a {@link LocalUser} object
   */
  public LocalUser updateLocalUser(UUID userId, User user) {
    LocalUser localUser = (LocalUser) user;
    LocalUser result = localUserRepository.findByIdOrThrow(userId);
    result.setPhoto(localUser.getPhoto());
    result.setGivenName(localUser.getGivenName());
    result.setSurname(localUser.getSurname());
    result.setDepartment(localUser.getDepartment());
    result.setJobTitle(localUser.getJobTitle());
    result.setMail(localUser.getMail());
    result.setActive(localUser.isActive());

    localUserRepository.save(result);

    activityService.createActivity(result, result, Action.EDITED);
    return result;
  }

  /**
   * Delete Local User.
   *
   * @param id an {@link UUID} object
   */
  public void deleteLocalUserById(UUID id) {
    LocalUser localUser = localUserRepository.findByIdOrThrow(id);
    localUser.setActive(false);
    localUserRepository.save(localUser);
    activityService.createActivity(localUser, localUser, Action.DELETED);
  }

  public void setPassword(UUID emailIdentifier, String password) {
    passwordService.setPassword(emailIdentifier, password);
  }

  public void changePassword(ChangePasswordData changePasswordData) {
    passwordService.changePassword(changePasswordData);
  }

  /**
   * Create a Password Link to reset the password of a User.
   *
   * @param forgotPassword a {@link ForgotPassword} object
   */
  public void resetPassword(ForgotPassword forgotPassword) {
    Optional<LocalUser> user = localUserRepository.findByMail(forgotPassword.getEmail());
    if (user.isPresent()) {
      passwordService.sendPasswordLinkToUser(user.get());
    } else {
      throw new EntityNotFoundException("An User with the given e-mail address does not exist.");
    }
  }
}

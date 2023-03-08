package org.burningokr.service.userhandling;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.users.IUser;
import org.burningokr.model.users.LocalUser;
import org.burningokr.repositories.users.LocalUserRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.DuplicateEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class LocalUserService implements UserService {
  private final Logger logger = LoggerFactory.getLogger(LocalUserService.class);

  private final LocalUserRepository localUserRepository;
  private final ActivityService activityService;

  @Override
  public Collection<IUser> findAll() {
    return Lists.newArrayList(localUserRepository.findAll());
  }

  @Override
  public Collection<IUser> findAllActive() {
    return Lists.newArrayList(localUserRepository.findByActive(true));
  }

  @Override
  public Collection<IUser> findAllInactive() {
    return Lists.newArrayList(localUserRepository.findByActive(false));
  }

  @Override
  public LocalUser getCurrentUser() {
    // TODO fix auth
    return null;
  }

  @Override
  public LocalUser findById(UUID userId) {
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
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
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
   * @param IUser an {@link IUser} object
   * @return an {@link LocalUser} object
   */
  public LocalUser createLocalUser(IUser IUser) {
    return createLocalUser(IUser, true);
  }

  /**
   * Create a Local User.
   *
   * @param IUser        an {@link IUser} object
   * @param sendPassword if set to true a change password email will be sent
   * @return an {@link LocalUser} object
   */
  public LocalUser createLocalUser(IUser IUser, boolean sendPassword) {
    LocalUser localUser = (LocalUser) IUser;
    localUser.setCreatedAt(LocalDateTime.now());
    LocalUser result;
    try {
      result = localUserRepository.save(localUser);
    } catch (DataIntegrityViolationException e) {

      // (R.J. 14.10.20)
      // The only DataIntegrityViolationException possible is an exception due to a duplicate email
      // adress
      // So we can simply throw a DuplicateEmailException.
      throw new DuplicateEmailException("The given Email adress already exists.");
    }

    activityService.createActivity(localUser, localUser, Action.CREATED);
    return result;
  }

  /**
   * Creates a list of Local Users.
   *
   * @param IUsers a {@link Collection} of {@link IUser}
   * @return a {@link Collection} of {@link IUser}
   */
  public Collection<IUser> bulkCreateLocalUsers(Collection<IUser> IUsers) {
    List<IUser> ret = new ArrayList<>();
    IUsers.forEach(user -> ret.add(createLocalUser(user)));

    return ret;
  }

  /**
   * Update a Local User.
   *
   * @param userId an {@link UUID} object
   * @param IUser  an {@link IUser} object
   * @return a {@link LocalUser} object
   */
  public LocalUser updateLocalUser(UUID userId, IUser IUser) {
    LocalUser localUser = (LocalUser) IUser;
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
}

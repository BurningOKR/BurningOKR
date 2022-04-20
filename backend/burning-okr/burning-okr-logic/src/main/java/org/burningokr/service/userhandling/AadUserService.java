package org.burningokr.service.userhandling;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.burningokr.model.users.AadUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.AadUserRepository;
import org.burningokr.service.exceptions.AzureApiException;
import org.burningokr.service.exceptions.AzureUserFetchException;
import org.burningokr.service.userutil.AadUserListUpdater;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class AadUserService implements UserService {

  private final AadUserRepository aadUserRepository;
  private final AadUserListUpdater aadUserListUpdater;
  private final String everyHalfHourCronExpr = "0 0/30 * * * ?";
  private final Logger logger = LoggerFactory.getLogger(AadUserListUpdater.class);

  @Autowired
  public AadUserService(
      AadUserRepository aadUserRepository, AadUserListUpdater aadUserListUpdater) {
    this.aadUserRepository = aadUserRepository;
    this.aadUserListUpdater = aadUserListUpdater;
  }

  @Override
  public Collection<User> findAll() {
    return Lists.newArrayList(this.aadUserRepository.findAll());
  }

  @Override
  public Collection<User> findAllActive() {
    return findAll();
  }

  /**
   * Loads the Current User from OAuth2Authentication.
   *
   * @return an {@link User} object
   */
  @Override
  public User getCurrentUser() {
    OAuth2Authentication auth =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    Gson g = new Gson();
    String userString = g.toJson(auth.getUserAuthentication().getDetails());
    return parseUserString(userString);
  }

  @Override
  public User findById(UUID userId) {
    return aadUserRepository.findByIdOrThrow(userId);
  }

  @Override
  public boolean doesUserExist(UUID userId) {
    Optional<AadUser> user = aadUserRepository.findById(userId);
    return user.isPresent();
  }

  private User parseUserString(String userString) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();

      return objectMapper
          .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(userString, AadUser.class);
    } catch (IOException ex) {
      logger.error("There was an error parsing the User Token.");
      logger.debug("parseUserString", ex);
    }

    return null;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void updateUsersAtStartup() throws AzureUserFetchException, AzureApiException {
    updateUsers();
  }

  @Async
  void updateUsers() throws AzureUserFetchException, AzureApiException {
    logger.info("Updating user database...");
    this.aadUserListUpdater.updateAadUserList();
    logger.info("Successfully updated user database.");
  }

  @Scheduled(cron = everyHalfHourCronExpr)
  private void updateUsersAtFixedRate() throws AzureUserFetchException, AzureApiException {
    updateUsers();
  }
}

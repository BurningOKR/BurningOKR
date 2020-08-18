package org.burningokr.service.initialisation;

import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.initialisation.InitState;
import org.burningokr.model.initialisation.InitStateName;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.LocalUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.initialisation.InitStateRepository;
import org.burningokr.service.configuration.OAuthClientDetailsService;
import org.burningokr.service.configuration.OAuthConfigurationService;
import org.burningokr.service.exceptions.InvalidInitStateException;
import org.burningokr.service.userhandling.AdminUserService;
import org.burningokr.service.userhandling.LocalUserService;
import org.burningokr.service.userhandling.PasswordService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InitService {

  private final InitStateRepository initStateRepository;
  private final UserService userService;
  private final AdminUserService adminUserService;
  private final PasswordService passwordService;
  private final InitOrderService initOrderService;
  private final OAuthClientDetailsService oauthClientDetailsService;
  private final OAuthConfigurationService oauthConfigurationService;
  private final ApplicationContext applicationContext;

  @EventListener(ApplicationReadyEvent.class)
  @Order(1)
  public void onApplicationEvent() {
    setCurrentInitStateToInitialInitStateIfNoneIsPresent();
  }

  /**
   * gets the current InitState.
   *
   * @return the current InitState
   */
  public InitState getInitState() throws EntityNotFoundException {
    List<InitState> initStates = Lists.newArrayList(initStateRepository.findAll());
    if (!initStates.isEmpty()) {
      InitState initState = initStates.get(0);
      initState.setRuntimeId(applicationContext.getId());
      return initState;
    } else {
      throw new EntityNotFoundException();
    }
  }

  /**
   * saves the current InitState.
   *
   * @param initState the new InitState. Use getInitState()!
   */
  public InitState saveInitState(InitState initState) {
    return initStateRepository.save(initState);
  }

  /**
   * checks whether the current InitState matches the InitStateName given.
   *
   * @param initStateName The InitStateName to check against
   * @return true when the InitStateName matches, false otherwise.
   */
  public boolean checkInitState(InitStateName initStateName) {
    return initStateName == getInitState().getInitState();
  }

  /**
   * only for InitState SET_OAUTH_CLIENT_DETAILS. Sets the OAuthClientDetials
   *
   * @param oauthClientDetails The ClientDetails that should be set.
   * @return the new InitState
   */
  public InitState setOAuthClientDetails(OAuthClientDetails oauthClientDetails) {
    isInitStateElseThrow(InitStateName.SET_OAUTH_CLIENT_DETAILS);

    oauthClientDetailsService.fillDefaultValues(oauthClientDetails);
    oauthConfigurationService.updateOAuthConfiguration(oauthClientDetails);
    oauthClientDetailsService.encodeClientSecret(oauthClientDetails);
    oauthClientDetailsService.updateOAuthClientDetails(oauthClientDetails);

    return nextInitState();
  }

  /**
   * only for InitState CREATE_USER. Creates an Admin User.
   *
   * @param adminUser The user that should be created
   * @param password The password of the new user
   * @return the new InitState
   * @throws InvalidInitStateException when the current {@link InitState} is not CREATE_USER.
   */
  public InitState setAdminUser(User adminUser, String password) throws InvalidInitStateException {
    isInitStateElseThrow(InitStateName.CREATE_USER);

    LocalUser user = ((LocalUserService) userService).createLocalUser(adminUser, false);
    passwordService.setPassword(user, password);
    adminUserService.addAdmin(createAdminUser(user));

    return nextInitState();
  }

  /**
   * only for InitState NO_AZURE_ADMIN_USER. Sets the given Azure User as an Admin.
   *
   * @param adminUser
   * @return
   * @throws InvalidInitStateException
   */
  public InitState setAzureAdminUser(AdminUser adminUser) throws InvalidInitStateException {
    isInitStateElseThrow(InitStateName.NO_AZURE_ADMIN_USER);

    adminUserService.addAdmin(adminUser);

    return nextInitState();
  }

  private InitState nextInitState() throws InvalidInitStateException {

    InitState currentInitState = getInitState();

    InitStateName nextInitStateName =
        initOrderService.getNextInitState(currentInitState.getInitState());

    currentInitState.setInitState(nextInitStateName);
    return saveInitState(currentInitState);
  }

  private void setCurrentInitStateToInitialInitStateIfNoneIsPresent() {
    List<InitState> initStates = Lists.newArrayList(initStateRepository.findAll());

    if (initStates.isEmpty()) {
      final InitState initialInitState = new InitState();
      initialInitState.setInitState(this.initOrderService.getInitialInitState());
      saveInitState(initialInitState);
    }
  }

  private void isInitStateElseThrow(InitStateName initStateName) throws InvalidInitStateException {
    if (!checkInitState(initStateName)) {
      throw new InvalidInitStateException("This method is not allowed in the current InitState");
    }
  }

  private AdminUser createAdminUser(LocalUser user) {
    AdminUser adminUser = new AdminUser();
    adminUser.setId(user.getId());
    return adminUser;
  }
}

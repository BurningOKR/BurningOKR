package org.burningokr.controller.initialisation;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.configuration.OAuthClientDetailsDto;
import org.burningokr.dto.initialisation.AdminAccountInitialisationDto;
import org.burningokr.dto.initialisation.InitStateDto;
import org.burningokr.dto.users.LocalUserDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.initialisation.InitState;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.User;
import org.burningokr.service.initialisation.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RestApiController
@RequiredArgsConstructor
public class InitController {

  private final DataMapper<InitState, InitStateDto> initStateMapper;
  private final DataMapper<User, LocalUserDto> userMapper;
  private final DataMapper<OAuthClientDetails, OAuthClientDetailsDto> oauthClientDetailsMapper;
  private final InitService initService;
  private final Logger logger = LoggerFactory.getLogger(InitController.class);

  /**
   * Get the current init state
   * @return the current init state
   */
  @GetMapping("/init")
  public ResponseEntity<InitStateDto> init() {
    InitState initState = this.initService.getInitState();
    return ResponseEntity.ok(initStateMapper.mapEntityToDto(initState));
  }

  /**
   * Tells the client, that the server won't cook coffee, because it is a teapot.
   * @return the message "I am a teapot"
   */
  @GetMapping("/init/teapot")
  public ResponseEntity<String> teapot() {
    logger.warn("Someone asked me to cook coffee, but i am a teapot.");
    return ResponseEntity.status(418).body("I am a Teapot");
  }

  /**
   * Sets the oauthClientDetails of the Local Authorization Server.
   *
   * @param oauthClientDetailsDto an {@link OAuthClientDetailsDto} object
   * @return the new InitState
   */
  @PostMapping("/init/oauth-server")
  public ResponseEntity<InitStateDto> setOauthServerSettings(
      @Valid @RequestBody OAuthClientDetailsDto oauthClientDetailsDto) {

    OAuthClientDetails oauthClientDetails =
        oauthClientDetailsMapper.mapDtoToEntity(oauthClientDetailsDto);
    InitState initState = initService.setOAuthClientDetails(oauthClientDetails);

    return ResponseEntity.ok(initStateMapper.mapEntityToDto(initState));
  }

  /**
   * Initialises a new Admin Account with a password in a local environment.
   *
   * @param adminAccountInitialisationDto an {@link AdminAccountInitialisationDto} object
   * @return the new InitState
   */
  @PostMapping("/init/admin-account")
  public ResponseEntity<InitStateDto> setAdminAccount(
      @Valid @RequestBody AdminAccountInitialisationDto adminAccountInitialisationDto) {

    User user = userMapper.mapDtoToEntity(adminAccountInitialisationDto.getUserDto());
    InitState initState =
        initService.setAdminUser(user, adminAccountInitialisationDto.getPassword());

    return ResponseEntity.ok(initStateMapper.mapEntityToDto(initState));
  }

  /**
   * Sets the admin user in an azure oauth environment
   *
   * @param adminUser an {@link AdminUser} object
   * @return the new InitState
   */
  @PostMapping("/init/azure-admin-user")
  public ResponseEntity<InitStateDto> setAdminAzureAdminUser(
      @Valid @RequestBody AdminUser adminUser) {

    InitState initState = initService.setAzureAdminUser(adminUser);

    return ResponseEntity.ok(initStateMapper.mapEntityToDto(initState));
  }
}

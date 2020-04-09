package org.burningokr.controller.initialisation;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.BurningOkrApp;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.configuration.OAuthClientDetailsDto;
import org.burningokr.dto.initialisation.AdminAccountInitialisationDto;
import org.burningokr.dto.initialisation.InitStateDto;
import org.burningokr.dto.users.LocalUserDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.initialisation.InitState;
import org.burningokr.model.users.User;
import org.burningokr.service.initialisation.InitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;

@RestApiController
@RequiredArgsConstructor
public class InitController {

  private final DataMapper<InitState, InitStateDto> initStateMapper;
  private final DataMapper<User, LocalUserDto> userMapper;
  private final DataMapper<OAuthClientDetails, OAuthClientDetailsDto> oauthClientDetailsMapper;
  private final InitService initService;

  @GetMapping("/init")
  public ResponseEntity<InitStateDto> init() {
    InitState initState = this.initService.getInitState();
    return ResponseEntity.ok(initStateMapper.mapEntityToDto(initState));
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

    BurningOkrApp.restart();

    return ResponseEntity.ok(initStateMapper.mapEntityToDto(initState));
  }

  /**
   * Initialises a new Admin Account with a password.
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
}

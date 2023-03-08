package org.burningokr.controller.settings;

import jakarta.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.settings.UserSettingsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.users.IUser;
import org.burningokr.service.security.AuthorizationService;
import org.burningokr.service.settings.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class UserSettingsController {

  private UserSettingsService userSettingsService;
  private DataMapper<UserSettings, UserSettingsDto> mapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize UserSettingsController.
   *
   * @param userSettingsService  an {@link UserSettingsService} object
   * @param dataMapper           a {@link DataMapper} object with {@link UserSettings} and {@link
   *                             UserSettingsDto}
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public UserSettingsController(
    UserSettingsService userSettingsService,
    DataMapper<UserSettings, UserSettingsDto> dataMapper,
    AuthorizationService authorizationService
  ) {
    this.userSettingsService = userSettingsService;
    this.mapper = dataMapper;
    this.authorizationService = authorizationService;
  }

  /**
   * API Endpoint to get the user settings of an user.
   *
   * @param IUser an {@link IUser} object
   * @return a {@link ResponseEntity} ok with the user settings.
   */
  @GetMapping("/settings")
  public ResponseEntity<UserSettingsDto> getUserSettings(IUser IUser) {
    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(IUser);
    return ResponseEntity.ok(this.mapper.mapEntityToDto(userSettings));
  }

  /**
   * API Endpoint to update the user settings.
   *
   * @param userSettingsDto an {@link UserSettingsDto} object
   * @param IUser           an {@link IUser} object
   * @return a {@link ResponseEntity} ok with the updated user settings
   */
  @PutMapping("/settings")
  public ResponseEntity<UserSettingsDto> updateUserSettingsDto(
    @Valid
    @RequestBody
    UserSettingsDto userSettingsDto, IUser IUser
  ) {
    UserSettings userSettings = this.mapper.mapDtoToEntity(userSettingsDto);
    return ResponseEntity.ok(
      this.mapper.mapEntityToDto(
        this.userSettingsService.updateUserSettings(userSettings, IUser)));
  }
}

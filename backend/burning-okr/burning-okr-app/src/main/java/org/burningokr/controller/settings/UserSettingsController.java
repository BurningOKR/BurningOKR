package org.burningokr.controller.settings;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.settings.UserSettingsDto;
import org.burningokr.mapper.settings.UserSettingsMapper;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.burningokr.service.settings.UserSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
@RequiredArgsConstructor
public class UserSettingsController {

  private final UserSettingsService userSettingsService;
    private final UserSettingsMapper mapper;
    private final AuthenticationUserContextService authenticationUserContextService;

  @GetMapping("/settings")
  public ResponseEntity<UserSettingsDto> getUserSettings() {
      User user = authenticationUserContextService.getAuthenticatedUser();
      UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);
    return ResponseEntity.ok(this.mapper.mapEntityToDto(userSettings));
  }

  /**
   * API Endpoint to update the user settings.
   *
   * @param userSettingsDto an {@link UserSettingsDto} object
   * @return a {@link ResponseEntity} ok with the updated user settings
   */
  @PutMapping("/settings")
  public ResponseEntity<UserSettingsDto> updateUserSettingsDto(
    @Valid
    @RequestBody
    UserSettingsDto userSettingsDto
  ) {
    UserSettings userSettings = this.mapper.mapDtoToEntity(userSettingsDto);
    return ResponseEntity.ok(
      this.mapper.mapEntityToDto(
        this.userSettingsService.updateUserSettings(userSettings)));
  }
}

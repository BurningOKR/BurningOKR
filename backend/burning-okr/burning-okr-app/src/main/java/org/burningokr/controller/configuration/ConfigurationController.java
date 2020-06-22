package org.burningokr.controller.configuration;

import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.annotation.TurnOff;
import org.burningokr.dto.configuration.ConfigurationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.model.users.User;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.mail.MailService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiController
@RequiredArgsConstructor
public class ConfigurationController {

  private final ConfigurationService configurationService;
  private final DataMapper<Configuration, ConfigurationDto> dataMapper;
  private final AuthorizationService authorizationService;
  private final MailService mailService;

  @GetMapping("/configurations/hasmail")
  public boolean getHasMailConfigured() {
    return mailService.hasMailConfigured();
  }

  @GetMapping("/configurations")
  public ResponseEntity<Collection<ConfigurationDto>> getConfigurations() {
    Collection<Configuration> configurations = configurationService.getAllConfigurations();
    return ResponseEntity.ok().body(dataMapper.mapEntitiesToDtos(configurations));
  }

  @GetMapping("/configurations/id/{configurationId}")
  public ResponseEntity<ConfigurationDto> getConfigurationById(@PathVariable Long configurationId) {
    Configuration configuration = configurationService.getConfigurationById(configurationId);
    return ResponseEntity.ok().body(dataMapper.mapEntityToDto(configuration));
  }

  @GetMapping("/configurations/name/{configurationName}")
  public ResponseEntity<ConfigurationDto> getConfigurationByName(
      @PathVariable String configurationName) {
    Configuration configuration = configurationService.getConfigurationByName(configurationName);
    return ResponseEntity.ok().body(dataMapper.mapEntityToDto(configuration));
  }

  /**
   * API Endpoint to add Configuration.
   *
   * @param configurationDto a {@link ConfigurationDto} object
   * @param user an {@link User} object
   * @return
   */
  @PostMapping("/configurations")
  @TurnOff
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<ConfigurationDto> createConfiguration(
      @Valid @RequestBody ConfigurationDto configurationDto, User user) {
    Configuration requestConfigurationEntity = dataMapper.mapDtoToEntity(configurationDto);
    Configuration responseConfigurationEntity =
        configurationService.createConfiguration(requestConfigurationEntity, user);
    return ResponseEntity.ok().body(dataMapper.mapEntityToDto(responseConfigurationEntity));
  }

  /**
   * API Endpoint to update a Configuration.
   *
   * @param configurationId a long value
   * @param configurationDto a {@link ConfigurationDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Configuration
   */
  @PutMapping("/configurations/{configurationId}")
  @TurnOff
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<ConfigurationDto> updateConfigurationById(
      @PathVariable Long configurationId,
      @Valid @RequestBody ConfigurationDto configurationDto,
      User user) {
    Configuration requestConfigurationEntity = dataMapper.mapDtoToEntity(configurationDto);
    Configuration responseConfigurationEntity =
        configurationService.updateConfigurationById(requestConfigurationEntity, user);
    return ResponseEntity.ok().body(dataMapper.mapEntityToDto(responseConfigurationEntity));
  }

  @DeleteMapping("/configurations/{configurationId}")
  @TurnOff
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity deleteConfigurationById(@PathVariable Long configurationId, User user) {
    configurationService.deleteConfigurationById(configurationId, user);
    return ResponseEntity.ok().build();
  }
}

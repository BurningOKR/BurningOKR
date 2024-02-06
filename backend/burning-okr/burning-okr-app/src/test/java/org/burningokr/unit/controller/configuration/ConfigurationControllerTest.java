package org.burningokr.unit.controller.configuration;

import org.burningokr.controller.configuration.ConfigurationController;
import org.burningokr.dto.configuration.ConfigurationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.mail.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigurationControllerTest {

  private ConfigurationController configurationController;
  @Mock
  private ConfigurationService configurationService;
  @Mock
  private DataMapper<Configuration, ConfigurationDto> dataMapper;
  @Mock
  private MailService mailService;

  @BeforeEach
  void setUp() {
    this.configurationController = new ConfigurationController(
      this.configurationService,
      this.dataMapper,
      this.mailService
    );
  }

  @Test
  void getHasMailConfigured_shouldReturnTrue() {
    when(this.mailService.hasMailConfigured()).thenReturn(true);
    Assertions.assertTrue(this.configurationController.getHasMailConfigured());
  }

  @Test
  void getHasMailConfigured_shouldReturnFalse() {
    when(this.mailService.hasMailConfigured()).thenReturn(false);
    Assertions.assertFalse(this.configurationController.getHasMailConfigured());
  }

  @Test
  void getConfigurations_shouldReturnResponseWithCodeOK() {
    when(this.configurationService.getAllConfigurations()).thenReturn(new ArrayList<>());
    when(this.dataMapper.mapEntitiesToDtos(any())).thenReturn(new ArrayList<>());
    int expected = 200;

    ResponseEntity<Collection<ConfigurationDto>> response = this.configurationController.getConfigurations();
    int actual = response.getStatusCode().value();

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void getConfigurations_shouldReturnResponseWithEmptyList() {
    when(this.configurationService.getAllConfigurations()).thenReturn(new ArrayList<>());
    when(this.dataMapper.mapEntitiesToDtos(any())).thenReturn(new ArrayList<>());
    int expectedSize = 0;

    ResponseEntity<Collection<ConfigurationDto>> response = this.configurationController.getConfigurations();

    Assertions.assertNotNull(response.getBody());
    int actualSize = response.getBody().size();
    Assertions.assertEquals(expectedSize, actualSize);
  }

  @Test
  void getConfigurations_shouldReturnResponseWithListContainingElements() {
    ConfigurationDto configDto1 = ConfigurationDto.builder()
      .id(1L)
      .name("Config 1")
      .value("Value 1")
      .build();
    ConfigurationDto configDto2 = ConfigurationDto.builder()
      .id(1L)
      .name("Config 2")
      .value("Value 2")
      .build();

    when(this.configurationService.getAllConfigurations()).thenReturn(new ArrayList<>());
    when(this.dataMapper.mapEntitiesToDtos(any())).thenReturn(Arrays.asList(configDto1, configDto2));

    ResponseEntity<Collection<ConfigurationDto>> response = this.configurationController.getConfigurations();

    Assertions.assertNotNull(response.getBody());
    Collection<ConfigurationDto> body = response.getBody();
    Assertions.assertEquals(2, body.size());
    Assertions.assertTrue(body.contains(configDto1));
    Assertions.assertTrue(body.contains(configDto2));
  }

  @Test
  void getConfigurationById_shouldReturnResponseWithCodeOK() {
    when(this.configurationService.getConfigurationById(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(null);
    int expected = 200;

    ResponseEntity<ConfigurationDto> response = this.configurationController.getConfigurationById(1L);
    int actual = response.getStatusCode().value();

    Assertions.assertEquals(expected,actual);
  }

  @Test
  void getConfigurationById_shouldReturnResponseContainingDto() {
    ConfigurationDto configDto = ConfigurationDto.builder()
      .id(1L)
      .name("Config")
      .value("Value")
      .build();
    when(this.configurationService.getConfigurationById(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(configDto);

    ResponseEntity<ConfigurationDto> response = this.configurationController.getConfigurationById(1L);

    Assertions.assertNotNull(response.getBody());
    ConfigurationDto actualDto = response.getBody();
    Assertions.assertEquals(configDto, actualDto);
  }

  @Test
  void getConfigurationByName_shouldReturnResponseWithCodeOK() {
    when(this.configurationService.getConfigurationByName(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(any());
    int expected = 200;

    ResponseEntity<ConfigurationDto> response = this.configurationController.getConfigurationByName("dummy");
    int actual = response.getStatusCode().value();

    Assertions.assertEquals(expected,actual);
  }

  @Test
  void getConfigurationByName_shouldReturnResponseContainingDto() {
    ConfigurationDto configDto = ConfigurationDto.builder()
      .id(1L)
      .name("Config")
      .value("Value")
      .build();
    when(this.configurationService.getConfigurationByName(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(configDto);

    ResponseEntity<ConfigurationDto> response = this.configurationController.getConfigurationByName("dummy");

    Assertions.assertNotNull(response.getBody());
    ConfigurationDto actualDto = response.getBody();
    Assertions.assertEquals(configDto, actualDto);
  }

  @Test
  void createConfiguration_shouldReturnResponseWithCodeOK() {
    when(this.configurationService.createConfiguration(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(null);
    int expected = 200;

    ResponseEntity<ConfigurationDto> response = this.configurationController.createConfiguration(any());
    int actual = response.getStatusCode().value();

    Assertions.assertEquals(expected,actual);
  }

  @Test
  void createConfiguration_shouldReturnResponseContainingDtoOfCreatedEntity() {
    ConfigurationDto configDto = ConfigurationDto.builder()
      .id(1L)
      .name("Config")
      .value("Value")
      .build();
    when(this.dataMapper.mapDtoToEntity(any())).thenReturn(null);
    when(this.configurationService.createConfiguration(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(configDto);

    ResponseEntity<ConfigurationDto> response = this.configurationController.createConfiguration(any());

    Assertions.assertNotNull(response.getBody());
    ConfigurationDto actualDto = response.getBody();
    Assertions.assertEquals(configDto, actualDto);
  }

  @Test
  void updateConfigurationById_shouldReturnResponseWithCodeOK() {
    when(this.dataMapper.mapDtoToEntity(any())).thenReturn(null);
    when(this.configurationService.updateConfigurationById(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(null);
    int expected = 200;

    //configurationId in controller method is never used
    ResponseEntity<ConfigurationDto> response = this.configurationController.updateConfigurationById(anyLong(), null);
    int actual = response.getStatusCode().value();

    Assertions.assertEquals(expected,actual);
  }

  @Test
  void updateConfigurationById_shouldReturnResponseContainingDtoOfUpdatedEntity() {
    ConfigurationDto configDto = ConfigurationDto.builder()
      .id(1L)
      .name("Config")
      .value("Value")
      .build();
    when(this.dataMapper.mapDtoToEntity(any())).thenReturn(null);
    when(this.configurationService.updateConfigurationById(any())).thenReturn(null);
    when(this.dataMapper.mapEntityToDto(any())).thenReturn(configDto);

    //configurationId in controller method is never used
    ResponseEntity<ConfigurationDto> response = this.configurationController.updateConfigurationById(anyLong(), null);

    Assertions.assertNotNull(response.getBody());
    ConfigurationDto actualDto = response.getBody();
    Assertions.assertEquals(configDto, actualDto);
  }

  @Test
  void deleteConfigurationById_shouldReturnResponseWithCodeOK() {
    doNothing().when(this.configurationService).deleteConfigurationById(any());
    int expected = 200;

    ResponseEntity response = this.configurationController.deleteConfigurationById(any());
    int actual = response.getStatusCode().value();

    Assertions.assertEquals(expected,actual);
  }
}

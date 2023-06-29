package org.burningokr.service.configuration;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.repositories.configuration.ConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfigurationServiceTest {

  @Mock
  private ConfigurationRepository configurationRepository;

  @InjectMocks
  private ConfigurationService configurationService;

  @Test
  public void getAllConfiguration_expectEmptyCollection() {
    when(configurationRepository.findAll()).thenReturn(new ArrayList<>());
    Collection<Configuration> configurations = configurationService.getAllConfigurations();
    assertTrue(configurations.isEmpty());
  }

  @Test
  public void getAllConfiguration_expectCollectionWith2ConfigurationEntities() {
    Collection<Configuration> dbResult = Arrays.asList(new Configuration(), new Configuration());
    when(configurationRepository.findAll()).thenReturn(dbResult);
    Collection<Configuration> configurations = configurationService.getAllConfigurations();
    assertEquals(2, configurations.size());
  }

  @Test()
  public void getConfigurationById_expectException() {
    when(configurationRepository.findByIdOrThrow(42L)).thenThrow(EntityNotFoundException.class);
    assertThrows(EntityNotFoundException.class, () -> configurationService.getConfigurationById(42L));
  }

  @Test
  public void getConfigurationById_expectConfiguration() {
    Configuration dbConfiguration = new Configuration();
    dbConfiguration.setId(42L);
    dbConfiguration.setName("name");
    dbConfiguration.setValue("3");
    when(configurationRepository.findByIdOrThrow(42L)).thenReturn(dbConfiguration);
    Configuration configuration = configurationService.getConfigurationById(42L);
    assertEquals(dbConfiguration.getId(), configuration.getId());
    assertEquals(dbConfiguration.getName(), configuration.getName());
    assertEquals(dbConfiguration.getValue(), configuration.getValue());
  }

  @Test()
  public void getConfigurationByName_expectException() {
    when(configurationRepository.findByName("name")).thenThrow(EntityNotFoundException.class);
    assertThrows(EntityNotFoundException.class, () -> configurationService.getConfigurationByName("name"));
  }

  @Test
  public void getConfigurationByName_expectConfiguration() {
    Configuration dbConfiguration = new Configuration();
    dbConfiguration.setId(42L);
    dbConfiguration.setName("name");
    dbConfiguration.setValue("3");
    when(configurationRepository.findByName("name")).thenReturn(Optional.of(dbConfiguration));
    Configuration configuration = configurationService.getConfigurationByName("name");
    assertEquals(dbConfiguration.getId(), configuration.getId());
    assertEquals(dbConfiguration.getName(), configuration.getName());
    assertEquals(dbConfiguration.getValue(), configuration.getValue());
  }

  @Test
  public void createConfiguration_expectConfigurationToBeCreated() {
    Configuration configurationArgument = new Configuration();
    configurationArgument.setName("name");
    configurationArgument.setValue("3");
    when(configurationRepository.save(configurationArgument)).thenReturn(configurationArgument);
    Configuration actual = configurationService.createConfiguration(configurationArgument);
    assertConfigurationsWithoutId(configurationArgument, actual);
  }

  @Test()
  public void updateConfigurationById_expectEntityNotFoundException() {
    when(configurationRepository.findByIdOrThrow(42L)).thenThrow(EntityNotFoundException.class);
    Configuration configuration = new Configuration();
    configuration.setId(42L);
    assertThrows(EntityNotFoundException.class, () -> configurationService.updateConfigurationById(configuration));
  }

  @Test
  public void updateConfigurationById_expectConfigurationToBeSaved() {
    Configuration dbConfiguration = new Configuration();
    dbConfiguration.setId(42L);
    dbConfiguration.setName("name");
    dbConfiguration.setValue("3");
    when(configurationRepository.findByIdOrThrow(42L)).thenReturn(dbConfiguration);
    when(configurationRepository.save(dbConfiguration)).thenReturn(dbConfiguration);
    Configuration configuration = new Configuration();
    configuration.setId(42L);
    configuration.setName("name2");
    configuration.setValue("4");
    Configuration actual = configurationService.updateConfigurationById(configuration);
    assertConfigurations(configuration, actual);
  }

  @Test()
  public void deleteConfigurationById_expectEntityNotFoundException() {
    when(configurationRepository.findByIdOrThrow(42L)).thenThrow(EntityNotFoundException.class);
    assertThrows(EntityNotFoundException.class, () -> configurationService.deleteConfigurationById(42L));
  }

  @Test
  public void deleteConfigurationById_shouldDeleteEntity() {
    Configuration dbConfiguration = new Configuration();
    dbConfiguration.setId(42L);
    dbConfiguration.setName("name");
    dbConfiguration.setValue("3");
    when(configurationRepository.findByIdOrThrow(dbConfiguration.getId()))
      .thenReturn(dbConfiguration);
    configurationService.deleteConfigurationById(dbConfiguration.getId());
    verify(configurationRepository, times(1)).delete(dbConfiguration);
  }

  private void assertConfigurationsWithoutId(Configuration expected, Configuration actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getValue(), actual.getValue());
  }

  private void assertConfigurations(Configuration expected, Configuration actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getValue(), actual.getValue());
  }
}

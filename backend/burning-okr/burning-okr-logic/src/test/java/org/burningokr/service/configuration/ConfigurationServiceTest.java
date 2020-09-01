package org.burningokr.service.configuration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.model.users.User;
import org.burningokr.repositories.configuration.ConfigurationRepository;
import org.burningokr.service.activity.ActivityService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

  @Mock private ConfigurationRepository configurationRepository;

  @Mock private ActivityService activityService;

  @Mock private User mockedUser;

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks private ConfigurationService configurationService;

  @Test
  public void getAllConfiguration_expectEmptyCollection() {
    when(configurationRepository.findAll()).thenReturn(new ArrayList<>());
    Collection<Configuration> configurations = configurationService.getAllConfigurations();
    Assert.assertTrue(configurations.isEmpty());
  }

  @Test
  public void getAllConfiguration_expectCollectionWith2ConfigurationEntities() {
    Collection<Configuration> dbResult = Arrays.asList(new Configuration(), new Configuration());
    when(configurationRepository.findAll()).thenReturn(dbResult);
    Collection<Configuration> configurations = configurationService.getAllConfigurations();
    Assert.assertEquals(2, configurations.size());
  }

  @Test(expected = EntityNotFoundException.class)
  public void getConfigurationById_expectException() {
    when(configurationRepository.findByIdOrThrow(42L)).thenThrow(EntityNotFoundException.class);
    Configuration configuration = configurationService.getConfigurationById(42L);
  }

  @Test
  public void getConfigurationById_expectConfiguration() {
    Configuration dbConfiguration = new Configuration();
    dbConfiguration.setId(42L);
    dbConfiguration.setName("name");
    dbConfiguration.setValue("3");
    when(configurationRepository.findByIdOrThrow(42L)).thenReturn(dbConfiguration);
    Configuration configuration = configurationService.getConfigurationById(42L);
    Assert.assertEquals(dbConfiguration.getId(), configuration.getId());
    Assert.assertEquals(dbConfiguration.getName(), configuration.getName());
    Assert.assertEquals(dbConfiguration.getValue(), configuration.getValue());
  }

  @Test(expected = EntityNotFoundException.class)
  public void getConfigurationByName_expectException() {
    when(configurationRepository.findByName("name")).thenThrow(EntityNotFoundException.class);
    Configuration configuration = configurationService.getConfigurationByName("name");
  }

  @Test
  public void getConfigurationByName_expectConfiguration() {
    Configuration dbConfiguration = new Configuration();
    dbConfiguration.setId(42L);
    dbConfiguration.setName("name");
    dbConfiguration.setValue("3");
    when(configurationRepository.findByName("name")).thenReturn(Optional.of(dbConfiguration));
    Configuration configuration = configurationService.getConfigurationByName("name");
    Assert.assertEquals(dbConfiguration.getId(), configuration.getId());
    Assert.assertEquals(dbConfiguration.getName(), configuration.getName());
    Assert.assertEquals(dbConfiguration.getValue(), configuration.getValue());
  }

  @Test
  public void createConfiguration_expectConfigurationToBeCreated() {
    Configuration configurationArgument = new Configuration();
    configurationArgument.setName("name");
    configurationArgument.setValue("3");
    when(configurationRepository.save(configurationArgument)).thenReturn(configurationArgument);
    Configuration actual =
        configurationService.createConfiguration(configurationArgument, mockedUser);
    assertConfigurationsWithoutId(configurationArgument, actual);
  }

  @Test(expected = EntityNotFoundException.class)
  public void updateConfigurationById_expectEntityNotFoundException() {
    when(configurationRepository.findByIdOrThrow(42L)).thenThrow(EntityNotFoundException.class);
    Configuration configuration = new Configuration();
    configuration.setId(42L);
    configurationService.updateConfigurationById(configuration, mockedUser);
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
    Configuration actual = configurationService.updateConfigurationById(configuration, mockedUser);
    assertConfigurations(configuration, actual);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteConfigurationById_expectEntityNotFoundException() {
    when(configurationRepository.findByIdOrThrow(42L)).thenThrow(EntityNotFoundException.class);
    configurationService.deleteConfigurationById(42L, mockedUser);
  }

  @Test
  public void deleteConfigurationById_shouldDeleteEntity() {
    Configuration dbConfiguration = new Configuration();
    dbConfiguration.setId(42L);
    dbConfiguration.setName("name");
    dbConfiguration.setValue("3");
    when(configurationRepository.findByIdOrThrow(dbConfiguration.getId()))
        .thenReturn(dbConfiguration);
    configurationService.deleteConfigurationById(dbConfiguration.getId(), mockedUser);
    verify(configurationRepository, times(1)).delete(dbConfiguration);
  }

  private void assertConfigurationsWithoutId(Configuration expected, Configuration actual) {
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getValue(), actual.getValue());
  }

  private void assertConfigurations(Configuration expected, Configuration actual) {
    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getValue(), actual.getValue());
  }
}

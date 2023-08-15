package org.burningokr.service.configuration;

import com.google.common.collect.Lists;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.repositories.configuration.ConfigurationRepository;
import org.burningokr.service.ConfigurationChangedEvent;
import org.burningokr.service.activity.ActivityService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationService {

  private final ConfigurationRepository configurationRepository;
  private final ActivityService activityService;
  private final ApplicationEventPublisher applicationEventPublisher;

  public Collection<Configuration> getAllConfigurations() {
    return Lists.newArrayList(configurationRepository.findAll());
  }

  public Configuration getConfigurationById(Long configurationId) {
    return configurationRepository.findByIdOrThrow(configurationId);
  }

  /**
   * Gets a Configuration by Name.
   *
   * @param name a String value
   * @return a {@link Configuration} object
   */
  public Configuration getConfigurationByName(String name) {
    Optional<Configuration> configuration = configurationRepository.findByName(name);
    if (configuration.isPresent()) {
      return configuration.get();
    } else {
      throw new EntityNotFoundException("Entity with name " + name + " could not be found");
    }
  }

  /**
   * Creates a Configuration.
   *
   * @param configuration a {@link Configuration} object
   * @return a {@link Configuration} object
   */
  public Configuration createConfiguration(Configuration configuration) {
    Configuration result = configurationRepository.save(configuration);
    log.debug("Created configuration %s (id: %d.)".formatted(result.getName(), result.getId()));
    activityService.createActivity(result, Action.CREATED);
    return result;
  }

  /**
   * Updates a Configuration by ID.
   *
   * @param configuration a {@link Configuration} object
   * @return a {@link Configuration} object
   */
  public Configuration updateConfigurationById(Configuration configuration) {
    Configuration dbConfiguration = configurationRepository.findByIdOrThrow(configuration.getId());
    dbConfiguration.setValue(configuration.getValue());
    dbConfiguration.setName(configuration.getName());
    log.debug("Updated configuration %s (id: %d.)".formatted(dbConfiguration.getName(), dbConfiguration.getId()));
    activityService.createActivity(dbConfiguration, Action.EDITED);
    publishConfigurationChangedEvent(dbConfiguration);
    return configurationRepository.save(dbConfiguration);
  }

  /**
   * Deletes a Configuration by ID.
   *
   * @param configurationId a long value
   */
  public void deleteConfigurationById(Long configurationId) {
    Configuration configuration = configurationRepository.findByIdOrThrow(configurationId);
    log.debug("Deleted configuration %s (id: %d.)".formatted(configuration.getName(), configuration.getId()));
    activityService.createActivity(configuration, Action.DELETED);
    configurationRepository.delete(configuration);
  }

  private void publishConfigurationChangedEvent(final Configuration configuration) {
    ConfigurationChangedEvent configurationChangedEvent =
      new ConfigurationChangedEvent(this, configuration);
    applicationEventPublisher.publishEvent(configurationChangedEvent);
  }
}

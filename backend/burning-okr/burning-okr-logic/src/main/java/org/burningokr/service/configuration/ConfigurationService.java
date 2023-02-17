package org.burningokr.service.configuration;

import com.google.common.collect.Lists;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.model.users.User;
import org.burningokr.repositories.configuration.ConfigurationRepository;
import org.burningokr.service.ConfigurationChangedEvent;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

  private final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

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
   * @param user          an {@link User} object
   * @return a {@link Configuration} object
   */
  public Configuration createConfiguration(Configuration configuration, User user) {
    Configuration result = configurationRepository.save(configuration);
    logger.info("Created configuration " + result.getName() + " (id: " + result.getId() + ")");
    activityService.createActivity(user, result, Action.CREATED);
    return result;
  }

  /**
   * Updates a Configuration by ID.
   *
   * @param configuration a {@link Configuration} object
   * @param user          an {@link User} object
   * @return a {@link Configuration} object
   */
  public Configuration updateConfigurationById(Configuration configuration, User user) {
    Configuration dbConfiguration = configurationRepository.findByIdOrThrow(configuration.getId());
    dbConfiguration.setValue(configuration.getValue());
    dbConfiguration.setName(configuration.getName());
    logger.info(
      "Updated configuration "
        + dbConfiguration.getName()
        + " (id: "
        + dbConfiguration.getId()
        + ")");
    activityService.createActivity(user, dbConfiguration, Action.EDITED);
    publishConfigurationChangedEvent(dbConfiguration);
    return configurationRepository.save(dbConfiguration);
  }

  /**
   * Deletes a Configuration by ID.
   *
   * @param configurationId a long value
   * @param user            an {@link User} object
   */
  public void deleteConfigurationById(Long configurationId, User user) {
    Configuration configuration = configurationRepository.findByIdOrThrow(configurationId);
    logger.info(
      "Deleted configuration "
        + configuration.getName()
        + " (id: "
        + configuration.getId()
        + ")");
    activityService.createActivity(user, configuration, Action.DELETED);
    configurationRepository.delete(configuration);
  }

  private void publishConfigurationChangedEvent(final Configuration configuration) {
    ConfigurationChangedEvent configurationChangedEvent =
      new ConfigurationChangedEvent(this, configuration);
    applicationEventPublisher.publishEvent(configurationChangedEvent);
  }
}

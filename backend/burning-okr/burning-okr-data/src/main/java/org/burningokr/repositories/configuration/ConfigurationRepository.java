package org.burningokr.repositories.configuration;

import org.burningokr.model.configuration.Configuration;
import org.burningokr.repositories.ExtendedRepository;

import java.util.Optional;

public interface ConfigurationRepository extends ExtendedRepository<Configuration, Long> {
  Optional<Configuration> findByName(String name);
}

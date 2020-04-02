package org.burningokr.repositories.configuration;

import java.util.Optional;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.repositories.ExtendedRepository;

public interface ConfigurationRepository extends ExtendedRepository<Configuration, Long> {
  Optional<Configuration> findByName(String name);
}

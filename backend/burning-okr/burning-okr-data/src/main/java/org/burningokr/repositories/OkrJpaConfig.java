package org.burningokr.repositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  basePackages = "org.burningokr.repositories",
  repositoryBaseClass = ExtendedRepositoryImpl.class
)
public class OkrJpaConfig {
}

package org.burningokr.mapper.configuration;

import org.burningokr.dto.configuration.ConfigurationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationMapper implements DataMapper<Configuration, ConfigurationDto> {

  private final Logger logger = LoggerFactory.getLogger(ConfigurationMapper.class);

  @Override
  public Configuration mapDtoToEntity(ConfigurationDto configurationDto) {
    Configuration configuration = new Configuration();
    configuration.setId(configurationDto.getId());
    configuration.setName(configurationDto.getName());
    configuration.setValue(configurationDto.getValue());
    configuration.setType(configurationDto.getType());
    logger.debug("Mapped ConfigurationDto (id: %d) to Configuration".formatted(configurationDto.getId()));
    return configuration;
  }

  @Override
  public ConfigurationDto mapEntityToDto(Configuration configuration) {
    ConfigurationDto configurationDto = new ConfigurationDto();
    configurationDto.setId(configuration.getId());
    configurationDto.setName(configuration.getName());
    configurationDto.setValue(configuration.getValue());
    configurationDto.setType(configuration.getType());
    logger.debug("Mapped Configuration (id: %d) to ConfigurationDto".formatted(configuration.getId()));
    return configurationDto;
  }
}

package org.burningokr.mapper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.configuration.ConfigurationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.Configuration;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConfigurationMapper implements DataMapper<Configuration, ConfigurationDto> {

  @Override
  public Configuration mapDtoToEntity(ConfigurationDto configurationDto) {
    Configuration configuration = new Configuration();
    configuration.setId(configurationDto.getId());
    configuration.setName(configurationDto.getName());
    configuration.setValue(configurationDto.getValue());
    configuration.setType(configurationDto.getType());
    log.debug("Mapped ConfigurationDto (id: %d) to Configuration".formatted(configurationDto.getId()));
    return configuration;
  }

  @Override
  public ConfigurationDto mapEntityToDto(Configuration configuration) {
    ConfigurationDto configurationDto = new ConfigurationDto();
    configurationDto.setId(configuration.getId());
    configurationDto.setName(configuration.getName());
    configurationDto.setValue(configuration.getValue());
    configurationDto.setType(configuration.getType());
    log.debug("Mapped Configuration (id: %d) to ConfigurationDto".formatted(configuration.getId()));
    return configurationDto;
  }
}

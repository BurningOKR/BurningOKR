package org.burningokr.mapper.configuration;

import java.util.ArrayList;
import java.util.Collection;
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
    logger.info("Mapped ConfigurationDto (id: )" + configurationDto.getId() + ") to Configuration");
    return configuration;
  }

  @Override
  public ConfigurationDto mapEntityToDto(Configuration configuration) {
    ConfigurationDto configurationDto = new ConfigurationDto();
    configurationDto.setId(configuration.getId());
    configurationDto.setName(configuration.getName());
    configurationDto.setValue(configuration.getValue());
    configurationDto.setType(configuration.getType());
    logger.info("Mapped Configuration (id: )" + configuration.getId() + ") to ConfigurationDto");
    return configurationDto;
  }

  @Override
  public Collection<Configuration> mapDtosToEntities(Collection<ConfigurationDto> input) {
    Collection<Configuration> configurations = new ArrayList<>();
    input.forEach(configurationDto -> configurations.add(mapDtoToEntity(configurationDto)));
    return configurations;
  }

  @Override
  public Collection<ConfigurationDto> mapEntitiesToDtos(Collection<Configuration> configurations) {
    Collection<ConfigurationDto> configurationDtos = new ArrayList<>();
    for (Configuration configuration : configurations) {
      ConfigurationDto configurationDto = mapEntityToDto(configuration);
      configurationDtos.add(configurationDto);
    }
    return configurationDtos;
  }
}

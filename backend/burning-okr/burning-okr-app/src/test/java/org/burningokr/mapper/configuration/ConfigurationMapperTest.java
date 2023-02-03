package org.burningokr.mapper.configuration;

import org.burningokr.dto.configuration.ConfigurationDto;
import org.burningokr.model.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ConfigurationMapperTest {

  private Configuration configuration;
  private ConfigurationDto configurationDto;
  private ConfigurationMapper configurationMapper;

  @Before
  public void init() {
    this.configuration = new Configuration();
    this.configurationDto = new ConfigurationDto();
    this.configurationMapper = new ConfigurationMapper();
  }

  @Test
  public void mapDtoToEntity_expectIdIsMapped() {
    Long expectedId = 42L;
    configurationDto.setId(expectedId);
    configuration = configurationMapper.mapDtoToEntity(configurationDto);
    Assert.assertEquals(expectedId, configuration.getId());
  }

  @Test
  public void mapDtoToEntity_expectNameIsMapped() {
    String expectedName = "name";
    configurationDto.setName(expectedName);
    configuration = configurationMapper.mapDtoToEntity(configurationDto);
    Assert.assertEquals(expectedName, configuration.getName());
  }

  @Test
  public void mapDtoToEntity_expectValueIsMapped() {
    String expectedValue = "3";
    configurationDto.setValue(expectedValue);
    configuration = configurationMapper.mapDtoToEntity(configurationDto);
    Assert.assertEquals(expectedValue, configuration.getValue());
  }

  @Test
  public void mapDtoToEntity_expectTypeIsMapped() {
    String expectedType = "number";
    configurationDto.setType(expectedType);
    configuration = configurationMapper.mapDtoToEntity(configurationDto);
    Assert.assertEquals(expectedType, configuration.getType());
  }

  @Test
  public void mapEntityToDto_expectIdIsMapped() {
    Long expectedId = 44L;
    configuration.setId(expectedId);
    configurationDto = configurationMapper.mapEntityToDto(configuration);
    Assert.assertEquals(expectedId, configurationDto.getId());
  }

  @Test
  public void mapEntityToDto_expectNameIsMapped() {
    String expectName = "name2";
    configuration.setName(expectName);
    configurationDto = configurationMapper.mapEntityToDto(configuration);
    Assert.assertEquals(expectName, configurationDto.getName());
  }

  @Test
  public void mapEntityToDto_expectValueIsMapped() {
    String expectedValue = "4.3";
    configuration.setValue(expectedValue);
    configurationDto = configurationMapper.mapEntityToDto(configuration);
    Assert.assertEquals(expectedValue, configurationDto.getValue());
  }

  @Test
  public void mapEntityToDto_expectTypeIsMapped() {
    String expectedType = "text";
    configuration.setType(expectedType);
    configurationDto = configurationMapper.mapEntityToDto(configuration);
    Assert.assertEquals(expectedType, configurationDto.getType());
  }

  @Test
  public void mapEntitiesToDtos_expectEmptyList() {
    Collection<Configuration> configurations = new ArrayList<>();
    Collection<ConfigurationDto> configurationDtos =
        configurationMapper.mapEntitiesToDtos(configurations);
    Assert.assertTrue(configurationDtos.isEmpty());
  }

  @Test
  public void mapEntitiesToDtos_expectListWithTwoElements() {
    Collection<Configuration> configurations =
        Arrays.asList(new Configuration(), new Configuration());
    Collection<ConfigurationDto> configurationDtos =
        configurationMapper.mapEntitiesToDtos(configurations);
    Assert.assertEquals(configurations.size(), configurationDtos.size());
  }
}

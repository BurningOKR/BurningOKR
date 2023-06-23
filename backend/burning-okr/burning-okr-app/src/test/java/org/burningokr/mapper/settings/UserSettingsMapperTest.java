package org.burningokr.mapper.settings;

import org.burningokr.dto.settings.UserSettingsDto;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.settings.UserSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserSettingsMapperTest {

  private UserSettings entity;
  private UserSettingsDto dto;
  private UserSettingsMapper mapper;

  @BeforeEach
  public void before() {
    this.entity = new UserSettings();
    this.dto = new UserSettingsDto();
    this.mapper = new UserSettingsMapper();
  }

  @Test
  public void test_mapEntityToDto_expectIdIsMapped() {
    //Arrange
    Long id = 42L;
    this.entity.setId(id);

    //Act
    this.dto = this.mapper.mapEntityToDto(entity);

    //Assert
    assertEquals(id, this.dto.getId());
  }

  @Test
  public void test_mapEntityToDto_expectDefaultCompanyIsMapped() {
    //Arrange
    Long companyId = 40L;
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(companyId);
    this.entity.setDefaultOkrCompany(okrCompany);

    //Act
    this.dto = this.mapper.mapEntityToDto(entity);

    //Assert
    assertEquals(companyId, this.dto.getDefaultCompanyId());
  }

  @Test
  public void test_mapEntityToDto_expectDefaultTeamIsMapped() {
    //Arrange
    Long defaultTeamId = 100L;
    OkrDepartment defaultTeam = new OkrDepartment();
    defaultTeam.setId(defaultTeamId);
    this.entity.setDefaultTeam(defaultTeam);

    //Act
    this.dto = this.mapper.mapEntityToDto(entity);

    //Assert
    assertEquals(defaultTeamId, this.dto.getDefaultTeamId());
  }

  @Test
  public void test_mapEntitiesToDtos_expectEmptyList() {
    //Arrange
    Collection<UserSettings> entities = new ArrayList<>();

    //Act
    Collection<UserSettingsDto> dtos = this.mapper.mapEntitiesToDtos(entities);

    //Assert
    assertTrue(dtos.isEmpty());
  }

  @Test
  public void test_mapEntitiesToDtos_expectListIsMappedWithTwoElements() {
    //Arrange
    Collection<UserSettings> entities = Arrays.asList(new UserSettings(), new UserSettings());

    //Act
    Collection<UserSettingsDto> dtos = mapper.mapEntitiesToDtos(entities);

    //Assert
    assertEquals(2, dtos.size());
  }

  @Test
  public void test_mapDtoToEntity_expectIdIsMapped() {
    //Arrange
    Long id = 42L;
    this.dto.setId(id);

    //Act
    this.entity = this.mapper.mapDtoToEntity(dto);

    //Assert
    assertEquals(id, this.entity.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectCompanyIsNullIfThereIsNoCompanyId() {
    //Arrange

    //Act
    this.entity = this.mapper.mapDtoToEntity(dto);

    //Assert
    assertNull(this.entity.getDefaultOkrCompany());
  }

  @Test
  public void test_mapDtoToEntity_expectDefaultCompanyIsMapped() {
    //Arrange
    Long defaultCompanyId = 50L;
    this.dto.setDefaultCompanyId(defaultCompanyId);

    //Act
    this.entity = this.mapper.mapDtoToEntity(dto);

    //Assert
    assertEquals(defaultCompanyId, this.entity.getDefaultOkrCompany().getId());
  }

  @Test
  public void test_mapDtoToEntity_expectTeamIsNullIfThereIsNoTeamId() {
    //Arrange

    //Act
    this.entity = this.mapper.mapDtoToEntity(dto);

    //Assert
    assertNull(this.entity.getDefaultTeam());
  }

  @Test
  public void test_mapDtoToEntity_expectDefaultTeamIsMapped() {
    //Arrange
    Long defaultTeamId = 40L;
    this.dto.setDefaultTeamId(defaultTeamId);

    //Act
    this.entity = this.mapper.mapDtoToEntity(dto);

    //Assert
    assertEquals(defaultTeamId, this.entity.getDefaultTeam().getId());
  }

}

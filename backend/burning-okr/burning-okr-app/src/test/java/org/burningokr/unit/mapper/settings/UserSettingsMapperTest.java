package org.burningokr.unit.mapper.settings;

import org.burningokr.dto.settings.UserSettingsDto;
import org.burningokr.mapper.settings.UserSettingsMapper;
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

  private UserSettings userSettings;
  private UserSettingsDto userSettingsDto;
  private UserSettingsMapper userSettingsMapper;

  @BeforeEach
  public void before() {
    this.userSettings = new UserSettings();
    this.userSettingsDto = new UserSettingsDto();
    this.userSettingsMapper = new UserSettingsMapper();
  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    //Arrange
    Long id = 42L;
    this.userSettings.setId(id);

    //Act
    this.userSettingsDto = this.userSettingsMapper.mapEntityToDto(userSettings);

    //Assert
    assertEquals(id, this.userSettingsDto.getId());
  }

  @Test
  public void mapEntityToDto_shouldMapCompanyId() {
    //Arrange
    Long companyId = 40L;
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(companyId);
    this.userSettings.setDefaultOkrCompany(okrCompany);

    //Act
    this.userSettingsDto = this.userSettingsMapper.mapEntityToDto(userSettings);

    //Assert
    assertEquals(companyId, this.userSettingsDto.getDefaultCompanyId());
  }

  @Test
  public void mapEntityToDto_shouldMapDefaultTeamId() {
    //Arrange
    Long defaultTeamId = 100L;
    OkrDepartment defaultTeam = new OkrDepartment();
    defaultTeam.setId(defaultTeamId);
    this.userSettings.setDefaultTeam(defaultTeam);

    //Act
    this.userSettingsDto = this.userSettingsMapper.mapEntityToDto(userSettings);

    //Assert
    assertEquals(defaultTeamId, this.userSettingsDto.getDefaultTeamId());
  }

  @Test
  public void mapEntitiesToDtos_shouldHaveAnEmptyListOfElements() {
    //Arrange
    Collection<UserSettings> userSettingsList = new ArrayList<>();

    //Act
    Collection<UserSettingsDto> userSettingsDtoList = this.userSettingsMapper.mapEntitiesToDtos(userSettingsList);

    //Assert
    assertTrue(userSettingsDtoList.isEmpty());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapListWithTwoElements() {
    //Arrange
    Collection<UserSettings> userSettingsList = Arrays.asList(new UserSettings(), new UserSettings());

    //Act
    Collection<UserSettingsDto> userSettingsDtoList = userSettingsMapper.mapEntitiesToDtos(userSettingsList);

    //Assert
    assertEquals(2, userSettingsDtoList.size());
  }

  @Test
  public void mapDtoToEntity_shouldMapUserSettingsId() {
    //Arrange
    Long id = 42L;
    this.userSettingsDto.setId(id);

    //Act
    this.userSettings = this.userSettingsMapper.mapDtoToEntity(userSettingsDto);

    //Assert
    assertEquals(id, this.userSettings.getId());
  }

  @Test
  public void mapDtoToEntity_shouldSetDefaultOkrCompanyNullWhenCompanyIdIsNull() {
    //Arrange

    //Act
    this.userSettings = this.userSettingsMapper.mapDtoToEntity(userSettingsDto);

    //Assert
    assertNull(this.userSettings.getDefaultOkrCompany());
  }

  @Test
  public void mapDtoToEntity_shouldMapUserSettingsDefaultOkrCompanyId() {
    //Arrange
    Long defaultCompanyId = 50L;
    this.userSettingsDto.setDefaultCompanyId(defaultCompanyId);

    //Act
    this.userSettings = this.userSettingsMapper.mapDtoToEntity(userSettingsDto);

    //Assert
    assertEquals(defaultCompanyId, this.userSettings.getDefaultOkrCompany().getId());
  }

  @Test
  public void mapDtoToEntity_shouldSetDefaultTeamNullWhenTeamIdIsNull() {
    //Arrange

    //Act
    this.userSettings = this.userSettingsMapper.mapDtoToEntity(userSettingsDto);

    //Assert
    assertNull(this.userSettings.getDefaultTeam());
  }

  @Test
  public void mapDtoToEntity_shouldMapUserSettingsDefaultTeamId() {
    //Arrange
    Long defaultTeamId = 40L;
    this.userSettingsDto.setDefaultTeamId(defaultTeamId);

    //Act
    this.userSettings = this.userSettingsMapper.mapDtoToEntity(userSettingsDto);

    //Assert
    assertEquals(defaultTeamId, this.userSettings.getDefaultTeam().getId());
  }

  @Test
  public void mapDtosToEntities_shouldHaveAnEmptyListOfElements() {
    //Arrange
    Collection<UserSettingsDto> userSettingsDtos = new ArrayList<>();

    //Act
    Collection<UserSettings> userSettings = this.userSettingsMapper.mapDtosToEntities(userSettingsDtos);

    //Assert
    assertTrue(userSettings.isEmpty());
  }

  @Test
  public void mapDtosToEntities_shouldMapListWithTwoElements() {
    //Arrange
    Collection<UserSettingsDto> userSettingsDtos = Arrays.asList(new UserSettingsDto(), new UserSettingsDto());

    //Act
    Collection<UserSettings> userSettings = userSettingsMapper.mapDtosToEntities(userSettingsDtos);

    //Assert
    assertEquals(2, userSettings.size());
  }

}

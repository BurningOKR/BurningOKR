package org.burningokr.mapper.settings;

import org.burningokr.dto.settings.UserSettingsDto;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.settings.UserSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class UserSettingsMapperTest {

  private UserSettings entity;
  private UserSettingsDto dto;
  private UserSettingsMapper mapper;

  @Before
  public void before() {
    this.entity = new UserSettings();
    this.dto = new UserSettingsDto();
    this.mapper = new UserSettingsMapper();
  }

  @Test
  public void test_mapDtoToEntity_ExpectIdIsMapped() {
    Long id = 42L;
    this.dto.setId(id);

    this.entity = this.mapper.mapDtoToEntity(dto);

    Assert.assertEquals(id, this.entity.getId());
  }

  @Test
  public void test_mapDtoToEntity_ExpectCompanyIsNullIfThereIsNoCompanyId() {
    this.entity = this.mapper.mapDtoToEntity(dto);

    Assert.assertNull(this.entity.getDefaultOkrCompany());
  }

  @Test
  public void test_mapDtoToEntity_ExpectDefaultCompanyIsMapped() {
    Long defaultCompanyId = 50L;
    this.dto.setDefaultCompanyId(defaultCompanyId);

    this.entity = this.mapper.mapDtoToEntity(dto);

    Assert.assertEquals(defaultCompanyId, this.entity.getDefaultOkrCompany().getId());
  }

  @Test
  public void test_mapDtoToEntity_ExpectTeamIsNullIfThereIsNoTeamId() {
    this.entity = this.mapper.mapDtoToEntity(dto);

    Assert.assertNull(this.entity.getDefaultTeam());
  }

  @Test
  public void test_mapDtoToEntity_ExpectDefaultTeamIsMapped() {
    Long defaultTeamId = 40L;
    this.dto.setDefaultTeamId(defaultTeamId);

    this.entity = this.mapper.mapDtoToEntity(dto);

    Assert.assertEquals(defaultTeamId, this.entity.getDefaultTeam().getId());
  }

  @Test
  public void test_mapEntityToDto_ExpectIdIsMapped() {
    Long id = 42L;
    this.entity.setId(id);

    this.dto = this.mapper.mapEntityToDto(entity);

    Assert.assertEquals(id, this.dto.getId());
  }

  @Test
  public void test_mapEntityToDto_ExpectDefaultCompanyIsMapped() {
    Long companyId = 40L;
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(companyId);
    this.entity.setDefaultOkrCompany(okrCompany);

    this.dto = this.mapper.mapEntityToDto(entity);

    Assert.assertEquals(companyId, this.dto.getDefaultCompanyId());
  }

  @Test
  public void test_mapEntityToDto_ExpectDefaultTeamIsMapped() {
    Long defaultTeamId = 100L;
    OkrDepartment defaultTeam = new OkrDepartment();
    defaultTeam.setId(defaultTeamId);
    this.entity.setDefaultTeam(defaultTeam);

    this.dto = this.mapper.mapEntityToDto(entity);

    Assert.assertEquals(defaultTeamId, this.dto.getDefaultTeamId());
  }

  @Test
  public void test_mapEntitiesToDtos_ExpectEmptyList() {
    Collection<UserSettings> entities = new ArrayList<>();
    Collection<UserSettingsDto> dtos = this.mapper.mapEntitiesToDtos(entities);
    Assert.assertTrue(dtos.isEmpty());
  }

  @Test
  public void test_mapEntitiesToDtos_ExpectListIsMappedWithTwoElements() {
    Collection<UserSettings> entities = Arrays.asList(new UserSettings(), new UserSettings());
    Collection<UserSettingsDto> dtos = mapper.mapEntitiesToDtos(entities);
    Assert.assertEquals(2, dtos.size());
  }
}

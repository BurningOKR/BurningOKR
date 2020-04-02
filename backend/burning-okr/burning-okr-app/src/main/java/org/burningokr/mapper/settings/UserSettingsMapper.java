package org.burningokr.mapper.settings;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.settings.UserSettingsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsMapper implements DataMapper<UserSettings, UserSettingsDto> {

  @Override
  public UserSettings mapDtoToEntity(UserSettingsDto dto) {
    UserSettings entity = new UserSettings();
    entity.setId(dto.getId());
    if (dto.getDefaultCompanyId() != null) {
      Company mockCompany = new Company();
      mockCompany.setId(dto.getDefaultCompanyId());
      entity.setDefaultCompany(mockCompany);
    }
    if (dto.getDefaultTeamId() != null) {
      Department mockDepartment = new Department();
      mockDepartment.setId(dto.getDefaultTeamId());
      entity.setDefaultTeam(mockDepartment);
    }
    return entity;
  }

  @Override
  public UserSettingsDto mapEntityToDto(UserSettings entity) {
    UserSettingsDto dto = new UserSettingsDto();
    dto.setId(entity.getId());
    if (entity.getDefaultCompany() != null) {
      dto.setDefaultCompanyId(entity.getDefaultCompany().getId());
    }
    if (entity.getDefaultTeam() != null) {
      dto.setDefaultTeamId(entity.getDefaultTeam().getId());
    }
    return dto;
  }

  @Override
  public Collection<UserSettings> mapDtosToEntities(Collection<UserSettingsDto> input) {
    Collection<UserSettings> userSettings = new ArrayList<>();
    input.forEach(userSettingsDto -> userSettings.add(mapDtoToEntity(userSettingsDto)));
    return userSettings;
  }

  @Override
  public Collection<UserSettingsDto> mapEntitiesToDtos(Collection<UserSettings> input) {
    Collection<UserSettingsDto> dtos = new ArrayList<UserSettingsDto>();
    input.forEach(entity -> dtos.add(mapEntityToDto(entity)));
    return dtos;
  }
}

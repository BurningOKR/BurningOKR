package org.burningokr.mapper.settings;

import org.burningokr.dto.settings.UserSettingsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.settings.UserSettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserSettingsMapper implements DataMapper<UserSettings, UserSettingsDto> {

  @Override
  public UserSettings mapDtoToEntity(UserSettingsDto dto) {
    UserSettings entity = new UserSettings();
    entity.setId(dto.getId());
    if (dto.getDefaultCompanyId() != null) {
      OkrCompany mockOkrCompany = new OkrCompany();
      mockOkrCompany.setId(dto.getDefaultCompanyId());
      entity.setDefaultOkrCompany(mockOkrCompany);
    }
    if (dto.getDefaultTeamId() != null) {
      OkrDepartment mockOkrDepartment = new OkrDepartment();
      mockOkrDepartment.setId(dto.getDefaultTeamId());
      entity.setDefaultTeam(mockOkrDepartment);
    }
    return entity;
  }

  @Override
  public UserSettingsDto mapEntityToDto(UserSettings entity) {
    UserSettingsDto dto = new UserSettingsDto();
    dto.setId(entity.getId());
    if (entity.getDefaultOkrCompany() != null) {
      dto.setDefaultCompanyId(entity.getDefaultOkrCompany().getId());
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

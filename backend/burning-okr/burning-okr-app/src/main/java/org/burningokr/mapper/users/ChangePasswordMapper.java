package org.burningokr.mapper.users;

import org.burningokr.dto.users.ChangePasswordDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.ChangePasswordData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ChangePasswordMapper implements DataMapper<ChangePasswordData, ChangePasswordDto> {

  private final Logger logger = LoggerFactory.getLogger(ChangePasswordMapper.class);

  @Override
  public ChangePasswordData mapDtoToEntity(ChangePasswordDto changePasswordDto) {
    ChangePasswordData result = new ChangePasswordData();
    result.setEmail(changePasswordDto.getEmail());
    result.setNewPassword(changePasswordDto.getNewPassword());
    result.setOldPassword(changePasswordDto.getOldPassword());

    logger.info(
      "Mapped ChangePasswordDto (email: "
        + changePasswordDto.getEmail()
        + ") to ChangePasswordData");

    return result;
  }

  @Override
  public ChangePasswordDto mapEntityToDto(ChangePasswordData changePasswordData) {
    ChangePasswordDto result = new ChangePasswordDto();

    result.setEmail(changePasswordData.getEmail());
    result.setNewPassword(changePasswordData.getNewPassword());
    result.setOldPassword(changePasswordData.getOldPassword());

    logger.info(
      "Mapped ChangePasswordDto (email: "
        + changePasswordData.getEmail()
        + "to ChangePasswordDto");

    return result;
  }

  @Override
  public Collection<ChangePasswordData> mapDtosToEntities(Collection<ChangePasswordDto> input) {
    Collection<ChangePasswordData> changePasswordDataSets = new ArrayList<>();
    input.forEach(
      changePasswordDto -> changePasswordDataSets.add(mapDtoToEntity(changePasswordDto)));
    return changePasswordDataSets;
  }

  @Override
  public Collection<ChangePasswordDto> mapEntitiesToDtos(
    Collection<ChangePasswordData> changePasswordDataSets
  ) {
    Collection<ChangePasswordDto> changePasswordDtos = new ArrayList<>();
    for (ChangePasswordData changePasswordData : changePasswordDataSets) {
      ChangePasswordDto changePasswordDto = mapEntityToDto(changePasswordData);
      changePasswordDtos.add(changePasswordDto);
    }
    return changePasswordDtos;
  }
}

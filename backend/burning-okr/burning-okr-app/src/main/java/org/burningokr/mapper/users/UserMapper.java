package org.burningokr.mapper.users;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.users.UserDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserMapper implements DataMapper<IUser, UserDto> {

  private final Logger logger = LoggerFactory.getLogger(UserMapper.class);

  @Override
  public IUser mapDtoToEntity(UserDto dto) {
    IUser IUser = getEmptyUser();
    IUser.setGivenName(dto.getGivenName());
    IUser.setSurname(dto.getSurname());
    IUser.setId(dto.getId());
    IUser.setMail(dto.getEmail());
    IUser.setDepartment(dto.getDepartment());
    IUser.setJobTitle(dto.getJobTitle());
    IUser.setPhoto(dto.getPhoto());
    logger.info("Mapped UserDto (id: " + dto.getId() + " ) to User");
    return IUser;
  }

  @Override
  public UserDto mapEntityToDto(IUser IUser) {
    UserDto dto = new UserDto();
    dto.setGivenName(IUser.getGivenName());
    dto.setSurname(IUser.getSurname());
    dto.setId(IUser.getId());
    dto.setEmail(IUser.getMail());
    dto.setJobTitle(IUser.getJobTitle());
    dto.setDepartment(IUser.getDepartment());
    dto.setPhoto(IUser.getPhoto());
    logger.info("Mapped User (id: )" + IUser.getId() + " ) to UserDto");
    return dto;
  }

  @Override
  public Collection<IUser> mapDtosToEntities(Collection<UserDto> userDtos) {
    Collection<IUser> IUsers = new ArrayList<>();
    userDtos.forEach(userDto -> IUsers.add(mapDtoToEntity(userDto)));

    return IUsers;
  }

  @Override
  public Collection<UserDto> mapEntitiesToDtos(Collection<IUser> IUsers) {
    Collection<UserDto> dtos = new ArrayList<>();
    IUsers.forEach(user -> dtos.add(mapEntityToDto(user)));
    return dtos;
  }

  private IUser getEmptyUser() {
    // TODO fix auth
    return null;
  }
}

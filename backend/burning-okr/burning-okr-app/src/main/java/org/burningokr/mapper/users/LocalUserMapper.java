package org.burningokr.mapper.users;

import org.burningokr.dto.users.LocalUserDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.LocalUser;
import org.burningokr.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class LocalUserMapper implements DataMapper<User, LocalUserDto> {

  private final Logger logger = LoggerFactory.getLogger(LocalUserMapper.class);

  @Override
  public User mapDtoToEntity(LocalUserDto localUserDto) {
    LocalUser result = new LocalUser();
    result.setActive(localUserDto.isActive());
    result.setCreatedAt(localUserDto.getCreatedAt());
    result.setDepartment(localUserDto.getDepartment());
    result.setGivenName(localUserDto.getGivenName());
    result.setSurname(localUserDto.getSurname());
    result.setId(localUserDto.getId());
    result.setJobTitle(localUserDto.getJobTitle());
    result.setMail(localUserDto.getEmail().toLowerCase());
    result.setPhoto((localUserDto.getPhoto()));

    logger.info("Mapped UserDto (id: " + localUserDto.getId() + " ) to User");

    return result;
  }

  @Override
  public LocalUserDto mapEntityToDto(User localUser) {
    LocalUser user = (LocalUser) localUser;
    LocalUserDto resultDto = new LocalUserDto();
    resultDto.setActive(user.isActive());
    resultDto.setCreatedAt(user.getCreatedAt());
    resultDto.setDepartment(user.getDepartment());
    resultDto.setGivenName(user.getGivenName());
    resultDto.setSurname(user.getSurname());
    resultDto.setId(user.getId());
    resultDto.setJobTitle(user.getJobTitle());
    resultDto.setEmail(user.getMail());
    resultDto.setPhoto(user.getPhoto());

    logger.info("Mapped LocalUser (id: " + user.getId() + " ) to UserDto");

    return resultDto;
  }

  @Override
  public Collection<User> mapDtosToEntities(Collection<LocalUserDto> localUserDtos) {
    Collection<User> localUsers = new ArrayList<>();
    localUserDtos.forEach(localUser -> localUsers.add(mapDtoToEntity(localUser)));

    return localUsers;
  }

  @Override
  public Collection<LocalUserDto> mapEntitiesToDtos(Collection<User> localUsers) {
    Collection<LocalUserDto> localUserDtos = new ArrayList<>();
    localUsers.forEach(localUser -> localUserDtos.add(mapEntityToDto(localUser)));

    return localUserDtos;
  }
}

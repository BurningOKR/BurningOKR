package org.burningokr.mapper.users;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.users.UserDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.AadUser;
import org.burningokr.model.users.LocalUser;
import org.burningokr.model.users.User;
import org.burningokr.service.EnvironmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements DataMapper<User, UserDto> {

  private final Logger logger = LoggerFactory.getLogger(UserMapper.class);
  private final EnvironmentService environmentService;

  @Autowired
  public UserMapper(EnvironmentService environmentService) {
    this.environmentService = environmentService;
  }

  @Override
  public User mapDtoToEntity(UserDto dto) {
    User user = getEmptyUser();
    user.setGivenName(dto.getGivenName());
    user.setSurname(dto.getSurname());
    user.setId(dto.getId());
    user.setMail(dto.getEmail());
    user.setDepartment(dto.getDepartment());
    user.setJobTitle(dto.getJobTitle());
    user.setPhoto(dto.getPhoto());
    logger.info("Mapped UserDto (id: " + dto.getId() + " ) to User");
    return user;
  }

  @Override
  public UserDto mapEntityToDto(User user) {
    UserDto dto = new UserDto();
    dto.setGivenName(user.getGivenName());
    dto.setSurname(user.getSurname());
    dto.setId(user.getId());
    dto.setEmail(user.getMail());
    dto.setJobTitle(user.getJobTitle());
    dto.setDepartment(user.getDepartment());
    dto.setPhoto(user.getPhoto());
    logger.info("Mapped User (id: )" + user.getId() + " ) to UserDto");
    return dto;
  }

  @Override
  public Collection<User> mapDtosToEntities(Collection<UserDto> userDtos) {
    Collection<User> users = new ArrayList<>();
    userDtos.forEach(userDto -> users.add(mapDtoToEntity(userDto)));

    return users;
  }

  @Override
  public Collection<UserDto> mapEntitiesToDtos(Collection<User> users) {
    Collection<UserDto> dtos = new ArrayList<>();
    users.forEach(user -> dtos.add(mapEntityToDto(user)));
    return dtos;
  }

  private User getEmptyUser() {
    User user = null;
    switch (environmentService.getProperty(EnvironmentService.authMode)) {
      case EnvironmentService.authModeAad:
        user = new AadUser();
        break;
      case EnvironmentService.authModeLocal:
        user = new LocalUser();
        break;
      default:
        break;
    }

    return user;
  }
}

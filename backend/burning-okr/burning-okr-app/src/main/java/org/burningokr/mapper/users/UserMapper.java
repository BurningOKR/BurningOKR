package org.burningokr.mapper.users;

import org.burningokr.dto.users.UserDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements DataMapper<User, UserDto> {

  @Override
  public User mapDtoToEntity(UserDto dto) {
    return User.builder()
      .id(dto.getId())
      .active(dto.isActive())
      .mail(dto.getMail())
      .photo(dto.getPhoto())
      .surname(dto.getSurname())
      .givenName(dto.getGivenName())
      .department(dto.getDepartment())
      .jobTitle(dto.getJobTitle())
      .admin(dto.isAdmin())
      .build();
  }

  @Override
  public UserDto mapEntityToDto(User entity) {
    return UserDto.builder()
      .id(entity.getId())
      .active(entity.isActive())
      .mail(entity.getMail())
      .photo(entity.getPhoto())
      .surname(entity.getSurname())
      .givenName(entity.getGivenName())
      .department(entity.getDepartment())
      .jobTitle(entity.getJobTitle())
      .admin(entity.isAdmin())
      .build();
  }
}

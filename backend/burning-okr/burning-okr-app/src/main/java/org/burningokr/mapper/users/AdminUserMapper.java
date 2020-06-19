package org.burningokr.mapper.users;

import java.util.Collection;
import java.util.stream.Collectors;
import org.burningokr.dto.users.AdminUserDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.AdminUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminUserMapper implements DataMapper<AdminUser, AdminUserDto> {
  private static final Logger logger = LoggerFactory.getLogger(AdminUserMapper.class);

  @Override
  public AdminUser mapDtoToEntity(AdminUserDto input) {
    AdminUser output = new AdminUser();

    output.setId(input.getId());

    logger.info("Mapped AdminUserDto (id: {}) to AdminUser.", input.getId());
    return output;
  }

  @Override
  public AdminUserDto mapEntityToDto(AdminUser input) {
    AdminUserDto output = new AdminUserDto();

    output.setId(input.getId());

    logger.info("Mapped AdminUser (id: {}) to AdminUserDto.", input.getId());
    return output;
  }

  @Override
  public Collection<AdminUser> mapDtosToEntities(Collection<AdminUserDto> input) {
    return input.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
  }

  @Override
  public Collection<AdminUserDto> mapEntitiesToDtos(Collection<AdminUser> input) {
    return input.stream().map(this::mapEntityToDto).collect(Collectors.toList());
  }
}

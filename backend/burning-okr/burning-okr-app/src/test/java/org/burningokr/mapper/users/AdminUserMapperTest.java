package org.burningokr.mapper.users;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.UUID;
import org.burningokr.dto.users.AdminUserDto;
import org.burningokr.model.users.AdminUser;
import org.junit.Before;
import org.junit.Test;

public class AdminUserMapperTest {
  private final AdminUserMapper adminUserMapper = new AdminUserMapper();
  private AdminUserDto adminUserDto;
  private AdminUser adminUser;

  @Before
  public void init() {
    adminUserDto = new AdminUserDto();
    adminUser = new AdminUser();
  }

  @Test
  public void mapDtoToEntity_expectAllMapped() {
    adminUserDto.setId(UUID.randomUUID());

    adminUser = adminUserMapper.mapDtoToEntity(adminUserDto);

    assertMapped(adminUserDto, adminUser);
  }

  @Test
  public void mapEntityToDto_expectAllMapped() {
    adminUser.setId(UUID.randomUUID());

    adminUserDto = adminUserMapper.mapEntityToDto(adminUser);

    assertMapped(adminUser, adminUserDto);
  }

  @Test
  public void mapDtosToEntities_expectAllMapped() {
    AdminUserDto adminUserDto1 = new AdminUserDto();
    adminUserDto1.setId(UUID.randomUUID());
    AdminUserDto adminUserDto2 = new AdminUserDto();
    adminUserDto2.setId(UUID.randomUUID());
    ArrayList<AdminUserDto> adminUserDtos = new ArrayList<>(2);
    adminUserDtos.add(adminUserDto1);
    adminUserDtos.add(adminUserDto2);

    ArrayList<AdminUser> adminUsers =
        (ArrayList<AdminUser>) adminUserMapper.mapDtosToEntities(adminUserDtos);

    assertThat(adminUserDtos, hasSize(2));
    assertEquals(adminUserDtos.size(), adminUsers.size());
    for (int i = 0; i < adminUserDtos.size(); i++) {
      assertMapped(adminUserDtos.get(i), adminUsers.get(i));
    }
  }

  @Test
  public void mapEntitiesToDtos_expectAllMapped() {
    AdminUser adminUser1 = new AdminUser();
    adminUser1.setId(UUID.randomUUID());
    AdminUser adminUser2 = new AdminUser();
    adminUser2.setId(UUID.randomUUID());
    ArrayList<AdminUser> adminUsers = new ArrayList<>(2);
    adminUsers.add(adminUser1);
    adminUsers.add(adminUser2);

    ArrayList<AdminUserDto> adminUserDtos =
        (ArrayList<AdminUserDto>) adminUserMapper.mapEntitiesToDtos(adminUsers);

    assertThat(adminUsers, hasSize(2));
    assertEquals(adminUsers.size(), adminUserDtos.size());
    for (int i = 0; i < adminUsers.size(); i++) {
      assertMapped(adminUsers.get(i), adminUserDtos.get(i));
    }
  }

  private void assertMapped(AdminUser expected, AdminUserDto actual) {
    assertEquals(expected.getId(), actual.getId());
  }

  private void assertMapped(AdminUserDto expected, AdminUser actual) {
    assertEquals(expected.getId(), actual.getId());
  }
}

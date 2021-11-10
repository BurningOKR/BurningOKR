package org.burningokr.controller.userhandling;

import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.annotation.TurnOff;
import org.burningokr.dto.users.AdminUserDto;
import org.burningokr.dto.users.UserDto;
import org.burningokr.dto.validators.AdminUserValidator;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.User;
import org.burningokr.service.security.AuthorizationService;
import org.burningokr.service.userhandling.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
@RequiredArgsConstructor
public class AdminUserController {

  private final AuthorizationService authorizationService;
  private final AdminUserService adminUserService;
  private final AdminUserValidator adminUserValidator;
  private final DataMapper<AdminUser, AdminUserDto> adminUserMapper;
  private final DataMapper<User, UserDto> userMapper;

  /**
   * API Endpoint to check if the current user is an admin.
   *
   * @return a {@link ResponseEntity} ok with a boolean
   */
  @GetMapping("/admins/self")
  public ResponseEntity<Boolean> isCurrentUserAdmin() {
    return ResponseEntity.ok(adminUserService.isCurrentUserAdmin());
  }

  /**
   * API Endpoint to get the UUID of all admins.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of {@link UUID}
   */
  @GetMapping("/admins")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<Collection<UUID>> getAllAdmins() {
    return ResponseEntity.ok(adminUserService.getAllAdmins());
  }

  /**
   * API Endpoint to add an admin.
   *
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok an {@link User}
   * @throws InvalidDtoException if admin user is invalid
   */
  @PostMapping("/admins")
  @TurnOff
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<UserDto> addAdmin(@RequestBody AdminUserDto user)
      throws InvalidDtoException {
    AdminUser adminUser = adminUserMapper.mapDtoToEntity(user);
    adminUserValidator.validateAdminUserOnAdd(adminUser);
    return ResponseEntity.ok(userMapper.mapEntityToDto(adminUserService.addAdmin(adminUser)));
  }

  /**
   * API Endpoint to delete an admin.
   *
   * @param adminUuid an {@link UUID} object
   * @return a {@link ResponseEntity} ok
   * @throws InvalidDtoException if the Admin user is invalid
   */
  @DeleteMapping("/admins/{adminUuid}")
  @TurnOff
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity removeAdmin(@PathVariable UUID adminUuid) throws InvalidDtoException {
    adminUserValidator.validateAdminUserId(adminUuid);
    adminUserService.removeAdmin(adminUuid);
    return ResponseEntity.ok().build();
  }
}

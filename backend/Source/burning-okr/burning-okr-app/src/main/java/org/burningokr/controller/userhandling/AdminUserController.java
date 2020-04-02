package org.burningokr.controller.userhandling;

import java.util.Collection;
import java.util.UUID;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.validators.AdminUserValidator;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.model.users.User;
import org.burningokr.service.security.AuthorizationService;
import org.burningokr.service.userhandling.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class AdminUserController {

  private AuthorizationService authorizationService;
  private AdminUserService adminUserService;
  private AdminUserValidator adminUserValidator;

  /**
   * Initialize AdminUserController.
   *
   * @param authorizationService an {@link AuthorizationService} object
   * @param adminUserService an {@link AdminUserService} object
   * @param adminUserValidator an {@link AdminUserValidator} object
   */
  @Autowired
  public AdminUserController(
      AuthorizationService authorizationService,
      AdminUserService adminUserService,
      AdminUserValidator adminUserValidator) {
    this.authorizationService = authorizationService;
    this.adminUserService = adminUserService;
    this.adminUserValidator = adminUserValidator;
  }

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
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<User> addAdmin(@RequestBody User user) throws InvalidDtoException {
    adminUserValidator.validateAdminUserOnAdd(user);
    return ResponseEntity.ok(adminUserService.addAdmin(user));
  }

  /**
   * API Endpoint to delete an admin.
   *
   * @param adminUuid an {@link UUID} object
   * @return a {@link ResponseEntity} ok
   * @throws InvalidDtoException if the Admin user is invalid
   */
  @DeleteMapping("/admins/{adminUuid}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity removeAdmin(@PathVariable UUID adminUuid) throws InvalidDtoException {
    adminUserValidator.validateAdminUserId(adminUuid);
    adminUserService.removeAdmin(adminUuid);
    return ResponseEntity.ok().build();
  }
}

package org.burningokr.controller.userhandling;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.users.UserDto;
import org.burningokr.mapper.users.UserMapper;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.UUID;

@RestApiController()
@RequiredArgsConstructor
public class UserController {

  private final UserMapper userMapper;
  private final UserService userService;
  private final AuthenticationUserContextService authenticationUserContextService;

  @GetMapping("/users/current")
  public ResponseEntity<UserDto> getCurrentUser() {
    var currentUser = authenticationUserContextService.getAuthenticatedUser();
    return ResponseEntity.ok(userMapper.mapEntityToDto(currentUser));
  }

  @GetMapping("/users")
  public ResponseEntity<Collection<UserDto>> getAllUsers(
          @RequestParam(value = "activeUsers", required = false) Boolean activeUsers
  ) {
    Collection<User> userCollection;

    if (activeUsers == null) {
      userCollection = userService.findAll();
    } else if (activeUsers) {
      userCollection = userService.findAllActive();
    } else {
      userCollection = userService.findAllInactive();
    }

    return ResponseEntity.ok(userMapper.mapEntitiesToDtos(userCollection));
  }

  @GetMapping("/admins")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<Collection<UserDto>> getAllAdmins() {
    Collection<User> adminUserCollection;

    adminUserCollection = userService.findAll();

    return ResponseEntity.ok(userMapper.mapEntitiesToDtos(
            adminUserCollection.stream()
                    .filter(User::isAdmin)
                    .toList()));
  }


  @GetMapping("/users/{userId}")
  public ResponseEntity<UserDto> getUserById(
          @PathVariable UUID userId
  ) {
    var user = userService.findById(userId);
    return ResponseEntity.ok(userMapper.mapEntityToDto(user.orElseThrow(EntityNotFoundException::new)));
  }
}

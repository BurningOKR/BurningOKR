package org.burningokr.controller.userhandling;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.users.UserDto;
import org.burningokr.mapper.users.UserMapper;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RestApiController()
@RequiredArgsConstructor
public class UserController {

  private final UserMapper userMapper;
  private final UserService userService;

  @GetMapping("/users/current")
  public ResponseEntity<UserDto> getCurrentUser() {
    var currentUser = userService.getCurrentUser();
    return ResponseEntity.ok(userMapper.mapEntityToDto(currentUser));
  }

  @GetMapping("/users")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<Collection<UserDto>> getAllUsers(
    @RequestParam(value = "activeUsers", required = false) Boolean activeUsers
  ) {

    var t = SecurityContextHolder.getContext().getAuthentication();

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

//  @GetMapping("/users/{userId}") TODO UserOptional
//  public ResponseEntity<UserDto> getUserById(
//    @PathVariable UUID userId
//  ) {
//    var user = userService.findById(userId);
//    return ResponseEntity.ok(userMapper.mapEntityToDto(user));
//  }
}

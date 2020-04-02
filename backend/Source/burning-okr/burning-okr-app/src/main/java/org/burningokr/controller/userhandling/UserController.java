package org.burningokr.controller.userhandling;

import java.util.Collection;
import java.util.UUID;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.users.UserDto;
import org.burningokr.mapper.users.UserMapper;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestApiController
public class UserController {

  private UserService userService;
  private UserMapper userMapper;

  @Autowired
  public UserController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @GetMapping("/users/current")
  public ResponseEntity<UserDto> getCurrentUser() {
    return ResponseEntity.ok(userMapper.mapEntityToDto(userService.getCurrentUser()));
  }

  @GetMapping("/users")
  public ResponseEntity<Collection<UserDto>> getAllUsers() {
    return ResponseEntity.ok(userMapper.mapEntitiesToDtos(userService.findAll()));
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<UserDto> getUserByInformation(@PathVariable UUID userId) {
    return ResponseEntity.ok(userMapper.mapEntityToDto(userService.findById(userId)));
  }
}

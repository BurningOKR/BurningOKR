package org.burningokr.dto.validators;

import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminUserValidatorTest {

  @Mock
  org.burningokr.service.userhandling.UserService userService;

  @Mock
  AdminUserRepository adminUserRepository;

  @InjectMocks
  AdminUserValidator adminUserValidator;

  private User userToInsert;
  private UUID userToInsertId;
  private User currentUser;
  private AdminUser adminUser;
  private UUID currentUserId;
  private UUID existingAdminId;

  @BeforeEach
  public void prepareTests() {
    existingAdminId = UUID.randomUUID();
    userToInsertId = UUID.randomUUID();
    currentUserId = UUID.randomUUID();

    userToInsert = mock(User.class);
    currentUser = mock(User.class);
    adminUser = new AdminUser();

    when(userToInsert.getId()).thenReturn(userToInsertId);
    when(currentUser.getId()).thenReturn(currentUserId);

    when(userService.getCurrentUser()).thenReturn(currentUser);
    when(userService.doesUserExist(any())).thenReturn(false);
    when(userService.doesUserExist(userToInsertId)).thenReturn(true);

    when(adminUserRepository.findById(any())).thenReturn(Optional.empty());
    when(adminUserRepository.findById(existingAdminId)).thenReturn(Optional.of(new AdminUser()));
  }

  @Test
  public void validateAdminUserOnAdd_userIsNull_expectedThrow() {
    try {
      adminUserValidator.validateAdminUserOnAdd(null);
      fail("Should throw InvalidDtoException.");
    } catch (Exception ex) {
      assertEquals(ex.getClass(), InvalidDtoException.class);
    }

    verify(userService, never()).getCurrentUser();
    verify(userService, never()).doesUserExist(UUID.randomUUID());
  }

  @Test
  public void validateAdminUserOnAdd_existingAdmin_expectedThrow() {
    when(userToInsert.getId()).thenReturn(existingAdminId);
    adminUser.setId(userToInsert.getId());

    try {
      adminUserValidator.validateAdminUserOnAdd(adminUser);
      fail("Should throw InvalidDtoException.");
    } catch (Exception ex) {
      assertEquals(ex.getClass(), InvalidDtoException.class);
    }

    verify(adminUserRepository).findById(existingAdminId);
    verify(userService, never()).getCurrentUser();
    verify(userService, never()).doesUserExist(existingAdminId);
  }

  @Test
  public void validateAdminUserOnAdd_existingAdmin_expectedNoThrow() {
    adminUser.setId(userToInsert.getId());
    try {
      adminUserValidator.validateAdminUserOnAdd(adminUser);
    } catch (Exception ex) {
      fail("Should not throw any exception");
    }
  }

  @Test
  public void validateAdminUserId_idIsNull_expectedThrow() {
    try {
      adminUserValidator.validateAdminUserId(null);
      fail("Should throw InvalidDtoException.");
    } catch (Exception ex) {
      assertEquals(ex.getClass(), InvalidDtoException.class);
    }

    verify(adminUserRepository, never()).findById(null);
  }

  @Test
  public void validateAdminUserId_adminIsCurrentUser_expectedThrow() {
    try {
      adminUserValidator.validateAdminUserId(currentUserId);
      fail("Should throw InvalidDtoException.");
    } catch (Exception ex) {
      assertEquals(ex.getClass(), InvalidDtoException.class);
    }

    verify(userService).getCurrentUser();
    verify(userService, never()).doesUserExist(currentUserId);
  }

  @Test
  public void validateAdminUserId_adminIsNotInUserList_expectedThrow() {
    UUID uuid = UUID.randomUUID();
    try {
      adminUserValidator.validateAdminUserId(uuid);
      fail("Should throw InvalidDtoException.");
    } catch (Exception ex) {
      assertEquals(ex.getClass(), InvalidDtoException.class);
    }

    verify(userService).getCurrentUser();
    verify(userService).doesUserExist(uuid);
  }

  @Test
  public void validateAdminUserId_validAdminUser_expectedNoThrow() throws InvalidDtoException {
    adminUserValidator.validateAdminUserId(userToInsertId);
  }
}

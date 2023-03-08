package org.burningokr.service.userhandling;

import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.users.AdminUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {

  private static IUser IUserToAdd;
  private static AdminUser adminUser;
  private static UUID userId;
  @Mock
  private UserService userService;
  @Mock
  private AdminUserRepository adminUserRepository;
  @InjectMocks
  private AdminUserService adminUserService;
  private ArrayList<AdminUser> adminUsersToReturn;

  private UUID adminUserUuid1;
  private UUID adminUserUuid2;

  private AdminUser adminUser1;
  private AdminUser adminUser2;

  @BeforeAll
  public static void init() {
    userId = UUID.randomUUID();
    IUserToAdd = mock(IUser.class);
    when(IUserToAdd.getId()).thenReturn(userId);
    adminUser = new AdminUser();
  }

  private void setupGetAllAdminsTest() {
    adminUserUuid1 = UUID.randomUUID();
    adminUserUuid2 = UUID.randomUUID();

    adminUser1 = new AdminUser();
    adminUser1.setId(adminUserUuid1);
    adminUser2 = new AdminUser();
    adminUser2.setId(adminUserUuid2);

    adminUsersToReturn = new ArrayList<>();

    when(adminUserRepository.findAll()).thenReturn(adminUsersToReturn);
  }

  @Test
  public void getAllAdmins_noAdmins_expectedEmptyList() {
    setupGetAllAdminsTest();

    Collection<UUID> actual = adminUserService.getAllAdmins();

    assertEquals(0, actual.size());
  }

  @Test
  public void getAllAdmins_oneAdmin_expectedOneEntry() {
    setupGetAllAdminsTest();
    adminUsersToReturn.add(adminUser1);

    Collection<UUID> actual = adminUserService.getAllAdmins();

    assertEquals(1, actual.size());
    assertTrue(actual.contains(adminUserUuid1));
  }

  @Test
  public void getAllAdmins_twoAdmins_expectedTwoEntries() {
    setupGetAllAdminsTest();
    adminUsersToReturn.add(adminUser1);
    adminUsersToReturn.add(adminUser2);

    Collection<UUID> actual = adminUserService.getAllAdmins();

    assertEquals(2, actual.size());
    assertTrue(actual.contains(adminUserUuid1));
    assertTrue(actual.contains(adminUserUuid2));
  }

  @Test
  public void addAdmin_expectedSavedAdminUserCorrectId() {
    ArgumentCaptor<AdminUser> capturedSave = ArgumentCaptor.forClass(AdminUser.class);

    adminUser.setId(IUserToAdd.getId());

    adminUserService.addAdmin(adminUser);
    verify(adminUserRepository).save(capturedSave.capture());

    AdminUser capturedAdmin = capturedSave.getValue();

    assertEquals(userId, capturedAdmin.getId());
  }

  @Test
  public void addAdmin_expectedReturnFromUserService() {
    IUser IUserToReturn = mock(IUser.class);
    adminUser.setId(IUserToAdd.getId());

    when(userService.findById(userId)).thenReturn(IUserToReturn);

    IUser actual = adminUserService.addAdmin(adminUser);

    assertEquals(IUserToReturn, actual);
  }

  @Test
  public void removeAdmin_expectedRemoveCall() {
    UUID userId = UUID.randomUUID();

    adminUserService.removeAdmin(userId);

    verify(adminUserRepository).deleteById(userId);
  }
}

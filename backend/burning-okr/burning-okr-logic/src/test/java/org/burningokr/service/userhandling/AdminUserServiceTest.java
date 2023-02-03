package org.burningokr.service.userhandling;

import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.AdminUserRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminUserServiceTest {

  private static User userToAdd;
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

  @BeforeClass
  public static void init() {
    userId = UUID.randomUUID();
    userToAdd = mock(User.class);
    when(userToAdd.getId()).thenReturn(userId);
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

    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void getAllAdmins_oneAdmin_expectedOneEntry() {
    setupGetAllAdminsTest();
    adminUsersToReturn.add(adminUser1);

    Collection<UUID> actual = adminUserService.getAllAdmins();

    Assert.assertEquals(1, actual.size());
    Assert.assertTrue(actual.contains(adminUserUuid1));
  }

  @Test
  public void getAllAdmins_twoAdmins_expectedTwoEntries() {
    setupGetAllAdminsTest();
    adminUsersToReturn.add(adminUser1);
    adminUsersToReturn.add(adminUser2);

    Collection<UUID> actual = adminUserService.getAllAdmins();

    Assert.assertEquals(2, actual.size());
    Assert.assertTrue(actual.contains(adminUserUuid1));
    Assert.assertTrue(actual.contains(adminUserUuid2));
  }

  @Test
  public void addAdmin_expectedSavedAdminUserCorrectId() {
    ArgumentCaptor<AdminUser> capturedSave = ArgumentCaptor.forClass(AdminUser.class);

    adminUser.setId(userToAdd.getId());

    adminUserService.addAdmin(adminUser);
    verify(adminUserRepository).save(capturedSave.capture());

    AdminUser capturedAdmin = capturedSave.getValue();

    Assert.assertEquals(userId, capturedAdmin.getId());
  }

  @Test
  public void addAdmin_expectedReturnFromUserService() {
    User userToReturn = mock(User.class);
    adminUser.setId(userToAdd.getId());

    when(userService.findById(userId)).thenReturn(userToReturn);

    User actual = adminUserService.addAdmin(adminUser);

    Assert.assertEquals(userToReturn, actual);
  }

  @Test
  public void removeAdmin_expectedRemoveCall() {
    UUID userId = UUID.randomUUID();

    adminUserService.removeAdmin(userId);

    verify(adminUserRepository).deleteById(userId);
  }
}

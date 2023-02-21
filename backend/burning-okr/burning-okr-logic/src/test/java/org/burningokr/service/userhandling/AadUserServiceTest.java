package org.burningokr.service.userhandling;

import org.burningokr.model.users.AadUser;
import org.burningokr.repositories.users.AadUserRepository;
import org.burningokr.service.userutil.AadUserListUpdater;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AadUserServiceTest {

  @Mock
  AadUserListUpdater aadUserListUpdater;

  @Mock
  AadUserRepository aadUserRepository;

  @InjectMocks
  AadUserService aadUserService;

  @Test
  public void test_expectEmptyUserList() {
    when(aadUserRepository.findAll()).thenReturn(new ArrayList<>());

    assertTrue(aadUserService.findAll().isEmpty());
  }

  @Test
  public void test_expectUserListWithTwoEntities() {
    Collection<AadUser> users = Arrays.asList(new AadUser(), new AadUser());

    when(aadUserRepository.findAll()).thenReturn(users);

    assertEquals(2, aadUserService.findAll().size());
  }
}

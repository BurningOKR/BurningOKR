package org.burningokr.service.userutil;

import org.burningokr.model.users.AadUser;
import org.burningokr.repositories.users.AadUserRepository;
import org.burningokr.service.exceptions.AzureApiException;
import org.burningokr.service.exceptions.AzureUserFetchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AadUserListUpdaterTest {

  @Mock
  private AzureUserFetcher azureUserFetcher;
  @Mock
  private AadUserRepository aadUserRepository;
  @Mock
  private AzurePhotoFetcher azurePhotoFetcher;
  @Mock
  private AzureApiCaller azureApiCaller;

  @InjectMocks
  private AadUserListUpdater aadUserListUpdater;

  @Test
  public void testUpdateAadUserListShouldFetchUsers()
    throws AzureApiException, AzureUserFetchException {
    String accessToken = "testToken";
    AadUser firstUser = new AadUser();
    AadUser secondUser = new AadUser();

    firstUser.setMail("test");
    secondUser.setMail("test2");

    when(azureApiCaller.getAccessToken()).thenReturn(accessToken);
    when(azureUserFetcher.fetchAzureUsers(accessToken))
      .thenReturn(Arrays.asList(firstUser, secondUser));

    aadUserListUpdater.updateAadUserList();

    verify(azureApiCaller).getAccessToken();
    verify(azureUserFetcher).fetchAzureUsers(accessToken);
  }

  @Test
  public void testUpdateAadUserListShouldUpdateUsers()
    throws AzureApiException, AzureUserFetchException {
    final String accessToken = "testToken";
    final AadUser updatingUser = new AadUser();
    final String expectedNewEmail =
      "user1@test.mail"; // use lowercase because method updateAADUserList sets the mail
    // toLowerCase()

    updatingUser.setId(UUID.randomUUID());
    updatingUser.setMail("test");

    when(azureApiCaller.getAccessToken()).thenReturn(accessToken);
    when(azureUserFetcher.fetchAzureUsers(accessToken))
      .thenReturn(Collections.singletonList(updateUserMail(updatingUser, expectedNewEmail)));

    aadUserListUpdater.updateAadUserList();

    verify(azureApiCaller).getAccessToken();
    verify(azureUserFetcher).fetchAzureUsers(accessToken);
    assertEquals(updatingUser.getMail(), expectedNewEmail);
  }

  @SuppressWarnings("SameParameterValue")
  private AadUser updateUserMail(AadUser aadUser, String mail) {
    aadUser.setMail(mail);
    return aadUser;
  }

  @Test
  public void testUpdateAadUserListShouldMailSetToLowerCase()
    throws AzureApiException, AzureUserFetchException {
    final String accessToken = "testToken";
    final AadUser updatingUser = new AadUser();
    final String expectedNewEmail = "user1@test.mail";

    updatingUser.setId(UUID.randomUUID());
    updatingUser.setMail("User1@Test.Mail");

    when(azureApiCaller.getAccessToken()).thenReturn(accessToken);
    when(azureUserFetcher.fetchAzureUsers(accessToken))
      .thenReturn(Collections.singletonList(updatingUser));

    aadUserListUpdater.updateAadUserList();

    verify(azureApiCaller).getAccessToken();
    verify(azureUserFetcher).fetchAzureUsers(accessToken);
    assertEquals(updatingUser.getMail(), expectedNewEmail);
  }
}

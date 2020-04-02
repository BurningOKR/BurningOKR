package org.burningokr.service.userutil;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.burningokr.model.users.AadUser;
import org.burningokr.service.exceptions.AzureUserFetchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AzureUserFetcherTest {

  @Mock private AzureAdProperties azureAdProperties;

  @Mock private AzureApiCaller azureApiCaller;

  @InjectMocks private AzureUserFetcher azureUserFetcher;

  @Test
  public void fetchAzureUsers_workingThroughTwoPages() throws IOException, AzureUserFetchException {
    when(azureAdProperties.getAzureGroups())
        .thenReturn(
            new ArrayList<>(
                Collections.singletonList(
                    AzureGroup.builder().name("group1").id("groupId").build())));

    when(azureApiCaller.callApi(anyString(), any()))
        .thenReturn(
            new ByteArrayInputStream(
                "{\"@odata.context\":\"https://test.test.com\",\"@odata.nextLink\":\"https://test.test.com\",\"value\":[{\"@odata.type\":\"user\",\"id\":\"88888888-4444-4444-4444-121212121212\",\"givenName\":\"John\",\"surname\":\"Doe\",\"mail\":\"john.doee@burning-okr.de\",\"mailNickname\":\"jdoe\",\"jobTitle\":\"Tester\",\"department\":\"Test\"}]}"
                    .getBytes()))
        .thenReturn(
            new ByteArrayInputStream(
                "{\"@odata.context\":\"https://test.test.com\",\"value\":[{\"@odata.type\":\"user\",\"id\":\"88888888-4444-4444-4444-121212121213\",\"givenName\":\"Max\",\"surname\":\"Mustermann\",\"mail\":\"max.mustermann@burning-okr.de\",\"mailNickname\":\"mmustermann\",\"jobTitle\":\"Tester\",\"department\":\"Testing\"}]}"
                    .getBytes()));

    Collection<AadUser> aadUsers = azureUserFetcher.fetchAzureUsers("token");

    assertEquals(aadUsers.size(), 2);
  }

  @Test
  public void fetchAzureUsers_workingThroughOnePage() throws IOException, AzureUserFetchException {
    when(azureAdProperties.getAzureGroups())
        .thenReturn(
            new ArrayList<>(
                Collections.singletonList(
                    AzureGroup.builder().name("group1").id("groupId").build())));

    when(azureApiCaller.callApi(anyString(), any()))
        .thenReturn(
            new ByteArrayInputStream(
                "{\"@odata.context\":\"https://test.test.com\",\"value\":[{\"@odata.type\":\"user\",\"id\":\"88888888-4444-4444-4444-121212121213\",\"givenName\":\"Burning\",\"surname\":\"Okr\",\"mail\":\"burning.okr@burning-okr.de\",\"mailNickname\":\"bokr\",\"jobTitle\":\"OKR\",\"department\":\"OKR\"}]}"
                    .getBytes()));

    Collection<AadUser> aadUsers = azureUserFetcher.fetchAzureUsers("token");

    assertEquals(aadUsers.size(), 1);
  }

  @Test
  public void fetchAzureUsers_returnEmptyListForNoResponse()
      throws IOException, AzureUserFetchException {
    when(azureAdProperties.getAzureGroups())
        .thenReturn(
            new ArrayList<>(
                Collections.singletonList(
                    AzureGroup.builder().name("group1").id("groupId").build())));

    when(azureApiCaller.callApi(anyString(), any()))
        .thenReturn(
            new ByteArrayInputStream(
                "{\"@odata.context\":\"https://test.test.com\",\"value\":[]}".getBytes()));

    Collection<AadUser> aadUsers = azureUserFetcher.fetchAzureUsers("token");

    assertEquals(aadUsers.size(), 0);
  }
}

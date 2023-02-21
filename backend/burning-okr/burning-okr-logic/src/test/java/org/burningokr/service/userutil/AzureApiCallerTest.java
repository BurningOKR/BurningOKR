package org.burningokr.service.userutil;

import org.burningokr.service.exceptions.AzureApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AzureApiCallerTest {

  private ExternalOAuthClientDetails externalOAuthClientDetails;

  @BeforeEach
  public void setup() {
    externalOAuthClientDetails =
      new ExternalOAuthClientDetails("", "", "clientId", "clientSecret", "", "");
    List<AzureGroup> azureGroups = new ArrayList<>();
    azureGroups.add(AzureGroup.builder().id("groupId").name("groupIntern").build());
  }

  @Test
  public void getAccessToken_shouldFailForInvalidData() throws AzureApiException {
    externalOAuthClientDetails.setClientSecret("notSoSecret");
    AzureApiCaller caller = new AzureApiCaller(externalOAuthClientDetails);

    assertThrows(AzureApiException.class, () -> {
      String accessToken = caller.getAccessToken();
    });
  }
}

package org.burningokr.service.userutil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.burningokr.model.users.AadUser;
import org.burningokr.service.condition.AadCondition;
import org.burningokr.service.exceptions.AzureUserFetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

@Conditional(AadCondition.class)
@Service
public class AzureUserFetcher {

  private final Logger logger = LoggerFactory.getLogger(AzureUserFetcher.class);

  private final AzureApiCaller azureApiCaller;

  private AzureAdProperties azureAdProperties;

  @Autowired
  public AzureUserFetcher(AzureApiCaller azureApiCaller, AzureAdProperties azureAdProperties) {
    this.azureApiCaller = azureApiCaller;
    this.azureAdProperties = azureAdProperties;
  }

  /**
   * Load user data from Azure Active Directory.
   *
   * <p>Load the users with the configured groups from Azure Active Directory.
   *
   * @param accessToken a string value
   * @return a {@link Collection} of {@link AadUser}
   * @throws AzureUserFetchException if a problem occurs while calling the API
   */
  @SuppressWarnings("checkstyle:linelength") // Deactivated to not cut URL String
  public Collection<AadUser> fetchAzureUsers(String accessToken) throws AzureUserFetchException {
    Collection<AadUser> azureUsers = new ArrayList<>();
    for (AzureGroup azureGroup : this.azureAdProperties.getAzureGroups()) {
      String uri =
        "https://graph.microsoft.com/v1.0/groups/"
          + azureGroup.getId()
          + "/members?$select=id,givenName,surname,mail,mailNickname,jobTitle,department&$top=1";

      AzureGroupResponse response;
      do {
        response = fetchAzureUsersForGroupId(accessToken, uri);
        azureUsers.addAll(response.getUsers());
        uri = response.getOdataNextLink();
      } while (uri != null);
    }
    return azureUsers;
  }

  private AzureGroupResponse fetchAzureUsersForGroupId(String accessToken, String uri)
    throws AzureUserFetchException {
    try {
      InputStream inputStream = azureApiCaller.callApi(accessToken, new URL(uri));

      BufferedReader bufferedReader = getReader(inputStream);

      StringBuilder stringBuilder = new StringBuilder();

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }

      return new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .readValue(stringBuilder.toString(), AzureGroupResponse.class);
    } catch (IOException e) {
      throw new AzureUserFetchException(e.getMessage());
    }
  }

  private BufferedReader getReader(InputStream inputStream) {
    return new BufferedReader(new InputStreamReader(inputStream));
  }
}

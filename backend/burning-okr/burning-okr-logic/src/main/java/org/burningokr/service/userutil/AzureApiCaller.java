package org.burningokr.service.userutil;

import com.google.common.collect.ImmutableMap;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.burningokr.service.condition.AadCondition;
import org.burningokr.service.exceptions.AzureApiException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Conditional(AadCondition.class)
@Service
public class AzureApiCaller {

  private ExternalOAuthClientDetails externalOAuthClientDetails;
  private AzureAdProperties azureAdProperties;

  @Autowired
  public AzureApiCaller(
      ExternalOAuthClientDetails externalOAuthClientDetails, AzureAdProperties azureAdProperties) {
    this.externalOAuthClientDetails = externalOAuthClientDetails;
    this.azureAdProperties = azureAdProperties;
  }

  InputStream callApi(String accessToken, URL url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    String authHeader = "Bearer " + accessToken;

    connection.setRequestProperty("Authorization", authHeader);
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setInstanceFollowRedirects(false);

    if (connection.getResponseCode() == 200) {
      return connection.getInputStream();
    }

    throw new IllegalStateException("STATE " + connection.getResponseCode());
  }

  /**
   * Get Access Token for Azure Active Directory.
   *
   * @return a string value
   * @throws AzureApiException if errors occurs while getting Access Token
   */
  public String getAccessToken() throws AzureApiException {
    try {
      final URL url =
          new URL(
              "https://login.microsoftonline.com/"
                  + this.azureAdProperties.getTenantId()
                  + "/oauth2/v2.0/token");

      ImmutableMap<String, String> params =
          ImmutableMap.<String, String>builder()
              .put("client_id", externalOAuthClientDetails.getClientId())
              .put("client_secret", externalOAuthClientDetails.getClientSecret())
              .put("scope", "https://graph.microsoft.com/.default")
              .put("grant_type", "client_credentials")
              .build();

      String urlFormattedParams = formatQueryParams(params);
      byte[] postData = urlFormattedParams.getBytes(StandardCharsets.UTF_8);

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setInstanceFollowRedirects(false);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty("charset", "utf-8");
      connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
      connection.setUseCaches(false);

      try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
        wr.write(postData);
      }

      BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder builder = new StringBuilder();

      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }

      AccessTokenResponse accessTokenResponse = getAccessTokenResponse(builder.toString());

      return accessTokenResponse.getAccess_token();
    } catch (IOException e) {
      throw new AzureApiException(e.getMessage());
    }
  }

  private AccessTokenResponse getAccessTokenResponse(String responseString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(responseString, AccessTokenResponse.class);
  }

  private String formatQueryParams(ImmutableMap<String, String> params) {
    return params.entrySet().stream()
        .map(p -> encodeUrl(p.getKey()) + "=" + encodeUrl(p.getValue()))
        .reduce((p1, p2) -> p1 + "&" + p2)
        .orElse("");
  }

  private String encodeUrl(String stringToEncode) {

    try {
      return URLEncoder.encode(stringToEncode, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return null;
  }
}

package org.burningokr.service.userutil;

import java.util.Collection;
import org.burningokr.model.users.AadUser;
import org.burningokr.repositories.users.AadUserRepository;
import org.burningokr.service.condition.AadCondition;
import org.burningokr.service.exceptions.AzureApiException;
import org.burningokr.service.exceptions.AzureUserFetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Conditional(AadCondition.class)
@Service
public class AadUserListUpdater {

  private final AzureUserFetcher azureUserFetcher;
  private final AadUserRepository aadUserRepository;
  private final AzurePhotoFetcher azurePhotoFetcher;
  private final AzureApiCaller azureApiCaller;

  private final Logger logger = LoggerFactory.getLogger(AadUserListUpdater.class);

  /**
   * Initialize AadUserListUpdater.
   *
   * @param azureUserFetcher an {@link AzureUserFetcher} object
   * @param aadUserRepository an {@link AadUserRepository} object
   * @param azurePhotoFetcher an {@link AzurePhotoFetcher} object
   * @param azureApiCaller an {@link AzureApiCaller} object
   */
  @Autowired
  public AadUserListUpdater(
      AzureUserFetcher azureUserFetcher,
      AadUserRepository aadUserRepository,
      AzurePhotoFetcher azurePhotoFetcher,
      AzureApiCaller azureApiCaller) {
    this.azureUserFetcher = azureUserFetcher;
    this.aadUserRepository = aadUserRepository;
    this.azurePhotoFetcher = azurePhotoFetcher;
    this.azureApiCaller = azureApiCaller;
  }

  /**
   * Update the AAD User List.
   *
   * @throws AzureUserFetchException if problem occurs while getting the Users from Azure Active
   *     Directory.
   * @throws AzureApiException if problem occurs while getting the Access Token
   */
  public void updateAadUserList() throws AzureUserFetchException, AzureApiException {

    String accessToken = this.azureApiCaller.getAccessToken();

    Collection<AadUser> aadUserList = azureUserFetcher.fetchAzureUsers(accessToken);
    logger.info("Updating " + aadUserList.size() + " users...");
    for (AadUser aadUser : aadUserList) {
      aadUser.setMail(aadUser.getMail().toLowerCase());
      String photoAsBase64 = azurePhotoFetcher.getPhotoForUserId(accessToken, aadUser.getId());
      aadUser.setPhoto(photoAsBase64);
    }
    this.updateUserList(aadUserList);
  }

  private void updateUserList(Collection<AadUser> userList) {
    userList.forEach(this.aadUserRepository::save);
  }
}

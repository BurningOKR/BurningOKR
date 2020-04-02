package org.burningokr.service.configuration;

import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.configuration.OAuthFrontendDetails;
import org.burningokr.repositories.configuration.OAuthFrontendDetailsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthFrontendDetailsService {

  private final OAuthFrontendDetailsRepository oauthFrontendDetailsRepository;

  /** Gets the OAuthFrontendDetails. */
  public OAuthFrontendDetails getOAuthFrontendDetails() {
    List<OAuthFrontendDetails> oauthClientDetails =
        Lists.newArrayList(oauthFrontendDetailsRepository.findAll());
    if (!oauthClientDetails.isEmpty()) {
      return oauthClientDetails.get(0);
    } else {
      throw new EntityNotFoundException();
    }
  }

  /**
   * Updates the OauthFrontendDetails to match the given OauthClientDetails.
   *
   * @param oauthClientDetails a {@link OAuthClientDetails} object
   */
  public void updateOauthFrontendDetails(OAuthClientDetails oauthClientDetails) {
    OAuthFrontendDetails frontendDetails = getOAuthFrontendDetails();

    oauthFrontendDetailsRepository.deleteAll();

    frontendDetails.setClientId(oauthClientDetails.getClientId());
    frontendDetails.setDummyClientSecret(oauthClientDetails.getClientSecret());

    oauthFrontendDetailsRepository.save(frontendDetails);
  }
}

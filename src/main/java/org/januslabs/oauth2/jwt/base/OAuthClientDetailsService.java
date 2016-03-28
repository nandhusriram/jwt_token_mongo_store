package org.januslabs.oauth2.jwt.base;

import java.util.HashSet;

import org.januslabs.oauth2.jwt.mongo.domain.OAuthClientDetails;
import org.januslabs.oauth2.jwt.mongo.repository.OAuthClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OAuthClientDetailsService implements ClientDetailsService {

  private @Autowired OAuthClientDetailsRepository clientDetailsRepository;

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    OAuthClientDetails oauthClientDetails = clientDetailsRepository.findByClientId(clientId);
    if (oauthClientDetails == null) {
      throw new UsernameNotFoundException(
          String.format("ClientDetails %s does not exist!", clientId));
    }
    BaseClientDetails clientDetails = new BaseClientDetails();

    clientDetails.setClientId(oauthClientDetails.getClientId());
    clientDetails.setScope(StringUtils.commaDelimitedListToSet(oauthClientDetails.getScope()));
    clientDetails.setAuthorizedGrantTypes(
        StringUtils.commaDelimitedListToSet(oauthClientDetails.getAuthorizedGrantTypes()));
    clientDetails.setAuthorities(AuthorityUtils.createAuthorityList(
        StringUtils.commaDelimitedListToStringArray(oauthClientDetails.getAuthorities())));
    clientDetails.setAccessTokenValiditySeconds(oauthClientDetails.getAccessTokenValidity());
    clientDetails.setClientSecret(oauthClientDetails.getClientSecret());
    clientDetails.setAdditionalInformation(oauthClientDetails.getAdditionalInformation());
    if (oauthClientDetails.isAutoApprove())
      clientDetails.setAutoApproveScopes(
          StringUtils.commaDelimitedListToSet(oauthClientDetails.getAutoApproveScopes()));
    else
      clientDetails.setAutoApproveScopes(new HashSet<String>());

    clientDetails.setAccessTokenValiditySeconds(oauthClientDetails.getAccessTokenValidity());
    clientDetails.setRegisteredRedirectUri(
        StringUtils.commaDelimitedListToSet(oauthClientDetails.getWebserverRedirectURL()));
    clientDetails
        .setResourceIds(StringUtils.commaDelimitedListToSet(oauthClientDetails.getResourceIds()));
    clientDetails.setAdditionalInformation(oauthClientDetails.getAdditionalInformation());

    return clientDetails;

  }

}

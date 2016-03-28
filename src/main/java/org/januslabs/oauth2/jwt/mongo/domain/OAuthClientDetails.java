package org.januslabs.oauth2.jwt.mongo.domain;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import lombok.Data;

@Data
public class OAuthClientDetails {

  @Id
  private String clientId;
  private String resourceIds;
  private String clientSecret;
  private String scope;
  private String authorizedGrantTypes;
  private String webserverRedirectURL;
  private String authorities;
  private Integer accessTokenValidity;
  private Integer refreshTokenValidity;
  private Map<String, ?> additionalInformation;
  private boolean autoApprove;
  private String autoApproveScopes;

  public OAuthClientDetails from(ClientDetails clientDetails) {
    OAuthClientDetails details = new OAuthClientDetails();
    BaseClientDetails clientDetailsBase = (BaseClientDetails) clientDetails;

    details.setAccessTokenValidity(clientDetailsBase.getAccessTokenValiditySeconds());
    details.setRefreshTokenValidity(clientDetailsBase.getRefreshTokenValiditySeconds());
    details.setAdditionalInformation(clientDetailsBase.getAdditionalInformation());
    details.setAuthorities(clientDetailsBase.getAuthorities().stream().map(i -> i.getAuthority())
        .collect(Collectors.joining(",")));
    details.setAuthorizedGrantTypes(clientDetailsBase.getAuthorizedGrantTypes().stream()
        .map(i -> i.toString()).collect(Collectors.joining(",")));
    if (clientDetailsBase.getAutoApproveScopes().size() > 0)
      details.setAutoApprove(Boolean.TRUE);
    else
      details.setAutoApprove(Boolean.FALSE);
    details.setAutoApproveScopes(clientDetailsBase.getAutoApproveScopes().stream()
        .map(i -> i.toString()).collect(Collectors.joining(",")));
    details.setClientId(clientDetailsBase.getClientId());
    details.setClientSecret(clientDetailsBase.getClientSecret());
    details.setResourceIds(clientDetailsBase.getResourceIds().stream().map(i -> i.toString())
        .collect(Collectors.joining(",")));
    details.setScope(clientDetailsBase.getScope().stream().map(i -> i.toString())
        .collect(Collectors.joining(",")));
    details.setWebserverRedirectURL(clientDetailsBase.getRegisteredRedirectUri().stream()
        .map(i -> i.toString()).collect(Collectors.joining(",")));
    return details;
  }
}

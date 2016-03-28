package org.januslabs.oauth2.jwt.mongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import lombok.Data;

@Data
public class OAuthAccessToken {
  @Id
  private String tokenId;
  private String authenticationId;
  private String userName;
  private String clientId;
  private String refreshToken;
  private byte[] oAuth2AccessToken;
  private byte[] oauth2Request;
  private byte[] authentication;
  private OAuthUser user;

  public OAuthAccessToken(final OAuth2AccessToken oAuth2AccessToken,
      final OAuth2Authentication authentication, final String authenticationId, String jti,
      String refreshJTI) {
    this.tokenId = jti;
    this.oAuth2AccessToken = SerializationUtils.serialize(oAuth2AccessToken);
    this.authenticationId = authenticationId;
    this.userName = authentication.getName();
    this.oauth2Request = SerializationUtils
        .serialize(SerializationUtils.serialize(authentication.getOAuth2Request()));
    this.clientId = authentication.getOAuth2Request().getClientId();
    this.authentication = SerializationUtils.serialize(authentication);
    this.refreshToken = refreshJTI;
    this.user = authentication.getUserAuthentication() != null
        ? (OAuthUser) authentication.getUserAuthentication().getPrincipal() : null;
  }
}

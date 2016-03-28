package org.januslabs.oauth2.jwt.mongo.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import lombok.Data;

@Data
public class OAuthRefreshToken {

  @Id
  private String tokenId;
  private byte[] oAuth2RefreshToken;
  private byte[] authentication;

  public OAuthRefreshToken(OAuth2RefreshToken oAuth2RefreshToken,
      OAuth2Authentication authentication, String jti) {
    this.oAuth2RefreshToken = SerializationUtils.serialize((Serializable) oAuth2RefreshToken);
    this.authentication = SerializationUtils.serialize((Serializable) authentication);
    this.tokenId = jti;
  }
}

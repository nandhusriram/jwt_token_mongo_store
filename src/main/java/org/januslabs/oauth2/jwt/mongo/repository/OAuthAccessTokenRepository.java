package org.januslabs.oauth2.jwt.mongo.repository;

import java.util.List;

import org.januslabs.oauth2.jwt.mongo.domain.OAuthAccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuthAccessTokenRepository extends MongoRepository<OAuthAccessToken, String> {
  public OAuthAccessToken findByTokenId(String tokenId);

  public OAuthAccessToken findByAuthenticationId(String authenticationId);

  public List<OAuthAccessToken> findByClientId(String clientId);

  public List<OAuthAccessToken> findByClientIdAndUserName(String clientId, String userName);

  public OAuthAccessToken findByRefreshToken(String refreshToken);

  public void delete(OAuthAccessToken oauthAccessToken);
}

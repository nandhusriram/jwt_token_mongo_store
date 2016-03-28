package org.januslabs.oauth2.jwt.mongo.repository;

import org.januslabs.oauth2.jwt.mongo.domain.OAuthRefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuthRefreshTokenRepository extends MongoRepository<OAuthRefreshToken, String> {

  public OAuthRefreshToken findByTokenId(String tokenId);

  public void delete(OAuthRefreshToken oauthRefreshToken);
}

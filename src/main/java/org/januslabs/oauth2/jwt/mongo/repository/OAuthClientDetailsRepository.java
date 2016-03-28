package org.januslabs.oauth2.jwt.mongo.repository;

import org.januslabs.oauth2.jwt.mongo.domain.OAuthClientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuthClientDetailsRepository extends MongoRepository<OAuthClientDetails, String> {

  public OAuthClientDetails findByClientId(String clientId);
}

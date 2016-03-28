package org.januslabs.oauth2.jwt.mongo.repository;

import org.januslabs.oauth2.jwt.mongo.domain.OAuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuthUserRepository extends MongoRepository<OAuthUser, String> {

  public OAuthUser findByLogin(String login);
}

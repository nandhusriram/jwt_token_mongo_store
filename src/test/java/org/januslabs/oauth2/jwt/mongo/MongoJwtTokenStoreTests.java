package org.januslabs.oauth2.jwt.mongo;

import org.januslabs.oauth2.jwt.base.OAuthClientDetailsService;
import org.januslabs.oauth2.jwt.base.OAuthUserService;
import org.januslabs.oauth2.jwt.mongo.domain.OAuthUser;
import org.januslabs.oauth2.jwt.mongo.repository.OAuthAccessTokenRepository;
import org.januslabs.oauth2.jwt.mongo.repository.OAuthClientDetailsRepository;
import org.januslabs.oauth2.jwt.mongo.repository.OAuthRefreshTokenRepository;
import org.januslabs.oauth2.jwt.mongo.repository.OAuthUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JwtMongoTestApplication.class)
@Rollback
public class MongoJwtTokenStoreTests {

  private @Autowired OAuthUserRepository userRepository;
  private @Autowired OAuthAccessTokenRepository accessTokenRepository;
  private @Autowired OAuthRefreshTokenRepository refreshTokenRepository;
  private @Autowired OAuthClientDetailsRepository clientDetailsRepository;
  private @Autowired OAuthUserService authUserService;
  private @Autowired OAuthClientDetailsService clientsDetailsService;

  @Test
  public void contextLoads() {
    Assert.notNull(accessTokenRepository);
    Assert.notNull(refreshTokenRepository);
    Assert.notNull(userRepository);
    Assert.notNull(clientDetailsRepository);
    Assert.notNull(authUserService);
    Assert.notNull(clientsDetailsService);
  }

  @Test
  public void dummyUser() {
    OAuthUser user = OAuthUser.builder().password("test").email("nandhusriram@gmail.com.com")
        .login("nandhusriram").name("Nandhu Sriram").enabled(true)
        .roles("ROLE_USER, SCOPE_READ, SCOPE_WRITE").build();
    user = userRepository.save(user);
    Assert.notNull(userRepository.findByLogin("nandhusriram").getLogin());
  }

}

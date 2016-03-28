package org.januslabs.oauth2.jwt.mongo.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.januslabs.oauth2.jwt.base.JWTCommon;
import org.januslabs.oauth2.jwt.mongo.domain.OAuthAccessToken;
import org.januslabs.oauth2.jwt.mongo.domain.OAuthRefreshToken;
import org.januslabs.oauth2.jwt.mongo.domain.OAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional(value = "JWTMongoTokenStore", rollbackFor = Exception.class)
public class JWTMongoTokenStore extends JwtTokenStore {

  private @Autowired OAuthAccessTokenRepository accessTokenDao;
  private @Autowired OAuthRefreshTokenRepository refreshTokenDao;
  private final AuthenticationKeyGenerator authenticationKeyGenerator =
      new DefaultAuthenticationKeyGenerator();

  public JWTMongoTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
    super(jwtTokenEnhancer);

  }

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken paramOAuth2AccessToken) {
    OAuthAccessToken token = readAuthenticationFromDB(paramOAuth2AccessToken.getValue());

    OAuth2Authentication token2 = SerializationUtils.deserialize(token.getAuthentication());

    if (!token2.isClientOnly()) {
      @SuppressWarnings("unused")
      OAuthUser loggedUser = (OAuthUser) token2.getUserAuthentication().getPrincipal();

    }
    return token2;
  }

  private OAuthAccessToken readAuthenticationFromDB(String value) {
    JWTCommon common = extractJtiFromRefreshToken(value);
    OAuthAccessToken storedObject = accessTokenDao.findByTokenId(common.getJti());

    if (storedObject == null)
      return null;

    return storedObject;
  }

  @Override
  public OAuth2Authentication readAuthentication(String paramString) {
    OAuthAccessToken storedObject = readAuthenticationFromDB(paramString);

    return SerializationUtils.deserialize(storedObject.getAuthentication());
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken paramOAuth2AccessToken,
      OAuth2Authentication paramOAuth2Authentication) {
    JWTCommon accessJTI = null;
    try {
      accessJTI = Serializer.createFromJson(JWTCommon.class,
          JwtHelper.decode(paramOAuth2AccessToken.getValue()).getClaims());
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String refreshJTIValue = null;
    if (paramOAuth2AccessToken.getRefreshToken() != null) {
      JWTCommon refreshJTI =
          extractJtiFromRefreshToken(paramOAuth2AccessToken.getRefreshToken().getValue());
      refreshJTIValue = refreshJTI.getJti();
    } else {
      refreshJTIValue = null;
    }

    OAuthAccessToken accessToken = new OAuthAccessToken(paramOAuth2AccessToken,
        paramOAuth2Authentication, authenticationKeyGenerator.extractKey(paramOAuth2Authentication),
        accessJTI.getJti(), refreshJTIValue);

    accessTokenDao.save(accessToken);
  }


  private JWTCommon extractJtiFromRefreshToken(String original) {
    JWTCommon result = null;
    try {
      result = Serializer.createFromJson(JWTCommon.class, JwtHelper.decode(original).getClaims());
      while (result.getJti().length() > 36) {
        result = extractJtiFromRefreshToken(result.getJti());
      }
    } catch (Exception e) {
      result = new JWTCommon();
      result.setJti(original);
    }
    return result;

  }

  @Override
  public OAuth2AccessToken readAccessToken(String paramString) {
    JWTCommon common = extractJtiFromRefreshToken(paramString);
    OAuthAccessToken storedObject = accessTokenDao.findByTokenId(common.getJti());
    if (storedObject == null)
      return null;
    Object authentication = SerializationUtils.deserialize(storedObject.getOAuth2AccessToken());

    return (OAuth2AccessToken) authentication;
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken paramOAuth2AccessToken) {
    JWTCommon common = extractJtiFromRefreshToken(paramOAuth2AccessToken.getValue());
    OAuthAccessToken storedObject = accessTokenDao.findByTokenId(common.getJti());
    if (storedObject != null)
      accessTokenDao.delete(storedObject);
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken paramOAuth2RefreshToken,
      OAuth2Authentication paramOAuth2Authentication) {
    JWTCommon common = extractJtiFromRefreshToken(paramOAuth2RefreshToken.getValue());
    refreshTokenDao.save(
        new OAuthRefreshToken(paramOAuth2RefreshToken, paramOAuth2Authentication, common.getJti()));
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String paramString) {
    JWTCommon common = extractJtiFromRefreshToken(paramString);
    OAuthRefreshToken refreshEntity = refreshTokenDao.findByTokenId(common.getJti());
    if (refreshEntity == null)
      return null;
    return SerializationUtils.deserialize(refreshEntity.getOAuth2RefreshToken());
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(
      OAuth2RefreshToken paramOAuth2RefreshToken) {
    JWTCommon common = extractJtiFromRefreshToken(paramOAuth2RefreshToken.getValue());
    OAuthRefreshToken storedObject = refreshTokenDao.findByTokenId(common.getJti());
    if (storedObject == null)
      return null;

    return SerializationUtils.deserialize(storedObject.getAuthentication());
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken paramOAuth2RefreshToken) {
    JWTCommon common = extractJtiFromRefreshToken(paramOAuth2RefreshToken.getValue());
    OAuthRefreshToken storedObject = refreshTokenDao.findByTokenId(common.getJti());
    if (storedObject != null)
      refreshTokenDao.delete(storedObject);
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken paramOAuth2RefreshToken) {
    JWTCommon common = extractJtiFromRefreshToken(paramOAuth2RefreshToken.getValue());
    OAuthAccessToken storedToken = accessTokenDao.findByRefreshToken(common.getJti());
    if (storedToken != null)
      accessTokenDao.delete(storedToken);
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication paramOAuth2Authentication) {
    OAuthAccessToken storedObject = accessTokenDao
        .findByAuthenticationId(authenticationKeyGenerator.extractKey(paramOAuth2Authentication));
    if (storedObject == null)
      return null;

    Object authentication = SerializationUtils.deserialize(storedObject.getOAuth2AccessToken());

    return (OAuth2AccessToken) authentication;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String paramString1,
      String paramString2) {
    List<OAuthAccessToken> result =
        accessTokenDao.findByClientIdAndUserName(paramString1, paramString2);
    List<OAuth2AccessToken> oauthAccTokens = new ArrayList<>();
    for (OAuthAccessToken token : result) {
      oauthAccTokens
          .add((OAuth2AccessToken) SerializationUtils.deserialize(token.getOAuth2AccessToken()));
    }
    return oauthAccTokens;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String paramString) {
    List<OAuthAccessToken> result = accessTokenDao.findByClientId(paramString);
    List<OAuth2AccessToken> oauthAccTokens = new ArrayList<>();
    for (OAuthAccessToken token : result) {
      oauthAccTokens
          .add((OAuth2AccessToken) SerializationUtils.deserialize(token.getOAuth2AccessToken()));
    }
    return oauthAccTokens;
  }

  public static class Serializer {
    public static JWTCommon createFromJson(Class<JWTCommon> clazz, String json)
        throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return objectMapper.readValue(json, clazz);
    }
  }


}

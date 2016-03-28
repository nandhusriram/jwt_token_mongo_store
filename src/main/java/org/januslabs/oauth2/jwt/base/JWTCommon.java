package org.januslabs.oauth2.jwt.base;

import java.util.List;

import org.januslabs.oauth2.jwt.mongo.domain.OAuthUser;

import lombok.Data;

@Data
public class JWTCommon {

  private Long exp;
  private String jti;
  private String client_id;
  private List<String> authorities;
  private List<String> scope;
  private String user_name;
  private OAuthUser user;
}

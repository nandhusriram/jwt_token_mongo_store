package org.januslabs.oauth2.jwt.mongo.domain;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class OAuthUser {

  private String name;
  @Id
  private String login;
  private String password;
  private String roles;
  private boolean enabled;
  private String email;

  public OAuthUser(OAuthUser user) {
    super();
    this.name = user.getName();
    this.login = user.getLogin();
    this.password = user.getPassword();
    this.roles = user.getRoles();
    this.enabled = user.isEnabled();
    this.email = user.getEmail();
  }

 

}

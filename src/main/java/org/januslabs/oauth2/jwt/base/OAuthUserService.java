package org.januslabs.oauth2.jwt.base;

import java.util.Collection;

import org.januslabs.oauth2.jwt.mongo.domain.OAuthUser;
import org.januslabs.oauth2.jwt.mongo.repository.OAuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OAuthUserService implements UserDetailsService {

  private @Autowired OAuthUserRepository userRepository;

  private static final class UserRepositoryUserDetails extends OAuthUser implements UserDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -2971203147338138382L;

    private UserRepositoryUserDetails(OAuthUser user) {
      super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return AuthorityUtils.commaSeparatedStringToAuthorityList(getRoles());
    }

    @Override
    public String getPassword() {
      return getPassword();
    }

    @Override
    public String getUsername() {
      return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
      return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
      return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return isEnabled();
    }

    @Override
    public boolean isEnabled() {
      return true;
    }

  }



  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    OAuthUser user = userRepository.findByLogin(username);
    if (user == null) {
      throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
    }
    return new UserRepositoryUserDetails(user);
  }

}

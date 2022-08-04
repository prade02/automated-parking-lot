package com.automated.parkinglot.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service("fakeService")
@AllArgsConstructor
public class FakeUserDetailsService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;

  /**
   * A dummy implementation which always returns a fake UserDetails irrespective of credentials in
   * request. Note: Password should be `admin` else authentication will fail.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .authorities(new SimpleGrantedAuthority("ROLE_Admin"))
            .build();
  }
}


package com.automated.parkinglot.security.configuration;

import com.automated.parkinglot.security.authentication.JwtInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtInterceptor jwtInterceptor;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .accessDecisionManager(accessDecisionManager())
            .antMatchers("/api/v1/booked-slot/amount/**").permitAll()
            .antMatchers("/api/v1/slot-booking/book/**").hasAuthority("BOT")
            .antMatchers("/api/v1/booked-slot/release/**").hasAuthority("BOT")
            .antMatchers("/api/v1/**").hasAuthority("ADMIN")
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtInterceptor, FilterSecurityInterceptor.class)
            .csrf().disable();
  }

  private AccessDecisionManager accessDecisionManager() {
    return new UnanimousBased(List.of(new WebExpressionVoter()));
  }

  @Configuration
  static class RoleHierarchyConfiguration {
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
      return new RoleHierarchyImpl();
    }
  }
}

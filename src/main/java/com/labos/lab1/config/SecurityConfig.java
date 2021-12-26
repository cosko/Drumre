package com.labos.lab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/assets/**", "/login**", "/").permitAll();
    http.authorizeRequests()
        .antMatchers("/login").permitAll().anyRequest().authenticated()
        .and()
        .oauth2Login()
          .loginPage("/login")
          .defaultSuccessUrl("/loginSuccess");

    http.logout(l -> l.logoutSuccessUrl("/").permitAll());
  }

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    UserDetails user =
        User.builder()
            .username("user")
            .password("pass")
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(user);
  }
}

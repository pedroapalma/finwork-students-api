package com.ppalma.studentsapi.shared.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authz) -> authz
            .requestMatchers(HttpMethod.GET, "/api/v1/students/**")
            .hasAuthority("SCOPE_students/basic")
            .requestMatchers(HttpMethod.POST, "/api/v1/students")
            .hasAuthority("SCOPE_students/basic")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/students/**")
            .hasAuthority("SCOPE_students/basic")
            .requestMatchers(HttpMethod.GET, "/api/v1/students")
            .hasAuthority("SCOPE_students/basic")
            .requestMatchers(HttpMethod.POST, "/api/v1/students/average-notes")
            .hasAuthority("SCOPE_students/average-notes")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
    return http.build();
  }
}
package com.ppalma.studentsapi.infrastructure.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication/scopes")
class UserInfoController {

  @GetMapping
  Map<String, Object> currentUserDetails() {
    return this.getLoginUserDetails();
  }

  Map<String, Object> getLoginUserDetails() {
    Map<String, Object> map = new HashMap<>();
    JwtAuthenticationToken authentication =
        (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();

    map.put("username", jwt.getClaimAsString("preferred_username"));
    map.put("email", jwt.getClaimAsString("email"));
    map.put("name", jwt.getClaimAsString("name"));
    map.put("token", jwt.getTokenValue());
    map.put("authorities", authentication.getAuthorities());
    map.put("roles", this.getRoles(jwt));

    return map;
  }

  List<String> getRoles(Jwt jwt) {
    Map<String, Object> realm_access = (Map<String, Object>) jwt.getClaims().get("realm_access");
    if (realm_access != null && !realm_access.isEmpty()) {
      return (List<String>) realm_access.get("roles");
    }
    return List.of();
  }
}
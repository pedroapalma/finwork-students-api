package com.ppalma.studentsapi.infrastructure.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ppalma.studentsapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
class AuthInfoControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testAuthDetails() throws Exception {
    this.mockMvc.perform(get("/api/v1/auth/details")
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority("VALID_SCOPE"))))
        .andExpect(status().isOk());
  }
}

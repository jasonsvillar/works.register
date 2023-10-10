package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.configs.security.JwtTokenProvider;
import com.jasonvillar.works.register.configs.security.SecurityUser;
import com.jasonvillar.works.register.configtests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.dto.security.AuthenticationRequest;
import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.services.JWTBlacklistService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AuthController.class})
class AuthControllerTest extends ControllerTestTemplate {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JWTBlacklistService jwtBlacklistService;

    @MockBean
    private TaskScheduler taskScheduler;

    private final User user = User.builder()
            .name("Test Name")
            .password("Top Secret")
            .email("test@test.com")
            .build();

    SecurityUser principal = new SecurityUser(user);

    @Test
    void basicAuth() throws Exception {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());

        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
        Mockito.when(jwtTokenProvider.createToken(any())).thenReturn("NewJwtToken");

        String requestJson = ow.writeValueAsString(new AuthenticationRequest("Test Name", "Top Secret"));
        this.mockMvc.perform(post("/api/auth/basic-authentication").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    void logoutSuccess() throws Exception {
        this.mockMvc.perform(get("/api/auth/logout/success").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Some User")
    void givenAuthenticatedUser_whenCheckAuthentication_thenStatusOk() throws Exception {
        this.mockMvc.perform(get("/api/auth/authentication-required").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}

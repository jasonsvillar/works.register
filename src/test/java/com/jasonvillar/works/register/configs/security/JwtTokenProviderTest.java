package com.jasonvillar.works.register.configs.security;

import com.jasonvillar.works.register.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration
class JwtTokenProviderTest {
    private final User user = User.builder()
            .name("Name")
            .password("Sarasa")
            .email("test@test.com")
            .build();

    SecurityUser principal = new SecurityUser(user);

    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    Authentication authentication =
            new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());

    @Test
    void createTokenTest() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        Assertions.assertThat(token).isNotBlank();
    }

    @Test
    void getUsernameTest() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        String username = jwtTokenProvider.getUsername(token);

        Assertions.assertThat(username).isEqualTo("Name");
    }

    @Test
    void validateTokenTest() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        boolean isValid = jwtTokenProvider.validateToken(token);

        String invalidToken = token + "asd123";

        isValid = jwtTokenProvider.validateToken(invalidToken);

        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    void resolveToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        String token = jwtTokenProvider.createToken(authentication);

        when(request.getHeader("Authorization")).thenReturn(null);
        String bearerToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertThat(bearerToken).isNull();

        when(request.getHeader("Authorization")).thenReturn(token);
        bearerToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertThat(bearerToken).isNull();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        bearerToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertThat(bearerToken).isEqualTo(token);
    }
}

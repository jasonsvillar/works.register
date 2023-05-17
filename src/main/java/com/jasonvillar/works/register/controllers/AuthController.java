package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.configs.security.JwtTokenProvider;
import com.jasonvillar.works.register.dto.security.AuthenticationRequest;
import com.jasonvillar.works.register.dto.security.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final OAuth2AuthorizedClientService clientService;

    @PostMapping("/basic-authentication")
    public ResponseEntity<AuthenticationResponse> basicAuthentication(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (
                        authenticationRequest.userName(),
                        authenticationRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<String> oauth2SuccessAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal().equals("anonymousUser")) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        } else {
            OAuth2AuthenticationToken oauthToken =
                    (OAuth2AuthenticationToken) authentication;

            OAuth2AuthorizedClient client =
                    clientService.loadAuthorizedClient(
                            oauthToken.getAuthorizedClientRegistrationId(),
                            oauthToken.getName());

            String accessToken = client.getAccessToken().getTokenValue();

            HashMap<String, String> map = new HashMap<>();
            map.put("access_token", accessToken);
            map.put("scope", "read:user");
            map.put("token_type", "bearer");

            OAuth2RefreshToken oAuth2RefreshToken = client.getRefreshToken();
            if (oAuth2RefreshToken != null) {
                map.put("refresh_token", oAuth2RefreshToken.getTokenValue());
            }

            return ResponseEntity.ok("Authenticated");
        }
    }

    @GetMapping("/authentication-required")
    public ResponseEntity<String> authenticationRequired() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        } else {
            return ResponseEntity.ok("You already are authenticated");
        }
    }

    @GetMapping( "/logout/success")
    public ResponseEntity<String> logoutSuccess() {
        return ResponseEntity.ok("Logout success");
    }
}

package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.configs.security.JwtTokenProvider;
import com.jasonvillar.works.register.dto.security.AuthenticationRequest;
import com.jasonvillar.works.register.dto.security.AuthenticationResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
@SecurityRequirements
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

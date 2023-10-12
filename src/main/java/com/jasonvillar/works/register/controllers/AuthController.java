package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.configs.security.JwtTokenProvider;
import com.jasonvillar.works.register.dto.security.AuthenticationRequest;
import com.jasonvillar.works.register.dto.security.AuthenticationResponse;
import com.jasonvillar.works.register.entities.JWTBlacklist;
import com.jasonvillar.works.register.schedule_tasks.DeleteToExpireJWT;
import com.jasonvillar.works.register.services.JWTBlacklistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
@SecurityRequirements
@Transactional
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private final JWTBlacklistService jwtBlacklistService;

    private final TaskScheduler taskScheduler;

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

    @GetMapping("/custom-logout")
    public ResponseEntity<String> performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String jwt = this.jwtTokenProvider.resolveToken(request);
        this.logoutHandler.logout(request, response, authentication);

        Date expiration = this.jwtTokenProvider.getExpirationDate(jwt);

        JWTBlacklist jwtBlacklist = new JWTBlacklist();
        jwtBlacklist.setToken(jwt);
        jwtBlacklist.setDateExpire(expiration);

        this.jwtBlacklistService.save(jwtBlacklist);

        DeleteToExpireJWT task = new DeleteToExpireJWT(jwt, this.jwtBlacklistService);
        this.taskScheduler.schedule(task, expiration.toInstant());

        return ResponseEntity.ok("Logout success");
    }

    @GetMapping( "/logout/success")
    public ResponseEntity<String> logoutSuccess() {
        return ResponseEntity.ok("Logout success");
    }
}

package com.jasonvillar.works.register.authentication;

import com.jasonvillar.works.register.security.JwtTokenProvider;
import com.jasonvillar.works.register.authentication.port.in.AuthenticationRequest;
import com.jasonvillar.works.register.authentication.port.out.AuthenticationResponse;
import com.jasonvillar.works.register.security.SecurityUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "2 - Authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private final JWTBlacklistService jwtBlacklistService;

    private final TaskScheduler taskScheduler;

    private final SecurityUserDetailsService securityUserDetailsService;

    @SecurityRequirements
    @PostMapping("/basic-authentication")
    public ResponseEntity<AuthenticationResponse> basicAuthentication(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (
                        authenticationRequest.userName(),
                        authenticationRequest.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);

        SecurityUser securityUser = this.securityUserDetailsService.userDetailsToSecurityUser(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                securityUser.getId(),
                securityUser.getUsername(),
                securityUser.getEmail(),
                securityUser.getAuthorities()
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        jwt
                )
                .body(authenticationResponse);
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

    @GetMapping(value = "/logout-jwt", produces = "application/json")
    public ResponseEntity<String> performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String jwt = this.jwtTokenProvider.resolveToken(request);
        this.logoutHandler.logout(request, response, authentication);

        Date expiration = this.jwtTokenProvider.getExpirationDate(jwt);

        JWTBlacklist jwtBlacklist = new JWTBlacklist();
        jwtBlacklist.setToken(jwt);
        jwtBlacklist.setDateExpire(expiration);

        this.jwtBlacklistService.save(jwtBlacklist);

        DeleteToExpireJwtTask task = new DeleteToExpireJwtTask(jwt, this.jwtBlacklistService);
        this.taskScheduler.schedule(task, expiration.toInstant());

        return ResponseEntity.ok().build();
    }
}

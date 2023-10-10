package com.jasonvillar.works.register.configs.security;

import com.jasonvillar.works.register.services.JWTBlacklistService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JWTBlacklistService jwtBlacklistService;

    private Key getSignInKey() {
        String secretKey = "asd+SFG5fg-QWEKLsfdgsd4115SDADGHJGH4sd5f4sd4gs32d1534hg54da35sf4g5r44g35dfa1g352dfg135f45hj45k4jk5l4j35kl4546413sd13sa4r534hb53df1b35n135hm454l543d1vg32d1b534";
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        int minutes = 60;
        int minutesInMilliseconds = minutes * 60 * 1000;
        Date expiryDate = new Date(now.getTime() + minutesInMilliseconds);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(this.getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(this.getSignInKey()).build().parseClaimsJws(jwt);

            boolean isInBlacklist = this.jwtBlacklistService.isInBlacklist(jwt);
            if (isInBlacklist) {
                return false;
            }

            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        } catch (SignatureException e) {
            log.error("there is an error with the signature of you token ");
        }

        return false;
    }

    public String getUsername(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public Date getExpirationDate(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody().getExpiration();
    }
}
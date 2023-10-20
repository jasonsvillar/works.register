package com.jasonvillar.works.register.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JWTBlacklistService {
    private final JWTBlacklistRepository jwtBlacklistRepository;

    public Optional<JWTBlacklist> getOptionalByToken(String jwt) {
        return jwtBlacklistRepository.findOptionalByToken(jwt);
    }

    public JWTBlacklist save(JWTBlacklist entity) {
        return this.jwtBlacklistRepository.save(entity);
    }

    public void deleteByToken(String jwt) {
        this.jwtBlacklistRepository.deleteByToken(jwt);
    }

    public boolean isInBlacklist(String jwt) {
        return this.getOptionalByToken(jwt).isPresent();
    }
}

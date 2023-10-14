package com.jasonvillar.works.register.authentication;

import com.jasonvillar.works.register.authentication.JWTBlacklist;
import com.jasonvillar.works.register.authentication.JWTBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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

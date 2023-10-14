package com.jasonvillar.works.register.authentication;

import com.jasonvillar.works.register.authentication.JWTBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface JWTBlacklistRepository extends JpaRepository<JWTBlacklist, String> {
    Optional<JWTBlacklist> findOptionalByToken(String token);
    List<JWTBlacklist> findAllByDateExpireBefore(Date date);
    void deleteByToken(String jwt);
    void deleteByDateExpireBefore(Date date);
}

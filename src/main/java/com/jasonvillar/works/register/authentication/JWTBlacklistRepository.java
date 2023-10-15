package com.jasonvillar.works.register.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface JWTBlacklistRepository extends JpaRepository<JWTBlacklist, String> {
    Optional<JWTBlacklist> findOptionalByToken(String token);
    List<JWTBlacklist> findAllByDateExpireBefore(Date date);
    void deleteByToken(String jwt);
    void deleteByDateExpireBefore(Date date);
}

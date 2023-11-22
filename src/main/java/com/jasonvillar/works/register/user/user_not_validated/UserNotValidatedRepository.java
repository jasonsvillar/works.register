package com.jasonvillar.works.register.user.user_not_validated;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserNotValidatedRepository extends JpaRepository<UserNotValidated, UserNotValidatedId> {
    Optional<UserNotValidated> findByUserNotValidatedIdAndCode(UserNotValidatedId userNotValidatedId, String code);
    long deleteByUserNotValidatedId(UserNotValidatedId userNotValidatedId);
}

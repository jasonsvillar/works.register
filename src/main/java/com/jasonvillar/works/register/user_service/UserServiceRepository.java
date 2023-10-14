package com.jasonvillar.works.register.user_service;

import com.jasonvillar.works.register.user_service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserServiceRepository extends JpaRepository<UserService, Long> {
    Optional<UserService> findOptionalByUserIdAndServiceId(long userId, long serviceId);

    List<UserService> findAllByUserId(long userId);

    List<UserService> findAllByServiceId(long serviceId);

    UserService findUserServiceById(long id);

    Optional<UserService> findOptionalById(long id);
}

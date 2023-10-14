package com.jasonvillar.works.register.user_client;

import com.jasonvillar.works.register.user_client.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserClientRepository extends JpaRepository<UserClient, Long> {
    UserClient findUserClientById(long id);
    Optional<UserClient> findOptionalById(long id);
    Optional<UserClient> findOptionalByUserIdAndClientId(long userId, long clientId);
    List<UserClient> findAllByUserId(long userId);
    List<UserClient> findAllByClientId(long clientId);
}

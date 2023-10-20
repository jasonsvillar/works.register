package com.jasonvillar.works.register.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(long id);

    Optional<User> findOptionalById(long id);

    Optional<User> findOptionalByName(String userName);

    Optional<User> findOptionalByEmail(String email);

    List<User> findAllByNameContainingIgnoreCase(String name);

    List<User> findAllByEmailContainingIgnoreCase(String email);

    List<User> findAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(String name, String email);
}

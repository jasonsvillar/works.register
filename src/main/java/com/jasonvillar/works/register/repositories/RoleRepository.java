package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(long id);

    Optional<Role> findOptionalById(long id);

    List<Role> findAllByNameContainingIgnoreCase(String name);
}

package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findPrivilegeById(Integer id);

    Optional<Privilege> findOptionalById(Integer id);

    Optional<Privilege> findOptionalByName(String name);

    List<Privilege> findAllByNameContainingIgnoreCase(String name);
}

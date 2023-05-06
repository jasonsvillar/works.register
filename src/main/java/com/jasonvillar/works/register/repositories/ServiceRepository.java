package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findServiceById(long id);
    Optional<Service> findOptionalById(long id);
    Optional<Service> findOptionalByName(String name);
    List<Service> findAllByNameContainingIgnoreCase(String name);
}

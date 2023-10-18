package com.jasonvillar.works.register.service;

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
    Optional<Service> findOptionalByIdAndUserServiceListUserId(long id, long userId);
    List<Service> findAllByUserServiceListUserId(long userId);
    List<Service> findAllByNameContainingIgnoreCaseAndUserServiceListUserId(String name, long userId);
}

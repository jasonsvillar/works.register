package com.jasonvillar.works.register.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findServiceById(long id);
    Optional<Service> findOptionalById(long id);
    Optional<Service> findOptionalByName(String name);
    List<Service> findAllByNameContainingIgnoreCase(String name);
    Optional<Service> findOptionalByIdAndUserServiceListUserId(long id, long userId);
    List<Service> findAllByUserServiceListUserId(long userId, Pageable pageable);
    List<Service> findAllByNameContainingIgnoreCaseAndUserServiceListUserId(String name, long userId);
    long countByUserServiceListUserId(long userId);
    long count();

    @Query("SELECT s " +
            "FROM Service s " +
            "WHERE s.id NOT IN " +
            "( " +
            "SELECT us.serviceId " +
            "FROM UserService us " +
            "WHERE us.userId = :userId" +
            ")")
    List<Service> findAllByUserIdNot(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT COUNT(*) " +
            "FROM Service s " +
            "WHERE s.id NOT IN " +
            "(" +
            "SELECT us.serviceId " +
            "FROM UserService us " +
            "WHERE us.userId = :userId" +
            ")")
    long countByUserIdNot(@Param("userId") long userId);
}

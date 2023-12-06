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
    List<Service> findAllByUserId(long userId, Pageable pageable);
    Optional<Service> findOptionalById(long id);
    Optional<Service> findOptionalByNameAndUserId(String name, long userId);
    long countByUserId(long userId);
    long count();
    Optional<Service> findOptionalByIdAndUserId(long id, long userId);
    List<Service> findAllByNameContainingIgnoreCaseAndUserId(String name, long userId);
    boolean deleteByIdAndUserId(long id, long userId);

    @Query("SELECT s " +
            "FROM Service s " +
            "LEFT JOIN WorkRegister w ON (s.id = w.serviceId) " +
            "WHERE " +
            "s.id IN :idList AND " +
            "s.user.id = :userId AND " +
            "w.id IS NULL")
    List<Service> findAllByIdAndUserIdAndServiceNotInWorkRegister(@Param("idList") List<Long> idList, @Param("userId") long userId);
}

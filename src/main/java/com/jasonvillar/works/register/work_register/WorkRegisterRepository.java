package com.jasonvillar.works.register.work_register;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRegisterRepository extends JpaRepository<WorkRegister, Long> {
    List<WorkRegister> findAllByUserId(long userId);

    List<WorkRegister> findAllByClientId(long clientId);

    List<WorkRegister> findAllByUserIdAndClientId(long userId, long clientId);

    List<WorkRegister> findAllByTitleContainingIgnoreCase(String title);

    WorkRegister findWorkRegisterById(long id);

    Optional<WorkRegister> findOptionalById(long id);

    List<WorkRegister> findAllByServiceId(long id);

    Optional<WorkRegister> findOptionalByIdAndUserId(long id, long userId);

    List<WorkRegister> findAllByTitleContainingIgnoreCaseAndUserId(String title, long userId);
}

package com.jasonvillar.works.register.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientById(long id);
    Optional<Client> findOptionalById(long id);
    Optional<Client> findOptionalByIdentificationNumber(String identificationNumber);
    Optional<Client> findOptionalByIdentificationNumberAndUserId(String identificationNumber, long userId);
    List<Client> findAllByNameContainingIgnoreCase(String name);
    List<Client> findAllBySurnameContainingIgnoreCase(String surname);
    List<Client> findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname);
    List<Client> findAllByIdentificationNumberContainingIgnoreCase(String identificationNumber);
    List<Client> findAllByUserId(long userId);
    Optional<Client> findOptionalByIdAndUserId(long id, long userId);
    List<Client> findAllByNameContainingIgnoreCaseAndUserId(String name, long userId);
    List<Client> findAllBySurnameContainingIgnoreCaseAndUserId(String surname, long userId);
    List<Client> findAllByIdentificationNumberContainingIgnoreCaseAndUserId(String identificationNumber, long userId);
    List<Client> findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserId(String name, String surname, long userId);
}

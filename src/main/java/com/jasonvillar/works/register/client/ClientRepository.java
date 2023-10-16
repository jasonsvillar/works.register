package com.jasonvillar.works.register.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientById(long id);
    Optional<Client> findOptionalById(long id);
    Optional<Client> findOptionalByDni(String dni);
    List<Client> findAllByNameContainingIgnoreCase(String name);
    List<Client> findAllBySurnameContainingIgnoreCase(String surname);
    List<Client> findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname);
    List<Client> findAllByDniContainingIgnoreCase(String dni);
    List<Client> findAllByUserClientListUserId(long userId);
    Optional<Client> findOptionalByIdAndUserClientListUserId(long id, long userId);
    List<Client> findAllByNameContainingIgnoreCaseAndUserClientListUserId(String name, long userId);
    List<Client> findAllBySurnameContainingIgnoreCaseAndUserClientListUserId(String surname, long userId);
    List<Client> findAllByDniContainingIgnoreCaseAndUserClientListUserId(String dni, long userId);
    List<Client> findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserClientListUserId(String name, String surname, long userId);
}

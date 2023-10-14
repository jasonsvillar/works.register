package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.client.Client;
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
}

package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> getList() {
        return clientRepository.findAll();
    }

    public Client getById(long id) {
        return clientRepository.findClientById(id);
    }

    public Optional<Client> getOptionalById(long id) {
        return clientRepository.findOptionalById(id);
    }

    public Optional<Client> getOptionalByDni(String dni) {
        return clientRepository.findOptionalByDni(dni);
    }

    public List<Client> getListByDniLike(String dni) {
        return clientRepository.findAllByDniContainingIgnoreCase(dni);
    }

    public List<Client> getListByNameLike(String name) {
        return clientRepository.findAllByNameContainingIgnoreCase(name);
    }

    public List<Client> getListBySurnameLike(String surname) {
        return clientRepository.findAllBySurnameContainingIgnoreCase(surname);
    }

    public List<Client> getListByNameLikeAndSurnameLike(String name, String surname) {
        return this.clientRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(name, surname);
    }

    public boolean isExistId(long id) {
        return this.getOptionalById(id).isPresent();
    }

    public boolean isExistDni(String dni) {
        return this.getOptionalByDni(dni).isPresent();
    }

    public Client save(Client entity) {
        return clientRepository.save(entity);
    }

    public String getValidationsMessageWhenCantBeSaved(Client entity) {
        StringBuilder message = new StringBuilder();

        if (this.isExistDni(entity.getDni())) {
            message.append("dni must be unique");
        }

        return message.toString();
    }
}

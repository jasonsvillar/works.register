package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.user_client.UserClient;
import com.jasonvillar.works.register.user_client.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserClientService userClientService;

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

    public Client saveWithUser(Client entity, long userId) {
        Client clientSaved = this.save(entity);

        UserClient userClient = UserClient.builder()
                .clientId(clientSaved.getId())
                .userId(userId)
                .build();

        this.userClientService.save(userClient);

        return clientSaved;
    }

    public String getValidationsMessageWhenCantBeSaved(Client entity) {
        StringBuilder message = new StringBuilder();

        if (this.isExistDni(entity.getDni())) {
            message.append("dni must be unique");
        }

        return message.toString();
    }

    public List<Client> getListByUserId(long userId) {
        return clientRepository.findAllByUserClientListUserId(userId);
    }

    public Optional<Client> getOptionalByIdAndUserId(long id, long userId) {
        return clientRepository.findOptionalByIdAndUserClientListUserId(id, userId);
    }

    public List<Client> getListByNameLikeAndUserId(String name, long userId) {
        return clientRepository.findAllByNameContainingIgnoreCaseAndUserClientListUserId(name, userId);
    }

    public List<Client> getListBySurnameLikeAndUserId(String surname, long userId) {
        return clientRepository.findAllBySurnameContainingIgnoreCaseAndUserClientListUserId(surname, userId);
    }

    public List<Client> getListByDniLikeAndUserId(String dni, long userId) {
        return clientRepository.findAllByDniContainingIgnoreCaseAndUserClientListUserId(dni, userId);
    }

    public List<Client> getListByNameLikeAndSurnameLikeAndUserId(String name, String surname, long userId) {
        return this.clientRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserClientListUserId(name, surname, userId);
    }
}

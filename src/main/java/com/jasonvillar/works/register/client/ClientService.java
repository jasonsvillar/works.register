package com.jasonvillar.works.register.client;

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

    public Optional<Client> getOptionalByIdentificationNumberAndUserId(String identificationNumber, long userId) {
        return clientRepository.findOptionalByIdentificationNumberAndUserId(identificationNumber, userId);
    }

    public List<Client> getListByIdentificationNumberLike(String identificationNumber) {
        return clientRepository.findAllByIdentificationNumberContainingIgnoreCase(identificationNumber);
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

    public boolean isExistIdentificationNumberForUserId(String identificationNumber, long userId) {
        return this.getOptionalByIdentificationNumberAndUserId(identificationNumber, userId).isPresent();
    }

    public Client save(Client entity) {
        return clientRepository.save(entity);
    }

    public String getValidationsMessageWhenCantBeSaved(Client entity, long userId) {
        StringBuilder message = new StringBuilder();

        if (this.isExistIdentificationNumberForUserId(entity.getIdentificationNumber(), userId)) {
            message.append("identification number must be unique");
        }

        return message.toString();
    }

    public List<Client> getListByUserId(long userId) {
        return clientRepository.findAllByUserId(userId);
    }

    public Optional<Client> getOptionalByIdAndUserId(long id, long userId) {
        return clientRepository.findOptionalByIdAndUserId(id, userId);
    }

    public List<Client> getListByNameLikeAndUserId(String name, long userId) {
        return clientRepository.findAllByNameContainingIgnoreCaseAndUserId(name, userId);
    }

    public List<Client> getListBySurnameLikeAndUserId(String surname, long userId) {
        return clientRepository.findAllBySurnameContainingIgnoreCaseAndUserId(surname, userId);
    }

    public List<Client> getListByIdentificationNumberLikeAndUserId(String IdentificationNumber, long userId) {
        return clientRepository.findAllByIdentificationNumberContainingIgnoreCaseAndUserId(IdentificationNumber, userId);
    }

    public List<Client> getListByNameLikeAndSurnameLikeAndUserId(String name, String surname, long userId) {
        return this.clientRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserId(name, surname, userId);
    }
}

package com.jasonvillar.works.register.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Specification<Client> makeSpecification(long userId, Long id, String name, String surname, String identificationNumber) {
        Specification<Client> specifications = ClientRepository.equalsUserId(userId);

        if (id != null)
            specifications = specifications.and(ClientRepository.equalsId(id));

        if (name != null)
            specifications = specifications.and(ClientRepository.containsName(name));

        if (surname != null)
            specifications = specifications.and(ClientRepository.containsSurname(surname));

        if (identificationNumber != null)
            specifications = specifications.and(ClientRepository.containsIdentificationNumber(identificationNumber));

        return specifications;
    }

    public List<Client> getList() {
        return clientRepository.findAll();
    }

    public List<Client> getListBySpecificationAndPage(Specification<Client> specification, int pageNumber, int rows) {
        Pageable page = PageRequest.of(pageNumber, rows, Sort.by("name"));
        return this.clientRepository.findAll(specification, page).stream().toList();
    }

    public long getRowCountBySpecification(Specification<Client> specification) {
        return this.clientRepository.count(specification);
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

    public List<Client> getListByIdentificationNumberLikeAndUserId(String identificationNumber, long userId) {
        return clientRepository.findAllByIdentificationNumberContainingIgnoreCaseAndUserId(identificationNumber, userId);
    }

    public List<Client> getListByNameLikeAndSurnameLikeAndUserId(String name, String surname, long userId) {
        return this.clientRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserId(name, surname, userId);
    }

    @Transactional
    public boolean deleteByClientIdAndUserId(long id, long userId) {
        return this.clientRepository.deleteByIdAndUserId(id, userId) > 0;
    }
}

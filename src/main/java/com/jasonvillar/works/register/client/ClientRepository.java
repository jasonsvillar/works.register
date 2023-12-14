package com.jasonvillar.works.register.client;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    static Specification<Client> equalsId(long id) {
        return (client, cq, cb) -> cb.equal(
                client.get("id"), id
        );
    }

    static Specification<Client> containsName(String name) {
        return (client, cq, cb) -> cb.like(
                cb.lower( client.get("name") ), "%" + name.toLowerCase() + "%"
        );
    }

    static Specification<Client> containsSurname(String surname) {
        return (client, cq, cb) -> cb.like(
                cb.lower( client.get("surname") ), "%" + surname.toLowerCase() + "%"
        );
    }

    static Specification<Client> containsIdentificationNumber(String identificationNumber) {
        return (client, cq, cb) -> cb.like(
                cb.lower( client.get("identificationNumber") ), "%" + identificationNumber.toLowerCase() + "%"
        );
    }

    static Specification<Client> equalsUserId(long userId) {
        return (client, cq, cb) -> cb.equal(
                client.get("user").get("id"), userId
        );
    }

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
    Integer deleteByIdAndUserId(long id, long userId);

    @Query("SELECT c " +
            "FROM Client c " +
            "LEFT JOIN WorkRegister w ON (c.id = w.clientId) " +
            "WHERE " +
            "c.id IN :idList AND " +
            "c.user.id = :userId AND " +
            "w.id IS NULL")
    List<Client> findAllByIdListAndUserIdAndClientNotInWorkRegister(@Param("idList") List<Long> idList, @Param("userId") long userId);
}

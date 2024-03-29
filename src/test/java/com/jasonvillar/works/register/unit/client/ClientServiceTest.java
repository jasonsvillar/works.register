package com.jasonvillar.works.register.unit.client;

import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.ClientRepository;
import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService service;

    private User userInDatabase = User.builder()
            .name("Dummy name")
            .email("Dummy surname")
            .id(1)
            .build();

    private Client entity = Client.builder()
            .name("Name")
            .surname("Surname")
            .identificationNumber("11222333")
            .user(this.userInDatabase)
            .build();

    @BeforeEach
    void setup() {
        entity.setId(1L);
    }

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findAll()).thenReturn(List.of(entity));

        List<Client> list = service.getList();

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetById_thenReturnEntity() {
        Mockito.when(repository.findClientById(1)).thenReturn(entity);

        Client entity = service.getById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetOptionalById_thenReturnOptional() {
        Mockito.when(repository.findOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(2)).thenReturn(Optional.empty());

        Optional<Client> optional = service.getOptionalById(1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalById(2);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetOptionalByIdentificationNumber_thenReturnOptional() {
        Mockito.when(repository.findOptionalByIdentificationNumberAndUserId("11222333", 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByIdentificationNumberAndUserId("00000000", 1)).thenReturn(Optional.empty());

        Optional<Client> optional = service.getOptionalByIdentificationNumberAndUserId("11222333", 1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByIdentificationNumberAndUserId("00000000", 1);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetListByIdentificationNumberLike_thenReturnList() {
        Mockito.when(repository.findAllByIdentificationNumberContainingIgnoreCase("11222333")).thenReturn(List.of(entity));

        List<Client> list = service.getListByIdentificationNumberLike("11222333");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLike_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCase("Name")).thenReturn(List.of(entity));

        List<Client> list = service.getListByNameLike("Name");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetListBySurnameLike_thenReturnList() {
        Mockito.when(repository.findAllBySurnameContainingIgnoreCase("Surname")).thenReturn(List.of(entity));

        List<Client> list = service.getListBySurnameLike("Surname");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLikeAndSurnameLike_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase("Name","Surname")).thenReturn(List.of(entity));

        List<Client> list = service.getListByNameLikeAndSurnameLike("Name","Surname");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRequest_whenIsExistId_thenCheckTrueAndFalse() {
        Mockito.when(repository.findOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(0)).thenReturn(Optional.empty());

        boolean exist = service.isExistId(1);
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistId(0);
        Assertions.assertThat(exist).isFalse();
    }

    @Test
    void givenRequest_whenIsExistIdentificationNumber_thenCheckTrueAndFalse() {
        Mockito.when(repository.findOptionalByIdentificationNumberAndUserId("11222333", 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByIdentificationNumberAndUserId("00000000", 1)).thenReturn(Optional.empty());

        boolean exist = service.isExistIdentificationNumberForUserId("11222333", 1);
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistIdentificationNumberForUserId("00000000", 1);
        Assertions.assertThat(exist).isFalse();
    }

    @Test
    void givenEntity_whenSave_themReturnEntitySaved() {
        Client newEntity = Client.builder()
                .name("Name")
                .surname("Surname")
                .identificationNumber("11222333")
                .build();
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        Client entitySaved = service.save(newEntity);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(repository.findOptionalByIdentificationNumberAndUserId("11222333", 1)).thenReturn(Optional.of(this.entity));
        String message = this.service.getValidationsMessageWhenCantBeSaved(this.entity, userInDatabase.getId());
        Assertions.assertThat(message).isNotEmpty();
    }

    @Test
    void givenRepositories_whenGetListByUserId_thenReturnList() {
        Mockito.when(repository.findAllByUserId(any(long.class))).thenReturn(List.of(entity));

        List<Client> list = service.getListByUserId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetOptionalByUserId_thenReturnOptional() {
        Mockito.when(repository.findOptionalByIdAndUserId(any(long.class), any(long.class))).thenReturn(Optional.of(entity));

        Optional<Client> optional = service.getOptionalByIdAndUserId(1 ,1);

        Assertions.assertThat(optional).isPresent();
    }

    @Test
    void givenRepositories_whenGetListByNameLikeAndUserId_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCaseAndUserId("name", 1)).thenReturn(List.of(entity));

        List<Client> list = service.getListByNameLikeAndUserId("name", 1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetListBySurnameLikeAndUserId_thenReturnList() {
        Mockito.when(repository.findAllBySurnameContainingIgnoreCaseAndUserId("surname", 1)).thenReturn(List.of(entity));

        List<Client> list = service.getListBySurnameLikeAndUserId("surname", 1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetListByIdentificationNumberLikeAndUserId_thenReturnList() {
        Mockito.when(repository.findAllByIdentificationNumberContainingIgnoreCaseAndUserId("11222333", 1)).thenReturn(List.of(entity));

        List<Client> list = service.getListByIdentificationNumberLikeAndUserId("11222333", 1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLikeAndSurnameLikeAndUserId_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserId("name", "surname", 1)).thenReturn(List.of(entity));

        List<Client> list = service.getListByNameLikeAndSurnameLikeAndUserId("name", "surname", 1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Client.class);
    }

    @Test
    void makeSpecification() {
        Specification<Client> specification = service.makeSpecification(1L, 1L, "Client 1 name", "surname", "11.222.333");
        Assertions.assertThat(specification).isNotNull();
    }

    @Test
    void getRowCountBySpecification() {
        Specification<Client> specification = service.makeSpecification(1L, 1L, "Client 1 name", "surname", "11.222.333");

        Mockito.when(repository.count(specification)).thenReturn(1L);

        long count = service.getRowCountBySpecification(specification);
        Assertions.assertThat(count).isEqualTo(1L);
    }

    @Test
    void getListBySpecificationAndPage() {
        Specification<Client> specification = service.makeSpecification(1L, 1L, "Client 1 name", "surname", "11.222.333");

        List<Client> clients = new ArrayList<>();
        clients.add(Client.builder().id(1).build());
        clients.add(Client.builder().id(2).build());

        Page<Client> page = new PageImpl<>(clients);

        Mockito.when(repository.findAll(eq(specification), any(Pageable.class))).thenReturn(page);

        List<Client> clientList = service.getListBySpecificationAndPage(specification, 0, 5);

        Assertions.assertThat(clientList).hasSize(2);
    }

    @Test
    void deleteByClientIdAndUserId() {
        Mockito.when(repository.deleteByIdAndUserId(1, 1)).thenReturn(1);
        Mockito.when(repository.deleteByIdAndUserId(0, 0)).thenReturn(0);

        boolean deleted = service.deleteByClientIdAndUserId(1, 1);
        Assertions.assertThat(deleted).isTrue();

        deleted = service.deleteByClientIdAndUserId(0, 0);
        Assertions.assertThat(deleted).isFalse();
    }

    @Test
    void getListByIdListAndUserIdAndNotInWorkRegister() {
        Mockito.when(repository.findAllByIdListAndUserIdAndClientNotInWorkRegister(List.of(1L, 2L), 1L)).thenReturn(
                List.of(
                        Client.builder().id(1L).build(),
                        Client.builder().id(2L).build()
                )
        );

        List<Client> clientList = service.getListByIdListAndUserIdAndNotInWorkRegister(List.of(1L, 2L), 1L);

        Assertions.assertThat(clientList).hasSize(2);
    }

    @Test
    void deleteByClientList() {
        Mockito.doNothing().when(repository).deleteAll(any());

        service.deleteByClientList(
                List.of(
                        Client.builder().id(1L).build(),
                        Client.builder().id(2L).build()
                )
        );

        verify(repository, times(1)).deleteAll(
                any()
        );
    }
}

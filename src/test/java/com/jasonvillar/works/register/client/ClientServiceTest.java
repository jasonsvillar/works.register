package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.ClientRepository;
import com.jasonvillar.works.register.client.ClientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService service;

    private Client entity = Client.builder()
            .name("Name")
            .surname("Surname")
            .dni("11222333")
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
    void givenRepositories_whenGetOptionalByDni_thenReturnOptional() {
        Mockito.when(repository.findOptionalByDni("11222333")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByDni("00000000")).thenReturn(Optional.empty());

        Optional<Client> optional = service.getOptionalByDni("11222333");
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByDni("00000000");
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetListByDniLike_thenReturnList() {
        Mockito.when(repository.findAllByDniContainingIgnoreCase("11222333")).thenReturn(List.of(entity));

        List<Client> list = service.getListByDniLike("11222333");

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
    void givenRequest_whenIsExistDni_thenCheckTrueAndFalse() {
        Mockito.when(repository.findOptionalByDni("11222333")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByDni("00000000")).thenReturn(Optional.empty());

        boolean exist = service.isExistDni("11222333");
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistDni("00000000");
        Assertions.assertThat(exist).isFalse();
    }

    @Test
    void givenEntity_whenSave_themReturnEntitySaved() {
        Client newEntity = Client.builder()
                .name("Name")
                .surname("Surname")
                .dni("11222333")
                .build();
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        Client entitySaved = service.save(newEntity);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(repository.findOptionalByDni("11222333")).thenReturn(Optional.of(this.entity));
        String message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isNotEmpty();
    }
}

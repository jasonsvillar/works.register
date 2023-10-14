package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.user_client.UserClient;
import com.jasonvillar.works.register.user_client.UserClientRepository;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.user_client.UserClientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserClientServiceTest {
    @Mock
    private UserClientRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private UserClientService service;

    private UserClient entity = UserClient.builder()
            .clientId(1)
            .userId(1)
            .build();

    @BeforeEach
    void setup() {
        entity.setId(Long.valueOf(1));
    }

    @Test
    void givenEntity_whenSave_themReturnEntitySaved() {
        UserClient newEntity = UserClient.builder()
                .userId(1)
                .clientId(1)
                .build();
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        UserClient entitySaved = service.save(newEntity);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findAll()).thenReturn(List.of(entity));

        List<UserClient> list = service.getList();

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(UserClient.class);
    }

    @Test
    void givenRepositories_whenGetListByUserId_thenReturnList() {
        Mockito.when(repository.findAllByUserId(1)).thenReturn(List.of(entity));

        List<UserClient> list = service.getListByUserId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(UserClient.class);
    }

    @Test
    void givenRepositories_whenGetListByClientId_thenReturnList() {
        Mockito.when(repository.findAllByClientId(1)).thenReturn(List.of(entity));

        List<UserClient> list = service.getListByClientId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(UserClient.class);
    }

    @Test
    void givenRepositories_whenGetById_thenReturnEntity() {
        Mockito.when(repository.findUserClientById(1)).thenReturn(this.entity);

        UserClient entity = service.getById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetOptionalById_thenReturnOptional() {
        Mockito.when(repository.findOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(2)).thenReturn(Optional.empty());

        Optional<UserClient> optional = service.getOptionalById(1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalById(2);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetOptionalByUserIdAndClientId_thenReturnOptional() {
        Mockito.when(repository.findOptionalByUserIdAndClientId(1, 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByUserIdAndClientId(0, 0)).thenReturn(Optional.empty());

        Optional<UserClient> optional = service.getOptionalByUserIdAndClientId(1, 1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByUserIdAndClientId(0, 0);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(repository.findOptionalByUserIdAndClientId(1, 1)).thenReturn(Optional.of(this.entity));
        Mockito.when(userService.isExistId(1)).thenReturn(false);
        Mockito.when(clientService.isExistId(1)).thenReturn(false);

        String message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isNotEmpty();

        Mockito.when(repository.findOptionalByUserIdAndClientId(1, 1)).thenReturn(Optional.empty());
        Mockito.when(userService.isExistId(1)).thenReturn(true);
        Mockito.when(clientService.isExistId(1)).thenReturn(true);

        message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isEmpty();
    }

    @Test
    void givenRequest_whenIsExistUserIdAndClientId_thenCheckTrueAndFalse() {
        Mockito.when(repository.findOptionalByUserIdAndClientId(1, 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByUserIdAndClientId(0, 0)).thenReturn(Optional.empty());

        boolean exist = service.isExistUserIdAndClientId(1, 1);
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistUserIdAndClientId(0, 0);
        Assertions.assertThat(exist).isFalse();
    }
}

package com.jasonvillar.works.register.unit.service;

import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.service.ServiceRepository;
import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.user_service.UserService;
import com.jasonvillar.works.register.user_service.UserServiceRepository;
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

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {
    @Mock
    private ServiceRepository repository;

    @InjectMocks
    private ServiceService service;

    @Mock
    private UserServiceRepository userServiceRepository;

    private Service entity = Service.builder()
            .name("Name")
            .build();

    @BeforeEach
    void setup() {
        entity.setId(1L);
    }

    @Test
    void givenEntity_whenSave_themReturnEntitySaved() {
        Service newEntity = Service.builder()
                .name("Name")
                .build();
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        Service entitySaved = service.save(newEntity);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findAll()).thenReturn(List.of(entity));

        List<Service> list = service.getList();

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Service.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLike_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCase("Name")).thenReturn(List.of(entity));

        List<Service> list = service.getListByNameLike("Name");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Service.class);
    }

    @Test
    void givenRepositories_whenGetById_thenReturnEntity() {
        Mockito.when(repository.findServiceById(1)).thenReturn(this.entity);

        Service entity = service.getById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetOptionalById_thenReturnOptional() {
        Mockito.when(repository.findOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(2)).thenReturn(Optional.empty());

        Optional<Service> optional = service.getOptionalById(1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalById(2);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetOptionalByName_thenReturnOptional() {
        Mockito.when(repository.findOptionalByName("Name")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByName("Nonexistent name")).thenReturn(Optional.empty());

        Optional<Service> optional = service.getOptionalByName("Name");
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByName("Nonexistent name");
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(repository.findOptionalByName("Name")).thenReturn(Optional.of(this.entity));
        String message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isNotEmpty();
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
    void givenRequest_whenIsExistName_thenCheckTrueAndFalse() {
        Mockito.when(repository.findOptionalByName("Name")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByName("Nonexistent name")).thenReturn(Optional.empty());

        boolean exist = service.isExistName("Name");
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistName("Nonexistent name");
        Assertions.assertThat(exist).isFalse();
    }

    @Test
    void givenRepositories_whenGetOptionalByIdAndUserId_thenReturnOptional() {
        Mockito.when(repository.findOptionalByIdAndUserServiceListUserId(1, 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByIdAndUserServiceListUserId(1, 0)).thenReturn(Optional.empty());

        Optional<Service> optional = service.getOptionalByIdAndUserId(1, 1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByIdAndUserId(1, 0);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetListByUserId_thenReturnList() {
        Mockito.when(repository.findAllByUserServiceListUserId(1)).thenReturn(List.of(entity));

        List<Service> list = service.getListByUserId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Service.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLikeAndUserId_thenReturnOptional() {
        Mockito.when(repository.findAllByNameContainingIgnoreCaseAndUserServiceListUserId("Name", 1)).thenReturn(List.of(entity));

        List<Service> list = service.getListByNameLikeAndUserId("Name", 1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Service.class);
    }

    @Test
    void givenEntity_whenSaveWithUser_themReturnEntitySaved() {
        Service newEntity = Service.builder()
                .name("Name")
                .build();
        newEntity.setId(1L);
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        UserService userService = UserService.builder()
                .userId(1)
                .serviceId(1)
                .build();
        userService.setId(1L);
        Mockito.when(userServiceRepository.save(any())).thenReturn(userService);

        Service entitySaved = service.saveWithUser(newEntity, 1L);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }
}

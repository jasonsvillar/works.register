package com.jasonvillar.works.register.unit.user_service;

import com.jasonvillar.works.register.user_service.UserService;
import com.jasonvillar.works.register.user_service.UserServiceRepository;
import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.user_service.UserServiceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserServiceServiceTest {
    @Mock
    private UserServiceRepository repository;

    @Mock
    private com.jasonvillar.works.register.user.UserService userService;

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private UserServiceService service;

    private UserService entity = UserService.builder()
            .serviceId(1)
            .userId(1)
            .build();

    @BeforeEach
    void setup() {
        entity.setId(Long.valueOf(1));
    }

    @Test
    void givenEntity_whenSave_themReturnEntitySaved() {
        UserService newEntity = UserService.builder()
                .serviceId(1)
                .userId(1)
                .build();
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        UserService entitySaved = service.save(newEntity);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findAll()).thenReturn(List.of(entity));

        List<UserService> list = service.getList();

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(UserService.class);
    }

    @Test
    void givenRepositories_whenGetListByUserId_thenReturnList() {
        Mockito.when(repository.findAllByUserId(1)).thenReturn(List.of(entity));

        List<UserService> list = service.getListByUserId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(UserService.class);
    }

    @Test
    void givenRepositories_whenGetListByServiceId_thenReturnList() {
        Mockito.when(repository.findAllByServiceId(1)).thenReturn(List.of(entity));

        List<UserService> list = service.getListByServiceId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(UserService.class);
    }

    @Test
    void givenRepositories_whenGetById_thenReturnEntity() {
        Mockito.when(repository.findUserServiceById(1)).thenReturn(this.entity);

        UserService entity = service.getById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetOptionalById_thenReturnOptional() {
        Mockito.when(repository.findOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(2)).thenReturn(Optional.empty());

        Optional<UserService> optional = service.getOptionalById(1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalById(2);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetOptionalByUserIdAndServiceId_thenReturnOptional() {
        Mockito.when(repository.findOptionalByUserIdAndServiceId(1, 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByUserIdAndServiceId(0, 0)).thenReturn(Optional.empty());

        Optional<UserService> optional = service.getOptionalByUserIdAndServiceId(1, 1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByUserIdAndServiceId(0, 0);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(repository.findOptionalByUserIdAndServiceId(1, 1)).thenReturn(Optional.of(this.entity));
        Mockito.when(userService.isExistId(1)).thenReturn(false);
        Mockito.when(serviceService.isExistId(1)).thenReturn(false);

        String message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isNotEmpty();

        Mockito.when(repository.findOptionalByUserIdAndServiceId(1, 1)).thenReturn(Optional.empty());
        Mockito.when(userService.isExistId(1)).thenReturn(true);
        Mockito.when(serviceService.isExistId(1)).thenReturn(true);

        message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isEmpty();
    }

    @Test
    void givenRequest_whenIsExistUserIdAndServiceId_thenCheckTrueAndFalse() {
        Mockito.when(repository.findOptionalByUserIdAndServiceId(1, 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByUserIdAndServiceId(0, 0)).thenReturn(Optional.empty());

        boolean exist = service.isExistUserIdAndServiceId(1, 1);
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistUserIdAndServiceId(0, 0);
        Assertions.assertThat(exist).isFalse();
    }

    @Test
    void givenRequest_whenDeleteByServiceIdAndUserId_thenCheckIfDeleted() {
        Mockito.when(repository.deleteByServiceIdAndUserId(1, 1)).thenReturn(1L);

        boolean deleted = service.deleteByServiceIdAndUserId(1, 1);
        Assertions.assertThat(deleted).isTrue();
    }

    @Test
    void givenRequest_whenDeleteByServiceIdAndUserId_thenCheckIfNotDeleted() {
        Mockito.when(repository.deleteByServiceIdAndUserId(1, 1)).thenReturn(0L);

        boolean deleted = service.deleteByServiceIdAndUserId(1, 1);
        Assertions.assertThat(deleted).isFalse();
    }

    @Test
    void givenRequest_whenDeleteByServicesIdAndUserId_thenCheckIfNotDeleted() {
        List<Long> serviceIdList = List.of(1L);

        Mockito.when(repository.deleteByServiceIdInAndUserId(any(), eq(1L))).thenReturn(List.of(entity));

        List<UserService> userServiceList = service.deleteByServicesIdAndUserId(serviceIdList, 1L);
        Assertions.assertThat(userServiceList).hasSize(1);
    }
}

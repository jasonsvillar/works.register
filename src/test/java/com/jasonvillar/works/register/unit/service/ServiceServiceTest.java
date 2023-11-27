package com.jasonvillar.works.register.unit.service;

import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.service.ServiceRepository;
import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {
    @Mock
    private ServiceRepository repository;

    @InjectMocks
    private ServiceService service;

    User adminUser = User.builder().id(1L).name("Admin").validated(true).build();

    private Service entity = Service.builder()
            .name("Name")
            .user(this.adminUser)
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
        Page<Service> pagedServiceList = new PageImpl<>(List.of(entity));
        Mockito.when(repository.findAll((Pageable) any())).thenReturn(pagedServiceList);

        List<Service> list = service.getList(0, 10);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Service.class);
    }

    @Test
    void givenRepositories_whenGetListByUserId_thenReturnList() {
        Mockito.when(this.repository.findAllByUserId(eq(1L), any())).thenReturn(List.of(entity));

        List<Service> list = this.service.getListByUserId(1L, 0, 10);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Service.class);
    }

    @Test
    void givenRepositories_whenGetRowCountByUserId_thenReturnLong() {
        Mockito.when(repository.countByUserId(1)).thenReturn(1L);

        Long count = service.getRowCountByUserId(1);

        Assertions.assertThat(count.getClass()).isEqualTo(Long.class);
    }

    @Test
    void givenRepositories_whenGetRowCount_thenReturnLong() {
        Mockito.when(repository.count()).thenReturn(1L);

        Long count = service.getRowCount();

        Assertions.assertThat(count.getClass()).isEqualTo(Long.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLikeAndUserId_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCaseAndUserId("Name", 1)).thenReturn(List.of(entity));

        List<Service> list = service.getListByNameLikeAndUserId("Name", 1);

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
        Mockito.when(repository.findOptionalByNameAndUserId("Name", 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByNameAndUserId("Nonexistent name", 1)).thenReturn(Optional.empty());

        Optional<Service> optional = service.getOptionalByNameAndUserId("Name", 1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByNameAndUserId("Nonexistent name", 1);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(repository.findOptionalByNameAndUserId("Name", 1)).thenReturn(Optional.of(this.entity));
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
        Mockito.when(repository.findOptionalByNameAndUserId("Name", 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByNameAndUserId("Nonexistent name", 1)).thenReturn(Optional.empty());

        boolean exist = service.isExistNameAndUserId("Name", 1);
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistNameAndUserId("Nonexistent name", 1);
        Assertions.assertThat(exist).isFalse();
    }

    @Test
    void givenRepositories_whenGetOptionalByIdAndUserId_thenReturnOptional() {
        Mockito.when(repository.findOptionalByIdAndUserId(1, 1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByIdAndUserId(1, 0)).thenReturn(Optional.empty());

        Optional<Service> optional = service.getOptionalByIdAndUserId(1, 1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByIdAndUserId(1, 0);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void deleteByServiceIdAndUserId() {
        Mockito.when(repository.deleteByIdAndUserId(1, 1)).thenReturn(true);
        Mockito.when(repository.deleteByIdAndUserId(0, 0)).thenReturn(false);

        boolean deleted = service.deleteByServiceIdAndUserId(1, 1);
        Assertions.assertThat(deleted).isTrue();

        deleted = service.deleteByServiceIdAndUserId(0, 0);
        Assertions.assertThat(deleted).isFalse();
    }
}

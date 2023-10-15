package com.jasonvillar.works.register.work_register;

import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.work_register.WorkRegister;
import com.jasonvillar.works.register.work_register.WorkRegisterRepository;
import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.work_register.WorkRegisterService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WorkRegisterServiceTest {
    @Mock
    private WorkRegisterRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private ClientService clientService;

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private WorkRegisterService service;

    private WorkRegister entity = WorkRegister.builder()
            .title("Test work")
            .dateFrom(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
            .timeFrom(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
            .dateTo(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
            .timeTo(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
            .payment(new BigDecimal(1500))
            .userId(1)
            .serviceId(1)
            .clientId(1)
            .build();

    @BeforeEach
    void setup() {
        entity.setId(Long.valueOf(1));
    }

    @Test
    void givenEntity_whenSave_themReturnEntitySaved() {
        WorkRegister newEntity = WorkRegister.builder()
                .title("Test work")
                .dateFrom(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
                .timeFrom(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
                .dateTo(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
                .timeTo(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
                .payment(new BigDecimal(1500))
                .userId(1)
                .serviceId(1)
                .clientId(1)
                .build();
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        WorkRegister entitySaved = service.save(newEntity);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findAll()).thenReturn(List.of(entity));

        List<WorkRegister> list = service.getList();

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(WorkRegister.class);
    }

    @Test
    void givenRepositories_whenGetListByUserId_thenReturnList() {
        Mockito.when(repository.findAllByUserId(1)).thenReturn(List.of(entity));

        List<WorkRegister> list = service.getListByUserId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(WorkRegister.class);
    }

    @Test
    void givenRepositories_whenGetListByClientId_thenReturnList() {
        Mockito.when(repository.findAllByClientId(1)).thenReturn(List.of(entity));

        List<WorkRegister> list = service.getListByClientId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(WorkRegister.class);
    }

    @Test
    void givenRepositories_whenGetListByTitle_thenReturnList() {
        Mockito.when(repository.findAllByTitleContainingIgnoreCase("Test work")).thenReturn(List.of(entity));

        List<WorkRegister> list = service.getListByTitleLike("Test work");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(WorkRegister.class);
    }

    @Test
    void givenRepositories_whenGetListByUserIdAndClientId_thenReturnList() {
        Mockito.when(repository.findAllByUserIdAndClientId(1, 1)).thenReturn(List.of(entity));

        List<WorkRegister> list = service.getListByUserIdAndClientId(1, 1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(WorkRegister.class);
    }

    @Test
    void givenRepositories_whenGetById_thenReturnEntity() {
        Mockito.when(repository.findWorkRegisterById(1)).thenReturn(this.entity);

        WorkRegister entity = service.getById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetOptionalById_thenReturnOptional() {
        Mockito.when(repository.findOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(2)).thenReturn(Optional.empty());

        Optional<WorkRegister> optional = service.getOptionalById(1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalById(2);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(userService.isExistId(1)).thenReturn(false);
        Mockito.when(clientService.isExistId(1)).thenReturn(false);
        Mockito.when(serviceService.isExistId(1)).thenReturn(false);

        String message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isNotEmpty();

        Mockito.when(userService.isExistId(1)).thenReturn(true);
        Mockito.when(clientService.isExistId(1)).thenReturn(true);
        Mockito.when(serviceService.isExistId(1)).thenReturn(true);

        message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isEmpty();
    }
}

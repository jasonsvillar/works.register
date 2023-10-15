package com.jasonvillar.works.register.authentication;

import com.jasonvillar.works.register.authentication.Privilege;
import com.jasonvillar.works.register.authentication.PrivilegeRepository;
import com.jasonvillar.works.register.authentication.PrivilegeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PrivilegeServiceTest {
    @Mock
    private PrivilegeRepository repository;

    @InjectMocks
    private PrivilegeService service;

    private Privilege entity = Privilege.builder()
            .name("Privilege Name")
            .build();

    @BeforeEach
    void setup() {
        entity.setId(1L);
    }

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findAll()).thenReturn(List.of(entity));

        List<Privilege> list = service.getList();

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Privilege.class);
    }

    @Test
    void givenRepositories_whenGetById_thenReturnEntity() {
        Mockito.when(repository.findPrivilegeById(1L)).thenReturn(entity);

        Privilege entity = service.getById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetOptionalById_thenReturnOptional() {
        Mockito.when(repository.findOptionalById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(3L)).thenReturn(Optional.empty());

        Optional<Privilege> optional = service.getOptionalById(1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalById(3);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetListByNameLike_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCase("name")).thenReturn(List.of(entity));

        List<Privilege> list = service.getListByNameLike("name");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Privilege.class);
    }

    @Test
    void givenRepositories_whenGetListByUserId_thenReturnList() {
        Mockito.when(repository.findAllDistinctByInRoleListInUserListId(1)).thenReturn(List.of(entity));

        List<Privilege> list = service.getListByUserId(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(Privilege.class);
    }

    @Test
    void givenRepositories_whenGetSimpleGrantedAuthorityList_thenReturnList() {
        Mockito.when(repository.findAllDistinctByInRoleListInUserListId(1)).thenReturn(List.of(entity));

        List<SimpleGrantedAuthority> list = service.getSimpleGrantedAuthorityList(1);

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(SimpleGrantedAuthority.class);
    }
}

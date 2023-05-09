package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User entity = User.builder()
            .name("Name")
            .email("test@test.com")
            .password("top-secret-encrypted-password")
            .build();

    @BeforeEach
    void setup() {
        entity.setId(Long.valueOf(1));
    }

    @Test
    void givenEntity_whenSave_themReturnEntitySaved() {
        User newEntity = User.builder()
                .name("Name")
                .email("test@test.com")
                .password("top-secret-encrypted-password")
                .build();
        Mockito.when(repository.save(newEntity)).thenReturn(entity);

        User entitySaved = service.save(newEntity);
        Assertions.assertThat(entitySaved.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findAll()).thenReturn(List.of(entity));

        List<User> list = service.getList();

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(User.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLike_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCase("Name")).thenReturn(List.of(entity));

        List<User> list = service.getListByNameLike("Name");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(User.class);
    }

    @Test
    void givenRepositories_whenGetListByEmailLike_thenReturnList() {
        Mockito.when(repository.findAllByEmailContainingIgnoreCase("test@test.com")).thenReturn(List.of(entity));

        List<User> list = service.getListByEmailLike("test@test.com");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(User.class);
    }

    @Test
    void givenRepositories_whenGetListByNameLikeAndEmailLike_thenReturnList() {
        Mockito.when(repository.findAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCase("Name", "test@test.com")).thenReturn(List.of(entity));

        List<User> list = service.getListByNameLikeAndEmailLike("Name", "test@test.com");

        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.get(0).getClass()).isEqualTo(User.class);
    }

    @Test
    void givenRepositories_whenGetById_thenReturnEntity() {
        Mockito.when(repository.findUserById(1)).thenReturn(this.entity);

        User entity = service.getById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isEqualTo(1);
    }

    @Test
    void givenRepositories_whenGetOptionalById_thenReturnOptional() {
        Mockito.when(repository.findOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalById(2)).thenReturn(Optional.empty());

        Optional<User> optional = service.getOptionalById(1);
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalById(2);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetOptionalByName_thenReturnOptional() {
        Mockito.when(repository.findOptionalByName("Name")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByName("Nonexistent name")).thenReturn(Optional.empty());

        Optional<User> optional = service.getOptionalByName("Name");
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByName("Nonexistent name");
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenRepositories_whenGetOptionalByEmail_thenReturnOptional() {
        Mockito.when(repository.findOptionalByEmail("test@test.com")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByEmail("Nonexistent email")).thenReturn(Optional.empty());

        Optional<User> optional = service.getOptionalByEmail("test@test.com");
        Assertions.assertThat(optional).isPresent();

        optional = service.getOptionalByEmail("Nonexistent email");
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntity_whenGetValidationsMessageWhenCantBeSaved_themReturnMessage() {
        Mockito.when(repository.findOptionalByName("Name")).thenReturn(Optional.of(this.entity));
        Mockito.when(repository.findOptionalByEmail("test@test.com")).thenReturn(Optional.of(this.entity));
        String message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isNotEmpty();

        Mockito.when(repository.findOptionalByName("Name")).thenReturn(Optional.empty());
        Mockito.when(repository.findOptionalByEmail("test@test.com")).thenReturn(Optional.empty());
        message = this.service.getValidationsMessageWhenCantBeSaved(this.entity);
        Assertions.assertThat(message).isEmpty();
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
    void givenRequest_whenIsExistEmail_thenCheckTrueAndFalse() {
        Mockito.when(repository.findOptionalByEmail("test@test.com")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByEmail("Nonexistent email")).thenReturn(Optional.empty());

        boolean exist = service.isExistEmail("test@test.com");
        Assertions.assertThat(exist).isTrue();

        exist = service.isExistEmail("Nonexistent email");
        Assertions.assertThat(exist).isFalse();
    }

    @Test
    void givenPlainPassword_whenSaveUser_thenEncodeBcrypt() {
        String topSecretPassword = "top-secret-password";
        String encrypted = this.service.plainPasswordToBcrypt(topSecretPassword);

        boolean ok = BCrypt.checkpw("Incorrect password", encrypted);
        Assertions.assertThat(ok).isFalse();

        ok = BCrypt.checkpw(topSecretPassword, encrypted);
        Assertions.assertThat(ok).isTrue();
    }
}

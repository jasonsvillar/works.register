package com.jasonvillar.works.register.unit.user;

import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserRepository;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedService;
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

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Mock
    private UserNotValidatedService userNotValidatedService;

    private User entity = User.builder()
            .name("Name")
            .email("test@test.com")
            .password("top-secret-encrypted-password")
            .build();

    @BeforeEach
    void setup() {
        entity.setId(1L);
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

    @Test
    void givenRequest_whenGetOptionalByNameAndEmail_thenCheckIfPresent() {
        Mockito.when(repository.findOptionalByNameAndEmail("Name", "test@test.com")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByNameAndEmail("Random name", "Nonexistent email")).thenReturn(Optional.empty());

        Optional<User> userOptional = service.getOptionalByNameAndEmail("Name", "test@test.com");
        Assertions.assertThat(userOptional).isPresent();

        userOptional = service.getOptionalByNameAndEmail("Random name", "Nonexistent email");
        Assertions.assertThat(userOptional).isNotPresent();
    }

    @Test
    void givenPlainPasswordAndBcrypted_whenPasswordMatchWithActual_thenCheckIsTrue() {
        String password = "123456";
        String bcryptPassword = this.service.plainPasswordToBcrypt(password);

        boolean match = this.service.passwordMatchWithActual(password, bcryptPassword);
        Assertions.assertThat(match).isTrue();
    }

    @Test
    void givenPlainPasswordAndBcrypted_whenPasswordMatchWithActual_thenCheckIsFalse() {
        String password = "123456";
        String bcryptPassword = this.service.plainPasswordToBcrypt(password);

        boolean match = this.service.passwordMatchWithActual("bad-password", bcryptPassword);
        Assertions.assertThat(match).isFalse();
    }

    @Test
    void generateCodeToUser() {
        User user = User.builder()
                .id(1L)
                .build();

        User userWhitCode = User.builder()
                .id(1L)
                .code("123456")
                .build();

        Mockito.when(userNotValidatedService.makeRandomValidationCode()).thenReturn("123456");
        Mockito.when(repository.save(any())).thenReturn(userWhitCode);

        user = this.service.generateCodeToUser(user);
        Assertions.assertThat(user.getCode()).isEqualTo("123456");
    }

    @Test
    void updateEmailAndGenerateCodeToUser() {
        User user = User.builder()
                .id(1L)
                .build();

        User userWhitCodeAndEmail = User.builder()
                .id(1L)
                .code("123456")
                .email("new@mail.com")
                .build();

        Mockito.when(userNotValidatedService.makeRandomValidationCode()).thenReturn("123456");
        Mockito.when(repository.save(any())).thenReturn(userWhitCodeAndEmail);

        user = this.service.updateEmailAndGenerateCodeToUser(user, "new@mail.com");
        Assertions.assertThat(user.getCode()).isEqualTo("123456");
        Assertions.assertThat(user.getEmail()).isEqualTo("new@mail.com");
    }

    @Test
    void givenRequest_whenGetOptionalByNameAndEmailAndValidated_thenCheckIfPresent() {
        Mockito.when(repository.findOptionalByNameAndEmailAndValidated("Name", "test@test.com", true)).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByNameAndEmailAndValidated("Random name", "Nonexistent email", true)).thenReturn(Optional.empty());

        Optional<User> userOptional = service.getOptionalByNameAndEmailAndValidated("Name", "test@test.com", true);
        Assertions.assertThat(userOptional).isPresent();

        userOptional = service.getOptionalByNameAndEmailAndValidated("Random name", "Nonexistent email", true);
        Assertions.assertThat(userOptional).isNotPresent();
    }

    @Test
    void givenRequest_whenGetOptionalByNameAndEmailAndValidatedAndCode_thenCheckIfPresent() {
        Mockito.when(repository.findOptionalByNameAndEmailAndValidatedAndCode("Name", "test@test.com", true, "123456")).thenReturn(Optional.of(entity));
        Mockito.when(repository.findOptionalByNameAndEmailAndValidatedAndCode("Random name", "Nonexistent email", true, "123456")).thenReturn(Optional.empty());

        Optional<User> userOptional = service.getOptionalByNameAndEmailAndValidatedAndCode("Name", "test@test.com", true, "123456");
        Assertions.assertThat(userOptional).isPresent();

        userOptional = service.getOptionalByNameAndEmailAndValidatedAndCode("Random name", "Nonexistent email", true, "123456");
        Assertions.assertThat(userOptional).isNotPresent();
    }
}
